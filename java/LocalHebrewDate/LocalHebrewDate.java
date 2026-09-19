package LocalHebrewDate;

import Astro.Moon;
import Astro.MoonPhaseType;
import Astro.Sun;
import Date.Date;
import Date.DateTime;
import Date.Time;
import Geo.Location;
import HebrewDate.HebrewDate;
import HebrewDate.HebrewHoliday;
import HebrewDate.HebrewHolidayId;

import java.util.ArrayList;
import java.util.List;

public class LocalHebrewDate {
	public final HebrewDate hebrewDate;
	public final Location location;
	public final Custom custom;

	private List<Event> eventsCache;
	private Sun sunCache;
	private Sun sunYesterdayCache;

	public LocalHebrewDate(HebrewDate hebrewDate, Location location, Custom custom) {
		this.hebrewDate = hebrewDate;
		this.location = location;
		this.custom = custom;
	}

	private static double pymod(double x, double m) {
		double r = x % m;
		return r < 0 ? r + m : r;
	}

	private Event stampedEvent(DateTime dt, Zman zman) {
		Event e = new Event(dt, zman);
		e.localHebrewDateTime = new LocalHebrewDateTime(this, hour(dt.time));
		return e;
	}

	private Event stampedEvent(DateTime dt) {
		return stampedEvent(dt, null);
	}

	public List<Event> events() {
		if (eventsCache != null) return eventsCache;
		List<Event> events = new ArrayList<>();
		if (custom.dayDefinition == DayDefinition.GRA) {
			events.add(new Event(yemamaStart(), Zman.YEMAMA_START));
			events.add(new Event(nightfall(), Zman.NIGHTFALL));
			events.add(new Event(midnight(), Zman.MIDNIGHT));
			events.add(new Event(dawn(), Zman.DAWN));
			events.add(new Event(misheyakir(), Zman.MISHEYAKIR));
			events.add(new Event(sunrise(), Zman.SUNRISE));
			events.add(new Event(shemaEnd(), Zman.SHEMA_END));
			events.add(new Event(amidahEnd(), Zman.AMIDAH_END));
			events.add(new Event(noon(), Zman.NOON));
			events.add(new Event(mincha(), Zman.MINCHA_GEDOLAH));
			events.add(new Event(minchaKetana(), Zman.MINCHA_KETANA));
			events.add(new Event(plagMincha(), Zman.PLAG_MINCHA));
			events.add(new Event(sunset(), Zman.SUNSET));
			events.add(new Event(yemamaEnd(), Zman.YEMAMA_END));
		} else {
			events.add(new Event(yemamaStart(), Zman.YEMAMA_START));
			events.add(new Event(midnight(), Zman.MIDNIGHT));
			events.add(new Event(dawn(), Zman.DAWN));
			events.add(new Event(misheyakir(), Zman.MISHEYAKIR));
			events.add(new Event(sunrise(), Zman.SUNRISE));
			events.add(new Event(shemaEnd(), Zman.SHEMA_END));
			events.add(new Event(amidahEnd(), Zman.AMIDAH_END));
			events.add(new Event(noon(), Zman.NOON));
			events.add(new Event(mincha(), Zman.MINCHA_GEDOLAH));
			events.add(new Event(minchaKetana(), Zman.MINCHA_KETANA));
			events.add(new Event(plagMincha(), Zman.PLAG_MINCHA));
			events.add(new Event(sunset(), Zman.SUNSET));
			events.add(new Event(nightfall(), Zman.NIGHTFALL));
			events.add(new Event(yemamaEnd(), Zman.YEMAMA_END));
		}
		for (Event e : events) {
			e.localHebrewDateTime = new LocalHebrewDateTime(this, hour(e.datetime.time));
		}
		eventsCache = events;
		return eventsCache;
	}

