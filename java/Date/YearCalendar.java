package Date;

import java.util.ArrayList;
import java.util.List;

public class YearCalendar {
	public final int year;
	public final MonthCalendar[] monthCalendars;

	public YearCalendar(int year) {
		this.year = year;
		this.monthCalendars = new MonthCalendar[12];
		for (int m = 1; m <= 12; m++) {
			monthCalendars[m - 1] = new MonthCalendar(year, m);
		}
	}

	public List<Date[]> weeks() {
		Date start = new Date(year, 1, 1);
		Date end = new Date(year, 12, 31);

		Date cur = start;
		while (cur.dayOfWeek() != Weekday.SUNDAY.value) {
			cur = cur.prev();
		}
		Date firstSunday = cur;

		cur = end;
		while (cur.dayOfWeek() != Weekday.SATURDAY.value) {
			cur = cur.next();
		}
		Date lastSaturday = cur;

		List<Date[]> weeks = new ArrayList<>();
		Date d = firstSunday;
		while (d.ordinal <= lastSaturday.ordinal) {
			Date[] week = new Date[7];
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
}
