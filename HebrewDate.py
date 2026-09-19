# HebrewDate.py
from enum import IntEnum
from Date import Date, Weekday, WeekdayNames


class HebrewMonth(IntEnum):
	NISAN = 1
	IYAR = 2
	SIVAN = 3
	TAMMUZ = 4
	AV = 5
	ELUL = 6
	TISHREI = 7
	CHESHVAN = 8
	KISLEV = 9
	TEVET = 10
	SHEVAT = 11
	ADAR = 12
	ADAR_II = 13

class ShabbatType(IntEnum):
	NONE = 0
	SHEKALIM = 1
	ZAHOR = 2
	PARAH = 3
	HAHODESH = 4
	HAGGADOL = 5
	SHUVAH = 6
	CHAZON = 7
	NACHAMU = 8

class ShabbatChodeshType(IntEnum):
	NONE = 0
	MACHAR_CHODESH = 1
	ROSH_CHODESH = 2

class YearLength(IntEnum):
	DEFICIENT = 1
	REGULAR = 2
	COMPLETE = 3

class YearType(IntEnum):
	B_CH_G = 1
	B_SH_H = 2
	G_K_H = 3
	H_K_Z = 4
	H_SH_A = 5
	Z_CH_A = 6
	Z_SH_G = 7
	B_CH_H = 8
	B_SH_Z = 9
	G_K_Z = 10
	H_CH_A = 11
	H_SH_G = 12
	Z_CH_G = 13
	Z_SH_H = 14


