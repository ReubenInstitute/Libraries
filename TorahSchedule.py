from enum import IntEnum
from Date import Weekday
from HebrewDate import HebrewDate, HebrewMonth


class Parashah(IntEnum):
	BERESHIT = 1
	NOACH = 2
	LECH_LECHA = 3
	VAYERA = 4
	CHAYEI_SARAH = 5
	TOLEDOT = 6
	VAYETZE = 7
	VAYISHLACH = 8
	VAYESHEV = 9
	MIKETZ = 10
	VAYIGASH = 11
	VAYECHI = 12
	SHEMOT = 13
	VAEIRA = 14
	BO = 15
	BESHALACH = 16
	YITRO = 17
	MISHPATIM = 18
	TERUMAH = 19
	TETZAVEH = 20
	KI_TISA = 21
	VAYAKHEL = 22
	PEKUDEI = 23
	VAYIKRA = 24
	TZAV = 25
	SHEMINI = 26
	TAZRIA = 27
	METZORA = 28
	ACHAREI_MOT = 29
	KEDOSHIM = 30
	EMOR = 31
	BEHAR = 32
	BECHUKOTAI = 33
	BAMIDBAR = 34
	NASO = 35
	BEHAALOTECHA = 36
	SHELACH = 37
	KORACH = 38
	CHUKAT = 39
	BALAK = 40
	PINCHAS = 41
	MATOT = 42
	MASEI = 43
	DEVARIM = 44
	VAETCHANAN = 45
	EIKEV = 46
	REEH = 47
	SHOFTIM = 48
	KI_TEITZEI = 49
	KI_TAVO = 50
	NITZAVIM = 51
	VAYEILECH = 52
	HAAZINU = 53
	VEZOT_HABERACHAH = 54


class HolidayReading(IntEnum):
	NONE = 0
	ROSH_HASHANAH_I = 55
	YOM_KIPPUR = 56
	SUCCOTH_I = 57
	HOL_HAMOED_SUCCOTH = 58
	SHEMINI_AZERETH = 59
	PESAH_I = 60
	HOL_HAMOED_PESAH = 61
	PESAH_VII = 62
	PESAH_VIII = 63
	SHAVUOTH_II = 64


class ParashahNames:
	NAMES_EN = [
		"",
		"BeReshit",
		"Noach",
		"Lech Lecha",
		"VaYera",
		"Chayei Sarah",
		"Toledot",
		"VaYetze",
		"VaYishlach",
		"VaYeshev",
		"MiKetz",
		"VaYigash",
		"VaYechi",
		"Shemot",
		"VaEira",
		"Bo",
		"BeShalach",
		"Yitro",
		"Mishpatim",
		"Terumah",
		"Tetzaveh",
		"Ki Tisa",
		"VaYakhel",
		"Pekudei",
		"VaYikra",
		"Tzav",
		"Shemini",
		"Tazria",
		"Metzora",
		"Acharei Mot",
		"Kedoshim",
		"Emor",
		"Behar",
		"Bechukotai",
		"BaMidbar",
		"Naso",
		"BeHa'alotecha",
		"Shelach",
		"Korach",
		"Chukat",
		"Balak",
		"Pinchas",
		"Matot",
		"Masei",
		"Devarim",
		"Va'etchanan",
		"Eikev",
		"Re'eh",
		"Shoftim",
		"Ki Teitzei",
		"Ki Tavo",
		"Nitzavim",
		"Vayeilech",
		"Ha'azinu",
		"Vezot Haberachah"
	]
	NAMES_HE = [
		"",
		"בראשית",
		"נֹחַ",
		"לֶךְ-לְךָ",
		"וַיֵּרָא",
		"חַיֵּי שָׂרָה",
		"תּוֹלְדֹת",
		"וַיֵּצֵא",
		"וַיִּשְׁלַח",
		"וַיֵּשֶׁב",
		"מִקֵּץ",
		"וַיִּגַּשׁ",
		"וַיְחִי",
		"שְׁמוֹת",
		"וָאֵרָא",
		"בֹּא",
		"בְּשַׁלַּח",
		"יִתְרוֹ",
		"מִּשְׁפָּטִים",
		"תְּרוּמָה",
		"תְּצַוֶּה",
		"כִּי תִשָּׂא",
		"וַיַּקְהֵל",
		"פְקוּדֵי",
		"ויקרא",
		"צו",
		"שמיני",
		"תזריע",
		"מצרע",
		"אחרי מות",
		"קדשים",
		"אמור",
		"בְּהַר",
		"בְּחֻקֹּתָי",
		"בְּמִדְבַּר",
		"נָשֹׂא",
		"בְּהַעֲלוֹתֶךָ",
		"שְׁלַח",
		"קֹרַח",
		"חֻקַּת",
		"בָּלָק",
		"פִּינְחָס",
		"מַטּוֹת",
		"מַסְעֵי",
		"דְּבָרִים",
		"וָאֶתְחַנַּן",
		"עֵיקֶב",
		"רְאֵה",
		"שֹׁפְטִים",
		"כִּי תֵצֵא",
		"כִּי תָבוֹא",
		"נִצָּבִים",
		"וַיֵּלֶךְ",
		"הַאֲזִינוּ",
		"וְזֹאת הַבְּרָכָה"
	]

	def __init__(self, lang, id):
		self.lang = lang
		self.id = id

	def name(self):
		if self.lang == 'en':
			return self.NAMES_EN[self.id] if 1 <= self.id <= 54 else ""
		return self.NAMES_HE[self.id] if 1 <= self.id <= 54 else ""


