package Clock;

import Astro.MoonPhaseColor;
import Astro.MoonPhaseType;
import Date.DateTime;
import Date.Time;
import Geo.Location;
import HebrewDate.HebrewDate;
import LocalHebrewDate.Band;
import LocalHebrewDate.BandId;
import LocalHebrewDate.Custom;
import LocalHebrewDate.LocalHebrewDate;
import android.graphics.Typeface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

public abstract class Clock {
	public static final int TIMEZONE = 3 * 3600;

	public static class VisualBand {
		public final double startAngle;
		public final double endAngle;
		public final int color;
		public final boolean stripe;

		public VisualBand(double startAngle, double endAngle, int color) {
			this(startAngle, endAngle, color, false);
		}

		public VisualBand(double startAngle, double endAngle, int color, boolean stripe) {
			this.startAngle = startAngle;
			this.endAngle = endAngle;
			this.color = color;
			this.stripe = stripe;
		}
	}

	private static final Map<Integer, Integer> SUN_BAND_COLORS = new HashMap<>();
	private static final Map<Integer, Integer> HALAKHIC_BAND_COLORS = new HashMap<>();
	private static final Map<Integer, Integer> OBSERVANCE_BAND_COLORS = new HashMap<>();

	static {
		SUN_BAND_COLORS.put(BandId.DAY.value, 0xFFD700);

		HALAKHIC_BAND_COLORS.put(BandId.NIGHTFALL.value, 0x8A6A4A);
		HALAKHIC_BAND_COLORS.put(BandId.DAWN.value, 0x375C7B);
		HALAKHIC_BAND_COLORS.put(BandId.MISHEYAKIR.value, 0x567D9F);
		HALAKHIC_BAND_COLORS.put(BandId.SHEMA.value, 0x829DB5);
		HALAKHIC_BAND_COLORS.put(BandId.AMIDAH.value, 0xAEBECB);
		HALAKHIC_BAND_COLORS.put(BandId.MINCHA_GEDOLAH.value, 0x7A8C6D);
		HALAKHIC_BAND_COLORS.put(BandId.MINCHA_KETANA.value, 0xCBB88C);
		HALAKHIC_BAND_COLORS.put(BandId.PLAG_MINCHA.value, 0xB08A5C);

		OBSERVANCE_BAND_COLORS.put(BandId.REST.value, 0xA05050);
		OBSERVANCE_BAND_COLORS.put(BandId.REST_STRIPED.value, 0xA05050);
		OBSERVANCE_BAND_COLORS.put(BandId.YOMTOV.value, 0xD07070);
		OBSERVANCE_BAND_COLORS.put(BandId.FAST.value, 0xE6C300);
		OBSERVANCE_BAND_COLORS.put(BandId.FAST_STRIPED.value, 0xE6C300);
	}

	public int halakhicColor(Band band) {
		if (band.id == BandId.WATCH1.value) {
			return palette.watch1;
		}
		if (band.id == BandId.WATCH2.value) {
			return palette.watch2;
		}
		if (band.id == BandId.WATCH3.value) {
			return palette.watch3;
		}
		return HALAKHIC_BAND_COLORS.get(band.id);
	}

	public int observanceColor(Band band) {
		return OBSERVANCE_BAND_COLORS.get(band.id);
	}

	public int moonColor(Band band) {
		for (MoonPhaseType type : MoonPhaseType.values()) {
			if (type.value == band.id) {
				return MoonPhaseColor.valueOf(type.name()).value;
			}
		}
		throw new IllegalArgumentException("unknown moon phase " + band.id);
	}

	public int sunColor(Band band) {
		if (band.id == BandId.NIGHT.value) {
			return palette.sunNight;
		}
		return SUN_BAND_COLORS.get(band.id);
	}

	public static final double SEAM_GAP_DEGREES = 30.0;