	public Sun sun() {
		if (sunCache == null) {
			DateTime dt = new DateTime(Date.fromOrdinal(hebrewDate.ordinal), new Time(0, 0, 0));
			sunCache = new Sun(location, dt);
		}
		return sunCache;
	}

	public Sun sunYesterday() {
		if (sunYesterdayCache == null) {
			DateTime dt = new DateTime(Date.fromOrdinal(hebrewDate.ordinal - 1), new Time(0, 0, 0));
			sunYesterdayCache = new Sun(location, dt);
		}
		return sunYesterdayCache;
	}

	public DateTime sunrise() {
		return sun().rise();
	}

	public DateTime sunset() {
		return sun().set();
	}

	public DateTime dawn() {
		DateTime sr = sun().rise();
		int astroDaySec = sunset().minus(sr).seconds;
		DawnCustom c = custom.dawn;
		if (c == DawnCustom.DEG_16_1) return sun().dawnTime(16.1);
		if (c == DawnCustom.DEG_16_9) return sun().dawnTime(16.9);
		if (c == DawnCustom.DEG_18) return sun().dawnTime(18);
		if (c == DawnCustom.DEG_19_8) return sun().dawnTime(19.8);
		if (c == DawnCustom.MIN_72) return sr.plus(-72 * 60);
		if (c == DawnCustom.REL_MIN_72) return sr.plus((int) (-astroDaySec / 10.0));
		return sr;
	}

	private DateTime nightfallFor(Sun sunRef) {
		DateTime sunsetRef = sunRef.set();
		double refDayLength = 0.0;
		if (custom.nightfall == NightfallCustom.REL_MIN_72 || custom.nightfall == NightfallCustom.REL_MIN_90) {
			refDayLength = sunsetRef.minus(sunRef.rise()).seconds;
		}

		NightfallCustom c = custom.nightfall;
		if (c == NightfallCustom.DEG_5_95) return sunRef.duskTime(5.95);
		if (c == NightfallCustom.DEG_6) return sunRef.duskTime(6);
		if (c == NightfallCustom.DEG_6_45) return sunRef.duskTime(6.45);
		if (c == NightfallCustom.DEG_7_1) return sunRef.duskTime(7.1);
		if (c == NightfallCustom.DEG_8) return sunRef.duskTime(8);
		if (c == NightfallCustom.DEG_8_5) return sunRef.duskTime(8.5);
		if (c == NightfallCustom.MIN_72) return sunsetRef.plus(72 * 60);
		if (c == NightfallCustom.REL_MIN_72) return sunsetRef.plus((int) (refDayLength / 10.0));
		if (c == NightfallCustom.REL_MIN_90) return sunsetRef.plus((int) (refDayLength / 8.0));
		if (c == NightfallCustom.DEG_16_1) return sunRef.duskTime(16.1);
		if (c == NightfallCustom.DEG_18) return sunRef.duskTime(18);
		if (c == NightfallCustom.DEG_19_8) return sunRef.duskTime(19.8);
		return sunsetRef;
	}

	public DateTime nightfall() {
		if (custom.dayDefinition == DayDefinition.GRA) {
			return nightfallFor(sunYesterday());
		} else {
			return nightfallFor(sun());
		}
	}

	public DateTime misheyakir() {
		DateTime sr = sun().rise();
		MisheyakirCustom c = custom.misheyakir;
		if (c == MisheyakirCustom.DEG_11_5) return sun().dawnTime(11.5);
		if (c == MisheyakirCustom.DEG_11) return sun().dawnTime(11);
		if (c == MisheyakirCustom.DEG_10_2) return sun().dawnTime(10.2);
		if (c == MisheyakirCustom.MIN_50) return sr.plus(-50 * 60);
		if (c == MisheyakirCustom.DEG_7_65) return sun().dawnTime(7.65);
		return sr;
	}

	public DateTime yemamaStart() {
		if (custom.dayDefinition == DayDefinition.GRA) {
			return sunYesterday().set();
		} else {
			return nightfallFor(sunYesterday());
		}
	}

