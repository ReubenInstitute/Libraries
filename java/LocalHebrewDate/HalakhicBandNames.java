package LocalHebrewDate;

public class HalakhicBandNames {
	private static final String[] NAMES_EN = {
		"",
		"Nightfall",
		"Dawn",
		"Misheyakir",
		"Shema",
		"Amidah",
		"Minchah Gedolah",
		"Minchah Ketanah",
		"Plag Minchah",
		"Watch 1",
		"Watch 2",
		"Watch 3",
	};
	private static final String[] NAMES_HE = {
		"",
		"צאת הכוכבים",
		"עלות השחר",
		"משיכיר",
		"שמע",
		"תפילה",
		"מנחה גדולה",
		"מנחה קטנה",
		"פלג המנחה",
		"אשמורה א׳",
		"אשמורה ב׳",
		"אשמורה ג׳",
	};

	public final String lang;
	public final int band;

	public HalakhicBandNames(String lang, int band) {
		this.lang = lang;
		this.band = band;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[band];
		return NAMES_HE[band];
	}
}
