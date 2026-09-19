from enum import IntEnum

from Date import Weekday
from HebrewDate import HebrewDate, HebrewMonth


class HebrewHolidayId(IntEnum):
	NONE = 0
	PESACH = 1
	PESACH_VII = 3
	YOM_HASHOAH = 5
	YOM_HAZIKARON = 6
	YOM_HAATZMAUT = 7
	PESACH_SHENI = 8
	LAG_BOMER = 9
	YOM_YERUSHALAYIM = 10
	SHAVUOT = 11
	FAST_OF_TAMMUZ = 13
	FAST_OF_AV = 14
	TU_BAV = 15
	ROSH_HASHANA = 16
	ROSH_HASHANA_II = 17
	TZOM_GEDALIAH = 18
	YOM_KIPPUR = 19
	SUKKOT = 20
	HOSHANA_RABA = 22
	SHEMINI_ATZERET = 23
	SIMCHAT_TORAH = 24
	SIMCHAT_TORAH_SHEMINI_ATZERETH = 25
	CHANUKKA_I = 26
	CHANUKKA_II = 27
	CHANUKKA_III = 28
	CHANUKKA_IV = 29
	CHANUKKA_V = 30
	CHANUKKA_VI = 31
	CHANUKKA_VII = 32
	CHANUKKA_VIII = 33
	FAST_OF_TEVET = 34
	TU_BSHEVAT = 35
	FAST_OF_ESTHER = 36
	PURIM = 37
	SHUSHAN_PURIM = 38


class DiasporaHolidayId(IntEnum):
	PESACH_II = 2
	SUKKOT_II = 21


class HebrewSecondYomTovId(IntEnum):
	PESACH_VIII = 4
	SHAVUOT_II = 12


