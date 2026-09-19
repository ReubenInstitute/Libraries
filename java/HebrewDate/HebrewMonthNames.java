package HebrewDate;

public class HebrewMonthNames {
	private static final String[] NAMES_EN = {
		"", "Nisan", "Iyar", "Sivan", "Tammuz", "Av", "Elul",
		"Tishrei", "Cheshvan", "Kislev", "Tevet", "Shevat", "Adar", "Adar II"
	};
	private static final String[] NAMES_HE = {
		"", "ניסן", "אייר", "סיוון", "תמוז", "אב", "אלול",
		"תשרי", "חשון", "כסלו", "טבת", "שבט", "אדר", "אדר ב׳"
	};

	public final String lang;
	public final int year;
	public final int month;

	public HebrewMonthNames(String lang, int year, int month) {
		this.lang = lang;
		this.year = year;
		this.month = month;
	}

	public String name() {
		if ("en".equals(lang)) {
			if (month == HebrewMonth.ADAR.value && HebrewDate.isLeapYear(year)) {
				return "Adar I";
			}
			return NAMES_EN[month];
		} else if ("he".equals(lang)) {
			if (month == HebrewMonth.ADAR.value && HebrewDate.isLeapYear(year)) {
				return "אדר א׳";
			}
			return NAMES_HE[month];
		}
		return "";
	}
}
