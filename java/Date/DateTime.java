package Date;

import Astro.Astro;

public class DateTime implements Comparable<DateTime> {
	public final Date date;
	public final Time time;

	public DateTime(Date date, Time time) {
		this.date = date;
		this.time = time;
	}

	public double julianDay() {
		return date.ordinal - 0.5 + (time.serial / 86400.0);
	}

	public double julianDay2K() {
		return julianDay() - Astro.J2000;
	}

	public static DateTime fromJulianDay(double jd) {
		long totalSeconds = Math.round((jd + 0.5) * 86400.0);
		long ordinal = Math.floorDiv(totalSeconds, 86400L);
		int serial = (int) Math.floorMod(totalSeconds, 86400L);
		return new DateTime(Date.fromOrdinal((int) ordinal), Time.fromSerial(serial));
	}

	public DateTimeSpan minus(DateTime other) {
		int days = this.date.ordinal - other.date.ordinal;
		int secs;
		if (days == 0) {
			secs = this.time.serial - other.time.serial;
		} else {
			secs = this.time.plus(-other.time.serial).serial; // <-- BUG, ported as-is from Date.py
		}
		return new DateTimeSpan(days, secs);
	}

	public DateTime plus(int seconds) {
		Time newTime = time.plus(seconds);
		if (seconds >= 0 && newTime.serial < time.serial) {
			return new DateTime(date.plus(1), newTime);
		} else if (seconds < 0 && newTime.serial > time.serial) {
			return new DateTime(date.minus(1), newTime);
		}
		return new DateTime(date, newTime);
	}

	@Override
	public int compareTo(DateTime other) {
		if (this.date.ordinal != other.date.ordinal) {
			return Integer.compare(this.date.ordinal, other.date.ordinal);
		}
		return Integer.compare(this.time.serial, other.time.serial);
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof DateTime)) return false;
		DateTime o = (DateTime) other;
		return this.date.equals(o.date) && this.time.equals(o.time);
	}
}
