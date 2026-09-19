package Astro;

import Date.Date;
import Date.DateTime;
import Date.Time;
import Geo.Location;

public class Sun {
	private static final double RAD = Math.PI / 180.0;

	public Location location;
	public DateTime datetime;

	public Sun(Location location, DateTime datetime) {
		this.location = location;
		this.datetime = datetime;
	}

	public void updateLocation(Location location) {
		this.location = location;
	}

	public void updateDateTime(DateTime datetime) {
		this.datetime = datetime;
	}

	private DateTime midnight() {
		return new DateTime(datetime.date, new Time(0, 0, 0));
	}

	private double lw() {
		return RAD * -location.longitude;
	}

	private double n() {
		return julianCycle(midnight().julianDay(), lw());
	}

	private double ds() {
		return approxTransit(0.0, lw(), n());
	}

	private double solarMeanAnomalyM() {
		return solarMeanAnomaly(ds());
	}

	private double eclipticLongitudeL() {
		return eclipticLongitude(solarMeanAnomalyM());
	}

	public double distance() {
		return 149597870.7 + 2500000.0 * Math.cos(RAD * (357.5291 + 0.98560028 * datetime.julianDay2K()));
	}

	public CelestialLocation celestialLocation() {
		double days = datetime.julianDay2K();
		double M = RAD * (357.5291 + 0.98560028 * days);
		double C = RAD * (1.9148 * Math.sin(M) + 0.02 * Math.sin(2.0 * M) + 0.0003 * Math.sin(3.0 * M));
		double P = RAD * 102.9372;
		double L = M + C + P + Math.PI;
		double dec = Math.asin(
			Math.sin(0.0) * Math.cos(Astro.EARTH_OBLIQUITY) +
			Math.cos(0.0) * Math.sin(Astro.EARTH_OBLIQUITY) * Math.sin(L)
		);
		double ra = Math.atan2(
			Math.sin(L) * Math.cos(Astro.EARTH_OBLIQUITY) - Math.tan(0.0) * Math.sin(Astro.EARTH_OBLIQUITY),
			Math.cos(L)
		);
		return new CelestialLocation(ra, dec);
	}

	public double noonJD() {
		return solarTransitJ(ds(), solarMeanAnomalyM(), eclipticLongitudeL());
	}

	public double setJD() {
		double phi = RAD * location.latitude;
		double dec = declination(eclipticLongitudeL(), 0.0);
		double h0 = -0.833 * RAD;
		double w = hourAngle(h0, phi, dec);
		double a = approxTransit(w, lw(), n());
		return solarTransitJ(a, solarMeanAnomalyM(), eclipticLongitudeL());
	}

	public double riseJD() {
		return noonJD() - (setJD() - noonJD());
	}

	public DateTime noon() {
		return DateTime.fromJulianDay(noonJD());
	}

	public DateTime set() {
		return DateTime.fromJulianDay(setJD());
	}

	public DateTime rise() {
		return DateTime.fromJulianDay(riseJD());
	}

	public DateTime dawnTime(double angleDegrees) {
		return timeAtAltitude(-angleDegrees, true);
	}

	public DateTime duskTime(double angleDegrees) {
		return timeAtAltitude(-angleDegrees, false);
	}

	private DateTime timeAtAltitude(double altitudeDeg, boolean rising) {
		double phi = RAD * location.latitude;
		double dec = declination(eclipticLongitudeL(), 0.0);
		double h0 = altitudeDeg * RAD;
		double w = hourAngle(h0, phi, dec);
		double a = approxTransit(w, lw(), n());
		double Jset = solarTransitJ(a, solarMeanAnomalyM(), eclipticLongitudeL());
		double Jnoon = noonJD();
		return DateTime.fromJulianDay(rising ? Jnoon - (Jset - Jnoon) : Jset);
	}

	private static double julianCycle(double d, double lw) {
		return Math.round(d - Astro.J2000 - 0.0009 - lw / (2.0 * Math.PI));
	}

	private static double approxTransit(double Ht, double lw, double n) {
		return 0.0009 + (Ht + lw) / (2.0 * Math.PI) + n;
	}

	private static double solarTransitJ(double ds, double M, double L) {
		return Astro.J2000 + ds + 0.0053 * Math.sin(M) - 0.0069 * Math.sin(2.0 * L);
	}

	private static double solarMeanAnomaly(double daysSinceJ2000) {
		return RAD * (357.5291 + 0.98560028 * daysSinceJ2000);
	}

	private static double eclipticLongitude(double M) {
		double C = RAD * (1.9148 * Math.sin(M) + 0.02 * Math.sin(2.0 * M) + 0.0003 * Math.sin(3.0 * M));
		double P = RAD * 102.9372;
		return M + C + P + Math.PI;
	}

	private static double declination(double L, double b) {
		return Math.asin(
			Math.sin(b) * Math.cos(Astro.EARTH_OBLIQUITY) +
			Math.cos(b) * Math.sin(Astro.EARTH_OBLIQUITY) * Math.sin(L)
		);
	}

	private static double rightAscension(double L, double b) {
		return Math.atan2(
			Math.sin(L) * Math.cos(Astro.EARTH_OBLIQUITY) - Math.tan(b) * Math.sin(Astro.EARTH_OBLIQUITY),
			Math.cos(L)
		);
	}

	private static double siderealTime(double daysSinceJ2000, double lw) {
		return RAD * (280.16 + 360.9856235 * daysSinceJ2000) - lw;
	}

	private static double astroRefraction(double h) {
		if (h < 0.0) h = 0.0;
		return 0.0002967 / Math.tan(h + 0.00312536 / (h + 0.08901179));
	}

	private static double hourAngle(double h, double phi, double dec) {
		return Math.acos(
			(Math.sin(h) - Math.sin(phi) * Math.sin(dec)) /
			(Math.cos(phi) * Math.cos(dec))
		);
	}
}
