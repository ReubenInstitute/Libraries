package LocalHebrewDate;

import java.util.HashMap;
import java.util.Map;

public class Custom {
	public static final int LOCAL = 0;
	public static final int ASHKENAZI_GRA = 1;
	public static final int SEPHARDI = 2;
	public static final int CHABAD = 3;
	public static final int MAGEN_AVRAHAM = 4;
	public static final int RABBEINU_TAM_FIXED = 5;
	public static final int RABBEINU_TAM_SEASONAL = 6;
	public static final int RABBEINU_TAM_STRINGENT_90 = 7;
	public static final int BEN_ISH_CHAI = 8;
	public static final int WESTERN_EUROPEAN = 9;

	private static class Preset {
		final DayDefinition dayDef;
		final DawnCustom dawn;
		final MisheyakirCustom misheyakir;
		final NightfallCustom nightfall;

		Preset(DayDefinition dayDef, DawnCustom dawn, MisheyakirCustom misheyakir, NightfallCustom nightfall) {
			this.dayDef = dayDef;
			this.dawn = dawn;
			this.misheyakir = misheyakir;
			this.nightfall = nightfall;
		}
	}

	private static final Map<Integer, Preset> PRESET_VALUES = new HashMap<>();

	static {
		PRESET_VALUES.put(LOCAL, new Preset(
			DayDefinition.MA, DawnCustom.DEG_16_1, MisheyakirCustom.MIN_50, NightfallCustom.DEG_8_5
		));
		PRESET_VALUES.put(ASHKENAZI_GRA, new Preset(
			DayDefinition.GRA, DawnCustom.DEG_16_1, MisheyakirCustom.DEG_11_5, NightfallCustom.DEG_8_5
		));
		PRESET_VALUES.put(SEPHARDI, new Preset(
			DayDefinition.GRA, DawnCustom.DEG_16_1, MisheyakirCustom.DEG_11, NightfallCustom.DEG_6_45
		));
		PRESET_VALUES.put(CHABAD, new Preset(
			DayDefinition.GRA, DawnCustom.DEG_16_9, MisheyakirCustom.DEG_10_2, NightfallCustom.DEG_6
		));
		PRESET_VALUES.put(MAGEN_AVRAHAM, new Preset(
			DayDefinition.MA, DawnCustom.DEG_16_1, MisheyakirCustom.DEG_11, NightfallCustom.DEG_16_1
		));
		PRESET_VALUES.put(RABBEINU_TAM_FIXED, new Preset(
			DayDefinition.MA, DawnCustom.MIN_72, MisheyakirCustom.MIN_50, NightfallCustom.MIN_72
		));
		PRESET_VALUES.put(RABBEINU_TAM_SEASONAL, new Preset(
			DayDefinition.MA, DawnCustom.REL_MIN_72, MisheyakirCustom.MIN_50, NightfallCustom.REL_MIN_72
		));
		PRESET_VALUES.put(RABBEINU_TAM_STRINGENT_90, new Preset(
			DayDefinition.MA, DawnCustom.REL_MIN_72, MisheyakirCustom.MIN_50, NightfallCustom.REL_MIN_90
		));
		PRESET_VALUES.put(BEN_ISH_CHAI, new Preset(
			DayDefinition.MA, DawnCustom.DEG_19_8, MisheyakirCustom.DEG_11, NightfallCustom.DEG_7_1
		));
		PRESET_VALUES.put(WESTERN_EUROPEAN, new Preset(
			DayDefinition.GRA, DawnCustom.DEG_18, MisheyakirCustom.DEG_11_5, NightfallCustom.DEG_8_5
		));
	}

	public DayDefinition dayDefinition;
	public DawnCustom dawn;
	public MisheyakirCustom misheyakir;
	public NightfallCustom nightfall;
	public int watches;

	public Custom() {
		this(LOCAL, 3);
	}

	public Custom(int custom) {
		this(custom, 3);
	}

	public Custom(int custom, int watches) {
		this.dayDefinition = DayDefinition.MA;
		this.dawn = DawnCustom.DEG_16_1;
		this.misheyakir = MisheyakirCustom.MIN_50;
		this.nightfall = NightfallCustom.DEG_8_5;
		this.watches = watches;
		setCustom(custom);
	}

	public void setCustom(int custom) {
		Preset vals = PRESET_VALUES.get(custom);
		if (vals == null) {
			return;
		}
		this.dayDefinition = vals.dayDef;
		this.dawn = vals.dawn;
		this.misheyakir = vals.misheyakir;
		this.nightfall = vals.nightfall;
	}
}