	public DateTime dayStart() {
		return custom.dayDefinition == DayDefinition.GRA ? sunrise() : dawn();
	}

	public DateTime yemamaEnd() {
		return custom.dayDefinition == DayDefinition.GRA ? sunset() : nightfall();
	}

	public DateTime shemaEnd() {
		return dayStart().plus((int) ((3.0 / 12.0) * dayLength()));
	}

	public DateTime amidahEnd() {
		return dayStart().plus((int) ((4.0 / 12.0) * dayLength()));
	}

	public DateTime noon() {
		return dayStart().plus((int) ((6.0 / 12.0) * dayLength()));
	}

	public DateTime mincha() {
		return dayStart().plus((int) ((6.5 / 12.0) * dayLength()));
	}

	public DateTime minchaKetana() {
		return dayStart().plus((int) ((9.5 / 12.0) * dayLength()));
	}

	public DateTime plagMincha() {
		return dayStart().plus((int) ((10.75 / 12.0) * dayLength()));
	}

	public DateTime midnight() {
		return yemamaStart().plus((int) (nightLength() / 2.0));
	}

	public int dayLength() {
		return yemamaEnd().minus(dayStart()).seconds;
	}

	public int nightLength() {
		return dayStart().minus(yemamaStart()).seconds;
	}

	public double hour(Time time) {
		DateTime now = new DateTime(Date.fromOrdinal(hebrewDate.ordinal), time);
		int nowSec = now.minus(yemamaStart()).seconds;
		if (nowSec < 0) {
			nowSec = 0;
		}

		int nightLen = nightLength();
		int dayLen = dayLength();

		if (nowSec < nightLen) {
			return 12.0 * ((double) nowSec / nightLen);
		} else if (nowSec <= nightLen + dayLen) {
			int dayElapsed = nowSec - nightLen;
			return 12.0 + 12.0 * ((double) dayElapsed / dayLen);
		} else {
			LocalHebrewDate next = nextYemama();
			int nextNightLen = next.nightLength();
			int afterEnd = nowSec - (nightLen + dayLen);
			return 12.0 * ((double) afterEnd / nextNightLen);
		}
	}

	public LocalHebrewDate previousYemama() {
		return new LocalHebrewDate(hebrewDate.prev(), location, custom);
	}

	public LocalHebrewDate nextYemama() {
		return new LocalHebrewDate(hebrewDate.next(), location, custom);
	}

	public double angle(double hour) {
		int total = dayLength() + nightLength();
		double dayArc = ((double) dayLength() / total) * 360.0;
		double nightArc = 360.0 - dayArc;

		if (hour < 12) {
			double frac = hour / 12.0;
			return pymod(dayArc / 2.0 + frac * nightArc, 360.0);
		} else {
			double frac = (hour - 12.0) / 12.0;
			double startAngle = pymod(-dayArc / 2.0, 360.0);
			return pymod(startAngle + frac * dayArc, 360.0);
		}
	}

