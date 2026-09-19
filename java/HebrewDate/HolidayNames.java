package HebrewDate;

public class HolidayNames {
	private static final String[] NAMES_EN = {
		"",
		"Pesach",
		"Pesach II",
		"Pesach VII",
		"Pesach VIII",
		"Yom HaShoah",
		"Yom HaZikaron",
		"Yom HaAtzmaut",
		"Pesach Sheni",
		"Lag BaOmer",
		"Yom Yerushalayim",
		"Shavuot",
		"Shavuot II",
		"Fast of Tammuz",
		"Fast of Av",
		"Tu B'Av",
		"Rosh Hashanah",
		"Rosh Hashanah II",
		"Fast of Gedaliah",
		"Yom Kippur",
		"Sukkot",
		"Sukkot II",
		"Hoshana Rabbah",
		"Shemini Atzeret",
		"Simchat Torah",
		"Simchat Torah / Shemini Atzeret",
		"Chanukkah I",
		"Chanukkah II",
		"Chanukkah III",
		"Chanukkah IV",
		"Chanukkah V",
		"Chanukkah VI",
		"Chanukkah VII",
		"Chanukkah VIII",
		"Fast of Tevet",
		"Tu BiShvat",
		"Fast of Esther",
		"Purim",
		"Shushan Purim",
	};
	private static final String[] NAMES_HE = {
		"",
		"פסח",
		"פסח שני (גלות)",
		"פסח שביעי",
		"פסח שמיני (גלות)",
		"יום השואה",
		"יום הזיכרון",
		"יום העצמאות",
		"פסח שני",
		"ל״ג בעומר",
		"יום ירושלים",
		"שבועות",
		"שבועות שני (גלות)",
		"תענית תמוז",
		"תענית אב",
		"ט״ו באב",
		"ראש השנה",
		"ראש השנה ב׳",
		"צום גדליה",
		"יום כיפור",
		"סוכות",
		"סוכות שני (גלות)",
		"הושענא רבה",
		"שמיני עצרת",
		"שמחת תורה",
		"שמחת תורה / שמיני עצרת",
		"חנוכה א׳",
		"חנוכה ב׳",
		"חנוכה ג׳",
		"חנוכה ד׳",
		"חנוכה ה׳",
		"חנוכה ו׳",
		"חנוכה ז׳",
		"חנוכה ח׳",
		"תענית טבת",
		"ט״ו בשבט",
		"תענית אסתר",
		"פורים",
		"שושן פורים",
	};

	public final String lang;
	public final int id;

	public HolidayNames(String lang, int id) {
		this.lang = lang;
		this.id = id;
	}

	public String name() {
		if ("en".equals(lang)) return NAMES_EN[id];
		return NAMES_HE[id];
	}
}
