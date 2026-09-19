package LocalHebrewDate;

public enum NightfallCustom {
	DEG_5_95(0),
	DEG_6(1),
	DEG_6_45(2),
	DEG_7_1(3),
	DEG_8(4),
	DEG_8_5(5),
	MIN_72(6),
	REL_MIN_72(7),
	DEG_16_1(8),
	REL_MIN_90(9),
	DEG_18(10),
	DEG_19_8(11);

	public final int value;

	NightfallCustom(int value) {
		this.value = value;
	}
}