	public List<Band> halakhicBands() {
		List<Band> bands = new ArrayList<>();
		DateTime[] hrs = hours();
		if (custom.dayDefinition == DayDefinition.GRA) {
			bands.add(new Band(stampedEvent(yemamaStart(), Zman.SUNSET), stampedEvent(nightfall(), Zman.NIGHTFALL), BandId.NIGHTFALL.value));
			bands.add(new Band(stampedEvent(nightfall()), stampedEvent(hrs[4]), BandId.WATCH1.value));
			bands.add(new Band(stampedEvent(hrs[4]), stampedEvent(hrs[8]), BandId.WATCH2.value));
			bands.add(new Band(stampedEvent(hrs[8]), stampedEvent(dawn()), BandId.WATCH3.value));
			bands.add(new Band(stampedEvent(dawn(), Zman.DAWN), stampedEvent(misheyakir(), Zman.MISHEYAKIR), BandId.DAWN.value));
			bands.add(new Band(stampedEvent(misheyakir(), Zman.MISHEYAKIR), stampedEvent(sunrise(), Zman.SUNRISE), BandId.MISHEYAKIR.value));
			bands.add(new Band(stampedEvent(sunrise(), Zman.SUNRISE), stampedEvent(shemaEnd(), Zman.SHEMA_END), BandId.SHEMA.value));
			bands.add(new Band(stampedEvent(shemaEnd(), Zman.SHEMA_END), stampedEvent(amidahEnd(), Zman.AMIDAH_END), BandId.AMIDAH.value));
			bands.add(new Band(stampedEvent(mincha(), Zman.MINCHA_GEDOLAH), stampedEvent(minchaKetana(), Zman.MINCHA_KETANA), BandId.MINCHA_GEDOLAH.value));
			bands.add(new Band(stampedEvent(minchaKetana(), Zman.MINCHA_KETANA), stampedEvent(plagMincha(), Zman.PLAG_MINCHA), BandId.MINCHA_KETANA.value));
			bands.add(new Band(stampedEvent(plagMincha(), Zman.PLAG_MINCHA), stampedEvent(sunset(), Zman.SUNSET), BandId.PLAG_MINCHA.value));
		} else {
			bands.add(new Band(stampedEvent(yemamaStart()), stampedEvent(hrs[4]), BandId.WATCH1.value));
			bands.add(new Band(stampedEvent(hrs[4]), stampedEvent(hrs[8]), BandId.WATCH2.value));
			bands.add(new Band(stampedEvent(hrs[8]), stampedEvent(dayStart()), BandId.WATCH3.value));
			bands.add(new Band(stampedEvent(dawn(), Zman.DAWN), stampedEvent(misheyakir(), Zman.MISHEYAKIR), BandId.DAWN.value));
			bands.add(new Band(stampedEvent(misheyakir(), Zman.MISHEYAKIR), stampedEvent(sunrise(), Zman.SUNRISE), BandId.MISHEYAKIR.value));
			bands.add(new Band(stampedEvent(sunrise(), Zman.SUNRISE), stampedEvent(shemaEnd(), Zman.SHEMA_END), BandId.SHEMA.value));
			bands.add(new Band(stampedEvent(shemaEnd(), Zman.SHEMA_END), stampedEvent(amidahEnd(), Zman.AMIDAH_END), BandId.AMIDAH.value));
			bands.add(new Band(stampedEvent(mincha(), Zman.MINCHA_GEDOLAH), stampedEvent(minchaKetana(), Zman.MINCHA_KETANA), BandId.MINCHA_GEDOLAH.value));
			bands.add(new Band(stampedEvent(minchaKetana(), Zman.MINCHA_KETANA), stampedEvent(plagMincha(), Zman.PLAG_MINCHA), BandId.MINCHA_KETANA.value));
			bands.add(new Band(stampedEvent(plagMincha(), Zman.PLAG_MINCHA), stampedEvent(sunset(), Zman.SUNSET), BandId.PLAG_MINCHA.value));
			bands.add(new Band(stampedEvent(sunset(), Zman.SUNSET), stampedEvent(nightfall(), Zman.NIGHTFALL), BandId.NIGHTFALL.value));
		}
		return bands;
	}

	public List<Band> sunBands() {
		List<Band> bands = new ArrayList<>();
		DateTime sunsetPrev = sunYesterday().set();
		DateTime sunriseToday = sun().rise();
		DateTime sunsetToday = sun().set();

		bands.add(new Band(stampedEvent(sunsetPrev, Zman.SUNSET), stampedEvent(sunriseToday, Zman.SUNRISE), BandId.NIGHT.value));
		bands.add(new Band(stampedEvent(sunriseToday, Zman.SUNRISE), stampedEvent(sunsetToday, Zman.SUNSET), BandId.DAY.value));

		return bands;
	}