class HebrewDate:
	EPOCH = 347996

	def __init__(self, year=1, month=HebrewMonth.TISHREI, day=1):
		self.year = year
		self.month = month
		self.day = day

		raw = day
		if month < HebrewMonth.TISHREI:
			for m in range(HebrewMonth.TISHREI, HebrewDate.numMonthInYear(year) + 1):
				raw += HebrewDate.numdaysinmonth(year, m)
			for m in range(HebrewMonth.NISAN, month):
				raw += HebrewDate.numdaysinmonth(year, m)
		else:
			for m in range(HebrewMonth.TISHREI, month):
				raw += HebrewDate.numdaysinmonth(year, m)
		raw += HebrewDate.elapsedDays(year)
		self.ordinal = raw + HebrewDate.EPOCH

	@classmethod
	def fromordinal(cls, ordinal):
		raw = ordinal - HebrewDate.EPOCH

		y = raw // 366
		while True:
			tempRaw = cls(y + 1, HebrewMonth.TISHREI, 1).ordinal - HebrewDate.EPOCH
			if raw < tempRaw:
				break
			y += 1

		raw1Nisan = cls(y, HebrewMonth.NISAN, 1).ordinal - HebrewDate.EPOCH
		start = HebrewMonth.TISHREI if raw < raw1Nisan else HebrewMonth.NISAN

		m = start
		while True:
			monthEnd = cls(y, m, HebrewDate.numdaysinmonth(y, m)).ordinal - HebrewDate.EPOCH
			if raw <= monthEnd:
				break
			m += 1

		day = raw - (cls(y, m, 1).ordinal - HebrewDate.EPOCH) + 1

		return cls(y, m, day)

	@classmethod
	def fromdate(cls, date):
		return cls.fromordinal(date.ordinal)

	@property
	def next(self):
		return HebrewDate.fromordinal(self.ordinal + 1)

	@property
	def prev(self):
		return HebrewDate.fromordinal(self.ordinal - 1)

	@staticmethod
	def isleapyear(year):
		return ((year * 7) + 1) % 19 < 7

	@staticmethod
	def numMonthInYear(year):
		return HebrewMonth.ADAR_II if HebrewDate.isleapyear(year) else HebrewMonth.ADAR

	@staticmethod
	def elapsedDays(year):
		monthsElapsed = 235 * ((year - 1) // 19)
		monthsElapsed += 12 * ((year - 1) % 19)
		monthsElapsed += (((year - 1) % 19 * 7 + 1) // 19)

		partsElapsed = ((monthsElapsed % 1080) * 793) + 204
		hoursElapsed = 5 + (monthsElapsed * 12) + \
			((monthsElapsed // 1080) * 793) + (partsElapsed // 1080)

		day = 1 + (29 * monthsElapsed) + (hoursElapsed // 24)
		parts = ((hoursElapsed % 24) * 1080) + (partsElapsed % 1080)

		addDay = (parts >= 19440) or \
			(day % 7 == 2 and parts >= 9924 and not HebrewDate.isleapyear(year)) or \
			(day % 7 == 1 and parts >= 16789 and HebrewDate.isleapyear(year - 1))

		altDay = day + 1 if addDay else day
		if altDay % 7 == 0 or altDay % 7 == 3 or altDay % 7 == 5:
			altDay += 1
		return altDay

	@staticmethod
	def numDaysInYear(year):
		return HebrewDate.elapsedDays(year + 1) - HebrewDate.elapsedDays(year)

	@staticmethod
	def hasLongHeshvan(year):
		return HebrewDate.numDaysInYear(year) % 10 == 5

	@staticmethod
	def hasShortKislev(year):
		return HebrewDate.numDaysInYear(year) % 10 == 3

	@staticmethod
	def numdaysinmonth(year, month):
		if month in (HebrewMonth.IYAR, HebrewMonth.TAMMUZ, HebrewMonth.ELUL, HebrewMonth.TEVET, HebrewMonth.ADAR_II):
			return 29
		if month == HebrewMonth.ADAR and not HebrewDate.isleapyear(year):
			return 29
		if month == HebrewMonth.CHESHVAN and not HebrewDate.hasLongHeshvan(year):
			return 29
		if month == HebrewMonth.KISLEV and HebrewDate.hasShortKislev(year):
			return 29
		return 30

	@staticmethod
	def yearLengthType(year):
		if HebrewDate.hasLongHeshvan(year) and not HebrewDate.hasShortKislev(year):
			return YearLength.COMPLETE
		if not HebrewDate.hasLongHeshvan(year) and HebrewDate.hasShortKislev(year):
			return YearLength.DEFICIENT
		return YearLength.REGULAR

	@staticmethod
	def yeartype(year):
		rh = HebrewDate(year, HebrewMonth.TISHREI, 1).dayofweek
		pesach = HebrewDate(year, HebrewMonth.NISAN, 15).dayofweek
		length = HebrewDate.yearLengthType(year)
		if not HebrewDate.isleapyear(year):
			if rh == Weekday.MONDAY and length == YearLength.DEFICIENT and pesach == Weekday.TUESDAY:
				return YearType.B_CH_G
			if rh == Weekday.MONDAY and length == YearLength.COMPLETE and pesach == Weekday.THURSDAY:
				return YearType.B_SH_H
			if rh == Weekday.TUESDAY and length == YearLength.REGULAR and pesach == Weekday.THURSDAY:
				return YearType.G_K_H
			if rh == Weekday.THURSDAY and length == YearLength.REGULAR and pesach == Weekday.SATURDAY:
				return YearType.H_K_Z
			if rh == Weekday.THURSDAY and length == YearLength.COMPLETE and pesach == Weekday.SUNDAY:
				return YearType.H_SH_A
			if rh == Weekday.SATURDAY and length == YearLength.DEFICIENT and pesach == Weekday.SUNDAY:
				return YearType.Z_CH_A
			if rh == Weekday.SATURDAY and length == YearLength.COMPLETE and pesach == Weekday.TUESDAY:
				return YearType.Z_SH_G
		else:
			if rh == Weekday.MONDAY and length == YearLength.DEFICIENT and pesach == Weekday.THURSDAY:
				return YearType.B_CH_H
			if rh == Weekday.MONDAY and length == YearLength.COMPLETE and pesach == Weekday.SATURDAY:
				return YearType.B_SH_Z
			if rh == Weekday.TUESDAY and length == YearLength.REGULAR and pesach == Weekday.SATURDAY:
				return YearType.G_K_Z
			if rh == Weekday.THURSDAY and length == YearLength.DEFICIENT and pesach == Weekday.SUNDAY:
				return YearType.H_CH_A
			if rh == Weekday.THURSDAY and length == YearLength.COMPLETE and pesach == Weekday.TUESDAY:
				return YearType.H_SH_G
			if rh == Weekday.SATURDAY and length == YearLength.DEFICIENT and pesach == Weekday.TUESDAY:
				return YearType.Z_CH_G
			if rh == Weekday.SATURDAY and length == YearLength.COMPLETE and pesach == Weekday.THURSDAY:
				return YearType.Z_SH_H
		return 0


	@staticmethod
	def biblical(year, civil_month):
		is_leap = HebrewDate.isleapyear(year)
		if civil_month <= 6:
			return civil_month + 6
		elif civil_month == 7:
			return 13 if is_leap else 1
		else:
			return civil_month - 7 if is_leap else civil_month - 6


	@property
	def dayofweek(self):
		return (self.ordinal % 7 + 1) % 7 + 1

	@property
	def shabbat(self):
		return HebrewDate.fromordinal(self.ordinal + (7 - self.dayofweek))

	@property
	def shabbatType(self):
		sh = self.shabbat
		year = sh.year

		anchor = HebrewDate(year, HebrewMonth.NISAN, 1)
		hahodeshShabbat = HebrewDate.fromordinal(anchor.ordinal - (anchor.dayofweek % 7))
		if sh.ordinal == hahodeshShabbat.ordinal:
			return ShabbatType.HAHODESH

		if sh.ordinal == hahodeshShabbat.ordinal - 7:
			return ShabbatType.PARAH

		adarMonth = HebrewDate.numMonthInYear(year)
		anchor = HebrewDate(year, adarMonth, 13)
		zahorShabbat = HebrewDate.fromordinal(anchor.ordinal - (anchor.dayofweek % 7))
		if sh.ordinal == zahorShabbat.ordinal:
			return ShabbatType.ZAHOR

		anchor = HebrewDate(year, adarMonth, 1)
		shekalimShabbat = HebrewDate.fromordinal(anchor.ordinal - (anchor.dayofweek % 7))
		if sh.ordinal == shekalimShabbat.ordinal:
			return ShabbatType.SHEKALIM

		anchor = HebrewDate(year, HebrewMonth.NISAN, 14)
		haggadolShabbat = HebrewDate.fromordinal(anchor.ordinal - (anchor.dayofweek % 7))
		if sh.ordinal == haggadolShabbat.ordinal:
			return ShabbatType.HAGGADOL

		anchor = HebrewDate(year, HebrewMonth.TISHREI, 9)
		shuvahShabbat = HebrewDate.fromordinal(anchor.ordinal - (anchor.dayofweek % 7))
		if sh.ordinal == shuvahShabbat.ordinal:
			return ShabbatType.SHUVAH

		anchor = HebrewDate(year, HebrewMonth.AV, 9)
		chazonShabbat = HebrewDate.fromordinal(anchor.ordinal - (anchor.dayofweek % 7))
		if sh.ordinal == chazonShabbat.ordinal:
			return ShabbatType.CHAZON

		dayAfter = HebrewDate.fromordinal(anchor.ordinal + 1)
		nachamuShabbat = HebrewDate.fromordinal(dayAfter.ordinal + (7 - dayAfter.dayofweek))
		if sh.ordinal == nachamuShabbat.ordinal:
			return ShabbatType.NACHAMU

		return None

	@property
	def shabbatChodeshType(self):
		sh = self.shabbat

		if sh.day == 1 and sh.month != HebrewMonth.TISHREI:
			return ShabbatChodeshType.ROSH_CHODESH

		nextDay = HebrewDate.fromordinal(sh.ordinal + 1)
		if nextDay.day == 1 and nextDay.month != HebrewMonth.TISHREI:
			return ShabbatChodeshType.MACHAR_CHODESH

		return None

	def __repr__(self):
		return f"HebrewDate({self.year}, {self.month}, {self.day})"

	def __eq__(self, other):
		return isinstance(other, HebrewDate) and self.ordinal == other.ordinal

	def __lt__(self, other):
		return isinstance(other, HebrewDate) and self.ordinal < other.ordinal

	def __le__(self, other):
		return isinstance(other, HebrewDate) and self.ordinal <= other.ordinal

	def __gt__(self, other):
		return isinstance(other, HebrewDate) and self.ordinal > other.ordinal

	def __ge__(self, other):
		return isinstance(other, HebrewDate) and self.ordinal >= other.ordinal

	def __hash__(self):
		return hash(self.ordinal)


class HebrewMonthCalendar:
	def __init__(self, year, month):
		daysInMonth = HebrewDate.numdaysinmonth(year, month)
		firstDate = HebrewDate(year, month, 1)
		startCol = firstDate.dayofweek - 1

		totalCells = startCol + daysInMonth
		rowCount = (totalCells + 6) // 7

		self.dates = [[None] * 7 for _ in range(6)]

		dayCounter = 1
		for r in range(rowCount):
			for c in range(7):
				if (r == 0 and c < startCol) or dayCounter > daysInMonth:
					self.dates[r][c] = None
				else:
					self.dates[r][c] = HebrewDate(year, month, dayCounter)
					dayCounter += 1


class HebrewYearCalendar:
	def __init__(self, year):
		self.year = year
		self.monthCalendars = []
		for month in range(HebrewMonth.TISHREI, HebrewDate.numMonthInYear(year) + 1):
			self.monthCalendars.append(HebrewMonthCalendar(year, month))
		for month in range(HebrewMonth.NISAN, HebrewMonth.TISHREI):
			self.monthCalendars.append(HebrewMonthCalendar(year, month))

	@property
	def weeks(self):
		start = HebrewDate(self.year, HebrewMonth.TISHREI, 1)
		end   = HebrewDate.fromordinal(start.ordinal + HebrewDate.numDaysInYear(self.year) - 1)
		cur = start
		while cur.dayofweek != 1:
			cur = cur.prev
		first_sunday = cur
		cur = end
		while cur.dayofweek != 7:
			cur = cur.next
		last_saturday = cur
		weeks = []
		d = first_sunday
		while d.ordinal <= last_saturday.ordinal:
			week = []
			for _ in range(7):
				if d.ordinal < start.ordinal or d.ordinal > end.ordinal:
					week.append(None)
				else:
					week.append(d)
				d = d.next
			weeks.append(week)
		return weeks


	@property
	def saturdays(self):
		return [week[6] for week in self.weeks if week[6]]







class HebrewWeekdayDayNames:
	NAMES_EN = [
		"",
		"First day",
		"Second day",
		"Third day",
		"Fourth day",
		"Fifth day",
		"Sixth day",
		"Shabbat",
	]
	NAMES_HE = [
		"",
		"יום ראשון",
		"יום שני",
		"יום שלישי",
		"יום רביעי",
		"יום חמישי",
		"יום שישי",
		"יום שבת",
	]

	def __init__(self, lang, dayofweek):
		self.lang = lang
		self.dayofweek = dayofweek

	def name(self):
		if self.lang == 'en':
			return self.NAMES_EN[self.dayofweek]
		return self.NAMES_HE[self.dayofweek]

class HebrewWeekdayNightNames:
	NAMES_EN = [
		"",
		"Eve of first day",
		"Eve of second day",
		"Eve of third day",
		"Eve of fourth day",
		"Eve of fifth day",
		"Eve of sixth day",
		"Eve of Shabbat",
	]
	NAMES_HE = [
		"",
		"ליל ראשון",
		"ליל שני",
		"ליל שלישי",
		"ליל רביעי",
		"ליל חמישי",
		"ליל שישי",
		"ליל שבת",
	]

	def __init__(self, lang, dayofweek):
		self.lang = lang
		self.dayofweek = dayofweek

	def name(self):
		if self.lang == 'en':
			return self.NAMES_EN[self.dayofweek]
		return self.NAMES_HE[self.dayofweek]







class HebrewMonthNames:
	NAMES_EN = [
		"", "Nisan", "Iyar", "Sivan", "Tammuz", "Av", "Elul",
		"Tishrei", "Cheshvan", "Kislev", "Tevet", "Shevat", "Adar", "Adar II",
	]
	NAMES_HE = [
		"", "ניסן", "אייר", "סיוון", "תמוז", "אב", "אלול",
		"תשרי", "חשון", "כסלו", "טבת", "שבט", "אדר", "אדר ב׳",
	]

	def __init__(self, lang, year, month):
		self.lang = lang
		self.year = year
		self.month = month

	def name(self):
		if self.lang == 'en':
			if self.month == HebrewMonth.ADAR and HebrewDate.isleapyear(self.year):
				return "Adar I"
			return self.NAMES_EN[self.month]
		elif self.lang == 'he':
			if self.month == HebrewMonth.ADAR and HebrewDate.isleapyear(self.year):
				return "אדר א׳"
			return self.NAMES_HE[self.month]
		return ""


class ShabbatTypeNames:
	NAMES_EN = [
		"", "Shekalim", "Zachor", "Parah", "HaChodesh", "HaGadol",
		"Shuvah", "Chazon", "Nachamu",
	]
	NAMES_HE = [
		"", "שקלים", "זכור", "פרה", "החודש", "הגדול",
		"שובה", "חזון", "נחמו",
	]

	def __init__(self, lang, shabbatType):
		self.lang = lang
		self.shabbatType = shabbatType

	def name(self):
		if self.lang == 'en':
			return self.NAMES_EN[self.shabbatType]
		return self.NAMES_HE[self.shabbatType]


class ShabbatChodeshTypeNames:
	NAMES_EN = ["", "Machar Chodesh", "Rosh Chodesh"]
	NAMES_HE = ["", "מחר חודש", "ראש חודש"]

	def __init__(self, lang, shabbatChodeshType):
		self.lang = lang
		self.shabbatChodeshType = shabbatChodeshType

	def name(self):
		if self.lang == 'en':
			return self.NAMES_EN[self.shabbatChodeshType]
		return self.NAMES_HE[self.shabbatChodeshType]


if __name__ == "__main__":
	import time as _systime
	from Date import Date

	now = _systime.localtime()
	today = Date(now.tm_year, now.tm_mon, now.tm_mday)
	hToday = HebrewDate.fromdate(today)

	print(f"Today is {hToday!r} ({WeekdayNames('en', hToday.dayofweek).name()}), "
		f"{HebrewMonthNames('en', hToday.year, hToday.month).name()} {hToday.day}, {hToday.year}")
	print()

	year = hToday.year
	months = list(range(HebrewMonth.TISHREI, HebrewDate.numMonthInYear(year) + 1))
	months += list(range(HebrewMonth.NISAN, HebrewMonth.TISHREI))

	for month in months:
		cal = HebrewMonthCalendar(year, month)
		print(f"{HebrewMonthNames('en', year, month).name()} {year}".center(28))
		print("".join(f"{WeekdayNames('en', d).shortname():>4}" for d in range(1, 8)))
		for row in cal.dates:
			line = "".join(f"{cell.day:>4}" if cell else "    " for cell in row)
			shabbat = row[6]
			if shabbat:
				labels = []
				if shabbat.shabbatType is not None:
					labels.append(ShabbatTypeNames('en', shabbat.shabbatType).name())
				if shabbat.shabbatChodeshType is not None:
					labels.append(ShabbatChodeshTypeNames('en', shabbat.shabbatChodeshType).name())
				if labels:
					line += "  " + ", ".join(labels)
			print(line)
		print()