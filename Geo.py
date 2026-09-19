import math


EARTH_WGS84_RADIUS = 6371000.0
EARTH_RADIUS = 6371000.0


class Location:
	def __init__(self, latitude, longitude):
		self.latitude = latitude
		self.longitude = longitude

	def distance(self, other):
		dLat = (other.latitude - self.latitude) * math.pi / 180.0
		dLon = (other.longitude - self.longitude) * math.pi / 180.0
		sinDLat = math.sin(dLat / 2)
		sinDLon = math.sin(dLon / 2)
		a = sinDLat * sinDLat + \
			math.cos(self.latitude * math.pi / 180.0) * math.cos(other.latitude * math.pi / 180.0) * \
			sinDLon * sinDLon
		c = 2 * math.atan2(math.sqrt(a), math.sqrt(1 - a))
		return EARTH_RADIUS * c

	def direction(self, other):
		lat1 = self.latitude * math.pi / 180.0
		lat2 = other.latitude * math.pi / 180.0
		dLon = (other.longitude - self.longitude) * math.pi / 180.0
		y = math.sin(dLon) * math.cos(lat2)
		x = math.cos(lat1) * math.sin(lat2) - \
			math.sin(lat1) * math.cos(lat2) * math.cos(dLon)
		bearing = math.atan2(y, x) * 180.0 / math.pi
		return (bearing + 360.0) % 360.0

	def __repr__(self):
		return f"Location({self.latitude}, {self.longitude})"

	def __eq__(self, other):
		return isinstance(other, Location) and \
			self.latitude == other.latitude and self.longitude == other.longitude


DIASPORA_NORTH = Location(33.317, 35.767)
DIASPORA_SOUTH = Location(29.483, 34.900)
DIASPORA_EAST = Location(32.933, 35.883)
DIASPORA_WEST = Location(31.220, 34.268)


