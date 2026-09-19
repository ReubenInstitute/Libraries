package HebrewDate;

public class HebrewWeekdayDayNames {
	private static final String[] NAMES_EN = {
		"", "First day", "Second day", "Third day", "Fourth day", "Fifth day", "Sixth day", "Shabbat"
	};
	private static final String[] NAMES_HE = {
		"", "יום ראשון", "יום שני", "יום שלישי", "יום רביעי", "יום חמישי", "יום שישי", "יום שבת"
	};

	public final String lang;
	public final int dayOfWeek;

	public HebrewWeekdayDayNames(String lang, int dayOfWeek) {
		this.lang = lang;
		this.dayOfWeek = dayOfWeek;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[dayOfWeek];
		return NAMES_HE[dayOfWeek];
	}
}
