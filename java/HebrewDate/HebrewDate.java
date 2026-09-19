package HebrewDate;

import Date.Date;
import Date.Weekday;

public class HebrewDate implements Comparable<HebrewDate> {
	public static final int EPOCH = 347996;

	public final int year;
	public final int month;
	public final int day;
	public final int ordinal;

	public HebrewDate() {
		this(1, HebrewMonth.TISHREI.value, 1);
	}

	public HebrewDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;

		int raw = day;
		if (month < HebrewMonth.TISHREI.value) {
			for (int m = HebrewMonth.TISHREI.value; m <= numMonthInYear(year); m++) {
				raw += numDaysInMonth(year, m);
			}
			for (int m = HebrewMonth.NISAN.value; m < month; m++) {
				raw += numDaysInMonth(year, m);
			}
		} else {
			for (int m = HebrewMonth.TISHREI.value; m < month; m++) {
				raw += numDaysInMonth(year, m);
			}
		}
		raw += elapsedDays(year);
		this.ordinal = raw + EPOCH;
	}

	public static HebrewDate fromOrdinal(int ordinal) {
		int raw = ordinal - EPOCH;

		int y = raw / 366;
		while (true) {
			int tempRaw = new HebrewDate(y + 1, HebrewMonth.TISHREI.value, 1).ordinal - EPOCH;
			if (raw < tempRaw) break;
			y++;
		}

		int raw1Nisan = new HebrewDate(y, HebrewMonth.NISAN.value, 1).ordinal - EPOCH;
		int start = (raw < raw1Nisan) ? HebrewMonth.TISHREI.value : HebrewMonth.NISAN.value;

		int m = start;
		while (true) {
			int monthEnd = new HebrewDate(y, m, numDaysInMonth(y, m)).ordinal - EPOCH;
			if (raw <= monthEnd) break;
			m++;
		}

		int day = raw - (new HebrewDate(y, m, 1).ordinal - EPOCH) + 1;

		return new HebrewDate(y, m, day);
	}

	public static HebrewDate fromDate(Date date) {
		return fromOrdinal(date.ordinal);
	}

	public HebrewDate next() {
		return fromOrdinal(ordinal + 1);
	}

	public HebrewDate prev() {
		return fromOrdinal(ordinal - 1);
	}

	public static boolean isLeapYear(int year) {
		return ((year * 7) + 1) % 19 < 7;
	}

	public static int numMonthInYear(int year) {
		return isLeapYear(year) ? HebrewMonth.ADAR_II.value : HebrewMonth.ADAR.value;
	}

	public static int elapsedDays(int year) {
		int monthsElapsed = 235 * ((year - 1) / 19);
		monthsElapsed += 12 * ((year - 1) % 19);
		monthsElapsed += (((year - 1) % 19) * 7 + 1) / 19;

		int partsElapsed = ((monthsElapsed % 1080) * 793) + 204;
		int hoursElapsed = 5 + (monthsElapsed * 12)
				+ ((monthsElapsed / 1080) * 793)
				+ (partsElapsed / 1080);

		int day = 1 + (29 * monthsElapsed) + (hoursElapsed / 24);
		int parts = ((hoursElapsed % 24) * 1080) + (partsElapsed % 1080);

		boolean addDay = (parts >= 19440)
				|| (day % 7 == 2 && parts >= 9924 && !isLeapYear(year))
				|| (day % 7 == 1 && parts >= 16789 && isLeapYear(year - 1));

		int altDay = addDay ? day + 1 : day;
		if (altDay % 7 == 0 || altDay % 7 == 3 || altDay % 7 == 5) {
			altDay += 1;
		}
		return altDay;
	}

	public static int numDaysInYear(int year) {
		return elapsedDays(year + 1) - elapsedDays(year);
	}

	public static boolean hasLongHeshvan(int year) {
		return numDaysInYear(year) % 10 == 5;
	}

	public static boolean hasShortKislev(int year) {
		return numDaysInYear(year) % 10 == 3;
	}

	public static int numDaysInMonth(int year, int month) {
		if (month == HebrewMonth.IYAR.value || month == HebrewMonth.TAMMUZ.value
				|| month == HebrewMonth.ELUL.value || month == HebrewMonth.TEVET.value
				|| month == HebrewMonth.ADAR_II.value) {
			return 29;
		}
		if (month == HebrewMonth.ADAR.value && !isLeapYear(year)) {
			return 29;
		}
		if (month == HebrewMonth.CHESHVAN.value && !hasLongHeshvan(year)) {
			return 29;
		}
		if (month == HebrewMonth.KISLEV.value && hasShortKislev(year)) {
			return 29;
		}
		return 30;
	}

	public static YearLength yearLengthType(int year) {
		if (hasLongHeshvan(year) && !hasShortKislev(year)) return YearLength.COMPLETE;
		if (!hasLongHeshvan(year) && hasShortKislev(year)) return YearLength.DEFICIENT;
		return YearLength.REGULAR;
	}

	public static int yeartype(int year) {
		int rh = new HebrewDate(year, HebrewMonth.TISHREI.value, 1).dayOfWeek();
		int pesach = new HebrewDate(year, HebrewMonth.NISAN.value, 15).dayOfWeek();
		YearLength length = yearLengthType(year);
		if (!isLeapYear(year)) {
			if (rh == Weekday.MONDAY.value && length == YearLength.DEFICIENT && pesach == Weekday.TUESDAY.value) return YearType.B_CH_G.value;
			if (rh == Weekday.MONDAY.value && length == YearLength.COMPLETE && pesach == Weekday.THURSDAY.value) return YearType.B_SH_H.value;
			if (rh == Weekday.TUESDAY.value && length == YearLength.REGULAR && pesach == Weekday.THURSDAY.value) return YearType.G_K_H.value;
			if (rh == Weekday.THURSDAY.value && length == YearLength.REGULAR && pesach == Weekday.SATURDAY.value) return YearType.H_K_Z.value;
			if (rh == Weekday.THURSDAY.value && length == YearLength.COMPLETE && pesach == Weekday.SUNDAY.value) return YearType.H_SH_A.value;
			if (rh == Weekday.SATURDAY.value && length == YearLength.DEFICIENT && pesach == Weekday.SUNDAY.value) return YearType.Z_CH_A.value;
			if (rh == Weekday.SATURDAY.value && length == YearLength.COMPLETE && pesach == Weekday.TUESDAY.value) return YearType.Z_SH_G.value;
		} else {
			if (rh == Weekday.MONDAY.value && length == YearLength.DEFICIENT && pesach == Weekday.THURSDAY.value) return YearType.B_CH_H.value;
			if (rh == Weekday.MONDAY.value && length == YearLength.COMPLETE && pesach == Weekday.SATURDAY.value) return YearType.B_SH_Z.value;
			if (rh == Weekday.TUESDAY.value && length == YearLength.REGULAR && pesach == Weekday.SATURDAY.value) return YearType.G_K_Z.value;
			if (rh == Weekday.THURSDAY.value && length == YearLength.DEFICIENT && pesach == Weekday.SUNDAY.value) return YearType.H_CH_A.value;
			if (rh == Weekday.THURSDAY.value && length == YearLength.COMPLETE && pesach == Weekday.TUESDAY.value) return YearType.H_SH_G.value;
			if (rh == Weekday.SATURDAY.value && length == YearLength.DEFICIENT && pesach == Weekday.TUESDAY.value) return YearType.Z_CH_G.value;
			if (rh == Weekday.SATURDAY.value && length == YearLength.COMPLETE && pesach == Weekday.THURSDAY.value) return YearType.Z_SH_H.value;
		}
		return 0;
	}

	public static int biblical(int year, int civilMonth) {
		boolean isLeap = isLeapYear(year);
		if (civilMonth <= 6) {
			return civilMonth + 6;
		} else if (civilMonth == 7) {
			return isLeap ? 13 : 1;
		} else {
			return isLeap ? civilMonth - 7 : civilMonth - 6;
		}
	}

	public int dayOfWeek() {
		return (ordinal % 7 + 1) % 7 + 1;
	}

	public HebrewDate shabbat() {
		return fromOrdinal(ordinal + (7 - dayOfWeek()));
	}

	public ShabbatType shabbatType() {
		HebrewDate sh = shabbat();
		int year = sh.year;

		HebrewDate anchor = new HebrewDate(year, HebrewMonth.NISAN.value, 1);
		HebrewDate hahodeshShabbat = fromOrdinal(anchor.ordinal - (anchor.dayOfWeek() % 7));
		if (sh.ordinal == hahodeshShabbat.ordinal) return ShabbatType.HAHODESH;

		if (sh.ordinal == hahodeshShabbat.ordinal - 7) return ShabbatType.PARAH;

		int adarMonth = numMonthInYear(year);
		anchor = new HebrewDate(year, adarMonth, 13);
		HebrewDate zahorShabbat = fromOrdinal(anchor.ordinal - (anchor.dayOfWeek() % 7));
		if (sh.ordinal == zahorShabbat.ordinal) return ShabbatType.ZAHOR;

		anchor = new HebrewDate(year, adarMonth, 1);
		HebrewDate shekalimShabbat = fromOrdinal(anchor.ordinal - (anchor.dayOfWeek() % 7));
		if (sh.ordinal == shekalimShabbat.ordinal) return ShabbatType.SHEKALIM;

		anchor = new HebrewDate(year, HebrewMonth.NISAN.value, 14);
		HebrewDate haggadolShabbat = fromOrdinal(anchor.ordinal - (anchor.dayOfWeek() % 7));
		if (sh.ordinal == haggadolShabbat.ordinal) return ShabbatType.HAGGADOL;

		anchor = new HebrewDate(year, HebrewMonth.TISHREI.value, 9);
		HebrewDate shuvahShabbat = fromOrdinal(anchor.ordinal - (anchor.dayOfWeek() % 7));
		if (sh.ordinal == shuvahShabbat.ordinal) return ShabbatType.SHUVAH;

		anchor = new HebrewDate(year, HebrewMonth.AV.value, 9);
		HebrewDate chazonShabbat = fromOrdinal(anchor.ordinal - (anchor.dayOfWeek() % 7));
		if (sh.ordinal == chazonShabbat.ordinal) return ShabbatType.CHAZON;

		HebrewDate dayAfter = fromOrdinal(anchor.ordinal + 1);
		HebrewDate nachamuShabbat = fromOrdinal(dayAfter.ordinal + (7 - dayAfter.dayOfWeek()));
		if (sh.ordinal == nachamuShabbat.ordinal) return ShabbatType.NACHAMU;

		return null;
	}

	public ShabbatChodeshType shabbatChodeshType() {
		HebrewDate sh = shabbat();

		if (sh.day == 1 && sh.month != HebrewMonth.TISHREI.value) {
			return ShabbatChodeshType.ROSH_CHODESH;
		}

		HebrewDate nextDay = fromOrdinal(sh.ordinal + 1);
		if (nextDay.day == 1 && nextDay.month != HebrewMonth.TISHREI.value) {
			return ShabbatChodeshType.MACHAR_CHODESH;
		}

		return null;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof HebrewDate && this.ordinal == ((HebrewDate) other).ordinal;
	}

	@Override
	public int compareTo(HebrewDate other) {
		return Integer.compare(this.ordinal, other.ordinal);
	}
}
