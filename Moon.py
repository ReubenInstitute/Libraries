from enum import IntEnum
import math

import Astro
from Astro import CelestialLocation
from Date import DateTime, Date, Time
from Sun import Sun

RAD = math.pi / 180.0



class MoonPhaseColor(IntEnum):
	NEW_MOON = 0xB35900
	WAXING_CRESCENT = 0xC6650A
	FIRST_QUARTER = 0xD97614
	WAXING_GIBBOUS = 0xEC8A1E
	FULL_MOON = 0xFF9800
	WANING_GIBBOUS = 0xEC8A1E
	LAST_QUARTER = 0xD97614
	WANING_CRESCENT = 0xC6650A


class MoonPhaseType(IntEnum):
	NEW_MOON = 1
	WAXING_CRESCENT = 2
	FIRST_QUARTER = 3
	WAXING_GIBBOUS = 4
	FULL_MOON = 5
	WANING_GIBBOUS = 6
	LAST_QUARTER = 7
	WANING_CRESCENT = 8


class MoonIllumination:
	def __init__(self, moon):
		self.moon = moon

	@property
	def phi(self):
		sunLocation = self.moon.sun.celestialLocation
		moonLocation = self.moon.celestialLocation
		return math.acos(
			math.sin(sunLocation.declination) * math.sin(moonLocation.declination) +
			math.cos(sunLocation.declination) * math.cos(moonLocation.declination) *
			math.cos(sunLocation.rightascension - moonLocation.rightascension)
		)

	@property
	def inc(self):
		return math.atan2(
			self.moon.sun.distance * math.sin(self.phi),
			self.moon.distance - self.moon.sun.distance * math.cos(self.phi)
		)

	@property
	def fraction(self):
		return (1.0 + math.cos(self.inc)) / 2.0

	@property
	def angle(self):
		sunLocation = self.moon.sun.celestialLocation
		moonLocation = self.moon.celestialLocation
		return math.atan2(
			math.cos(sunLocation.declination) * math.sin(sunLocation.rightascension - moonLocation.rightascension),
			math.sin(sunLocation.declination) * math.cos(moonLocation.declination) -
			math.cos(sunLocation.declination) * math.sin(moonLocation.declination) *
			math.cos(sunLocation.rightascension - moonLocation.rightascension)
		)

	@property
	def phase(self):
		return 0.5 + 0.5 * self.inc * (-1.0 if self.angle < 0.0 else 1.0) / math.pi

	@property
	def phaseType(self):
		p = self.phase
		if p < 0.0625 or p >= 0.9375:
			return MoonPhaseType.NEW_MOON
		if p < 0.3125:
			return MoonPhaseType.WAXING_CRESCENT
		if p < 0.4375:
			return MoonPhaseType.FIRST_QUARTER
		if p < 0.5625:
			return MoonPhaseType.FULL_MOON
		if p < 0.6875:
			return MoonPhaseType.WANING_GIBBOUS
		if p < 0.8125:
			return MoonPhaseType.LAST_QUARTER
		return MoonPhaseType.WANING_CRESCENT

	@property
	def phaseEmoji(self):
		north = self.moon.location.latitude >= 0
		t = self.phaseType
		if t == MoonPhaseType.NEW_MOON:
			return "🌑"
		if t == MoonPhaseType.WAXING_CRESCENT:
			return "🌒" if north else "🌘"
		if t == MoonPhaseType.FIRST_QUARTER:
			return "🌓" if north else "🌗"
		if t == MoonPhaseType.FULL_MOON:
			return "🌕"
		if t == MoonPhaseType.WANING_GIBBOUS:
			return "🌖" if north else "🌔"
		if t == MoonPhaseType.LAST_QUARTER:
			return "🌗" if north else "🌓"
		if t == MoonPhaseType.WANING_CRESCENT:
			return "🌘" if north else "🌒"
		return "🌑"


class Moon:
	def __init__(self, location, datetime):
		self.location = location
		self.datetime = datetime
		self.sun = Sun(location, datetime)

	def updateDateTime(self, datetime):
		self.datetime = datetime
		self.sun.updateDateTime(datetime)

	@property
	def celestialLocation(self):
		days = self.datetime.julianDay2K
		L = RAD * (218.316 + 13.176396 * days)
		M = RAD * (134.963 + 13.064993 * days)
		F = RAD * (93.272 + 13.229350 * days)
		l = L + RAD * 6.289 * math.sin(M)
		b = RAD * 5.128 * math.sin(F)
		ra = math.atan2(
			math.sin(l) * math.cos(Astro.EARTH_OBLIQUITY) - math.tan(b) * math.sin(Astro.EARTH_OBLIQUITY),
			math.cos(l)
		)
		dec = math.asin(
			math.sin(b) * math.cos(Astro.EARTH_OBLIQUITY) +
			math.cos(b) * math.sin(Astro.EARTH_OBLIQUITY) * math.sin(l)
		)
		return CelestialLocation(ra, dec)

	@property
	def distance(self):
		days = self.datetime.julianDay2K
		M = RAD * (134.963 + 13.064993 * days)
		return 385001.0 - 20905.0 * math.cos(M)