	public final int size;
	public final double MOON_RING_RADIUS;
	public final double MOON_RING_THICKNESS;
	public final double SUN_RING_RADIUS;
	public final double SUN_RING_THICKNESS;
	public final double HALAKHIC_RING_RADIUS;
	public final double HALAKHIC_RING_THICKNESS;
	public final double OBSERVANCE_RING_RADIUS;
	public final double OBSERVANCE_RING_THICKNESS;
	public final double DIAL_RADIUS;
	public final double HOUR_SHORT_TICK_LENGTH;
	public final double HOUR_MEDIUM_TICK_LENGTH;
	public final double HOUR_LONG_TICK_LENGTH;
	public final double CAP_RADIUS;
	public final double RIM_THICKNESS;
	public final double HOUR_HAND_LENGTH;
	public final double HOUR_HAND_TAIL;
	public final double HOUR_HAND_THICKNESS;
	public final double SECOND_HAND_THICKNESS;
	// Off for displays that only redraw once a minute, where a frozen second hand looks wrong
	public boolean showSecondHand = true;

	protected final Location location;
	protected final Custom custom;
	protected final Palette palette;
	protected DateTime datetime;
	protected LocalHebrewDate localHebrewDate;
	protected byte[] face;
	protected Display d;
	protected int cx;
	protected int cy;

	protected Clock(DateTime datetime, Location location, Custom custom, int size, Palette palette) {
		this.palette = palette;
		this.datetime = datetime;
		this.location = location;
		this.custom = custom;
		this.size = size;
		this.MOON_RING_RADIUS = 53.0 / 240 * size;
		this.MOON_RING_THICKNESS = 3.0 / 240 * size;
		this.SUN_RING_RADIUS = 59.0 / 240 * size;
		this.SUN_RING_THICKNESS = 3.0 / 240 * size;
		this.HALAKHIC_RING_RADIUS = 66.0 / 240 * size;
		this.HALAKHIC_RING_THICKNESS = 6.0 / 240 * size;
		this.OBSERVANCE_RING_RADIUS = 75.0 / 240 * size;
		this.OBSERVANCE_RING_THICKNESS = 6.0 / 240 * size;
		this.DIAL_RADIUS = 100.0 / 240 * size;
		this.HOUR_SHORT_TICK_LENGTH = 10.0 / 240 * size;
		this.HOUR_MEDIUM_TICK_LENGTH = 16.0 / 240 * size;
		this.HOUR_LONG_TICK_LENGTH = 20.0 / 240 * size;
		this.CAP_RADIUS = 3.0 / 240 * size;
		this.RIM_THICKNESS = 2.0 / 240 * size;
		this.HOUR_HAND_LENGTH = 60.0 / 240 * size;
		this.HOUR_HAND_TAIL = 10.0 / 240 * size;
		this.HOUR_HAND_THICKNESS = 4.0 / 240 * size;
		this.SECOND_HAND_THICKNESS = 1.0 / 240 * size;
	}

	protected static double mod(double x, double m) {
		double r = x % m;
		return r < 0 ? r + m : r;
	}

	public void update(DateTime datetime) {
		this.datetime = datetime;
		HebrewDate hebrewDate = HebrewDate.fromDate(datetime.date);
		if (localHebrewDate == null || !hebrewDate.equals(localHebrewDate.hebrewDate)) {
			localHebrewDate = new LocalHebrewDate(hebrewDate, location, custom);
		}
	}

	public LocalHebrewDate localHebrewDate() {
		return localHebrewDate;
	}

	public abstract double hourHandAngle();

	public abstract double[] hourMajorMarks();

	public abstract double[] hourMarks();

	protected abstract void drawMarks();

	public abstract void draw(Display d);

	protected void drawTick(double angleDeg, double length, double thickness, int color) {
		double rad = Math.toRadians(angleDeg);
		int xOut = cx + (int) (DIAL_RADIUS * Math.sin(rad));
		int yOut = cy - (int) (DIAL_RADIUS * Math.cos(rad));
		double innerRadius = DIAL_RADIUS - length;
		int xIn = cx + (int) (innerRadius * Math.sin(rad));
		int yIn = cy - (int) (innerRadius * Math.cos(rad));
		d.drawLine(xIn, yIn, xOut, yOut, thickness, color);
	}

