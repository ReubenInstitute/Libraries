package HebrewDate;

public enum ShabbatChodeshType {
	NONE(0),
	MACHAR_CHODESH(1),
	ROSH_CHODESH(2);

	public final int value;

	ShabbatChodeshType(int value) {
		this.value = value;
	}
}
