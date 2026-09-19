package Date;

public class MonthCalendar {
	public final Date[][] dates;

	public MonthCalendar(int year, int month) {
		int daysInMonth = Date.numDaysInMonth(month, year);
		Date firstDate = new Date(year, month, 1);
		int startCol = firstDate.dayOfWeek() - 1;

		dates = new Date[6][7];
		int dayCounter = 1;
		for (int r = 0; r < 6; r++) {
			for (int c = 0; c < 7; c++) {
				if ((r == 0 && c < startCol) || dayCounter > daysInMonth) {
					dates[r][c] = null;
				} else {
					dates[r][c] = new Date(year, month, dayCounter);
					dayCounter++;
				}
			}
		}
	}
}
