package HebrewDate;

import Date.Weekday;
import java.util.ArrayList;
import java.util.List;

public class HebrewHoliday {
	public final HebrewDate hebrewDate;
	public final int id;

	public HebrewHoliday(HebrewDate hebrewDate) {
		int year = hebrewDate.year;
		int month = hebrewDate.month;
		int day = hebrewDate.day;

		this.hebrewDate = hebrewDate;
		int computedId = HebrewHolidayId.NONE.value;

		if (month == HebrewMonth.NISAN.value) {
			if (day == 15) {
				computedId = HebrewHolidayId.PESACH.value;
			} else if (day == 21) {
				computedId = HebrewHolidayId.PESACH_VII.value;
			} else {
				HebrewDate testDate = new HebrewDate(year, HebrewMonth.NISAN.value, 27);
				if (testDate.dayOfWeek() == Weekday.FRIDAY.value) {
					if (day == 26) {
						computedId = HebrewHolidayId.YOM_HASHOAH.value;
					}
				} else if (year >= 5757 && testDate.dayOfWeek() == Weekday.SUNDAY.value) {
					if (day == 28) {
						computedId = HebrewHolidayId.YOM_HASHOAH.value;
					}
				} else if (day == 27) {
					computedId = HebrewHolidayId.YOM_HASHOAH.value;
				}
			}
		}

		if (month == HebrewMonth.IYAR.value) {
			int nominal4 = new HebrewDate(year, HebrewMonth.IYAR.value, 4).dayOfWeek();
			int zikaronDay;
			int atzmautDay;
			if (nominal4 == Weekday.THURSDAY.value) {
				zikaronDay = 3;
				atzmautDay = 4;
			} else if (nominal4 == Weekday.FRIDAY.value) {
				zikaronDay = 2;
				atzmautDay = 3;
			} else if (nominal4 == Weekday.SUNDAY.value && year >= 5764) {
				zikaronDay = 5;
				atzmautDay = 6;
			} else {
				zikaronDay = 4;
				atzmautDay = 5;
			}

			if (day == zikaronDay) {
				computedId = HebrewHolidayId.YOM_HAZIKARON.value;
			} else if (day == atzmautDay) {
				computedId = HebrewHolidayId.YOM_HAATZMAUT.value;
			}

			if (day == 14) {
				computedId = HebrewHolidayId.PESACH_SHENI.value;
			} else if (day == 18) {
				computedId = HebrewHolidayId.LAG_BOMER.value;
			} else if (day == 28) {
				computedId = HebrewHolidayId.YOM_YERUSHALAYIM.value;
			}
		}

		if (month == HebrewMonth.SIVAN.value) {
			if (day == 6) {
				computedId = HebrewHolidayId.SHAVUOT.value;
			}
		}

		if (month == HebrewMonth.TAMMUZ.value) {
			HebrewDate fastDate = new HebrewDate(year, HebrewMonth.TAMMUZ.value, 17);
			if (fastDate.dayOfWeek() == Weekday.SATURDAY.value) {
				if (day == 18) {
					computedId = HebrewHolidayId.FAST_OF_TAMMUZ.value;
				}
			} else if (day == 17) {
				computedId = HebrewHolidayId.FAST_OF_TAMMUZ.value;
			}
		}

		if (month == HebrewMonth.AV.value) {
			HebrewDate fastDate = new HebrewDate(year, HebrewMonth.AV.value, 9);
			if (fastDate.dayOfWeek() == Weekday.SATURDAY.value) {
				if (day == 10) {
					computedId = HebrewHolidayId.FAST_OF_AV.value;
				}
			} else if (day == 9) {
				computedId = HebrewHolidayId.FAST_OF_AV.value;
			}
			if (day == 15) {
				computedId = HebrewHolidayId.TU_BAV.value;
			}
		}

		if (month == HebrewMonth.TISHREI.value) {
			if (day == 1) {
				computedId = HebrewHolidayId.ROSH_HASHANA.value;
			} else if (day == 2) {
				computedId = HebrewHolidayId.ROSH_HASHANA_II.value;
			} else {
				HebrewDate tzomDate = new HebrewDate(year, HebrewMonth.TISHREI.value, 3);
				if (tzomDate.dayOfWeek() == Weekday.SATURDAY.value) {
					if (day == 4) {
						computedId = HebrewHolidayId.TZOM_GEDALIAH.value;
					}
				} else if (day == 3) {
					computedId = HebrewHolidayId.TZOM_GEDALIAH.value;
				}

				if (day == 10) {
					computedId = HebrewHolidayId.YOM_KIPPUR.value;
				} else if (day == 15) {
					computedId = HebrewHolidayId.SUKKOT.value;
				} else if (day == 21) {
					computedId = HebrewHolidayId.HOSHANA_RABA.value;
				} else if (day == 22) {
					computedId = HebrewHolidayId.SIMCHAT_TORAH_SHEMINI_ATZERETH.value;
				}
			}
		}

		if (month == HebrewMonth.KISLEV.value || month == HebrewMonth.TEVET.value) {
			if (month == HebrewMonth.KISLEV.value && day >= 25) {
				computedId = HebrewHolidayId.CHANUKKA_I.value + (day - 25);
			}

			if (month == HebrewMonth.TEVET.value) {
				if (day == 10) {
					computedId = HebrewHolidayId.FAST_OF_TEVET.value;
				}
				int kislevDays = HebrewDate.numDaysInMonth(year, HebrewMonth.KISLEV.value);
				if (kislevDays == 29 && day <= 3) {
					computedId = HebrewHolidayId.CHANUKKA_I.value + (4 + day);
				}
				if (kislevDays == 30 && day <= 2) {
					computedId = HebrewHolidayId.CHANUKKA_I.value + (5 + day);
				}
			}
		}

		if (month == HebrewMonth.SHEVAT.value && day == 15) {
			computedId = HebrewHolidayId.TU_BSHEVAT.value;
		}

		int monthEsther = HebrewDate.numMonthInYear(year);
		if (month == monthEsther) {
			HebrewDate fastDate = new HebrewDate(year, monthEsther, 13);
			if (fastDate.dayOfWeek() == Weekday.SATURDAY.value) {
				if (day == 11) {
					computedId = HebrewHolidayId.FAST_OF_ESTHER.value;
				}
			} else if (day == 13) {
				computedId = HebrewHolidayId.FAST_OF_ESTHER.value;
			}
			if (day == 14) {
				computedId = HebrewHolidayId.PURIM.value;
			} else if (day == 15) {
				computedId = HebrewHolidayId.SHUSHAN_PURIM.value;
			}
		}

		this.id = computedId;
	}