	protected void drawHand(double length, double tail, double angleDeg, double thickness, int color) {
		double rad = Math.toRadians(angleDeg);
		int tipX = cx + (int) (length * Math.sin(rad));
		int tipY = cy - (int) (length * Math.cos(rad));
		int tailX = cx - (int) (tail * Math.sin(rad));
		int tailY = cy + (int) (tail * Math.cos(rad));
		d.drawLine(tailX, tailY, tipX, tipY, thickness, color);
	}

	protected void drawFace() {
		d.drawCircle(cx, cy, CAP_RADIUS, CAP_RADIUS * 2, palette.face);
		d.drawCircle(cx, cy, DIAL_RADIUS, RIM_THICKNESS, palette.face);
	}

	// Draws the static dial (background disc + tick marks) once, then copies it into the target
	protected void drawBackground(Display target) {
		d = target;
		cx = d.WIDTH / 2;
		cy = d.HEIGHT / 2;
		if (face == null) {
			Display temp = new Display(size);
			temp.fillCircle(cx, cy, DIAL_RADIUS, palette.background);
			Display old = d;
			d = temp;
			drawMarks();
			d = old;
			face = temp.buffer.clone();
		}
		System.arraycopy(face, 0, d.buffer, 0, face.length);
	}

	// Draws an arc for each band whose angles have already been clipped/normalised by the caller
	protected void drawArcBand(double startAngle, double endAngle, double radius, double thickness, int color, boolean stripe) {
		double pilStart = startAngle - 90.0;
		double pilEnd = endAngle - 90.0;
		if (pilEnd < pilStart) {
			pilEnd += 360.0;
		}
		d.drawArc(cx, cy, radius, pilStart, pilEnd, thickness, color, stripe);
	}

	public static class StandardClock extends Clock {
		private static final int FONT_SIZE_BASE = 18;

		private final double LABEL_RADIUS;
		private final int FONT_SIZE;
		private final double MINUTE_TICK_LENGTH;
		private final double MINUTE_HAND_LENGTH;
		private final double MINUTE_HAND_TAIL;
		private final double MINUTE_HAND_THICKNESS;
		private final double SECOND_HAND_LENGTH;
		private final double SECOND_HAND_TAIL;
		private final Typeface typeface;

		public StandardClock(DateTime datetime, Location location, Custom custom, Typeface typeface) {
			this(datetime, location, custom, typeface, 240);
		}

		public StandardClock(DateTime datetime, Location location, Custom custom, Typeface typeface, int size) {
			this(datetime, location, custom, typeface, size, Palette.LIGHT);
		}

		public StandardClock(DateTime datetime, Location location, Custom custom, Typeface typeface, int size, Palette palette) {
			super(datetime, location, custom, size, palette);
			this.typeface = typeface;
			this.LABEL_RADIUS = DIAL_RADIUS + 8.0 / 240 * size;
			this.FONT_SIZE = (int) Math.rint(FONT_SIZE_BASE / 240.0 * size);
			this.MINUTE_TICK_LENGTH = 6.0 / 240 * size;
			this.MINUTE_HAND_LENGTH = 84.0 / 240 * size;
			this.MINUTE_HAND_TAIL = 16.0 / 240 * size;
			this.MINUTE_HAND_THICKNESS = 2.0 / 240 * size;
			this.SECOND_HAND_LENGTH = 96.0 / 240 * size;
			this.SECOND_HAND_TAIL = 24.0 / 240 * size;
			update(datetime);
		}

		@Override
		public double hourHandAngle() {
			double totalHours = datetime.time.hour % 12 + datetime.time.minute / 60.0 + datetime.time.second / 3600.0;
			return totalHours * 30.0;
		}

