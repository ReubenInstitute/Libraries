import math

J2000 = 2451545.0
EARTH_OBLIQUITY = (math.pi / 180.0) * 23.4397


class CelestialLocation:
	def __init__(self, rightAscension, declination):
		self.rightascension = rightAscension
		self.declination = declination
