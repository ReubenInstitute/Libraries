package HebrewDate;

public class ShabbatTypeNames {
	private static final String[] NAMES_EN = {
		"", "Shekalim", "Zachor", "Parah", "HaChodesh", "HaGadol", "Shuvah", "Chazon", "Nachamu"
	};
	private static final String[] NAMES_HE = {
		"", "שקלים", "זכור", "פרה", "החודש", "הגדול", "שובה", "חזון", "נחמו"
	};

	public final String lang;
	public final ShabbatType shabbatType;

	public ShabbatTypeNames(String lang, ShabbatType shabbatType) {
		this.lang = lang;
		this.shabbatType = shabbatType;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[shabbatType.value];
		return NAMES_HE[shabbatType.value];
	}
}
