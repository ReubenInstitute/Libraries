import Date.Date;
import HebrewDate.HebrewDate;
import java.time.LocalDate;

public class Test {
	public static void main(String[] args) {
		LocalDate today = LocalDate.now();
		Date gregorian = new Date(today.getYear(), today.getMonthValue(), today.getDayOfMonth());
		HebrewDate hebrew = HebrewDate.fromDate(gregorian);

		System.out.println("Gregorian: " + gregorian.year + "-" + gregorian.month + "-" + gregorian.day);
		System.out.println("Hebrew:    " + hebrew.year + "-" + hebrew.month + "-" + hebrew.day);
	}
}
