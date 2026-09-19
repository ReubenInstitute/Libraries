package LocalHebrewDate;

public enum BandId {
	NONE(0),
	NIGHTFALL(1),
	DAWN(2),
	MISHEYAKIR(3),
	SHEMA(4),
	AMIDAH(5),
	MINCHA_GEDOLAH(6),
	MINCHA_KETANA(7),
	PLAG_MINCHA(8),
	WATCH1(9),
	WATCH2(10),
	WATCH3(11),
	REST(101),
	REST_STRIPED(102),
	YOMTOV(103),
	FAST(104),
	FAST_STRIPED(105),
	NIGHT(201),
	DAY(202);

	public final int value;

	BandId(int value) {
		this.value = value;
	}
}
