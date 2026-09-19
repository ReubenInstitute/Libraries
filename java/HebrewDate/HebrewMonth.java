package HebrewDate;

public enum HebrewMonth {
	NISAN(1),
	IYAR(2),
	SIVAN(3),
	TAMMUZ(4),
	AV(5),
	ELUL(6),
	TISHREI(7),
	CHESHVAN(8),
	KISLEV(9),
	TEVET(10),
	SHEVAT(11),
	ADAR(12),
	ADAR_II(13);

	public final int value;

	HebrewMonth(int value) {
		this.value = value;
	}
}
