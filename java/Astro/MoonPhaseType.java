package Astro;

public enum MoonPhaseType {
	NEW_MOON(1),
	WAXING_CRESCENT(2),
	FIRST_QUARTER(3),
	WAXING_GIBBOUS(4),
	FULL_MOON(5),
	WANING_GIBBOUS(6),
	LAST_QUARTER(7),
	WANING_CRESCENT(8);

	public final int value;

	MoonPhaseType(int value) {
		this.value = value;
	}
}
