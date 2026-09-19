package HebrewDate;

public class HebrewMonthCalendar {
	public final HebrewDate[][] dates;

	public HebrewMonthCalendar(int year, int month) {
		int daysInMonth = HebrewDate.numDaysInMonth(year, month);
		HebrewDate firstDate = new HebrewDate(year, month, 1);
		int startCol = firstDate.dayOfWeek() - 1;

		int totalCells = startCol + daysInMonth;
		int rowCount = (totalCells + 6) / 7;

		dates = new HebrewDate[6][7];

		int dayCounter = 1;
		for (int r = 0; r < rowCount; r++) {
			for (int c = 0; c < 7; c++) {
				if ((r == 0 && c < startCol) || dayCounter > daysInMonth) {
					dates[r][c] = null;
				} else {
					dates[r][c] = new HebrewDate(year, month, dayCounter);
					dayCounter++;
				}
			}
		}
	}
}