		public double minuteHandAngle() {
			return datetime.time.minute * 6.0 + datetime.time.second * 0.1;
		}

		public double secondHandAngle() {
			return datetime.time.second * 6.0;
		}

		@Override
		public double[] hourMajorMarks() {
			double[] marks = new double[4];
			for (int i = 0; i < 4; i++) {
				marks[i] = i * 3 * 30.0;
			}
			return marks;
		}

		@Override
		public double[] hourMarks() {
			double[] marks = new double[8];
			int n = 0;
			for (int h = 0; h < 12; h++) {
				if (h % 3 != 0) {
					marks[n++] = h * 30.0;
				}
			}
			return marks;
		}

		public double[] minuteMarks() {
			double[] marks = new double[48];
			int n = 0;
			for (int m = 0; m < 60; m++) {
				if (m % 5 != 0) {
					marks[n++] = m * 6.0;
				}
			}
			return marks;
		}

		public DateTime windowStart() {
			return datetime.plus(-12 * 3600);
		}

		public DateTime windowEnd() {
			return datetime.plus(12 * 3600);
		}

		public List<Band> halakhicBands() {
			LocalHebrewDate current = localHebrewDate;
			DateTime yemamaStart = current.yemamaStart().plus(TIMEZONE);
			DateTime yemamaEnd = current.yemamaEnd().plus(TIMEZONE);
			List<LocalHebrewDate> yemamot = new ArrayList<>();
			if (windowStart().compareTo(yemamaStart) < 0) {
				yemamot.add(current.previousYemama());
			}
			yemamot.add(current);
			if (windowEnd().compareTo(yemamaEnd) > 0) {
				yemamot.add(current.nextYemama());
			}
			List<Band> all = new ArrayList<>();
			for (LocalHebrewDate y : yemamot) {
				all.addAll(y.halakhicBands());
			}
			return all;
		}

		public List<Band> sunBands() {
			List<Band> all = new ArrayList<>();
			for (LocalHebrewDate y : threeYemamot()) {
				all.addAll(y.sunBands());
			}
			return all;
		}

		public List<Band> moonBands() {
			List<Band> all = new ArrayList<>();
			for (LocalHebrewDate y : threeYemamot()) {
				all.addAll(y.moonBands());
			}
			return all;
		}

		public List<Band> observanceBands() {
			List<Band> all = new ArrayList<>();
			for (LocalHebrewDate y : threeYemamot()) {
				all.addAll(y.observanceBands());
			}
			return all;
		}

		private List<LocalHebrewDate> threeYemamot() {
			List<LocalHebrewDate> yemamot = new ArrayList<>();
			yemamot.add(localHebrewDate.previousYemama());
			yemamot.add(localHebrewDate);
			yemamot.add(localHebrewDate.nextYemama());
			return yemamot;
		}

		// Seconds from now (local time) to dt, at 120 seconds per degree
		private double angleFromNow(DateTime dt) {
			DateTime local = dt.plus(TIMEZONE);
			int sec = (local.date.ordinal - datetime.date.ordinal) * 86400 + (local.time.serial - datetime.time.serial);
			return sec / 120.0;
		}

		private List<VisualBand> visualBands(List<Band> bands, ToIntFunction<Band> color) {
			List<VisualBand> result = new ArrayList<>();
			for (Band b : bands) {
				result.add(new VisualBand(angleFromNow(b.start.datetime), angleFromNow(b.end.datetime),
						color.applyAsInt(b), b.stripe));
			}
			return result;
		}

		public List<VisualBand> visualHalakhicBands() {
			return visualBands(halakhicBands(), this::halakhicColor);
		}

		public List<VisualBand> visualSunBands() {
			return visualBands(sunBands(), this::sunColor);
		}

		public List<VisualBand> visualMoonBands() {
			return visualBands(moonBands(), this::moonColor);
		}

		public List<VisualBand> visibleObservanceBands() {
			return visualBands(observanceBands(), this::observanceColor);
		}

