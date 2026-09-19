package HebrewDate;

public enum ShabbatType {
	NONE(0),
	SHEKALIM(1),
	ZAHOR(2),
	PARAH(3),
	HAHODESH(4),
	HAGGADOL(5),
	SHUVAH(6),
	CHAZON(7),
	NACHAMU(8);

	public final int value;

	ShabbatType(int value) {
		this.value = value;
	}
}