class Place:
	def __init__(self, id, latitude, longitude, area):
		self.id = id
		self.latitude = latitude
		self.longitude = longitude
		self.area = area

	def location(self):
		return Location(self.latitude, self.longitude)

	def isDiaspora(self):
		centerLat = (DIASPORA_NORTH.latitude + DIASPORA_SOUTH.latitude) / 2.0
		centerLon = (DIASPORA_EAST.longitude + DIASPORA_WEST.longitude) / 2.0
		semiMajor = (DIASPORA_NORTH.latitude - DIASPORA_SOUTH.latitude) / 2.0
		semiMinor = (DIASPORA_EAST.longitude - DIASPORA_WEST.longitude) / 2.0
		x = self.latitude - centerLat
		y = self.longitude - centerLon
		return ((x * x) / (semiMajor * semiMajor) + (y * y) / (semiMinor * semiMinor)) > 1.0

	def __repr__(self):
		return f"Place({self.id})"

	def __eq__(self, other):
		return isinstance(other, Place) and self.id == other.id

	def __hash__(self):
		return hash(self.id)

	# Static methods operating on all known places
	@staticmethod
	def fromCoordinates(latitude, longitude):
		for place in Place.all():
			if abs(place.latitude - latitude) < 1e-6 and abs(place.longitude - longitude) < 1e-6:
				return place
		return Place.NONE

	@staticmethod
	def closest(loc):
		closest = None
		minDist = math.inf
		for p in Place.all():
			d = loc.distance(p.location())
			if d < minDist:
				minDist = d
				closest = p
		return closest

	# Ordered list (matches original enum ordinal order)
	@staticmethod
	def all():
		return [
			Place.OFAKIM,
			Place.OR_HAGANUZ,
			Place.EILAT,
			Place.ELAD,
			Place.AMIRIM,
			Place.ARIEL,
			Place.ASHDOD,
			Place.ASHKELON,
			Place.BEER_YAAKOV,
			Place.BEER_SHEVA,
			Place.BEIT_SHEAN,
			Place.BEIT_SHEMESH,
			Place.BNEI_BRAK,
			Place.BAR_YOCHAI,
			Place.BAT_YAM,
			Place.GIVAT_SHMUEL,
			Place.JISH,
			Place.GAN_YAVNE,
			Place.GANEI_TIKVA,
			Place.GIVATAYIM,
			Place.DIMONA,
			Place.DALTON,
			Place.HOD_HASHARON,
			Place.HERZLIYA,
			Place.HADERA,
			Place.HOLON,
			Place.HAIFA,
			Place.HARISH,
			Place.TIBERIAS,
			Place.TIRAT_CARMEL,
			Place.YAVNE,
			Place.YOKNEAM_ILLIT,
			Place.YEHUD_MONOSSON,
			Place.JERUSALEM,
			Place.KARMIEL,
			Place.KFAR_SABA,
			Place.KFAR_YONA,
			Place.KIRYAT_ATA,
			Place.KIRYAT_BIALIK,
			Place.KIRYAT_GAT,
			Place.KIRYAT_MALAKHI,
			Place.KIRYAT_MOTZKIN,
			Place.KIRYAT_ONO,
			Place.KIRYAT_SHMONA,
			Place.KIRYAT_YAM,
			Place.KATZRIN,
			Place.LOD,
			Place.MAALOT_TARSHIHA,
			Place.MODIIN,
			Place.MODIIN_ILLIT,
			Place.MIGDAL_HAEMEK,
			Place.MAALE_ADUMIM,
			Place.MERON,
			Place.NAHARIYA,
			Place.NESS_ZIONA,
			Place.NETANYA,
			Place.NETIVOT,
			Place.NESHER,
			Place.NOF_HAGALIL,
			Place.SIFSUFA,
			Place.AKKO,
			Place.AFULA,
			Place.ARAD,
			Place.OR_AKIVA,
			Place.OR_YEHUDA,
			Place.FAROD,
			Place.PETAH_TIKVA,
			Place.SAFED,
			Place.KADITA,
			Place.RAHAT,
			Place.ROSH_HAAYIN,
			Place.RISHON_LEZION,
			Place.REHOVOT,
			Place.RAMLA,
			Place.RAMAT_GAN,
			Place.RAMAT_HASHARON,
			Place.RAANANA,
			Place.SDEROT,
			Place.SHEFER,
			Place.TEL_AVIV,
			Place.UMAN,
			Place.AMSTERDAM,
			Place.BANGKOK,
			Place.BUDAPEST,
			Place.DUBAI,
			Place.LONDON,
			Place.NEW_YORK,
			Place.PARIS,
			Place.KYIV,
			Place.ROME,
		]


