package Astro;

import Date.DateTime;
import Date.Time;
import Geo.Location;

public class Moon {
	private static final double RAD = Math.PI / 180.0;

	public Location location;
	public DateTime datetime;
	public Sun sun;

	public Moon(Location location, DateTime datetime) {
		this.location = location;
		this.datetime = datetime;
		this.sun = new Sun(location, datetime);
	}

	public void updateDateTime(DateTime datetime) {
		this.datetime = datetime;
		this.sun.updateDateTime(datetime);
	}

	public CelestialLocation celestialLocation() {
		double days = datetime.julianDay2K();
		double L = RAD * (218.316 + 13.176396 * days);
		double M = RAD * (134.963 + 13.064993 * days);
		double F = RAD * (93.272 + 13.229350 * days);
		double l = L + RAD * 6.289 * Math.sin(M);
		double b = RAD * 5.128 * Math.sin(F);
		double ra = Math.atan2(
			Math.sin(l) * Math.cos(Astro.EARTH_OBLIQUITY) - Math.tan(b) * Math.sin(Astro.EARTH_OBLIQUITY),
			Math.cos(l)
		);
		double dec = Math.asin(
			Math.sin(b) * Math.cos(Astro.EARTH_OBLIQUITY) +
			Math.cos(b) * Math.sin(Astro.EARTH_OBLIQUITY) * Math.sin(l)
		);
		return new CelestialLocation(ra, dec);
	}

	public double distance() {
		double days = datetime.julianDay2K();
		double M = RAD * (134.963 + 13.064993 * days);
		return 385001.0 - 20905.0 * Math.cos(M);
	}

	public double altitude() {
		double lw = RAD * -location.longitude;
		double phi = RAD * location.latitude;
		CelestialLocation moonDir = celestialLocation();
		double H = siderealTime(datetime, lw) - moonDir.rightAscension;
		double h = Math.asin(Math.sin(phi) * Math.sin(moonDir.declination) +
				Math.cos(phi) * Math.cos(moonDir.declination) * Math.cos(H));

		// Parallax correction (essential for the moon!)
		double parallax = Math.asin(6371.0 / distance()) * Math.cos(h);
		h = h - parallax; // Moon appears lower due to parallax

		return h + astroRefraction(h);
	}

	public double[] altitudes() {
		double[] alts = new double[25];
		for (int h = 0; h < 25; h++) {
			DateTime t;
			if (h == 24) {
				t = new DateTime(datetime.date.next(), new Time(0, 0, 0));
			} else {
				t = new DateTime(datetime.date, new Time(h, 0, 0));
			}
			Moon m = new Moon(location, t);
			alts[h] = m.altitude();
		}
		return alts;
	}

	private static class RiseSet {
		Double riseHour;
		Double setHour;
	}

	private RiseSet calculateTimes() {
		double[] alts = altitudes();
		RiseSet result = new RiseSet();

		for (int i = 0; i < 23; i++) {
			double h0 = alts[i];
			double h1 = alts[i + 1];
			double h2 = alts[i + 2];

			double a = (h0 + h2) / 2.0 - h1;
			double b = (h2 - h0) / 2.0;
			double d = b * b - 4.0 * a * h1;
			int roots = 0;
			double x1 = 0.0;
			double x2 = 0.0;

			if (a != 0.0) {
				double xe = -b / (2.0 * a);
				double ye = (a * xe + b) * xe + h1;
				if (d >= 0.0) {
					double dx = Math.sqrt(d) / (Math.abs(a) * 2.0);
					x1 = xe - dx;
					x2 = xe + dx;
					if (Math.abs(x1) <= 1.0) roots += 1;
					if (Math.abs(x2) <= 1.0) roots += 1;
					if (x1 < -1.0) x1 = x2;
				}

				if (roots == 1) {
					if (h0 < 0.0) {
						if (result.riseHour == null) result.riseHour = (double) (i + 1) + x1;
					} else {
						if (result.setHour == null) result.setHour = (double) (i + 1) + x1;
					}
				} else if (roots == 2) {
					if (result.riseHour == null) result.riseHour = (double) (i + 1) + (ye < 0.0 ? x2 : x1);
					if (result.setHour == null) result.setHour = (double) (i + 1) + (ye < 0.0 ? x1 : x2);
				}
			}

			if (result.riseHour != null && result.setHour != null) break;
		}

		return result;
	}

	public DateTime rise() {
		RiseSet rs = calculateTimes();
		if (rs.riseHour != null) {
			DateTime midnight = new DateTime(datetime.date, new Time(0, 0, 0));
			return midnight.plus((int) (rs.riseHour * 3600));
		}
		return null;
	}

	public DateTime set() {
		RiseSet rs = calculateTimes();
		if (rs.setHour != null) {
			DateTime midnight = new DateTime(datetime.date, new Time(0, 0, 0));
			return midnight.plus((int) (rs.setHour * 3600));
		}
		return null;
	}

	public boolean isAlwaysUp() {
		RiseSet rs = calculateTimes();
		if (rs.riseHour == null && rs.setHour == null) {
			DateTime noon = new DateTime(datetime.date, new Time(12, 0, 0));
			return new Moon(location, noon).altitude() > 0.0;
		}
		return false;
	}

	public MoonIllumination illumination() {
		return new MoonIllumination(this);
	}

	public String emoji() {
		return illumination().phaseEmoji();
	}

	public static double eclipticLongitude(double M) {
		double C = RAD * (1.9148 * Math.sin(M) + 0.02 * Math.sin(2.0 * M) + 0.0003 * Math.sin(3.0 * M));
		double P = RAD * 102.9372;
		return M + C + P + Math.PI;
	}

	public static double declination(double L, double b) {
		return Math.asin(
			Math.sin(b) * Math.cos(Astro.EARTH_OBLIQUITY) +
			Math.cos(b) * Math.sin(Astro.EARTH_OBLIQUITY) * Math.sin(L)
		);
	}

	public static double rightAscension(double L, double b) {
		return Math.atan2(
			Math.sin(L) * Math.cos(Astro.EARTH_OBLIQUITY) - Math.tan(b) * Math.sin(Astro.EARTH_OBLIQUITY),
			Math.cos(L)
		);
	}

	public static double siderealTime(DateTime dt, double lw) {
		double days = dt.julianDay2K();
		return RAD * (280.16 + 360.9856235 * days) - lw;
	}

	public static double astroRefraction(double h) {
		if (h < 0.0) h = 0.0;
		return 0.0002967 / Math.tan(h + 0.00312536 / (h + 0.08901179));
	}
}
