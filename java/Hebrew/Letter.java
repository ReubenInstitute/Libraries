package Hebrew;

public class Letter extends Char {
	public Letter(String ch) {
		super(ch);
	}

	public String baseLetter() {
		if (ch.equals(Hebrew.FINAL_KAF) || ch.equals(Hebrew.FINAL_MEM) || ch.equals(Hebrew.FINAL_NUN)
				|| ch.equals(Hebrew.FINAL_PE) || ch.equals(Hebrew.FINAL_TSADI)) {
			if (ch.equals(Hebrew.FINAL_KAF)) return Hebrew.KAF;
			if (ch.equals(Hebrew.FINAL_MEM)) return Hebrew.MEM;
			if (ch.equals(Hebrew.FINAL_NUN)) return Hebrew.NUN;
			if (ch.equals(Hebrew.FINAL_PE)) return Hebrew.PE;
			return Hebrew.TSADI;
		}
		if (ch.equals(Hebrew.WIDE_ALEF) || ch.equals(Hebrew.WIDE_DALET) || ch.equals(Hebrew.WIDE_HE)
				|| ch.equals(Hebrew.WIDE_KAF) || ch.equals(Hebrew.WIDE_LAMED) || ch.equals(Hebrew.WIDE_FINAL_MEM)
				|| ch.equals(Hebrew.WIDE_RESH) || ch.equals(Hebrew.WIDE_TAV)) {
			if (ch.equals(Hebrew.WIDE_ALEF)) return Hebrew.ALEF;
			if (ch.equals(Hebrew.WIDE_DALET)) return Hebrew.DALET;
			if (ch.equals(Hebrew.WIDE_HE)) return Hebrew.HE;
			if (ch.equals(Hebrew.WIDE_KAF)) return Hebrew.KAF;
			if (ch.equals(Hebrew.WIDE_LAMED)) return Hebrew.LAMED;
			if (ch.equals(Hebrew.WIDE_FINAL_MEM)) return Hebrew.MEM;
			if (ch.equals(Hebrew.WIDE_RESH)) return Hebrew.RESH;
			return Hebrew.TAV;
		}
		if (ch.equals(Hebrew.SIN) || ch.equals(Hebrew.SHIN)) {
			return Hebrew.SHIN.substring(0, 1);
		}
		return ch;
	}

	public boolean isFinal() {
		return ch.equals(Hebrew.FINAL_KAF) || ch.equals(Hebrew.FINAL_MEM) || ch.equals(Hebrew.FINAL_NUN)
				|| ch.equals(Hebrew.FINAL_PE) || ch.equals(Hebrew.FINAL_TSADI) || ch.equals(Hebrew.WIDE_FINAL_MEM);
	}

	public boolean isWide() {
		return ch.equals(Hebrew.WIDE_ALEF) || ch.equals(Hebrew.WIDE_DALET) || ch.equals(Hebrew.WIDE_HE)
				|| ch.equals(Hebrew.WIDE_KAF) || ch.equals(Hebrew.WIDE_LAMED) || ch.equals(Hebrew.WIDE_FINAL_MEM)
				|| ch.equals(Hebrew.WIDE_RESH) || ch.equals(Hebrew.WIDE_TAV);
	}

	public boolean isBegedkefet() {
		String b = baseLetter();
		return b.equals(Hebrew.BET) || b.equals(Hebrew.GIMEL) || b.equals(Hebrew.DALET)
				|| b.equals(Hebrew.KAF) || b.equals(Hebrew.PE) || b.equals(Hebrew.TAV);
	}

	public boolean isGuttural() {
		String b = baseLetter();
		return b.equals(Hebrew.ALEF) || b.equals(Hebrew.HE) || b.equals(Hebrew.HET) || b.equals(Hebrew.AYIN);
	}

	public boolean isEmphatic() {
		String b = baseLetter();
		return b.equals(Hebrew.TET) || b.equals(Hebrew.TSADI) || b.equals(Hebrew.QOF);
	}

	public boolean canHaveDagesh() {
		if (isGuttural()) return false;
		if (baseLetter().equals(Hebrew.RESH)) return false;
		if (isFinal()) {
			String b = baseLetter();
			return b.equals(Hebrew.KAF) || b.equals(Hebrew.PE);
		}
		return true;
	}

	public boolean canHaveRafe() {
		return isBegedkefet();
	}

	public boolean hasFinalForm() {
		String b = baseLetter();
		return b.equals(Hebrew.KAF) || b.equals(Hebrew.MEM) || b.equals(Hebrew.NUN)
				|| b.equals(Hebrew.PE) || b.equals(Hebrew.TSADI);
	}
}