class HebrewHoliday:
	def __init__(self, hebrewDate):
		year = hebrewDate.year
		month = hebrewDate.month
		day = hebrewDate.day

		self.hebrewDate = hebrewDate
		self.id = HebrewHolidayId.NONE

		if month == HebrewMonth.NISAN:
			if day == 15:
				self.id = HebrewHolidayId.PESACH
			elif day == 21:
				self.id = HebrewHolidayId.PESACH_VII
			else:
				# 27 Nisan can only ever fall on Sun/Tue/Thu/Fri.
				# Friday -> advance to Thursday (26); Sunday -> postpone to Monday (28);
				# Tue/Thu -> observed on the 27th itself.
				testDate = HebrewDate(year, HebrewMonth.NISAN, 27)
				if testDate.dayofweek == Weekday.FRIDAY:
					if day == 26:
						self.id = HebrewHolidayId.YOM_HASHOAH
				elif year >= 5757 and testDate.dayofweek == Weekday.SUNDAY:
					if day == 28:
						self.id = HebrewHolidayId.YOM_HASHOAH
				elif day == 27:
					self.id = HebrewHolidayId.YOM_HASHOAH

		if month == HebrewMonth.IYAR:
			# 4 Iyar can only ever fall on Sun/Tue/Thu/Fri.
			# Tuesday-Wednesday: normative, no shift (4/5 Iyar).
			# Thursday-Friday: advance one day to avoid running into Shabbat (3/4 Iyar).
			# Friday-Saturday: advance two days (2/3 Iyar).
			# Sunday-Monday (since 5764): postpone one day to avoid Motzei-Shabbat
			#   preparations (5/6 Iyar); before the reform, observed 4/5 Iyar as normal.
			nominal4 = HebrewDate(year, HebrewMonth.IYAR, 4).dayofweek
			if nominal4 == Weekday.THURSDAY:
				zikaronDay, atzmautDay = 3, 4
			elif nominal4 == Weekday.FRIDAY:
				zikaronDay, atzmautDay = 2, 3
			elif nominal4 == Weekday.SUNDAY and year >= 5764:
				zikaronDay, atzmautDay = 5, 6
			else:
				zikaronDay, atzmautDay = 4, 5

			if day == zikaronDay:
				self.id = HebrewHolidayId.YOM_HAZIKARON
			elif day == atzmautDay:
				self.id = HebrewHolidayId.YOM_HAATZMAUT

			if day == 14:
				self.id = HebrewHolidayId.PESACH_SHENI
			elif day == 18:
				self.id = HebrewHolidayId.LAG_BOMER
			elif day == 28:
				self.id = HebrewHolidayId.YOM_YERUSHALAYIM

		if month == HebrewMonth.SIVAN:
			if day == 6:
				self.id = HebrewHolidayId.SHAVUOT

		if month == HebrewMonth.TAMMUZ:
			# 17 Tammuz never falls on Friday, but can fall on Shabbat; postpone to Sunday.
			fastDate = HebrewDate(year, HebrewMonth.TAMMUZ, 17)
			if fastDate.dayofweek == Weekday.SATURDAY:
				if day == 18:
					self.id = HebrewHolidayId.FAST_OF_TAMMUZ
			elif day == 17:
				self.id = HebrewHolidayId.FAST_OF_TAMMUZ

		if month == HebrewMonth.AV:
			# 9 Av never falls on Friday, but can fall on Shabbat; postpone to Sunday.
			fastDate = HebrewDate(year, HebrewMonth.AV, 9)
			if fastDate.dayofweek == Weekday.SATURDAY:
				if day == 10:
					self.id = HebrewHolidayId.FAST_OF_AV
			elif day == 9:
				self.id = HebrewHolidayId.FAST_OF_AV
			if day == 15:
				self.id = HebrewHolidayId.TU_BAV

		if month == HebrewMonth.TISHREI:
			if day == 1:
				self.id = HebrewHolidayId.ROSH_HASHANA
			elif day == 2:
				self.id = HebrewHolidayId.ROSH_HASHANA_II
			else:
				# 3 Tishrei never falls on Friday, but can fall on Shabbat; postpone to Sunday.
				tzomDate = HebrewDate(year, HebrewMonth.TISHREI, 3)
				if tzomDate.dayofweek == Weekday.SATURDAY:
					if day == 4:
						self.id = HebrewHolidayId.TZOM_GEDALIAH
				elif day == 3:
					self.id = HebrewHolidayId.TZOM_GEDALIAH

				if day == 10:
					self.id = HebrewHolidayId.YOM_KIPPUR
				elif day == 15:
					self.id = HebrewHolidayId.SUKKOT
				elif day == 21:
					self.id = HebrewHolidayId.HOSHANA_RABA
				elif day == 22:
					self.id = HebrewHolidayId.SIMCHAT_TORAH_SHEMINI_ATZERETH

		if month in (HebrewMonth.KISLEV, HebrewMonth.TEVET):
			if month == HebrewMonth.KISLEV and day >= 25:
				self.id = HebrewHolidayId(HebrewHolidayId.CHANUKKA_I + (day - 25))

			if month == HebrewMonth.TEVET:
				if day == 10:
					self.id = HebrewHolidayId.FAST_OF_TEVET
				kislevDays = HebrewDate.numdaysinmonth(year, HebrewMonth.KISLEV)
				if kislevDays == 29 and day <= 3:
					self.id = HebrewHolidayId(HebrewHolidayId.CHANUKKA_I + (4 + day))
				if kislevDays == 30 and day <= 2:
					self.id = HebrewHolidayId(HebrewHolidayId.CHANUKKA_I + (5 + day))

		if month == HebrewMonth.SHEVAT and day == 15:
			self.id = HebrewHolidayId.TU_BSHEVAT

		monthEsther = HebrewDate.numMonthInYear(year)
		if month == monthEsther:
			# 13 Adar never falls on Friday, but can fall on Shabbat; advance to Thursday.
			fastDate = HebrewDate(year, monthEsther, 13)
			if fastDate.dayofweek == Weekday.SATURDAY:
				if day == 11:
					self.id = HebrewHolidayId.FAST_OF_ESTHER
			elif day == 13:
				self.id = HebrewHolidayId.FAST_OF_ESTHER
			if day == 14:
				self.id = HebrewHolidayId.PURIM
			elif day == 15:
				self.id = HebrewHolidayId.SHUSHAN_PURIM

	@property
	def is_shabbat(self):
		return self.hebrewDate.dayofweek == 7

	@property
	def is_rest_day(self):
		if self.hebrewDate.dayofweek == 7:
			return True
		return self.id in (
			HebrewHolidayId.ROSH_HASHANA,
			HebrewHolidayId.ROSH_HASHANA_II,
			HebrewHolidayId.YOM_KIPPUR,
			HebrewHolidayId.SUKKOT,
			HebrewHolidayId.PESACH,
			HebrewHolidayId.PESACH_VII,
			HebrewHolidayId.SHAVUOT,
			HebrewHolidayId.SIMCHAT_TORAH_SHEMINI_ATZERETH,
		)

	@property
	def is_festive(self):
		return self.id in (
			HebrewHolidayId.PURIM,
			HebrewHolidayId.SHUSHAN_PURIM,
			HebrewHolidayId.LAG_BOMER,
			HebrewHolidayId.TU_BAV,
			HebrewHolidayId.CHANUKKA_I,
			HebrewHolidayId.CHANUKKA_II,
			HebrewHolidayId.CHANUKKA_III,
			HebrewHolidayId.CHANUKKA_IV,
			HebrewHolidayId.CHANUKKA_V,
			HebrewHolidayId.CHANUKKA_VI,
			HebrewHolidayId.CHANUKKA_VII,
			HebrewHolidayId.CHANUKKA_VIII,
		)

	@property
	def is_fast(self):
		return self.id in (
			HebrewHolidayId.FAST_OF_TAMMUZ,
			HebrewHolidayId.FAST_OF_AV,
			HebrewHolidayId.TZOM_GEDALIAH,
			HebrewHolidayId.FAST_OF_TEVET,
			HebrewHolidayId.FAST_OF_ESTHER,
			HebrewHolidayId.YOM_KIPPUR,
		)

	@property
	def is_national(self):
		return self.id in (
			HebrewHolidayId.YOM_HASHOAH,
			HebrewHolidayId.YOM_HAZIKARON,
			HebrewHolidayId.YOM_HAATZMAUT,
			HebrewHolidayId.YOM_YERUSHALAYIM,
		)

	@property
	def moed(self):
		month = self.hebrewDate.month
		day = self.hebrewDate.day
		if month == HebrewMonth.NISAN and 15 <= day <= 21:
			return HebrewHoliday(HebrewDate(self.hebrewDate.year, HebrewMonth.NISAN, 15))
		if month == HebrewMonth.TISHREI and 15 <= day <= 21:
			return HebrewHoliday(HebrewDate(self.hebrewDate.year, HebrewMonth.TISHREI, 15))
		return None

	@property
	def erev(self):
		if HebrewHoliday(self.hebrewDate.next).id in (
			HebrewHolidayId.PESACH,
			HebrewHolidayId.SHAVUOT,
			HebrewHolidayId.ROSH_HASHANA,
			HebrewHolidayId.YOM_KIPPUR,
			HebrewHolidayId.SUKKOT,
			HebrewHolidayId.FAST_OF_AV,
			HebrewHolidayId.YOM_HAZIKARON,
			HebrewHolidayId.YOM_HAATZMAUT,
		):
			return HebrewHoliday(self.hebrewDate.next)
		return None

	@property
	def diaspora(self):
		month = self.hebrewDate.month
		day = self.hebrewDate.day

		if month == HebrewMonth.NISAN:
			if day == 16:
				return DiasporaHolidayId.PESACH_II
			if day == 22:
				return HebrewSecondYomTovId.PESACH_VIII

		if month == HebrewMonth.SIVAN and day == 7:
			return HebrewSecondYomTovId.SHAVUOT_II

		if month == HebrewMonth.TISHREI and day == 16:
			return DiasporaHolidayId.SUKKOT_II

		return self.id

	@property
	def is_solemn(self):
		return self.id in (
			HebrewHolidayId.YOM_HASHOAH,
			HebrewHolidayId.YOM_HAZIKARON,
		)

	def __repr__(self):
		return f"HebrewHoliday({self.id.name})"

	@staticmethod
	def holidays(year):
		first = HebrewDate(year, HebrewMonth.TISHREI, 1)
		last = HebrewDate(year + 1, HebrewMonth.TISHREI, 1)
		dates = []
		h = first
		while h.ordinal < last.ordinal:
			if HebrewHoliday(h).id != HebrewHolidayId.NONE:
				dates.append(h)
			h = h.next
		return dates


