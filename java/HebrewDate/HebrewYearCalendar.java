package HebrewDate;

import java.util.ArrayList;
import java.util.List;

public class HebrewYearCalendar {
	public final int year;
	public final List<HebrewMonthCalendar> monthCalendars;

	public HebrewYearCalendar(int year) {
		this.year = year;
		this.monthCalendars = new ArrayList<>();
		for (int month = HebrewMonth.TISHREI.value; month <= HebrewDate.numMonthInYear(year); month++) {
			monthCalendars.add(new HebrewMonthCalendar(year, month));
		}
		for (int month = HebrewMonth.NISAN.value; month < HebrewMonth.TISHREI.value; month++) {
			monthCalendars.add(new HebrewMonthCalendar(year, month));
		}
	}

	public List<HebrewDate[]> weeks() {
		HebrewDate start = new HebrewDate(year, HebrewMonth.TISHREI.value, 1);
		HebrewDate end = HebrewDate.fromOrdinal(start.ordinal + HebrewDate.numDaysInYear(year) - 1);
		HebrewDate cur = start;
		while (cur.dayOfWeek() != 1) {
			cur = cur.prev();
		}
		HebrewDate firstSunday = cur;
		cur = end;
		while (cur.dayOfWeek() != 7) {
			cur = cur.next();
		}
		HebrewDate lastSaturday = cur;
		List<HebrewDate[]> weeks = new ArrayList<>();
		HebrewDate d = firstSunday;
		while (d.ordinal <= lastSaturday.ordinal) {
			HebrewDate[] week = new HebrewDate[7];
			for (int i = 0; i < 7; i++) {
				if (d.ordinal < start.ordinal || d.ordinal > end.ordinal) {
					week[i] = null;
				} else {
					week[i] = d;
				}
				d = d.next();
			}
			weeks.add(week);
		}
		return weeks;
	}

	public List<HebrewDate> saturdays() {
		List<HebrewDate> result = new ArrayList<>();
		for (HebrewDate[] week : weeks()) {
			if (week[6] != null) result.add(week[6]);
		}
		return result;
	}
}