class HolidayReadingNames:
	NAMES_EN = [
		"Rosh Hashanah I",
		"Yom Kippur",
		"Succoth I",
		"Hol HaMoed Succoth",
		"Shemini Azereth",
		"Pesah I",
		"Hol HaMoed Pesah",
		"Pesah VII",
		"Pesah VIII",
		"Shavuoth II"
	]
	NAMES_HE = [
		"ראש השנה א׳",
		"יום כיפור",
		"סוכות א׳",
		"חול המועד סוכות",
		"שמיני עצרת",
		"פסח א׳",
		"חול המועד פסח",
		"פסח שביעי",
		"פסח שמיני",
		"שבועות ב׳"
	]

	def __init__(self, lang, id):
		self.lang = lang
		self.id = id

	def name(self):
		if self.id == HolidayReading.NONE:
			return ""
		idx = self.id - 55
		if 0 <= idx < len(self.NAMES_EN):
			if self.lang == 'en':
				return self.NAMES_EN[idx]
			return self.NAMES_HE[idx]
		return ""



from Date import Weekday
from HebrewDate import HebrewDate, HebrewMonth


class TorahSchedule:

	def __init__(self, hebrew_year):
		self.year = hebrew_year

		start = HebrewDate(hebrew_year, HebrewMonth.TISHREI, 1)
		end = HebrewDate.fromordinal(
				start.ordinal + HebrewDate.numDaysInYear(hebrew_year) - 1
		)

		self.dates = []
		d = start
		while d.ordinal <= end.ordinal:
			if d.dayofweek == Weekday.SATURDAY:
				self.dates.append(d)
			d = d.next

		n = len(self.dates)
		self.israel = [[None, None] for _ in range(n)]
		self.diaspora = [[None, None] for _ in range(n)]

		m_tishrei = HebrewDate(hebrew_year, HebrewMonth.TISHREI, 1).month
		m_nisan = HebrewDate(hebrew_year, HebrewMonth.NISAN, 1).month
		m_sivan = HebrewDate(hebrew_year, HebrewMonth.SIVAN, 1).month

		is_leap = HebrewDate.isleapyear(hebrew_year)
		year_len = HebrewDate.numDaysInYear(hebrew_year)

		rh_day = start.dayofweek
		pesach_day = HebrewDate(hebrew_year, HebrewMonth.NISAN, 15).dayofweek
		next_rh_day = HebrewDate(hebrew_year + 1, HebrewMonth.TISHREI, 1).dayofweek

		# Step 1: Overlay Holidays
		for i, d in enumerate(self.dates):
			if d.month == m_tishrei and d.day in (1, 2):
				self.israel[i] = [HolidayReading.ROSH_HASHANAH_I, None]
				self.diaspora[i] = [HolidayReading.ROSH_HASHANAH_I, None]

			if d.month == m_tishrei and d.day == 10:
				self.israel[i] = [HolidayReading.YOM_KIPPUR, None]
				self.diaspora[i] = [HolidayReading.YOM_KIPPUR, None]

			if d.month == m_tishrei and d.day == 15:
				self.israel[i] = [HolidayReading.SUCCOTH_I, None]
				self.diaspora[i] = [HolidayReading.SUCCOTH_I, None]

			if d.month == m_tishrei and 16 <= d.day <= 21:
				self.israel[i] = [HolidayReading.HOL_HAMOED_SUCCOTH, None]
				self.diaspora[i] = [HolidayReading.HOL_HAMOED_SUCCOTH, None]

			if d.month == m_tishrei and d.day == 22:
				self.israel[i] = [Parashah.VEZOT_HABERACHAH, None]
				self.diaspora[i] = [HolidayReading.SHEMINI_AZERETH, None]

			if d.month == m_tishrei and d.day == 23:
				self.diaspora[i] = [Parashah.VEZOT_HABERACHAH, None]

			if d.month == m_nisan and d.day == 15:
				self.israel[i] = [HolidayReading.PESAH_I, None]
				self.diaspora[i] = [HolidayReading.PESAH_I, None]

			if d.month == m_nisan and 16 <= d.day <= 20:
				self.israel[i] = [HolidayReading.HOL_HAMOED_PESAH, None]
				self.diaspora[i] = [HolidayReading.HOL_HAMOED_PESAH, None]

			if d.month == m_nisan and d.day == 21:
				self.israel[i] = [HolidayReading.PESAH_VII, None]
				self.diaspora[i] = [HolidayReading.PESAH_VII, None]

			if d.month == m_nisan and d.day == 22:
				self.diaspora[i] = [HolidayReading.PESAH_VIII, None]

			if d.month == m_sivan and d.day == 6:
				self.israel[i] = [HolidayReading.SHAVUOTH_II, None]
				self.diaspora[i] = [HolidayReading.SHAVUOTH_II, None]

			if d.month == m_sivan and d.day == 7:
				self.diaspora[i] = [HolidayReading.SHAVUOTH_II, None]

		# Step 2: Populate Israel Schedule
		st_israel = next(
				i
				for i, d in enumerate(self.dates)
				if d > HebrewDate(hebrew_year, HebrewMonth.TISHREI, 22)
		)
		unassigned = [
				i for i in range(st_israel) if self.israel[i][0] is None
		]
		if len(unassigned) == 1:
			self.israel[unassigned[0]] = [Parashah.HAAZINU, None]
		elif len(unassigned) == 2:
			self.israel[unassigned[0]] = [Parashah.VAYEILECH, None]
			self.israel[unassigned[1]] = [Parashah.HAAZINU, None]

		current = Parashah.BERESHIT
		for i in range(st_israel, n):
			if (
					self.israel[i][0] is None
					and current <= Parashah.VEZOT_HABERACHAH
			):
				combine = False
				if (
						current == Parashah.VAYAKHEL
						and not is_leap
						and not (rh_day == Weekday.THURSDAY and year_len == 355)
				):
					combine = True
				elif current in (Parashah.TAZRIA, Parashah.ACHAREI_MOT) and not is_leap:
					combine = True
				elif (
						current == Parashah.BEHAR
						and not is_leap
						and pesach_day != Weekday.SATURDAY
				):
					combine = True
				elif current == Parashah.MATOT:
					if (
							not is_leap
							or pesach_day == Weekday.THURSDAY
							or (year_len == 383 and rh_day == Weekday.THURSDAY)
					):
						combine = True
				elif current == Parashah.NITZAVIM and next_rh_day in (
						Weekday.THURSDAY,
						Weekday.SATURDAY,
				):
					combine = True

				if combine:
					self.israel[i] = [current, Parashah(current + 1)]
					current = Parashah(current + 2)
				else:
					self.israel[i] = [current, None]
					current = Parashah(current + 1)

		# Step 3: Populate Diaspora Schedule
		st_diaspora = next(
				i
				for i, d in enumerate(self.dates)
				if d > HebrewDate(hebrew_year, HebrewMonth.TISHREI, 23)
		)
		unassigned = [
				i for i in range(st_diaspora) if self.diaspora[i][0] is None
		]
		if len(unassigned) == 1:
			self.diaspora[unassigned[0]] = [Parashah.HAAZINU, None]
		elif len(unassigned) == 2:
			self.diaspora[unassigned[0]] = [Parashah.VAYEILECH, None]
			self.diaspora[unassigned[1]] = [Parashah.HAAZINU, None]

		current = Parashah.BERESHIT
		for i in range(st_diaspora, n):
			if (
					self.diaspora[i][0] is None
					and current <= Parashah.VEZOT_HABERACHAH
			):
				combine = False
				if (
						current == Parashah.VAYAKHEL
						and not is_leap
						and not (rh_day == Weekday.THURSDAY and year_len == 355)
				):
					combine = True
				elif (
						current in (Parashah.TAZRIA, Parashah.ACHAREI_MOT, Parashah.BEHAR)
						and not is_leap
				):
					combine = True
				elif current == Parashah.CHUKAT and pesach_day == Weekday.THURSDAY:
					combine = True
				elif current == Parashah.MATOT:
					if not is_leap:
						combine = True
					elif pesach_day in (Weekday.THURSDAY, Weekday.SATURDAY):
						combine = True
					elif year_len == 383 and rh_day in (Weekday.MONDAY, Weekday.THURSDAY):
						combine = True
				elif current == Parashah.NITZAVIM and next_rh_day in (
						Weekday.THURSDAY,
						Weekday.SATURDAY,
				):
					combine = True

				if combine:
					self.diaspora[i] = [current, Parashah(current + 1)]
					current = Parashah(current + 2)
				else:
					self.diaspora[i] = [current, None]
					current = Parashah(current + 1)