		public List<Double> visibleSeasonalHourMarks() {
			double lo = -180.0 + SEAM_GAP_DEGREES / 2.0;
			double hi = 180.0 - SEAM_GAP_DEGREES / 2.0;
			List<Double> marks = new ArrayList<>();
			for (LocalHebrewDate y : threeYemamot()) {
				for (DateTime dt : y.hours()) {
					double angle = angleFromNow(dt);
					if (lo <= angle && angle <= hi) {
						marks.add(angle);
					}
				}
			}
			return marks;
		}

		private void drawVisibleBands(List<VisualBand> bands, double radius, double thickness) {
			double handAngle = hourHandAngle();
			double lo = -180.0 + SEAM_GAP_DEGREES / 2.0;
			double hi = 180.0 - SEAM_GAP_DEGREES / 2.0;
			for (VisualBand vb : bands) {
				double clippedStart = Math.max(vb.startAngle, lo);
				double clippedEnd = Math.min(vb.endAngle, hi);
				if (clippedStart >= clippedEnd) {
					continue;
				}
				double absStart = mod(clippedStart + handAngle, 360.0);
				double absEnd = mod(clippedEnd + handAngle, 360.0);
				if (absStart == absEnd) {
					continue;
				}
				drawArcBand(absStart, absEnd, radius, thickness, vb.color, vb.stripe);
			}
		}

		private void drawVisibleSunBands() {
			drawVisibleBands(visualSunBands(), SUN_RING_RADIUS, SUN_RING_THICKNESS);
		}

		private void drawVisibleMoonBands() {
			drawVisibleBands(visualMoonBands(), MOON_RING_RADIUS, MOON_RING_THICKNESS);
		}

		private void drawVisibleObservanceBands() {
			drawVisibleBands(visibleObservanceBands(), OBSERVANCE_RING_RADIUS, OBSERVANCE_RING_THICKNESS);
		}

		private void drawVisibleHalakhicBands() {
			double handAngle = hourHandAngle();
			double lo = -180.0 + SEAM_GAP_DEGREES / 2.0;
			double hi = 180.0 - SEAM_GAP_DEGREES / 2.0;
			drawArcBand(mod(lo + handAngle, 360.0), mod(hi + handAngle, 360.0),
					HALAKHIC_RING_RADIUS, HALAKHIC_RING_THICKNESS, palette.ringTrack, false);
			drawVisibleBands(visualHalakhicBands(), HALAKHIC_RING_RADIUS, HALAKHIC_RING_THICKNESS);

			double halfThick = HALAKHIC_RING_THICKNESS / 2.0;
			double rInner = HALAKHIC_RING_RADIUS - halfThick;
			double rOuter = HALAKHIC_RING_RADIUS + halfThick;
			for (double angle : visibleSeasonalHourMarks()) {
				double rad = Math.toRadians(mod(angle + handAngle, 360.0) - 90.0);
				int x1 = cx + (int) (rInner * Math.cos(rad));
				int y1 = cy + (int) (rInner * Math.sin(rad));
				int x2 = cx + (int) (rOuter * Math.cos(rad));
				int y2 = cy + (int) (rOuter * Math.sin(rad));
				d.drawLine(x1, y1, x2, y2, 1, 0xFFFFFF);
			}
		}

