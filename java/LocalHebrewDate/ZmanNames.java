package LocalHebrewDate;

public class ZmanNames {
	private static final String[] NAMES_EN = {
		"",
		"Yemama Start",
		"Nightfall",
		"Midnight",
		"Dawn",
		"Misheyakir",
		"Sunrise",
		"Shema End",
		"Amidah End",
		"Noon",
		"Minchah Gedolah",
		"Minchah Ketanah",
		"Plag Minchah",
		"Sunset",
		"Yemama End",
		"Moonrise",
		"Moonset",
	};
	private static final String[] NAMES_HE = {
		"",
		"תחילת היום",
		"צאת הכוכבים",
		"חצות הלילה",
		"עלות השחר",
		"משיכיר",
		"הנץ החמה",
		"סוף זמן שמע",
		"סוף זמן תפילה",
		"חצות היום",
		"מנחה גדולה",
		"מנחה קטנה",
		"פלג המנחה",
		"שקיעה",
		"סוף היום",
		"זריחת ירח",
		"שקיעת ירח",
	};

	public final String lang;
	public final Zman zman;

	public ZmanNames(String lang, Zman zman) {
		this.lang = lang;
		this.zman = zman;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[zman.value];
		return NAMES_HE[zman.value];
	}
}