	private static class Interval {
		final DateTime start;
		final DateTime end;

		Interval(DateTime start, DateTime end) {
			this.start = start;
			this.end = end;
		}
	}

	public List<Band> moonBands() {
		Date dateStart = yemamaStart().date;
		Date dateEnd = yemamaEnd().date;

		Moon moonStart = new Moon(location, new DateTime(dateStart, new Time(0, 0, 0)));
		Moon moonEnd = new Moon(location, new DateTime(dateEnd, new Time(0, 0, 0)));

		List<Interval> intervals = new ArrayList<>();
		for (Moon moon : new Moon[]{moonStart, moonEnd}) {
			DateTime midnight = new DateTime(moon.datetime.date, new Time(0, 0, 0));
			DateTime nextMidnight = midnight.plus(86400);

			if (moon.isAlwaysUp()) {
				intervals.add(new Interval(midnight, nextMidnight));
			} else {
				DateTime rise = moon.rise();
				DateTime set = moon.set();
				if (rise != null && set != null) {
					if (rise.compareTo(set) < 0) {
						intervals.add(new Interval(rise, set));
					} else {
						intervals.add(new Interval(midnight, set));
						intervals.add(new Interval(rise, nextMidnight));
					}
				} else if (rise != null) {
					intervals.add(new Interval(rise, nextMidnight));
				} else if (set != null) {
					intervals.add(new Interval(midnight, set));
				}
			}
		}

		List<Interval> clipped = new ArrayList<>();
		DateTime yStart = yemamaStart();
		DateTime yEnd = yemamaEnd();
		for (Interval iv : intervals) {
			if (iv.end.compareTo(yStart) <= 0 || iv.start.compareTo(yEnd) >= 0) {
				continue;
			}
			DateTime clippedStart = iv.start.compareTo(yStart) > 0 ? iv.start : yStart;
			DateTime clippedEnd = iv.end.compareTo(yEnd) < 0 ? iv.end : yEnd;
			clipped.add(new Interval(clippedStart, clippedEnd));
		}

		Moon moonPhaseDay = new Moon(location, new DateTime(Date.fromOrdinal(hebrewDate.ordinal), new Time(0, 0, 0)));
		MoonPhaseType phaseType = moonPhaseDay.illumination().phaseType();

		List<Band> bands = new ArrayList<>();
		for (Interval iv : clipped) {
			bands.add(new Band(stampedEvent(iv.start), stampedEvent(iv.end), phaseType.value));
		}

		return bands;
	}

	public DateTime[] hours() {
		int nightLen = nightLength();
		int dayLen = dayLength();
		DateTime yStart = yemamaStart();
		DateTime dStart = dayStart();
		DateTime[] result = new DateTime[24];
		for (int h = 0; h < 12; h++) {
			result[h] = yStart.plus((int) ((h / 12.0) * nightLen));
		}
		for (int h = 12; h < 24; h++) {
			result[h] = dStart.plus((int) (((h - 12) / 12.0) * dayLen));
		}
		return result;
	}

	private static boolean isRestHoliday(int id) {
		return id == HebrewHolidayId.ROSH_HASHANA.value
				|| id == HebrewHolidayId.ROSH_HASHANA_II.value
				|| id == HebrewHolidayId.YOM_KIPPUR.value
				|| id == HebrewHolidayId.SUKKOT.value
				|| id == HebrewHolidayId.PESACH.value
				|| id == HebrewHolidayId.PESACH_VII.value
				|| id == HebrewHolidayId.SHAVUOT.value
				|| id == HebrewHolidayId.SIMCHAT_TORAH_SHEMINI_ATZERETH.value
				|| id == HebrewHolidayId.FAST_OF_AV.value
				|| id == HebrewHolidayId.YOM_HAATZMAUT.value;
	}

