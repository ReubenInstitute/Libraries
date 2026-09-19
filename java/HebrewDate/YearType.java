package HebrewDate;

public enum YearType {
	B_CH_G(1),
	B_SH_H(2),
	G_K_H(3),
	H_K_Z(4),
	H_SH_A(5),
	Z_CH_A(6),
	Z_SH_G(7),
	B_CH_H(8),
	B_SH_Z(9),
	G_K_Z(10),
	H_CH_A(11),
	H_SH_G(12),
	Z_CH_G(13),
	Z_SH_H(14);

	public final int value;

	YearType(int value) {
		this.value = value;
	}
}