		private void drawDigits() {
			double hourHand = hourHandAngle();
			double opposite = mod(hourHand + 180.0, 360.0);
			double gap = SEAM_GAP_DEGREES / 2.0;
			for (int mark = 0; mark < 12; mark++) {
				double markAngle = mark * 30.0;
				double diff = mod(markAngle - opposite + 180.0, 360.0) - 180.0;
				if (Math.abs(diff) < gap) {
					continue;
				}
				double delta = markAngle - hourHand;
				if (delta > 180.0) {
					delta -= 360.0;
				} else if (delta <= -180.0) {
					delta += 360.0;
				}
				double totalSec = mod(datetime.time.serial + delta * 120, 86400);
				int hour24 = (int) Math.rint(totalSec / 3600.0) % 24;
				double posAngle = markAngle - 90.0;
				double rotation;
				if (markAngle == 90.0) {
					rotation = 90.0;
				} else if (markAngle == 270.0) {
					rotation = -90.0;
				} else if (90.0 < markAngle && markAngle < 270.0) {
					rotation = 180.0;
				} else {
					rotation = 0.0;
				}
				d.drawCurvedChar(cx, cy, LABEL_RADIUS, posAngle, String.valueOf(hour24), typeface, FONT_SIZE, rotation, 0xFF000000 | palette.digit);
			}
		}

		@Override
		protected void drawMarks() {
			for (double angleDeg : minuteMarks()) {
				drawTick(angleDeg, MINUTE_TICK_LENGTH, 1, palette.minuteTick);
			}
			for (double angleDeg : hourMarks()) {
				drawTick(angleDeg, HOUR_SHORT_TICK_LENGTH, 2, palette.tick);
			}
			for (double angleDeg : hourMajorMarks()) {
				drawTick(angleDeg, HOUR_LONG_TICK_LENGTH, 2, palette.tick);
			}
		}

		private void drawHands() {
			drawHand(HOUR_HAND_LENGTH, HOUR_HAND_TAIL, hourHandAngle(), HOUR_HAND_THICKNESS, palette.hand);
			drawHand(MINUTE_HAND_LENGTH, MINUTE_HAND_TAIL, minuteHandAngle(), MINUTE_HAND_THICKNESS, palette.hand);
			if (showSecondHand) {
				drawHand(SECOND_HAND_LENGTH, SECOND_HAND_TAIL, secondHandAngle(), SECOND_HAND_THICKNESS, palette.secondHand);
			}
		}

		@Override
		public void draw(Display target) {
			drawBackground(target);
			drawVisibleSunBands();
			drawVisibleMoonBands();
			drawVisibleHalakhicBands();
			drawVisibleObservanceBands();
			drawDigits();
			drawHands();
			drawFace();
		}
	}

	public static class HebrewClock extends Clock {
		private final double SECOND_HAND_LENGTH;
		private final double SECOND_HAND_TAIL;

		public HebrewClock(DateTime datetime, Location location, Custom custom) {
			this(datetime, location, custom, 240);
		}

		public HebrewClock(DateTime datetime, Location location, Custom custom, int size) {
			this(datetime, location, custom, size, Palette.LIGHT);
		}

		public HebrewClock(DateTime datetime, Location location, Custom custom, int size, Palette palette) {
			super(datetime, location, custom, size, palette);
			this.SECOND_HAND_LENGTH = 28.0 / 240 * size;
			this.SECOND_HAND_TAIL = 12.0 / 240 * size;
			update(datetime);
		}

		public Time utcTime() {
			return datetime.plus(-TIMEZONE).time;
		}

		@Override
		public double hourHandAngle() {
			LocalHebrewDate current = localHebrewDate;
			return current.angle(current.hour(utcTime()));
		}

		public double secondHandAngle() {
			LocalHebrewDate current = localHebrewDate;
			double h = current.hour(utcTime());
			double hourFrac = h - (int) h;
			double minuteFrac = mod(hourFrac * 60.0, 1.0);
			return minuteFrac * 360.0;
		}

		@Override
		public double[] hourMajorMarks() {
			double[] marks = new double[4];
			for (int i = 0; i < 4; i++) {
				marks[i] = localHebrewDate.angle(i * 6);
			}
			return marks;
		}

		public double[] hourMiddleMarks() {
			double[] marks = new double[4];
			for (int i = 0; i < 4; i++) {
				marks[i] = localHebrewDate.angle(3 + i * 6);
			}
			return marks;
		}

