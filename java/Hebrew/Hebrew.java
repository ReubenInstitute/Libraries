package Hebrew;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hebrew {
	public static final String ALEF = "א";
	public static final String BET = "ב";
	public static final String GIMEL = "ג";
	public static final String DALET = "ד";
	public static final String VAV = "ו";
	public static final String ZAYIN = "ז";
	public static final String TET = "ט";
	public static final String YOD = "י";
	public static final String KAF = "כ";
	public static final String LAMED = "ל";
	public static final String MEM = "מ";
	public static final String NUN = "נ";
	public static final String SAMEKH = "ס";
	public static final String PE = "פ";
	public static final String TSADI = "צ";
	public static final String QOF = "ק";
	public static final String RESH = "ר";
	public static final String BASESHIN = "ש";
	public static final String SHIN = BASESHIN + "ׁ";
	public static final String SIN = BASESHIN + "ׂ";
	public static final String TAV = "ת";

	public static final String WIDE_ALEF = "ﬡ";
	public static final String WIDE_DALET = "ﬢ";
	public static final String WIDE_HE = "ﬣ";
	public static final String WIDE_KAF = "ﬤ";
	public static final String WIDE_LAMED = "ﬥ";
	public static final String WIDE_RESH = "ﬧ";
	public static final String WIDE_TAV = "ﬨ";

	public static final String HE = "ה";
	public static final String HET = "ח";
	public static final String AYIN = "ע";

	public static final String FINAL_KAF = "ך";
	public static final String FINAL_MEM = "ם";
	public static final String FINAL_NUN = "ן";
	public static final String FINAL_PE = "ף";
	public static final String FINAL_TSADI = "ץ";
	public static final String WIDE_FINAL_MEM = "ﬦ";

	public static final String SHEVA = "ְ";
	public static final String PATAH = "ַ";
	public static final String QAMATS = "ָ";
	public static final String QAMATS_KATAN = "ׇ";
	public static final String TSERE = "ֵ";
	public static final String SEGOL = "ֶ";
	public static final String HIRIQ = "ִ";
	public static final String HOLAM = "ֹ";
	public static final String HOLAM_HASER = "ֺ";
	public static final String QUBUTS = "ֻ";

	public static final String HATAF_SEGOL = "ֱ";
	public static final String HATAF_PATAH = "ֲ";
	public static final String HATAF_QAMATS = "ֳ";

	public static final String DAGESH = "ּ";
	public static final String RAFE = "ֿ";

	public static final String ETNAHTA = "֑";
	public static final String SEGOL_ACCENT = "֒";
	public static final String SHALSHELET = "֓";
	public static final String ZAQEF_QATAN = "֔";
	public static final String ZAQEF_GADOL = "֕";
	public static final String TIPEHA = "֖";
	public static final String REVIA = "֗";
	public static final String ZARQA = "֘";
	public static final String PASHTA = "֙";
	public static final String YETIV = "֚";
	public static final String TEVIR = "֛";
	public static final String GERESH_CANTILLATION = "֜";
	public static final String GERESH_MUQDAM = "֝";
	public static final String GERSHAYIM_CANTILLATION = "֞";
	public static final String QARNEY_PARA = "֟";
	public static final String TELISHA_GEDOLA = "֠";
	public static final String PAZER = "֡";
	public static final String QADMA = "֨";
	public static final String TELISHA_QETANA = "֩";
	public static final String YERAH_BEN_YOMO = "֪";
	public static final String OLE = "֫";
	public static final String ILUY = "֬";
	public static final String DEHI = "֭";
	public static final String ZINOR = "֮";

	public static final String MUNAH = "֢";
	public static final String MAHAPAKH = "֣";
	public static final String MERKHA = "֤";
	public static final String MERKHA_KEFULA = "֥";
	public static final String DARGA = "֦";
	public static final String QADMA_OLD = "֧";
	public static final String METEG = "ֽ";

	public static final String UPPER_DOT = "ׄ";
	public static final String LOWER_DOT = "ׅ";
	public static final String NUN_HAFUKHA = "׆";
	public static final String MASORA_CIRCLE = "֯";

	public static final String SOF_PASUK = "׃";
	public static final String PASEQ = "׀";
	public static final String MAQAF = "־";
	public static final String GERESH = "׳";
	public static final String GERSHAYIM = "״";

	public static final String YIDDISH_VAV_VAV = "װ";
	public static final String YIDDISH_VAV_YOD = "ױ";
	public static final String YIDDISH_YOD_YOD = "ײ";
	public static final String ALEF_LAMED = "ﭏ";
	public static final String YIDDISH_PASHTA = "ﬞ";

	public static final List<String> PLAIN_LETTERS = List.of(
		BET, GIMEL, DALET, VAV, ZAYIN, TET, YOD, KAF, LAMED, MEM, NUN, SAMEKH, PE, TSADI, QOF, RESH, SHIN, SIN, TAV,
		WIDE_DALET, WIDE_KAF, WIDE_LAMED, WIDE_RESH, WIDE_TAV
	);

	public static final List<String> GUTTURALS = List.of(ALEF, HE, HET, AYIN, WIDE_ALEF, WIDE_HE);

	public static final List<String> FINALS = List.of(FINAL_KAF, FINAL_MEM, FINAL_NUN, FINAL_PE, FINAL_TSADI, WIDE_FINAL_MEM);

	public static final List<String> ALL_LETTERS;
	static {
		List<String> all = new ArrayList<>();
		all.addAll(PLAIN_LETTERS);
		all.addAll(GUTTURALS);
		all.addAll(FINALS);
		ALL_LETTERS = List.copyOf(all);
	}

	// Single-character form of ALL_LETTERS: SHIN/SIN in ALL_LETTERS are each a
	// base letter + combining dot (two chars), which never matches a bare "ש"
	// (no dot) when filtering text one character at a time. Taking just the
	// leading/identifying character of each entry gives the right per-char set.
	public static final Set<String> ALL_LETTER_CHARS;
	static {
		Set<String> chars = new HashSet<>();
		for (String letter : ALL_LETTERS) chars.add(letter.substring(0, 1));
		ALL_LETTER_CHARS = Set.copyOf(chars);
	}

	public static final List<String> DIACRITICS = List.of(
		SHEVA, PATAH, QAMATS, QAMATS_KATAN, TSERE, SEGOL, HIRIQ, HOLAM, HOLAM_HASER, QUBUTS
	);

	public static final List<String> HATAFS = List.of(HATAF_SEGOL, HATAF_PATAH, HATAF_QAMATS);

	public static final List<String> UPPER_CANTILLATIONS = List.of(
		ETNAHTA, SEGOL_ACCENT, SHALSHELET, ZAQEF_QATAN, ZAQEF_GADOL, TIPEHA, REVIA, ZARQA, PASHTA, YETIV, TEVIR,
		GERESH_CANTILLATION, GERESH_MUQDAM, GERSHAYIM_CANTILLATION, QARNEY_PARA, TELISHA_GEDOLA, PAZER, QADMA,
		TELISHA_QETANA, YERAH_BEN_YOMO, OLE, ILUY, DEHI, ZINOR
	);

	public static final List<String> LOWER_CANTILLATIONS = List.of(
		MUNAH, MAHAPAKH, MERKHA, MERKHA_KEFULA, DARGA, QADMA_OLD, METEG
	);

	public static final List<String> CANTILLATIONS;
	static {
		List<String> c = new ArrayList<>();
		c.addAll(UPPER_CANTILLATIONS);
		c.addAll(LOWER_CANTILLATIONS);
		CANTILLATIONS = List.copyOf(c);
	}

	public static final List<String> MARKS = List.of(UPPER_DOT, LOWER_DOT, NUN_HAFUKHA, MASORA_CIRCLE);

	public static final List<String> PUNCTUATION = List.of(SOF_PASUK, PASEQ, MAQAF, GERESH, GERSHAYIM);

	public static final List<String> EXTRA = List.of(
		YIDDISH_VAV_VAV, YIDDISH_VAV_YOD, YIDDISH_YOD_YOD, ALEF_LAMED, YIDDISH_PASHTA
	);

	public static final Set<String> DAGESH_HAZZAK_LETTERS = Set.of(
		ZAYIN, TET, YOD, LAMED, MEM, NUN, SAMEKH, TSADI, QOF, SHIN.substring(0, 1), FINAL_KAF
	);

	public static final List<Letter> letters;
	public static final List<Letter> gutturals;
	public static final List<Letter> finals;
	public static final List<Diacritic> diacritics;
	public static final List<Diacritic> hatafs;

	public static final List<Letter> normalLetters;
	public static final List<Letter> wideLetters;
	public static final List<Letter> normalGutturals;
	public static final List<Letter> wideGutturals;
	public static final List<Letter> normalFinals;
	public static final List<Letter> wideFinals;

	public static final List<String> COMBINING_ORDER;

	static {
		List<Letter> l = new ArrayList<>();
		for (String c : PLAIN_LETTERS) l.add(new Letter(c));
		letters = List.copyOf(l);

		List<Letter> g = new ArrayList<>();
		for (String c : GUTTURALS) g.add(new Letter(c));
		gutturals = List.copyOf(g);

		List<Letter> f = new ArrayList<>();
		for (String c : FINALS) f.add(new Letter(c));
		finals = List.copyOf(f);

		List<Diacritic> d = new ArrayList<>();
		for (String c : DIACRITICS) d.add(new Diacritic(c));
		diacritics = List.copyOf(d);

		List<Diacritic> h = new ArrayList<>();
		for (String c : HATAFS) h.add(new Diacritic(c));
		hatafs = List.copyOf(h);

		List<Letter> nl = new ArrayList<>();
		List<Letter> wl = new ArrayList<>();
		for (Letter letter : letters) (letter.isWide() ? wl : nl).add(letter);
		normalLetters = List.copyOf(nl);
		wideLetters = List.copyOf(wl);

		List<Letter> ng = new ArrayList<>();
		List<Letter> wg = new ArrayList<>();
		for (Letter guttural : gutturals) (guttural.isWide() ? wg : ng).add(guttural);
		normalGutturals = List.copyOf(ng);
		wideGutturals = List.copyOf(wg);

		List<Letter> nf = new ArrayList<>();
		List<Letter> wf = new ArrayList<>();
		for (Letter fin : finals) (fin.isWide() ? wf : nf).add(fin);
		normalFinals = List.copyOf(nf);
		wideFinals = List.copyOf(wf);

		List<String> co = new ArrayList<>();
		co.add(SHIN.substring(1));
		co.add(SIN.substring(1));
		co.add(DAGESH);
		co.add(RAFE);
		co.addAll(DIACRITICS);
		co.addAll(HATAFS);
		co.addAll(UPPER_CANTILLATIONS);
		co.addAll(LOWER_CANTILLATIONS);
		co.addAll(MARKS);
		COMBINING_ORDER = List.copyOf(co);
	}

	private static final Pattern GRAPHEME_PATTERN = Pattern.compile("\\P{M}\\p{M}*", Pattern.UNICODE_CHARACTER_CLASS);

	private static Comparator<String> combiningOrderComparator() {
		return Comparator.comparingInt(c -> {
			int idx = COMBINING_ORDER.indexOf(c);
			return idx >= 0 ? idx : COMBINING_ORDER.size();
		});
	}

	public static String normalize(String text) {
		text = Normalizer.normalize(text, Normalizer.Form.NFC);
		Matcher matcher = GRAPHEME_PATTERN.matcher(text);
		StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			String block = matcher.group();
			if (block.length() == 1) {
				result.append(block);
			} else {
				String base = block.substring(0, 1);
				List<String> modifiers = new ArrayList<>();
				for (int i = 1; i < block.length(); i++) {
					modifiers.add(String.valueOf(block.charAt(i)));
				}
				modifiers.sort(combiningOrderComparator());
				result.append(base);
				for (String m : modifiers) result.append(m);
			}
		}
		return result.toString();
	}

	public static String addDageshHazzak(String text) {
		text = normalize(text);
		Matcher matcher = GRAPHEME_PATTERN.matcher(text);
		StringBuilder result = new StringBuilder();
		while (matcher.find()) {
			String block = matcher.group();
			String base = block.substring(0, 1);
			List<String> modifiers = new ArrayList<>();
			for (int i = 1; i < block.length(); i++) {
				modifiers.add(String.valueOf(block.charAt(i)));
			}
			String baseNormal = new Letter(base).baseLetter();
			if (DAGESH_HAZZAK_LETTERS.contains(baseNormal)) {
				int dageshCount = 0;
				for (String m : modifiers) if (m.equals(DAGESH)) dageshCount++;
				if (dageshCount == 1) {
					modifiers.add(DAGESH);
					modifiers.sort(combiningOrderComparator());
				}
			}
			result.append(base);
			for (String m : modifiers) result.append(m);
		}
		return result.toString();
	}

	public static String stripPunctuation(String text) {
		text = text.replaceAll("[,.!?:;\\-–—…'\"]", "");
		text = text.replaceAll(" +", " ");
		return text.strip();
	}

	public static String stripCantillation(String text) {
		return stripCantillation(text, true);
	}

	public static String stripCantillation(String text, boolean stripMeteg) {
		if (stripMeteg) {
			text = text.replace(METEG, "");
		}
		StringBuilder nonMeteg = new StringBuilder();
		for (String c : CANTILLATIONS) {
			if (!c.equals(METEG)) nonMeteg.append(c);
		}
		return text.replaceAll("[" + nonMeteg + "]", "");
	}

	public static String stripDiacritics(String text) {
		return stripDiacritics(text, true);
	}

	public static String stripDiacritics(String text, boolean stripRafe) {
		StringBuilder niqqud = new StringBuilder();
		for (String c : DIACRITICS) niqqud.append(c);
		for (String c : HATAFS) niqqud.append(c);
		niqqud.append(DAGESH).append(SHIN.substring(1)).append(SIN.substring(1));
		text = text.replaceAll("[" + niqqud + "]", "");
		if (stripRafe) {
			text = text.replace(RAFE, "");
		}
		return text;
	}

	public static String stripHebrewPunctuation(String text) {
		return stripHebrewPunctuation(text, true, true);
	}

	public static String stripHebrewPunctuation(String text, boolean stripMaqaf, boolean stripMasora) {
		text = text.replace(PASEQ, "").replace(SOF_PASUK, "");
		if (stripMaqaf) {
			text = text.replace(MAQAF, " ");
		}
		if (stripMasora) {
			text = text.replace(UPPER_DOT, "").replace(LOWER_DOT, "");
		}
		return text;
	}

	public static String stripYy(String text) {
		return text.replace("יְיָ", "יי");
	}

	public static String stripYhwh(String text) {
		String y = YOD;
		String h = HE;
		String bigH = WIDE_HE;
		String w = VAV;

		StringBuilder cantillationChars = new StringBuilder();
		for (String c : CANTILLATIONS) cantillationChars.append(c);
		cantillationChars.append(MASORA_CIRCLE).append(METEG).append(PASEQ).append(SOF_PASUK);
		String a = "([" + cantillationChars + "]*)";

		StringBuilder diacriticChars = new StringBuilder();
		for (String c : DIACRITICS) diacriticChars.append(c);
		for (String c : HATAFS) diacriticChars.append(c);
		diacriticChars.append(DAGESH).append(SHIN.substring(1)).append(SIN.substring(1));
		String p = "([" + diacriticChars + "]*)";

		String patternStr = y + p + a + h + p + a + w + p + a + h + p + a;
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(text);

		List<int[]> spans = new ArrayList<>();
		List<String> replacements = new ArrayList<>();
		while (matcher.find()) {
			spans.add(new int[]{matcher.start(), matcher.end()});
			String a1 = matcher.group(2);
			String a2 = matcher.group(4);
			String a3 = matcher.group(6);
			String a4 = matcher.group(8);
			replacements.add(y + a1 + bigH + a2 + w + a3 + bigH + a4);
		}

		for (int i = spans.size() - 1; i >= 0; i--) {
			int start = spans.get(i)[0];
			int end = spans.get(i)[1];
			text = text.substring(0, start) + replacements.get(i) + text.substring(end);
		}
		return text;
	}

	public static String presentation(String text) {
		return stripCantillation(stripHebrewPunctuation(stripYhwh(text)));
	}

	public static String rawText(String text) {
		text = stripYhwh(text);
		text = text.replace(MAQAF, " ");
		StringBuilder filtered = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			String c = String.valueOf(text.charAt(i));
			if (ALL_LETTER_CHARS.contains(c) || c.equals(" ")) {
				filtered.append(c);
			}
		}
		return filtered.toString().replaceAll(" +", " ").strip();
	}

	public static String transliterate(String text) {
		String hebrewToAscii = "ABGDHVZHTIKKLMMNNSAPPSSQRST";
		text = stripCantillation(text);
		text = stripDiacritics(text);
		StringBuilder result = new StringBuilder();
		char alefChar = ALEF.charAt(0);
		char tavChar = TAV.charAt(0);
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= alefChar && c <= tavChar) {
				int index = c - alefChar;
				if (index < hebrewToAscii.length()) {
					result.append(hebrewToAscii.charAt(index));
				} else {
					result.append(c);
				}
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}
}
