package Hebrew;

public class Diacritic extends Char {
	public Diacritic(String ch) {
		super(ch);
	}

	public boolean isVowel() {
		return ch.equals(Hebrew.SHEVA) || ch.equals(Hebrew.PATAH) || ch.equals(Hebrew.QAMATS)
				|| ch.equals(Hebrew.QAMATS_KATAN) || ch.equals(Hebrew.TSERE) || ch.equals(Hebrew.SEGOL)
				|| ch.equals(Hebrew.HIRIQ) || ch.equals(Hebrew.HOLAM) || ch.equals(Hebrew.HOLAM_HASER)
				|| ch.equals(Hebrew.QUBUTS);
	}

	public boolean isHataf() {
		return ch.equals(Hebrew.HATAF_SEGOL) || ch.equals(Hebrew.HATAF_PATAH) || ch.equals(Hebrew.HATAF_QAMATS);
	}

	public boolean isDagesh() {
		return ch.equals(Hebrew.DAGESH) || ch.equals(Hebrew.RAFE);
	}

	public boolean isRafe() {
		return ch.equals(Hebrew.RAFE);
	}

	public boolean isShinDot() {
		return ch.equals(Hebrew.SHIN.substring(1));
	}

	public boolean isSinDot() {
		return ch.equals(Hebrew.SIN.substring(1));
	}

	public boolean isAbove() {
		return ch.equals(Hebrew.HIRIQ) || ch.equals(Hebrew.TSERE) || ch.equals(Hebrew.SEGOL) || ch.equals(Hebrew.PATAH)
				|| ch.equals(Hebrew.QAMATS) || ch.equals(Hebrew.HOLAM) || ch.equals(Hebrew.HOLAM_HASER)
				|| ch.equals(Hebrew.QUBUTS) || ch.equals(Hebrew.SHIN.substring(1)) || ch.equals(Hebrew.SIN.substring(1));
	}

	public boolean isBelow() {
		return ch.equals(Hebrew.SHEVA) || ch.equals(Hebrew.HATAF_SEGOL) || ch.equals(Hebrew.HATAF_PATAH)
				|| ch.equals(Hebrew.HATAF_QAMATS) || ch.equals(Hebrew.DAGESH) || ch.equals(Hebrew.RAFE);
	}

	public boolean isInside() {
		return false;
	}

	public boolean isShortVowel() {
		return ch.equals(Hebrew.SHEVA) || ch.equals(Hebrew.HATAF_SEGOL) || ch.equals(Hebrew.HATAF_PATAH)
				|| ch.equals(Hebrew.HATAF_QAMATS) || ch.equals(Hebrew.PATAH) || ch.equals(Hebrew.SEGOL);
	}

	public boolean isLongVowel() {
		return ch.equals(Hebrew.QAMATS) || ch.equals(Hebrew.QAMATS_KATAN) || ch.equals(Hebrew.TSERE)
				|| ch.equals(Hebrew.HOLAM) || ch.equals(Hebrew.HOLAM_HASER) || ch.equals(Hebrew.QUBUTS);
	}

	public boolean isReducedVowel() {
		return isHataf();
	}

	public boolean canCombineWithGuttural() {
		return true;
	}

	public boolean canCombineWithFinal() {
		return !isDagesh() || ch.equals(Hebrew.DAGESH);
	}
}
