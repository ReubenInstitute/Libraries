package Astro;

public class MoonPhaseTypeNames {
	private static final String[] NAMES_EN = {
		"",
		"New Moon",
		"Waxing Crescent",
		"First Quarter",
		"Waxing Gibbous",
		"Full Moon",
		"Waning Gibbous",
		"Last Quarter",
		"Waning Crescent",
	};
	private static final String[] NAMES_HE = {
		"",
		"מולד",
		"סהר עולה",
		"רבע ראשון",
		"גבנון עולה",
		"ירח מלא",
		"גבנון יורד",
		"רבע אחרון",
		"סהר יורד",
	};

	public final String lang;
	public final MoonPhaseType phaseType;

	public MoonPhaseTypeNames(String lang, MoonPhaseType phaseType) {
		this.lang = lang;
		this.phaseType = phaseType;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[phaseType.value];
		return NAMES_HE[phaseType.value];
	}
}
