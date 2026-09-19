package LocalHebrewDate;

public enum Zman {
	YEMAMA_START(1),
	NIGHTFALL(2),
	MIDNIGHT(3),
	DAWN(4),
	MISHEYAKIR(5),
	SUNRISE(6),
	SHEMA_END(7),
	AMIDAH_END(8),
	NOON(9),
	MINCHA_GEDOLAH(10),
	MINCHA_KETANA(11),
	PLAG_MINCHA(12),
	SUNSET(13),
	YEMAMA_END(14),
	MOONRISE(15),
	MOONSET(16);

	public final int value;

	Zman(int value) {
		this.value = value;
	}
}