if __name__ == "__main__":
	import time as _systime
	import argparse
	from Date import Date
	from HebrewDate import HebrewDate, HebrewMonth, HebrewMonthNames

	parser = argparse.ArgumentParser(description='Display Torah readings for a Hebrew year')
	parser.add_argument('-y', '--year', type=int, help='Civil year to use (defaults to current year)')
	args = parser.parse_args()

	now = _systime.localtime()
	if args.year:
		today = Date(args.year, now.tm_mon, now.tm_mday)
	else:
		today = Date(now.tm_year, now.tm_mon, now.tm_mday)

	hToday = HebrewDate.fromdate(today)
	year = hToday.year

	print(year)

	torah = TorahSchedule(year)

	for i, d in enumerate(torah.dates):
		israel = torah.israel[i]
		diaspora = torah.diaspora[i]

		reading_israel = ""
		if israel[0] is not None:
			if israel[0] >= 55:
				reading_israel = HolidayReadingNames('en', israel[0]).name()
			else:
				reading_israel = ParashahNames('en', israel[0]).name()
			if israel[1] is not None:
				reading_israel += " + " + ParashahNames('en', israel[1]).name()

		reading_diaspora = ""
		if diaspora[0] is not None:
			if diaspora[0] >= 55:
				reading_diaspora = HolidayReadingNames('en', diaspora[0]).name()
			else:
				reading_diaspora = ParashahNames('en', diaspora[0]).name()
			if diaspora[1] is not None:
				reading_diaspora += " + " + ParashahNames('en', diaspora[1]).name()

		date_str = f"{HebrewMonthNames('en', d.year, d.month).name()} {d.day}"

		print(f"{i+1:2d} {date_str}: {reading_israel} / {reading_diaspora}")
		