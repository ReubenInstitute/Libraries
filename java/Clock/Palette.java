package Clock;

// Theme-dependent clock colors: the "chrome" (dial, ticks, hands, digits) and the night family
// (the night watches and the night half of the sun ring). The semantic band colors (dawn, shema,
// mincha, moon, observances, sun day...) mean the same in every theme and stay in Clock.
public class Palette {
	public final int background;
	public final int tick;
	public final int minuteTick;
	public final int hand;
	public final int secondHand;
	public final int face;
	public final int ringTrack;
	public final int digit;
	public final int watch1;
	public final int watch2;
	public final int watch3;
	public final int sunNight;

	public Palette(int background, int tick, int minuteTick, int hand, int secondHand, int face, int ringTrack,
			int digit, int watch1, int watch2, int watch3, int sunNight) {
		this.background = background;
		this.tick = tick;
		this.minuteTick = minuteTick;
		this.hand = hand;
		this.secondHand = secondHand;
		this.face = face;
		this.ringTrack = ringTrack;
		this.digit = digit;
		this.watch1 = watch1;
		this.watch2 = watch2;
		this.watch3 = watch3;
		this.sunNight = sunNight;
	}

	public static final Palette LIGHT = new Palette(
		0xFFFFFF, 0x333333, 0xAAAAAA, 0x222222, 0xCC0000, 0x333333, 0xDCDCDC, 0x000000,
		0x9A9A9A, 0x1A1A1A, 0x9A9A9A, 0x4A4A55);

	// Night is deep indigo, darkest in the middle of the night, lighter towards dusk and dawn.
	// The dial is a blue-gray, not black, so the night colors stay distinguishable from it.
	public static final Palette DARK = new Palette(
		0x2A2D34, 0xDDDDDD, 0x777777, 0xEEEEEE, 0xFF5252, 0xDDDDDD, 0x44484F, 0xE6E6E6,
		0x8090E0, 0x3340A8, 0x8090E0, 0x8592E0);
}