# Static instances
Place.NONE = Place('NONE', 0.0, 0.0, 0.0)
Place.OFAKIM = Place('OFAKIM', 31.3167, 34.6167, 16.35)
Place.OR_HAGANUZ = Place('OR_HAGANUZ', 33.0000, 35.4400, 0.32)
Place.EILAT = Place('EILAT', 29.5577, 34.9519, 101.49)
Place.ELAD = Place('ELAD', 32.0523, 34.9510, 3.49)
Place.AMIRIM = Place('AMIRIM', 32.9370, 35.4500, 0.76)
Place.ARIEL = Place('ARIEL', 32.1037, 35.1774, 14.48)
Place.ASHDOD = Place('ASHDOD', 31.8044, 34.6553, 63.92)
Place.ASHKELON = Place('ASHKELON', 31.6688, 34.5743, 52.32)
Place.BEER_YAAKOV = Place('BEER_YAAKOV', 31.94, 34.84, 9.46)
Place.BEER_SHEVA = Place('BEER_SHEVA', 31.2530, 34.7915, 117.39)
Place.BEIT_SHEAN = Place('BEIT_SHEAN', 32.4970, 35.4980, 10.97)
Place.BEIT_SHEMESH = Place('BEIT_SHEMESH', 31.7473, 34.9876, 38.29)
Place.BNEI_BRAK = Place('BNEI_BRAK', 32.0834, 34.8331, 7.35)
Place.BAR_YOCHAI = Place('BAR_YOCHAI', 32.9980, 35.4460, 0.78)
Place.BAT_YAM = Place('BAT_YAM', 32.0170, 34.7500, 9.41)
Place.GIVAT_SHMUEL = Place('GIVAT_SHMUEL', 32.0770, 34.8490, 2.58)
Place.JISH = Place('JISH', 33.0240, 35.4420, 6.92)
Place.GAN_YAVNE = Place('GAN_YAVNE', 31.7870, 34.7060, 4.47)
Place.GANEI_TIKVA = Place('GANEI_TIKVA', 32.08, 34.92, 2.16)
Place.GIVATAYIM = Place('GIVATAYIM', 32.072, 34.812, 3.24)
Place.DIMONA = Place('DIMONA', 31.0694, 35.0330, 220.46)
Place.DALTON = Place('DALTON', 33.0150, 35.4850, 1.31)
Place.HOD_HASHARON = Place('HOD_HASHARON', 32.15, 34.89, 19.26)
Place.HERZLIYA = Place('HERZLIYA', 32.1627, 34.8446, 24.07)
Place.HADERA = Place('HADERA', 32.4360, 34.9160, 56.28)
Place.HOLON = Place('HOLON', 32.0114, 34.7794, 19.04)
Place.HAIFA = Place('HAIFA', 32.7940, 34.9896, 72.93)
Place.HARISH = Place('HARISH', 32.4620, 35.0480, 9.4)
Place.TIBERIAS = Place('TIBERIAS', 32.7922, 35.5312, 16.19)
Place.TIRAT_CARMEL = Place('TIRAT_CARMEL', 32.77, 34.97, 6.71)
Place.YAVNE = Place('YAVNE', 31.8770, 34.7440, 29.18)
Place.YOKNEAM_ILLIT = Place('YOKNEAM_ILLIT', 32.66, 35.11, 8.31)
Place.YEHUD_MONOSSON = Place('YEHUD_MONOSSON', 32.03, 34.89, 5.73)
Place.JERUSALEM = Place('JERUSALEM', 31.7683, 35.2137, 125.55)
Place.KARMIEL = Place('KARMIEL', 32.9190, 35.3020, 22.03)
Place.KFAR_SABA = Place('KFAR_SABA', 32.1750, 34.9060, 14.48)
Place.KFAR_YONA = Place('KFAR_YONA', 32.32, 34.93, 11.51)
Place.KIRYAT_ATA = Place('KIRYAT_ATA', 32.81, 35.11, 24.16)
Place.KIRYAT_BIALIK = Place('KIRYAT_BIALIK', 32.83, 35.08, 8.48)
Place.KIRYAT_GAT = Place('KIRYAT_GAT', 31.6100, 34.7710, 15.85)
Place.KIRYAT_MALAKHI = Place('KIRYAT_MALAKHI', 31.73, 34.75, 4.58)
Place.KIRYAT_MOTZKIN = Place('KIRYAT_MOTZKIN', 32.83, 35.08, 3.84)
Place.KIRYAT_ONO = Place('KIRYAT_ONO', 32.06, 34.85, 4.60)
Place.KIRYAT_SHMONA = Place('KIRYAT_SHMONA', 33.2070, 35.5710, 14.38)
Place.KIRYAT_YAM = Place('KIRYAT_YAM', 32.85, 35.07, 11.34)
Place.KATZRIN = Place('KATZRIN', 32.9920, 35.6900, 4.0)
Place.LOD = Place('LOD', 31.9530, 34.8930, 14.79)
Place.MAALOT_TARSHIHA = Place('MAALOT_TARSHIHA', 33.02, 35.29, 9.26)
Place.MODIIN = Place('MODIIN', 31.8815, 35.0106, 48.33)
Place.MODIIN_ILLIT = Place('MODIIN_ILLIT', 31.9290, 35.0430, 6.0)
Place.MIGDAL_HAEMEK = Place('MIGDAL_HAEMEK', 32.67, 35.24, 8.71)
Place.MAALE_ADUMIM = Place('MAALE_ADUMIM', 31.7770, 35.2980, 8.0)
Place.MERON = Place('MERON', 32.9837, 35.4508, 1.17)
Place.NAHARIYA = Place('NAHARIYA', 33.0119, 35.0980, 13.83)
Place.NESS_ZIONA = Place('NESS_ZIONA', 31.93, 34.80, 15.68)
Place.NETANYA = Place('NETANYA', 32.3323, 34.8571, 34.75)
Place.NETIVOT = Place('NETIVOT', 31.4210, 34.5860, 16.22)
Place.NESHER = Place('NESHER', 32.76, 35.04, 12.94)
Place.NOF_HAGALIL = Place('NOF_HAGALIL', 32.71, 35.33, 32.86)
Place.SIFSUFA = Place('SIFSUFA', 33.0120, 35.4300, 1.09)
Place.AKKO = Place('AKKO', 32.9270, 35.0830, 18.09)
Place.AFULA = Place('AFULA', 32.6083, 35.2880, 29.17)
Place.ARAD = Place('ARAD', 31.2583, 35.2140, 126.13)
Place.OR_AKIVA = Place('OR_AKIVA', 32.51, 34.92, 5.55)
Place.OR_YEHUDA = Place('OR_YEHUDA', 32.03, 34.86, 6.73)
Place.FAROD = Place('FAROD', 32.9320, 35.4300, 1.2)
Place.PETAH_TIKVA = Place('PETAH_TIKVA', 32.0849, 34.8877, 35.77)
Place.SAFED = Place('SAFED', 32.9646, 35.4969, 29.94)
Place.KADITA = Place('KADITA', 33.0020, 35.4700, 0.90)
Place.RAHAT = Place('RAHAT', 31.39, 34.75, 33.48)
Place.ROSH_HAAYIN = Place('ROSH_HAAYIN', 32.0950, 34.9520, 15.86)
Place.RISHON_LEZION = Place('RISHON_LEZION', 31.9730, 34.7925, 61.91)
Place.REHOVOT = Place('REHOVOT', 31.8948, 34.8096, 23.76)
Place.RAMLA = Place('RAMLA', 31.9290, 34.8660, 13.39)
Place.RAMAT_GAN = Place('RAMAT_GAN', 32.0834, 34.8106, 16.39)
Place.RAMAT_HASHARON = Place('RAMAT_HASHARON', 32.15, 34.84, 16.73)
Place.RAANANA = Place('RAANANA', 32.1833, 34.8700, 14.86)
Place.SDEROT = Place('SDEROT', 31.5250, 34.5930, 10.66)
Place.SHEFER = Place('SHEFER', 32.9370, 35.4400, 0.35)
Place.TEL_AVIV = Place('TEL_AVIV', 32.0853, 34.7818, 57.14)
Place.UMAN = Place('UMAN', 48.7519, 30.2193, 10.0)
Place.AMSTERDAM = Place('AMSTERDAM', 52.3676, 4.9041, 219.0)
Place.BANGKOK = Place('BANGKOK', 13.7563, 100.5018, 1569.0)
Place.BUDAPEST = Place('BUDAPEST', 47.4979, 19.0402, 525.0)
Place.DUBAI = Place('DUBAI', 25.2048, 55.2708, 4114.0)
Place.LONDON = Place('LONDON', 51.5074, -0.1278, 1572.0)
Place.NEW_YORK = Place('NEW_YORK', 40.7128, -74.0060, 1214.0)
Place.PARIS = Place('PARIS', 48.8566, 2.3522, 105.4)
Place.KYIV = Place('KYIV', 50.4501, 30.5234, 839.0)
Place.ROME = Place('ROME', 41.9028, 12.4964, 1285.0)
