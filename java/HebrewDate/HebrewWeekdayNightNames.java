package HebrewDate;

public class HebrewWeekdayNightNames {
	private static final String[] NAMES_EN = {
		"", "Eve of first day", "Eve of second day", "Eve of third day", "Eve of fourth day",
		"Eve of fifth day", "Eve of sixth day", "Eve of Shabbat"
	};
	private static final String[] NAMES_HE = {
		"", "ליל ראשון", "ליל שני", "ליל שלישי", "ליל רביעי", "ליל חמישי", "ליל שישי", "ליל שבת"
	};

	public final String lang;
	public final int dayOfWeek;

	public HebrewWeekdayNightNames(String lang, int dayOfWeek) {
		this.lang = lang;
		this.dayOfWeek = dayOfWeek;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[dayOfWeek];
		return NAMES_HE[dayOfWeek];
	}
}
