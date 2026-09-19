import math
from enum import IntEnum

import Astro

class Weekday(IntEnum):
	SUNDAY = 1
	MONDAY = 2
	TUESDAY = 3
	WEDNESDAY = 4
	THURSDAY = 5
	FRIDAY = 6
	SATURDAY = 7

class GregorianMonth(IntEnum):
	JANUARY = 1
	FEBRUARY = 2
	MARCH = 3
	APRIL = 4
	MAY = 5
	JUNE = 6
	JULY = 7
	AUGUST = 8
	SEPTEMBER = 9
	OCTOBER = 10
	NOVEMBER = 11
	DECEMBER = 12

class Date:
	EPOCH = 1721425

	def __init__(self, year=1, month=1, day=1):
		self.year = year
		self.month = month
		self.day = day

		serial = day
		for m in range(1, month):
			serial += Date.numdaysinmonth(m, year)
		y = year - 1
		serial += 365 * y
		serial += y // 4
		serial -= y // 100
		serial += y // 400
		self.ordinal = serial + Date.EPOCH

	@classmethod
	def fromordinal(cls, ordinal):
		serial = ordinal - Date.EPOCH

		y = 1
		while True:
			daysInYear = 366 if Date.isleapyear(y) else 365
			if serial <= daysInYear:
				break
			serial -= daysInYear
			y += 1

		m = 1
		while True:
			daysInMonth = Date.numdaysinmonth(m, y)
			if serial <= daysInMonth:
				break
			serial -= daysInMonth
			m += 1

		return cls(y, m, serial)

	def __add__(self, x):
		return Date.fromordinal(self.ordinal + x)

	def __sub__(self, x):
		return Date.fromordinal(self.ordinal - x)

	@property
	def next(self):
		return self + 1

	@property
	def prev(self):
		return self - 1

	@staticmethod
	def isleapyear(year):
		if year % 4 != 0:
			return False
		if year % 100 != 0:
			return True
		return year % 400 == 0

	@staticmethod
	def numdaysinmonth(month, year):
		if month == GregorianMonth.FEBRUARY:
			return 29 if Date.isleapyear(year) else 28
		if month in (GregorianMonth.APRIL, GregorianMonth.JUNE, GregorianMonth.SEPTEMBER, GregorianMonth.NOVEMBER):
			return 30
		return 31

	@property
	def dayofweek(self):
		return (self.ordinal % 7 + 1) % 7 + 1

	def __repr__(self):
		return f"Date({self.year}, {self.month}, {self.day})"

	def __eq__(self, other):
		return isinstance(other, Date) and self.ordinal == other.ordinal

	def __hash__(self):
		return hash(self.ordinal)


class Time:
	def __init__(self, hour=0, minute=0, second=0):
		self.hour = hour
		self.minute = minute
		self.second = second
		self.serial = hour * 3600 + minute * 60 + second

	@classmethod
	def fromserial(cls, serial):
		hour = serial // 3600
		remainder = serial % 3600
		minute = remainder // 60
		second = remainder % 60
		return cls(hour, minute, second)

	def __add__(self, x):
		return Time.fromserial((self.serial + x) % 86400)

	def __repr__(self):
		return f"Time({self.hour}, {self.minute}, {self.second})"

	def __eq__(self, other):
		return isinstance(other, Time) and self.serial == other.serial

	def __hash__(self):
		return hash(self.serial)


class DateTimeSpan:
	def __init__(self, days, seconds):
		self.days = days
		self.seconds = seconds


