package Astro;

public class MoonIllumination {
	public final Moon moon;

	public MoonIllumination(Moon moon) {
		this.moon = moon;
	}

	public double phi() {
		CelestialLocation sunLocation = moon.sun.celestialLocation();
		CelestialLocation moonLocation = moon.celestialLocation();
		return Math.acos(
			Math.sin(sunLocation.declination) * Math.sin(moonLocation.declination) +
			Math.cos(sunLocation.declination) * Math.cos(moonLocation.declination) *
			Math.cos(sunLocation.rightAscension - moonLocation.rightAscension)
		);
	}

	public double inc() {
		return Math.atan2(
			moon.sun.distance() * Math.sin(phi()),
			moon.distance() - moon.sun.distance() * Math.cos(phi())
		);
	}

	public double fraction() {
		return (1.0 + Math.cos(inc())) / 2.0;
	}

	public double angle() {
		CelestialLocation sunLocation = moon.sun.celestialLocation();
		CelestialLocation moonLocation = moon.celestialLocation();
		return Math.atan2(
			Math.cos(sunLocation.declination) * Math.sin(sunLocation.rightAscension - moonLocation.rightAscension),
			Math.sin(sunLocation.declination) * Math.cos(moonLocation.declination) -
			Math.cos(sunLocation.declination) * Math.sin(moonLocation.declination) *
			Math.cos(sunLocation.rightAscension - moonLocation.rightAscension)
		);
	}

	public double phase() {
		return 0.5 + 0.5 * inc() * (angle() < 0.0 ? -1.0 : 1.0) / Math.PI;
	}

	public MoonPhaseType phaseType() {
		double p = phase();
		if (p < 0.0625 || p >= 0.9375) return MoonPhaseType.NEW_MOON;
		if (p < 0.3125) return MoonPhaseType.WAXING_CRESCENT;
		if (p < 0.4375) return MoonPhaseType.FIRST_QUARTER;
		if (p < 0.5625) return MoonPhaseType.FULL_MOON;
		if (p < 0.6875) return MoonPhaseType.WANING_GIBBOUS;
		if (p < 0.8125) return MoonPhaseType.LAST_QUARTER;
		return MoonPhaseType.WANING_CRESCENT;
	}

	public String phaseEmoji() {
		boolean north = moon.location.latitude >= 0;
		MoonPhaseType t = phaseType();
		switch (t) {
			case NEW_MOON: return "🌑";
			case WAXING_CRESCENT: return north ? "🌒" : "🌘";
			case FIRST_QUARTER: return north ? "🌓" : "🌗";
			case FULL_MOON: return "🌕";
			case WANING_GIBBOUS: return north ? "🌖" : "🌔";
			case LAST_QUARTER: return north ? "🌗" : "🌓";
			case WANING_CRESCENT: return north ? "🌘" : "🌒";
			default: return "🌑";
		}
	}
}
