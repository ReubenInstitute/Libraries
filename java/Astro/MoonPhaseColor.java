package Astro;

public enum MoonPhaseColor {
	NEW_MOON(0xB35900),
	WAXING_CRESCENT(0xC6650A),
	FIRST_QUARTER(0xD97614),
	WAXING_GIBBOUS(0xEC8A1E),
	FULL_MOON(0xFF9800),
	WANING_GIBBOUS(0xEC8A1E),
	LAST_QUARTER(0xD97614),
	WANING_CRESCENT(0xC6650A);

	public final int value;

	MoonPhaseColor(int value) {
		this.value = value;
	}
}
