package Hebrew;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class HebrewNumbers {
	private static final String GERESH = "׳";
	private static final String GERSHAYIM = "״";

	private static final Map<Integer, String> SPECIALS_SUFFIX = new HashMap<>();
	private static final Map<Integer, String> NUMERALS = new HashMap<>();
	private static final Map<Integer, String> SOFIT = new HashMap<>();
	private static final Map<String, Integer> REVERSE_MAP_DICT = new HashMap<>();
	private static final Map<String, Integer> REVERSE_SOFIT_MAP = new HashMap<>();

	static {
		SPECIALS_SUFFIX.put(15, "טו");
		SPECIALS_SUFFIX.put(16, "טז");

		NUMERALS.put(1, "א");
		NUMERALS.put(2, "ב");
		NUMERALS.put(3, "ג");
		NUMERALS.put(4, "ד");
		NUMERALS.put(5, "ה");
		NUMERALS.put(6, "ו");
		NUMERALS.put(7, "ז");
		NUMERALS.put(8, "ח");
		NUMERALS.put(9, "ט");
		NUMERALS.put(10, "י");
		NUMERALS.put(20, "כ");
		NUMERALS.put(30, "ל");
		NUMERALS.put(40, "מ");
		NUMERALS.put(50, "נ");
		NUMERALS.put(60, "ס");
		NUMERALS.put(70, "ע");
		NUMERALS.put(80, "פ");
		NUMERALS.put(90, "צ");
		NUMERALS.put(100, "ק");
		NUMERALS.put(200, "ר");
		NUMERALS.put(300, "ש");
		NUMERALS.put(400, "ת");
		NUMERALS.put(500, "תק");
		NUMERALS.put(600, "תר");
		NUMERALS.put(700, "תש");
		NUMERALS.put(800, "תת");
		NUMERALS.put(900, "תתק");

		SOFIT.put(500, "ך");
		SOFIT.put(600, "ם");
		SOFIT.put(700, "ן");
		SOFIT.put(800, "ף");
		SOFIT.put(900, "ץ");

		for (Map.Entry<Integer, String> e : NUMERALS.entrySet()) {
			REVERSE_MAP_DICT.put(e.getValue(), e.getKey());
		}
		REVERSE_SOFIT_MAP.putAll(REVERSE_MAP_DICT);
		for (Map.Entry<Integer, String> e : SOFIT.entrySet()) {
			REVERSE_SOFIT_MAP.put(e.getValue(), e.getKey());
		}
	}

	private static String addGershayim(String s) {
		if (s.length() == 1) {
			return s + GERESH;
		}
		return s.substring(0, s.length() - 1) + GERSHAYIM + s.substring(s.length() - 1);
	}

	private static String digitLetters(int value, boolean sofitNotation) {
		if (sofitNotation && SOFIT.containsKey(value)) {
			return SOFIT.get(value);
		}
		return NUMERALS.get(value);
	}

	private static String convertLessThan1000(int num, boolean gershayim, boolean sofitNotation) {
		if (num <= 0 || num >= 1000) return "";
		int base = num % 100;
		int hundreds = num - base;
		String remainderStr;
		if (SPECIALS_SUFFIX.containsKey(base)) {
			String suffix = SPECIALS_SUFFIX.get(base);
			if (hundreds == 0) {
				remainderStr = suffix;
			} else {
				remainderStr = digitLetters(hundreds, sofitNotation) + suffix;
			}
		} else {
			StringBuilder parts = new StringBuilder();
			String restStr = Integer.toString(num);
			while (!restStr.isEmpty()) {
				int digit = restStr.charAt(0) - '0';
				restStr = restStr.substring(1);
				if (digit == 0) continue;
				int power = 1;
				for (int i = 0; i < restStr.length(); i++) power *= 10;
				parts.append(digitLetters(power * digit, sofitNotation));
			}
			remainderStr = parts.toString();
		}
		if (gershayim) {
			return addGershayim(remainderStr);
		}
		return remainderStr;
	}

	public static String hebrewNumber(int num) {
		return hebrewNumber(num, false);
	}

	public static String hebrewNumber(int num, boolean sofitNotation) {
		return intToGematria(num, false, false, sofitNotation);
	}

	public static String hebrewFancyNumber(int num) {
		return hebrewFancyNumber(num, false);
	}

	public static String hebrewFancyNumber(int num, boolean sofitNotation) {
		return intToGematria(num, true, false, sofitNotation);
	}

	public static String intToGematria(int num) {
		return intToGematria(num, false, false, false);
	}

	public static String intToGematria(int num, boolean gershayim) {
		return intToGematria(num, gershayim, false, false);
	}

	public static String intToGematria(int num, boolean gershayim, boolean bookNotation, boolean sofitNotation) {
		if (bookNotation && sofitNotation) {
			throw new IllegalArgumentException(
				"book_notation and sofit_notation cannot both be used: book_notation never produces a value in sofit's 500-900 range.");
		}
		if (num < 1) return "";
		if (bookNotation) {
			int tavCount = num / 400;
			int remainder = num % 400;
			StringBuilder resultParts = new StringBuilder();
			if (tavCount > 0) {
				for (int i = 0; i < tavCount; i++) resultParts.append('ת');
			}
			if (remainder > 0) {
				resultParts.append(convertLessThan1000(remainder, false, sofitNotation));
			}
			String finalStr = resultParts.toString();
			if (gershayim && !finalStr.isEmpty()) {
				return addGershayim(finalStr);
			}
			return finalStr;
		} else {
			int thousands = num / 1000;
			int remainder = num % 1000;
			StringBuilder resultParts = new StringBuilder();
			if (thousands > 0) {
				String thousandsStr = intToGematria(thousands, false, false, sofitNotation);
				resultParts.append(thousandsStr).append(GERESH);
			}
			if (remainder > 0) {
				resultParts.append(convertLessThan1000(remainder, gershayim, sofitNotation));
			}
			return resultParts.toString();
		}
	}

	public static int gematriaToInt(String gematriaStr) {
		return gematriaToInt(gematriaStr, false, false);
	}

	public static int gematriaToInt(String gematriaStr, boolean bookNotation, boolean sofitNotation) {
		if (bookNotation && sofitNotation) {
			throw new IllegalArgumentException(
				"book_notation and sofit_notation cannot both be used: book_notation never produces a value in sofit's 500-900 range.");
		}
		if (gematriaStr == null || gematriaStr.isEmpty()) return 0;
		Map<String, Integer> reverseMap = sofitNotation ? REVERSE_SOFIT_MAP : REVERSE_MAP_DICT;
		String cleanStr = gematriaStr.replace(GERSHAYIM, "");

		if (bookNotation) {
			int tavCount = 0;
			for (int i = 0; i < cleanStr.length(); i++) {
				if (cleanStr.charAt(i) == 'ת') {
					tavCount++;
				} else {
					break;
				}
			}
			int tavValue = tavCount * 400;
			String remainderStr = cleanStr.substring(tavCount);
			int remainderValue = 0;
			for (int i = 0; i < remainderStr.length(); i++) {
				char c = remainderStr.charAt(i);
				if (String.valueOf(c).equals(GERESH)) continue;
				remainderValue += reverseMap.getOrDefault(String.valueOf(c), 0);
			}
			return tavValue + remainderValue;
		} else {
			if (cleanStr.length() == 2 && cleanStr.endsWith(GERESH)) {
				return reverseMap.getOrDefault(String.valueOf(cleanStr.charAt(0)), 0);
			}
			int res = 0;
			String[] parts = cleanStr.split(Pattern.quote(GERESH), -1);
			String remainderStr;
			if (parts.length > 1 && !parts[0].isEmpty()) {
				String thousandsStr = parts[0];
				int thousandsVal = 0;
				for (int i = 0; i < thousandsStr.length(); i++) {
					thousandsVal += reverseMap.getOrDefault(String.valueOf(thousandsStr.charAt(i)), 0);
				}
				res += thousandsVal * 1000;
				remainderStr = parts[1];
			} else {
				remainderStr = parts[0];
			}
			for (int i = 0; i < remainderStr.length(); i++) {
				res += reverseMap.getOrDefault(String.valueOf(remainderStr.charAt(i)), 0);
			}
			return res;
		}
	}
}
