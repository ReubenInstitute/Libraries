package HebrewDate;

public enum YearLength {
	DEFICIENT(1),
	REGULAR(2),
	COMPLETE(3);

	public final int value;

	YearLength(int value) {
		this.value = value;
	}
}