#	@property
#	def altitude(self):
#		lw = RAD * -self.location.longitude
#		phi = RAD * self.location.latitude
#		moonDir = self.celestialLocation
#		H = Moon.siderealTime(self.datetime, lw) - moonDir.rightascension
#		h = math.asin(math.sin(phi) * math.sin(moonDir.declination) +
#		              math.cos(phi) * math.cos(moonDir.declination) * math.cos(H))
#		return h + Moon.astroRefraction(h)


	@property
	def altitude(self):
		lw = RAD * -self.location.longitude
		phi = RAD * self.location.latitude
		moonDir = self.celestialLocation
		H = Moon.siderealTime(self.datetime, lw) - moonDir.rightascension
		h = math.asin(math.sin(phi) * math.sin(moonDir.declination) +
				 math.cos(phi) * math.cos(moonDir.declination) * math.cos(H))

		# Parallax correction (essential for the moon!)
		parallax = math.asin(6371.0 / self.distance) * math.cos(h)
		h = h - parallax  # Moon appears lower due to parallax

		return h + Moon.astroRefraction(h)


	@property
	def altitudes(self):
		alts = []
		for h in range(25):
			if h == 24:
				t = DateTime(self.datetime.date.next, Time(0, 0, 0))
			else:
				t = DateTime(self.datetime.date, Time(h, 0, 0))
			m = Moon(self.location, t)
			alts.append(m.altitude)
		return alts

	def _calculateTimes(self):
		alts = self.altitudes
		riseHour = None
		setHour = None

		for i in range(23):
			h0 = alts[i]
			h1 = alts[i + 1]
			h2 = alts[i + 2]

			a = (h0 + h2) / 2.0 - h1
			b = (h2 - h0) / 2.0
			d = b * b - 4.0 * a * h1
			roots = 0
			x1 = 0.0
			x2 = 0.0

			if a != 0.0:
				xe = -b / (2.0 * a)
				ye = (a * xe + b) * xe + h1
				if d >= 0.0:
					dx = math.sqrt(d) / (abs(a) * 2.0)
					x1 = xe - dx
					x2 = xe + dx
					if abs(x1) <= 1.0:
						roots += 1
					if abs(x2) <= 1.0:
						roots += 1
					if x1 < -1.0:
						x1 = x2

				if roots == 1:
					if h0 < 0.0:
						if riseHour is None:
							riseHour = (i + 1) + x1
					else:
						if setHour is None:
							setHour = (i + 1) + x1
				elif roots == 2:
					if riseHour is None:
						riseHour = (i + 1) + (x2 if ye < 0.0 else x1)
					if setHour is None:
						setHour = (i + 1) + (x1 if ye < 0.0 else x2)

			if riseHour is not None and setHour is not None:
				break

		return riseHour, setHour

	@property
	def rise(self):
		riseHour, _ = self._calculateTimes()
		if riseHour is not None:
			midnight = DateTime(self.datetime.date, Time(0, 0, 0))
			return midnight + int(riseHour * 3600)
		return None

	@property
	def set(self):
		_, setHour = self._calculateTimes()
		if setHour is not None:
			midnight = DateTime(self.datetime.date, Time(0, 0, 0))
			return midnight + int(setHour * 3600)
		return None

	@property
	def isAlwaysUp(self):
		riseHour, setHour = self._calculateTimes()
		if riseHour is None and setHour is None:
			noon = DateTime(self.datetime.date, Time(12, 0, 0))
			return Moon(self.location, noon).altitude > 0.0
		return False

	@property
	def illumination(self):
		return MoonIllumination(self)

	@property
	def emoji(self):
		return self.illumination.phaseEmoji

	@staticmethod
	def eclipticLongitude(M):
		C = RAD * (1.9148 * math.sin(M) + 0.02 * math.sin(2.0 * M) + 0.0003 * math.sin(3.0 * M))
		P = RAD * 102.9372
		return M + C + P + math.pi

	@staticmethod
	def declination(L, b):
		return math.asin(
			math.sin(b) * math.cos(Astro.EARTH_OBLIQUITY) +
			math.cos(b) * math.sin(Astro.EARTH_OBLIQUITY) * math.sin(L)
		)

	@staticmethod
	def rightAscension(L, b):
		return math.atan2(
			math.sin(L) * math.cos(Astro.EARTH_OBLIQUITY) - math.tan(b) * math.sin(Astro.EARTH_OBLIQUITY),
			math.cos(L)
		)

	@staticmethod
	def siderealTime(dt, lw):
		days = dt.julianDay2K
		return RAD * (280.16 + 360.9856235 * days) - lw

	@staticmethod
	def astroRefraction(h):
		if h < 0.0:
			h = 0.0
		return 0.0002967 / math.tan(h + 0.00312536 / (h + 0.08901179))



class MoonPhaseTypeNames:
	NAMES_EN = [
		"",
		"New Moon",
		"Waxing Crescent",
		"First Quarter",
		"Waxing Gibbous",
		"Full Moon",
		"Waning Gibbous",
		"Last Quarter",
		"Waning Crescent",
	]
	NAMES_HE = [
		"",
		"מולד",
		"סהר עולה",
		"רבע ראשון",
		"גבנון עולה",
		"ירח מלא",
		"גבנון יורד",
		"רבע אחרון",
		"סהר יורד",
	]

	def __init__(self, lang, phaseType):
		self.lang = lang
		self.phaseType = phaseType

	def name(self):
		if self.lang == 'en':
			return self.NAMES_EN[self.phaseType]
		return self.NAMES_HE[self.phaseType]






if __name__ == "__main__":
	import time as _systime
	from Date import DateTime, Date, Time
	from Geo import Place

	now = _systime.localtime()
	today = Date(now.tm_year, now.tm_mon, now.tm_mday)
	midnight = DateTime(today, Time(0, 0, 0))
	location = Place.JERUSALEM.location()

	moon = Moon(location, midnight)

	rise = moon.rise
	set_time = moon.set

	print("Moon rise / set for", today)
	print("  Rise:", rise if rise else "None")
	print("  Set :", set_time if set_time else "None")
	print("  Always up?", moon.isAlwaysUp)