		@Override
		public double[] hourMarks() {
			double[] marks = new double[16];
			int n = 0;
			for (int h = 0; h < 24; h++) {
				if (h % 3 != 0) {
					marks[n++] = localHebrewDate.angle(h);
				}
			}
			return marks;
		}

		private List<LocalHebrewDate> threeYemamot() {
			List<LocalHebrewDate> yemamot = new ArrayList<>();
			yemamot.add(localHebrewDate.previousYemama());
			yemamot.add(localHebrewDate);
			yemamot.add(localHebrewDate.nextYemama());
			return yemamot;
		}

		public List<Band> halakhicBands() {
			List<Band> all = new ArrayList<>();
			for (LocalHebrewDate y : threeYemamot()) {
				all.addAll(y.halakhicBands());
			}
			return all;
		}

		public List<Band> moonBands() {
			List<Band> all = new ArrayList<>();
			for (LocalHebrewDate y : threeYemamot()) {
				all.addAll(y.moonBands());
			}
			return all;
		}

		public List<Band> observanceBands() {
			List<Band> all = new ArrayList<>();
			for (LocalHebrewDate y : threeYemamot()) {
				all.addAll(y.observanceBands());
			}
			return all;
		}

		private List<VisualBand> visualBands(List<Band> bands, ToIntFunction<Band> color) {
			List<VisualBand> result = new ArrayList<>();
			for (Band b : bands) {
				double startAngle = b.start.localHebrewDateTime.localHebrewDate.angle(b.start.localHebrewDateTime.hour);
				double endAngle = b.end.localHebrewDateTime.localHebrewDate.angle(b.end.localHebrewDateTime.hour);
				result.add(new VisualBand(startAngle, endAngle, color.applyAsInt(b), b.stripe));
			}
			return result;
		}

		public List<VisualBand> visualHalakhicBands() {
			return visualBands(halakhicBands(), this::halakhicColor);
		}

		public List<VisualBand> visualMoonBands() {
			return visualBands(moonBands(), this::moonColor);
		}

		public List<VisualBand> visibleObservanceBands() {
			return visualBands(observanceBands(), this::observanceColor);
		}

		private void drawVisibleBands(List<VisualBand> bands, double radius, double thickness) {
			for (VisualBand vb : bands) {
				double a = mod(vb.startAngle, 360.0);
				double b = mod(vb.endAngle, 360.0);
				if (a == b) {
					continue;
				}
				drawArcBand(a, b, radius, thickness, vb.color, vb.stripe);
			}
		}

		@Override
		protected void drawMarks() {
			for (double angleDeg : hourMarks()) {
				drawTick(angleDeg, HOUR_SHORT_TICK_LENGTH, 2, palette.tick);
			}
			for (double angleDeg : hourMiddleMarks()) {
				drawTick(angleDeg, HOUR_MEDIUM_TICK_LENGTH, 2, palette.tick);
			}
			for (double angleDeg : hourMajorMarks()) {
				drawTick(angleDeg, HOUR_LONG_TICK_LENGTH, 2, palette.tick);
			}
		}

		private void drawHands() {
			drawHand(HOUR_HAND_LENGTH, HOUR_HAND_TAIL, hourHandAngle(), HOUR_HAND_THICKNESS, palette.hand);
			if (showSecondHand) {
				drawHand(SECOND_HAND_LENGTH, SECOND_HAND_TAIL, secondHandAngle(), SECOND_HAND_THICKNESS, palette.secondHand);
			}
		}

		@Override
		public void draw(Display target) {
			drawBackground(target);
			drawVisibleBands(visualMoonBands(), MOON_RING_RADIUS, MOON_RING_THICKNESS);
			drawVisibleBands(visualHalakhicBands(), HALAKHIC_RING_RADIUS, HALAKHIC_RING_THICKNESS);
			drawVisibleBands(visibleObservanceBands(), OBSERVANCE_RING_RADIUS, OBSERVANCE_RING_THICKNESS);
			drawHands();
			drawFace();
		}
	}
}