class HolidayNames:
	NAMES_EN = [
		"",
		"Pesach",
		"Pesach II",
		"Pesach VII",
		"Pesach VIII",
		"Yom HaShoah",
		"Yom HaZikaron",
		"Yom HaAtzmaut",
		"Pesach Sheni",
		"Lag BaOmer",
		"Yom Yerushalayim",
		"Shavuot",
		"Shavuot II",
		"Fast of Tammuz",
		"Fast of Av",
		"Tu B'Av",
		"Rosh Hashanah",
		"Rosh Hashanah II",
		"Fast of Gedaliah",
		"Yom Kippur",
		"Sukkot",
		"Sukkot II",
		"Hoshana Rabbah",
		"Shemini Atzeret",
		"Simchat Torah",
		"Simchat Torah / Shemini Atzeret",
		"Chanukkah I",
		"Chanukkah II",
		"Chanukkah III",
		"Chanukkah IV",
		"Chanukkah V",
		"Chanukkah VI",
		"Chanukkah VII",
		"Chanukkah VIII",
		"Fast of Tevet",
		"Tu BiShvat",
		"Fast of Esther",
		"Purim",
		"Shushan Purim",
	]
	NAMES_HE = [
		"",
		"פסח",
		"פסח שני (גלות)",
		"פסח שביעי",
		"פסח שמיני (גלות)",
		"יום השואה",
		"יום הזיכרון",
		"יום העצמאות",
		"פסח שני",
		"ל״ג בעומר",
		"יום ירושלים",
		"שבועות",
		"שבועות שני (גלות)",
		"תענית תמוז",
		"תענית אב",
		"ט״ו באב",
		"ראש השנה",
		"ראש השנה ב׳",
		"צום גדליה",
		"יום כיפור",
		"סוכות",
		"סוכות שני (גלות)",
		"הושענא רבה",
		"שמיני עצרת",
		"שמחת תורה",
		"שמחת תורה / שמיני עצרת",
		"חנוכה א׳",
		"חנוכה ב׳",
		"חנוכה ג׳",
		"חנוכה ד׳",
		"חנוכה ה׳",
		"חנוכה ו׳",
		"חנוכה ז׳",
		"חנוכה ח׳",
		"תענית טבת",
		"ט״ו בשבט",
		"תענית אסתר",
		"פורים",
		"שושן פורים",
	]

	def __init__(self, lang, id):
		self.lang = lang
		self.id = id

	def name(self):
		if self.lang == 'en':
			return self.NAMES_EN[self.id]
		return self.NAMES_HE[self.id]


if __name__ == "__main__":
	import time as _systime
	from Date import Date

	now = _systime.localtime()
	today = Date(now.tm_year, now.tm_mon, now.tm_mday)
	hToday = HebrewDate.fromdate(today)

	year = hToday.year

	yearStart = HebrewDate(year, HebrewMonth.TISHREI, 1)
	yearEnd = HebrewDate(year + 1, HebrewMonth.TISHREI, 1)

	print(f"Holidays in Hebrew year {year}")
	print()

	hDate = yearStart
	while hDate.ordinal < yearEnd.ordinal:
		holiday = HebrewHoliday(hDate)
		diasporaId = holiday.diaspora
		if holiday.id != HebrewHolidayId.NONE or diasporaId != holiday.id:
			gDate = Date.fromordinal(hDate.ordinal)
			localName = HolidayNames('en', holiday.id).name() if holiday.id != HebrewHolidayId.NONE else ""
			line = f"{gDate.year}-{gDate.month:02d}-{gDate.day:02d}  {localName}"
			if diasporaId != holiday.id:
				line += f"{HolidayNames('en', diasporaId).name()} (diaspora)"
			print(line)
		hDate = hDate.next