	public boolean isShabbat() {
		return hebrewDate.dayOfWeek() == 7;
	}

	public boolean isRestDay() {
		if (hebrewDate.dayOfWeek() == 7) return true;
		return id == HebrewHolidayId.ROSH_HASHANA.value
				|| id == HebrewHolidayId.ROSH_HASHANA_II.value
				|| id == HebrewHolidayId.YOM_KIPPUR.value
				|| id == HebrewHolidayId.SUKKOT.value
				|| id == HebrewHolidayId.PESACH.value
				|| id == HebrewHolidayId.PESACH_VII.value
				|| id == HebrewHolidayId.SHAVUOT.value
				|| id == HebrewHolidayId.SIMCHAT_TORAH_SHEMINI_ATZERETH.value;
	}

	public boolean isFestive() {
		return id == HebrewHolidayId.PURIM.value
				|| id == HebrewHolidayId.SHUSHAN_PURIM.value
				|| id == HebrewHolidayId.LAG_BOMER.value
				|| id == HebrewHolidayId.TU_BAV.value
				|| id == HebrewHolidayId.CHANUKKA_I.value
				|| id == HebrewHolidayId.CHANUKKA_II.value
				|| id == HebrewHolidayId.CHANUKKA_III.value
				|| id == HebrewHolidayId.CHANUKKA_IV.value
				|| id == HebrewHolidayId.CHANUKKA_V.value
				|| id == HebrewHolidayId.CHANUKKA_VI.value
				|| id == HebrewHolidayId.CHANUKKA_VII.value
				|| id == HebrewHolidayId.CHANUKKA_VIII.value;
	}

