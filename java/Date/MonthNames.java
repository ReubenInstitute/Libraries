package Date;

import java.util.HashMap;
import java.util.Map;

public class MonthNames {
	private static final Map<String, String[]> MONTH_NAMES = new HashMap<>();

	static {
		MONTH_NAMES.put("en", new String[]{
			"", "January", "February", "March", "April", "May", "June",
			"July", "August", "September", "October", "November", "December"
		});
		MONTH_NAMES.put("he", new String[]{
			"", "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני",
			"יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"
		});
	}

	public final String lang;
	public final int month;

	public MonthNames(String lang, int month) {
		this.lang = lang;
		this.month = month;
	}

	public String name() {
		return MONTH_NAMES.get(lang)[month];
	}
}