	public List<Band> observanceBands() {
		HebrewHoliday holiday = new HebrewHoliday(hebrewDate);
		boolean isRest = isRestHoliday(holiday.id) || hebrewDate.dayOfWeek() == 7;

		// Chol Hamoed (and Hoshana Raba) days fall inside the moed window
		// but aren't already flagged as full rest days.
		boolean isYomTov = holiday.moed() != null && !isRest;

		boolean isMajorFast = holiday.id == HebrewHolidayId.YOM_KIPPUR.value || holiday.id == HebrewHolidayId.FAST_OF_AV.value;

		boolean isMinorFast = holiday.id == HebrewHolidayId.FAST_OF_TAMMUZ.value
				|| holiday.id == HebrewHolidayId.TZOM_GEDALIAH.value
				|| holiday.id == HebrewHolidayId.FAST_OF_TEVET.value
				|| holiday.id == HebrewHolidayId.FAST_OF_ESTHER.value;

		List<Band> bands = new ArrayList<>();
		DateTime start = yemamaStart();
		DateTime origEnd = yemamaEnd();
		DateTime end = origEnd;

		BandId nextRestBandId = null;
		boolean nextRestStripe = false;
		if (custom.dayDefinition == DayDefinition.MA) {
			HebrewDate nextHdate = hebrewDate.next();
			HebrewHoliday nextHoliday = new HebrewHoliday(nextHdate);
			boolean nextIsRest = isRestHoliday(nextHoliday.id) || nextHdate.dayOfWeek() == 7;

			if (nextIsRest) {
				end = sunset();
				boolean nextMajor = nextHoliday.id == HebrewHolidayId.YOM_KIPPUR.value || nextHoliday.id == HebrewHolidayId.FAST_OF_AV.value;
				boolean nextYomtov = nextHoliday.moed() != null && !nextIsRest;
				if (nextMajor) {
					nextRestStripe = true;
					nextRestBandId = BandId.REST_STRIPED;
				} else if (nextYomtov) {
					nextRestStripe = false;
					nextRestBandId = BandId.YOMTOV;
				} else {
					nextRestStripe = false;
					nextRestBandId = BandId.REST;
				}
			}
		}

		if (!isRest && !isYomTov && !isMajorFast && !isMinorFast) {
			// no band for an ordinary weekday

		} else if (isRest && !isMajorFast) {
			bands.add(new Band(stampedEvent(start), stampedEvent(end), BandId.REST.value));

		} else if (isYomTov) {
			bands.add(new Band(stampedEvent(start), stampedEvent(end), BandId.YOMTOV.value));

		} else if (isMajorFast) {
			bands.add(new Band(stampedEvent(start), stampedEvent(end), BandId.REST_STRIPED.value, true));

		} else if (isMinorFast) {
			DateTime preFastEnd = dawn();
			DateTime postFastStart = nightfall();

			if (start.compareTo(preFastEnd) < 0) {
				bands.add(new Band(stampedEvent(start), stampedEvent(preFastEnd), BandId.FAST.value));
			}
			if (postFastStart.compareTo(end) < 0) {
				bands.add(new Band(stampedEvent(postFastStart), stampedEvent(end), BandId.FAST.value));
			}
			DateTime fastStart = dawn();
			DateTime fastEnd = nightfall();
			if (fastStart.compareTo(fastEnd) < 0) {
				bands.add(new Band(stampedEvent(fastStart), stampedEvent(fastEnd), BandId.FAST_STRIPED.value, true));
			}

			if (bands.isEmpty()) {
				bands.add(new Band(stampedEvent(start), stampedEvent(end), BandId.FAST_STRIPED.value, true));
			}
		}

		if (nextRestBandId != null) {
			bands.add(new Band(stampedEvent(sunset()), stampedEvent(origEnd), nextRestBandId.value, nextRestStripe));
		}

		return bands;
	}
}
