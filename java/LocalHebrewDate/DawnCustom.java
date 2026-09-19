package LocalHebrewDate;

public enum DawnCustom {
	DEG_16_1(0),
	DEG_16_9(1),
	DEG_18(2),
	DEG_19_8(3),
	MIN_72(4),
	REL_MIN_72(5);

	public final int value;

	DawnCustom(int value) {
		this.value = value;
	}
}
