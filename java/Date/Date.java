package Date;

public class Date {
	public static final int EPOCH = 1721425;

	public final int year;
	public final int month;
	public final int day;
	public final int ordinal;

	public Date() {
		this(1, 1, 1);
	}

	public Date(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;

		int serial = day;
		for (int m = 1; m < month; m++) {
			serial += numDaysInMonth(m, year);
		}
		int y = year - 1;
		serial += 365 * y;
		serial += y / 4;
		serial -= y / 100;
		serial += y / 400;
		this.ordinal = serial + EPOCH;
	}

	public static Date fromOrdinal(int ordinal) {
		int serial = ordinal - EPOCH;

		int y = 1;
		while (true) {
			int daysInYear = isLeapYear(y) ? 366 : 365;
			if (serial <= daysInYear) break;
			serial -= daysInYear;
			y++;
		}

		int m = 1;
		while (true) {
			int daysInMonth = numDaysInMonth(m, y);
			if (serial <= daysInMonth) break;
			serial -= daysInMonth;
			m++;
		}

		return new Date(y, m, serial);
	}

	public Date plus(int x) {
		return fromOrdinal(ordinal + x);
	}

	public Date minus(int x) {
		return fromOrdinal(ordinal - x);
	}

	public Date next() {
		return plus(1);
	}

	public Date prev() {
		return minus(1);
	}

	public static boolean isLeapYear(int year) {
		if (year % 4 != 0) return false;
		if (year % 100 != 0) return true;
		return year % 400 == 0;
	}

	public static int numDaysInMonth(int month, int year) {
		if (month == GregorianMonth.FEBRUARY.value) {
			return isLeapYear(year) ? 29 : 28;
		}
		if (month == GregorianMonth.APRIL.value || month == GregorianMonth.JUNE.value
				|| month == GregorianMonth.SEPTEMBER.value || month == GregorianMonth.NOVEMBER.value) {
			return 30;
		}
		return 31;
	}

	public int dayOfWeek() {
		return (ordinal % 7 + 1) % 7 + 1;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Date && this.ordinal == ((Date) other).ordinal;
	}
}
