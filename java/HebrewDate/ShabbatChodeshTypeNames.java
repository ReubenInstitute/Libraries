package HebrewDate;

public class ShabbatChodeshTypeNames {
	private static final String[] NAMES_EN = {"", "Machar Chodesh", "Rosh Chodesh"};
	private static final String[] NAMES_HE = {"", "מחר חודש", "ראש חודש"};

	public final String lang;
	public final ShabbatChodeshType shabbatChodeshType;

	public ShabbatChodeshTypeNames(String lang, ShabbatChodeshType shabbatChodeshType) {
		this.lang = lang;
		this.shabbatChodeshType = shabbatChodeshType;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[shabbatChodeshType.value];
		return NAMES_HE[shabbatChodeshType.value];
	}
}
