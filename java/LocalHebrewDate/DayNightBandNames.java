package LocalHebrewDate;

public class DayNightBandNames {
	private static final String[] NAMES_EN = {
		"",
		"Nightfall",
		"Watch 1 (3)",
		"Watch 2 (3)",
		"Watch 3 (3)",
		"Watch 1 (4)",
		"Watch 2 (4)",
		"Watch 3 (4)",
		"Watch 4 (4)",
		"Dawn",
		"Morning",
		"Afternoon",
	};
	private static final String[] NAMES_HE = {
		"",
		"שקיעה עד צאת",
		"אשמורה א׳ (3)",
		"אשמורה ב׳ (3)",
		"אשמורה ג׳ (3)",
		"אשמורה א׳ (4)",
		"אשמורה ב׳ (4)",
		"אשמורה ג׳ (4)",
		"אשמורה ד׳ (4)",
		"עלות השחר עד נץ",
		"בוקר",
		"צהריים",
	};

	public final String lang;
	public final int band;

	public DayNightBandNames(String lang, int band) {
		this.lang = lang;
		this.band = band;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[band];
		return NAMES_HE[band];
	}
}