	public boolean isFast() {
		return id == HebrewHolidayId.FAST_OF_TAMMUZ.value
				|| id == HebrewHolidayId.FAST_OF_AV.value
				|| id == HebrewHolidayId.TZOM_GEDALIAH.value
				|| id == HebrewHolidayId.FAST_OF_TEVET.value
				|| id == HebrewHolidayId.FAST_OF_ESTHER.value
				|| id == HebrewHolidayId.YOM_KIPPUR.value;
	}

	public boolean isNational() {
		return id == HebrewHolidayId.YOM_HASHOAH.value
				|| id == HebrewHolidayId.YOM_HAZIKARON.value
				|| id == HebrewHolidayId.YOM_HAATZMAUT.value
				|| id == HebrewHolidayId.YOM_YERUSHALAYIM.value;
	}

	public HebrewHoliday moed() {
		int month = hebrewDate.month;
		int day = hebrewDate.day;
		if (month == HebrewMonth.NISAN.value && day >= 15 && day <= 21) {
			return new HebrewHoliday(new HebrewDate(hebrewDate.year, HebrewMonth.NISAN.value, 15));
		}
		if (month == HebrewMonth.TISHREI.value && day >= 15 && day <= 21) {
			return new HebrewHoliday(new HebrewDate(hebrewDate.year, HebrewMonth.TISHREI.value, 15));
		}
		return null;
	}

	public HebrewHoliday erev() {
		HebrewHoliday nextHoliday = new HebrewHoliday(hebrewDate.next());
		int nid = nextHoliday.id;
		if (nid == HebrewHolidayId.PESACH.value
				|| nid == HebrewHolidayId.SHAVUOT.value
				|| nid == HebrewHolidayId.ROSH_HASHANA.value
				|| nid == HebrewHolidayId.YOM_KIPPUR.value
				|| nid == HebrewHolidayId.SUKKOT.value
				|| nid == HebrewHolidayId.FAST_OF_AV.value
				|| nid == HebrewHolidayId.YOM_HAZIKARON.value
				|| nid == HebrewHolidayId.YOM_HAATZMAUT.value) {
			return nextHoliday;
		}
		return null;
	}

	public int diaspora() {
		int month = hebrewDate.month;
		int day = hebrewDate.day;

		if (month == HebrewMonth.NISAN.value) {
			if (day == 16) return DiasporaHolidayId.PESACH_II.value;
			if (day == 22) return HebrewSecondYomTovId.PESACH_VIII.value;
		}

		if (month == HebrewMonth.SIVAN.value && day == 7) {
			return HebrewSecondYomTovId.SHAVUOT_II.value;
		}

		if (month == HebrewMonth.TISHREI.value && day == 16) {
			return DiasporaHolidayId.SUKKOT_II.value;
		}

		return id;
	}

	public boolean isSolemn() {
		return id == HebrewHolidayId.YOM_HASHOAH.value || id == HebrewHolidayId.YOM_HAZIKARON.value;
	}

	public static List<HebrewDate> holidays(int year) {
		HebrewDate first = new HebrewDate(year, HebrewMonth.TISHREI.value, 1);
		HebrewDate last = new HebrewDate(year + 1, HebrewMonth.TISHREI.value, 1);
		List<HebrewDate> dates = new ArrayList<>();
		HebrewDate h = first;
		while (h.ordinal < last.ordinal) {
			if (new HebrewHoliday(h).id != HebrewHolidayId.NONE.value) {
				dates.add(h);
			}
			h = h.next();
		}
		return dates;
	}
}
