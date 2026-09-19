package Date;

import java.util.HashMap;
import java.util.Map;

public class WeekdayNames {
	private static final Map<String, String[]> WEEKDAY_NAMES = new HashMap<>();
	private static final Map<String, String[]> SHORT_NAMES = new HashMap<>();

	static {
		WEEKDAY_NAMES.put("en", new String[]{"", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"});
		WEEKDAY_NAMES.put("he", new String[]{"", "ראשון", "שני", "שלישי", "רביעי", "חמישי", "שישי", "שבת"});
		SHORT_NAMES.put("en", new String[]{"", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"});
		SHORT_NAMES.put("he", new String[]{"", "א", "ב", "ג", "ד", "ה", "ו", "ש"});
	}

	public final String lang;
	public final int dayOfWeek;

	public WeekdayNames(String lang, int dayOfWeek) {
		this.lang = lang;
		this.dayOfWeek = dayOfWeek;
	}

	public String name() {
		return WEEKDAY_NAMES.get(lang)[dayOfWeek];
	}

	public String shortname() {
		return SHORT_NAMES.get(lang)[dayOfWeek];
	}
}
