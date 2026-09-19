import math

import Astro
from Astro import CelestialLocation
from Date import DateTime, Date, Time

RAD = math.pi / 180.0


class Sun:
	def __init__(self, location, datetime):
		self.location = location
		self.datetime = datetime

	def updateLocation(self, location):
		self.location = location

	def updateDateTime(self, datetime):
		self.datetime = datetime

	@property
	def _midnight(self):
		return DateTime(self.datetime.date, Time(0, 0, 0))

	@property
	def _lw(self):
		return RAD * -self.location.longitude

	@property
	def _n(self):
		return self._julianCycle(self._midnight.julianday, self._lw)

	@property
	def _ds(self):
		return self._approxTransit(0.0, self._lw, self._n)

	@property
	def _M(self):
		return self._solarMeanAnomaly(self._ds)

	@property
	def _L(self):
		return self._eclipticLongitude(self._M)

	@property
	def distance(self):
		return 149597870.7 + 2500000.0 * math.cos(
			RAD * (357.5291 + 0.98560028 * self.datetime.julianDay2K)
		)

	@property
	def celestialLocation(self):
		days = self.datetime.julianDay2K
		M = RAD * (357.5291 + 0.98560028 * days)
		C = RAD * (1.9148 * math.sin(M) + 0.02 * math.sin(2.0 * M) + 0.0003 * math.sin(3.0 * M))
		P = RAD * 102.9372
		L = M + C + P + math.pi
		dec = math.asin(
			math.sin(0.0) * math.cos(Astro.EARTH_OBLIQUITY) +
			math.cos(0.0) * math.sin(Astro.EARTH_OBLIQUITY) * math.sin(L)
		)
		ra = math.atan2(
			math.sin(L) * math.cos(Astro.EARTH_OBLIQUITY) - math.tan(0.0) * math.sin(Astro.EARTH_OBLIQUITY),
			math.cos(L)
		)
		return CelestialLocation(ra, dec)

	@property
	def noonJD(self):
		return self._solarTransitJ(self._ds, self._M, self._L)

	@property
	def setJD(self):
		phi = RAD * self.location.latitude
		dec = self._declination(self._L, 0.0)
		h0 = -0.833 * RAD
		w = self._hourAngle(h0, phi, dec)
		a = self._approxTransit(w, self._lw, self._n)
		return self._solarTransitJ(a, self._M, self._L)

	@property
	def riseJD(self):
		return self.noonJD - (self.setJD - self.noonJD)

	@property
	def noon(self):
		return DateTime.fromjulianday(self.noonJD)

	@property
	def set(self):
		return DateTime.fromjulianday(self.setJD)

	@property
	def rise(self):
		return DateTime.fromjulianday(self.riseJD)

	def dawnTime(self, angleDegrees):
		return self._timeAtAltitude(-angleDegrees, True)

	def duskTime(self, angleDegrees):
		return self._timeAtAltitude(-angleDegrees, False)

	def _timeAtAltitude(self, altitudeDeg, rising):
		phi = RAD * self.location.latitude
		dec = self._declination(self._L, 0.0)
		h0 = altitudeDeg * RAD
		w = self._hourAngle(h0, phi, dec)
		a = self._approxTransit(w, self._lw, self._n)
		Jset = self._solarTransitJ(a, self._M, self._L)
		Jnoon = self.noonJD
		return DateTime.fromjulianday(Jnoon - (Jset - Jnoon) if rising else Jset)

	def _julianCycle(self, d, lw):
		return round(d - Astro.J2000 - 0.0009 - lw / (2.0 * math.pi))

	def _approxTransit(self, Ht, lw, n):
		return 0.0009 + (Ht + lw) / (2.0 * math.pi) + n

	def _solarTransitJ(self, ds, M, L):
		return Astro.J2000 + ds + 0.0053 * math.sin(M) - 0.0069 * math.sin(2.0 * L)

	def _solarMeanAnomaly(self, daysSinceJ2000):
		return RAD * (357.5291 + 0.98560028 * daysSinceJ2000)

	def _eclipticLongitude(self, M):
		C = RAD * (1.9148 * math.sin(M) + 0.02 * math.sin(2.0 * M) + 0.0003 * math.sin(3.0 * M))
		P = RAD * 102.9372
		return M + C + P + math.pi

	def _declination(self, L, b):
		return math.asin(
			math.sin(b) * math.cos(Astro.EARTH_OBLIQUITY) +
			math.cos(b) * math.sin(Astro.EARTH_OBLIQUITY) * math.sin(L)
		)

	def _rightAscension(self, L, b):
		return math.atan2(
			math.sin(L) * math.cos(Astro.EARTH_OBLIQUITY) - math.tan(b) * math.sin(Astro.EARTH_OBLIQUITY),
			math.cos(L)
		)

	def _siderealTime(self, daysSinceJ2000, lw):
		return RAD * (280.16 + 360.9856235 * daysSinceJ2000) - lw

	def _astroRefraction(self, h):
		if h < 0.0:
			h = 0.0
		return 0.0002967 / math.tan(h + 0.00312536 / (h + 0.08901179))

	def _hourAngle(self, h, phi, dec):
		return math.acos(
			(math.sin(h) - math.sin(phi) * math.sin(dec)) /
			(math.cos(phi) * math.cos(dec))
		)