class DateTime:
	def __init__(self, date, time):
		self.date = date
		self.time = time

	@property
	def julianday(self):
		return self.date.ordinal - 0.5 + (self.time.serial / 86400.0)

	@property
	def julianDay2K(self):
		return self.julianday - Astro.J2000

	@classmethod
	def fromjulianday(cls, jd):
		totalSeconds = round((jd + 0.5) * 86400)
		ordinal, serial = divmod(totalSeconds, 86400)
		return cls(Date.fromordinal(ordinal), Time.fromserial(serial))

	def __sub__(self, other):
		if isinstance(other, DateTime):
			days = self.date.ordinal - other.date.ordinal
			if days == 0:
				secs = self.time.serial - other.time.serial
			else:
				secs = (self.time + (-other.time.serial)).serial   # <-- BUG
			return DateTimeSpan(days, secs)
		return NotImplemented

	def __add__(self, seconds):
		secs = int(seconds)
		new_time = self.time + secs
		if secs >= 0 and new_time.serial < self.time.serial:
			return DateTime(self.date + 1, new_time)
		elif secs < 0 and new_time.serial > self.time.serial:
			return DateTime(self.date - 1, new_time)
		return DateTime(self.date, new_time)

	def __lt__(self, other):
		if isinstance(other, DateTime):
			return (self.date.ordinal, self.time.serial) < (other.date.ordinal, other.time.serial)
		return NotImplemented

	def __le__(self, other):
		if isinstance(other, DateTime):
			return (self.date.ordinal, self.time.serial) <= (other.date.ordinal, other.time.serial)
		return NotImplemented

	def __gt__(self, other):
		if isinstance(other, DateTime):
			return (self.date.ordinal, self.time.serial) > (other.date.ordinal, other.time.serial)
		return NotImplemented

	def __ge__(self, other):
		if isinstance(other, DateTime):
			return (self.date.ordinal, self.time.serial) >= (other.date.ordinal, other.time.serial)
		return NotImplemented


	def __repr__(self):
		return f"DateTime({self.date!r}, {self.time!r})"

	def __eq__(self, other):
		return isinstance(other, DateTime) and \
			self.date == other.date and self.time == other.time



class MonthCalendar:
	def __init__(self, year, month):
		daysInMonth = Date.numdaysinmonth(month, year)
		firstDate = Date(year, month, 1)
		startCol = firstDate.dayofweek - 1
		self.dates = [[None] * 7 for _ in range(6)]
		dayCounter = 1
		for r in range(6):
			for c in range(7):
				if (r == 0 and c < startCol) or dayCounter > daysInMonth:
					self.dates[r][c] = None
				else:
					self.dates[r][c] = Date(year, month, dayCounter)
					dayCounter += 1

class YearCalendar:
	def __init__(self, year):
		self.year = year
		self.monthCalendars = [MonthCalendar(year, m) for m in range(1, 13)]

	@property
	def weeks(self):
		start = Date(self.year, 1, 1)
		end   = Date(self.year, 12, 31)
		cur = start
		while cur.dayofweek != Weekday.SUNDAY:
			cur = cur.prev
		first_sunday = cur
		cur = end
		while cur.dayofweek != Weekday.SATURDAY:
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


class WeekdayNames:
	WEEKDAY_NAMES = {
		'en': ["", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"],
		'he': ["", "ראשון", "שני", "שלישי", "רביעי", "חמישי", "שישי", "שבת"],
	}
	SHORT_NAMES = {
		'en': ["", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
		'he': ["", "א", "ב", "ג", "ד", "ה", "ו", "ש"],
	}

	def __init__(self, lang, dayofweek):
		self.lang = lang
		self.dayofweek = dayofweek

	def name(self):
		return self.WEEKDAY_NAMES[self.lang][self.dayofweek]

	def shortname(self):
		return self.SHORT_NAMES[self.lang][self.dayofweek]


class MonthNames:
	MONTH_NAMES = {
		'en': ["", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
		'he': ["", "ינואר", "פברואר", "מרץ", "אפריל", "מאי", "יוני", "יולי", "אוגוסט", "ספטמבר", "אוקטובר", "נובמבר", "דצמבר"],
	}

	def __init__(self, lang, month):
		self.lang = lang
		self.month = month

	def name(self):
		return self.MONTH_NAMES[self.lang][self.month]


if __name__ == "__main__":
	import time as _systime

	now = _systime.localtime()
	today = Date(now.tm_year, now.tm_mon, now.tm_mday)
	nowTime = Time(now.tm_hour, now.tm_min, now.tm_sec)

	print(f"Today is {today!r} ({WeekdayNames('en', today.dayofweek).name()}), {nowTime!r}")
	print()

	for month in range(1, 13):
		cal = MonthCalendar(today.year, month)
		print(f"{MonthNames('en', month).name()} {today.year}".center(28))
		print("".join(f"{WeekdayNames('en', d).shortname():>4}" for d in range(1, 8)))
		for row in cal.dates:
			print("".join(f"{cell.day:>4}" if cell else "   " for cell in row))
		print()
