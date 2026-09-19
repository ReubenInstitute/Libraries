package Geo;

import java.util.List;

public class Place {
	public final String id;
	public final double latitude;
	public final double longitude;
	public final double area;

	public Place(String id, double latitude, double longitude, double area) {
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.area = area;
	}

	public Location location() {
		return new Location(latitude, longitude);
	}

	public boolean isDiaspora() {
		double centerLat = (Geo.DIASPORA_NORTH.latitude + Geo.DIASPORA_SOUTH.latitude) / 2.0;
		double centerLon = (Geo.DIASPORA_EAST.longitude + Geo.DIASPORA_WEST.longitude) / 2.0;
		double semiMajor = (Geo.DIASPORA_NORTH.latitude - Geo.DIASPORA_SOUTH.latitude) / 2.0;
		double semiMinor = (Geo.DIASPORA_EAST.longitude - Geo.DIASPORA_WEST.longitude) / 2.0;
		double x = latitude - centerLat;
		double y = longitude - centerLon;
		return ((x * x) / (semiMajor * semiMajor) + (y * y) / (semiMinor * semiMinor)) > 1.0;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Place && this.id.equals(((Place) other).id);
	}

	public static Place fromCoordinates(double latitude, double longitude) {
		for (Place place : all()) {
			if (Math.abs(place.latitude - latitude) < 1e-6 && Math.abs(place.longitude - longitude) < 1e-6) {
				return place;
			}
		}
		return NONE;
	}

	public static Place closest(Location loc) {
		Place closest = null;
		double minDist = Double.POSITIVE_INFINITY;
		for (Place p : all()) {
			double d = loc.distance(p.location());
			if (d < minDist) {
				minDist = d;
				closest = p;
			}
		}
		return closest;
	}

	public static List<Place> all() {
		return List.of(
			OFAKIM, OR_HAGANUZ, EILAT, ELAD, AMIRIM, ARIEL, ASHDOD, ASHKELON, BEER_YAAKOV, BEER_SHEVA,
			BEIT_SHEAN, BEIT_SHEMESH, BNEI_BRAK, BAR_YOCHAI, BAT_YAM, GIVAT_SHMUEL, JISH, GAN_YAVNE, GANEI_TIKVA,
			GIVATAYIM, DIMONA, DALTON, HOD_HASHARON, HERZLIYA, HADERA, HOLON, HAIFA, HARISH, TIBERIAS, TIRAT_CARMEL,
			YAVNE, YOKNEAM_ILLIT, YEHUD_MONOSSON, JERUSALEM, KARMIEL, KFAR_SABA, KFAR_YONA, KIRYAT_ATA, KIRYAT_BIALIK,
			KIRYAT_GAT, KIRYAT_MALAKHI, KIRYAT_MOTZKIN, KIRYAT_ONO, KIRYAT_SHMONA, KIRYAT_YAM, KATZRIN, LOD,
			MAALOT_TARSHIHA, MODIIN, MODIIN_ILLIT, MIGDAL_HAEMEK, MAALE_ADUMIM, MERON, NAHARIYA, NESS_ZIONA, NETANYA,
			NETIVOT, NESHER, NOF_HAGALIL, SIFSUFA, AKKO, AFULA, ARAD, OR_AKIVA, OR_YEHUDA, FAROD, PETAH_TIKVA, SAFED,
			KADITA, RAHAT, ROSH_HAAYIN, RISHON_LEZION, REHOVOT, RAMLA, RAMAT_GAN, RAMAT_HASHARON, RAANANA, SDEROT,
			SHEFER, TEL_AVIV, UMAN, AMSTERDAM, BANGKOK, BUDAPEST, DUBAI, LONDON, NEW_YORK, PARIS, KYIV, ROME
		);
	}

	public static final Place NONE = new Place("NONE", 0.0, 0.0, 0.0);
	public static final Place OFAKIM = new Place("OFAKIM", 31.3167, 34.6167, 16.35);
	public static final Place OR_HAGANUZ = new Place("OR_HAGANUZ", 33.0000, 35.4400, 0.32);
	public static final Place EILAT = new Place("EILAT", 29.5577, 34.9519, 101.49);
	public static final Place ELAD = new Place("ELAD", 32.0523, 34.9510, 3.49);
	public static final Place AMIRIM = new Place("AMIRIM", 32.9370, 35.4500, 0.76);
	public static final Place ARIEL = new Place("ARIEL", 32.1037, 35.1774, 14.48);
	public static final Place ASHDOD = new Place("ASHDOD", 31.8044, 34.6553, 63.92);
	public static final Place ASHKELON = new Place("ASHKELON", 31.6688, 34.5743, 52.32);
	public static final Place BEER_YAAKOV = new Place("BEER_YAAKOV", 31.94, 34.84, 9.46);
	public static final Place BEER_SHEVA = new Place("BEER_SHEVA", 31.2530, 34.7915, 117.39);
	public static final Place BEIT_SHEAN = new Place("BEIT_SHEAN", 32.4970, 35.4980, 10.97);
	public static final Place BEIT_SHEMESH = new Place("BEIT_SHEMESH", 31.7473, 34.9876, 38.29);
	public static final Place BNEI_BRAK = new Place("BNEI_BRAK", 32.0834, 34.8331, 7.35);
	public static final Place BAR_YOCHAI = new Place("BAR_YOCHAI", 32.9980, 35.4460, 0.78);
	public static final Place BAT_YAM = new Place("BAT_YAM", 32.0170, 34.7500, 9.41);
	public static final Place GIVAT_SHMUEL = new Place("GIVAT_SHMUEL", 32.0770, 34.8490, 2.58);
	public static final Place JISH = new Place("JISH", 33.0240, 35.4420, 6.92);
	public static final Place GAN_YAVNE = new Place("GAN_YAVNE", 31.7870, 34.7060, 4.47);
	public static final Place GANEI_TIKVA = new Place("GANEI_TIKVA", 32.08, 34.92, 2.16);
	public static final Place GIVATAYIM = new Place("GIVATAYIM", 32.072, 34.812, 3.24);
	public static final Place DIMONA = new Place("DIMONA", 31.0694, 35.0330, 220.46);
	public static final Place DALTON = new Place("DALTON", 33.0150, 35.4850, 1.31);
	public static final Place HOD_HASHARON = new Place("HOD_HASHARON", 32.15, 34.89, 19.26);
	public static final Place HERZLIYA = new Place("HERZLIYA", 32.1627, 34.8446, 24.07);
	public static final Place HADERA = new Place("HADERA", 32.4360, 34.9160, 56.28);
	public static final Place HOLON = new Place("HOLON", 32.0114, 34.7794, 19.04);
	public static final Place HAIFA = new Place("HAIFA", 32.7940, 34.9896, 72.93);
	public static final Place HARISH = new Place("HARISH", 32.4620, 35.0480, 9.4);
	public static final Place TIBERIAS = new Place("TIBERIAS", 32.7922, 35.5312, 16.19);
	public static final Place TIRAT_CARMEL = new Place("TIRAT_CARMEL", 32.77, 34.97, 6.71);
	public static final Place YAVNE = new Place("YAVNE", 31.8770, 34.7440, 29.18);
	public static final Place YOKNEAM_ILLIT = new Place("YOKNEAM_ILLIT", 32.66, 35.11, 8.31);
	public static final Place YEHUD_MONOSSON = new Place("YEHUD_MONOSSON", 32.03, 34.89, 5.73);
	public static final Place JERUSALEM = new Place("JERUSALEM", 31.7683, 35.2137, 125.55);
	public static final Place KARMIEL = new Place("KARMIEL", 32.9190, 35.3020, 22.03);
	public static final Place KFAR_SABA = new Place("KFAR_SABA", 32.1750, 34.9060, 14.48);
	public static final Place KFAR_YONA = new Place("KFAR_YONA", 32.32, 34.93, 11.51);
	public static final Place KIRYAT_ATA = new Place("KIRYAT_ATA", 32.81, 35.11, 24.16);
	public static final Place KIRYAT_BIALIK = new Place("KIRYAT_BIALIK", 32.83, 35.08, 8.48);
	public static final Place KIRYAT_GAT = new Place("KIRYAT_GAT", 31.6100, 34.7710, 15.85);
	public static final Place KIRYAT_MALAKHI = new Place("KIRYAT_MALAKHI", 31.73, 34.75, 4.58);
	public static final Place KIRYAT_MOTZKIN = new Place("KIRYAT_MOTZKIN", 32.83, 35.08, 3.84);
	public static final Place KIRYAT_ONO = new Place("KIRYAT_ONO", 32.06, 34.85, 4.60);
	public static final Place KIRYAT_SHMONA = new Place("KIRYAT_SHMONA", 33.2070, 35.5710, 14.38);
	public static final Place KIRYAT_YAM = new Place("KIRYAT_YAM", 32.85, 35.07, 11.34);
	public static final Place KATZRIN = new Place("KATZRIN", 32.9920, 35.6900, 4.0);
	public static final Place LOD = new Place("LOD", 31.9530, 34.8930, 14.79);
	public static final Place MAALOT_TARSHIHA = new Place("MAALOT_TARSHIHA", 33.02, 35.29, 9.26);
	public static final Place MODIIN = new Place("MODIIN", 31.8815, 35.0106, 48.33);
	public static final Place MODIIN_ILLIT = new Place("MODIIN_ILLIT", 31.9290, 35.0430, 6.0);
	public static final Place MIGDAL_HAEMEK = new Place("MIGDAL_HAEMEK", 32.67, 35.24, 8.71);
	public static final Place MAALE_ADUMIM = new Place("MAALE_ADUMIM", 31.7770, 35.2980, 8.0);
	public static final Place MERON = new Place("MERON", 32.9837, 35.4508, 1.17);
	public static final Place NAHARIYA = new Place("NAHARIYA", 33.0119, 35.0980, 13.83);
	public static final Place NESS_ZIONA = new Place("NESS_ZIONA", 31.93, 34.80, 15.68);
	public static final Place NETANYA = new Place("NETANYA", 32.3323, 34.8571, 34.75);
	public static final Place NETIVOT = new Place("NETIVOT", 31.4210, 34.5860, 16.22);
	public static final Place NESHER = new Place("NESHER", 32.76, 35.04, 12.94);
	public static final Place NOF_HAGALIL = new Place("NOF_HAGALIL", 32.71, 35.33, 32.86);
	public static final Place SIFSUFA = new Place("SIFSUFA", 33.0120, 35.4300, 1.09);
	public static final Place AKKO = new Place("AKKO", 32.9270, 35.0830, 18.09);
	public static final Place AFULA = new Place("AFULA", 32.6083, 35.2880, 29.17);
	public static final Place ARAD = new Place("ARAD", 31.2583, 35.2140, 126.13);
	public static final Place OR_AKIVA = new Place("OR_AKIVA", 32.51, 34.92, 5.55);
	public static final Place OR_YEHUDA = new Place("OR_YEHUDA", 32.03, 34.86, 6.73);
	public static final Place FAROD = new Place("FAROD", 32.9320, 35.4300, 1.2);
	public static final Place PETAH_TIKVA = new Place("PETAH_TIKVA", 32.0849, 34.8877, 35.77);
	public static final Place SAFED = new Place("SAFED", 32.9646, 35.4969, 29.94);
	public static final Place KADITA = new Place("KADITA", 33.0020, 35.4700, 0.90);
	public static final Place RAHAT = new Place("RAHAT", 31.39, 34.75, 33.48);
	public static final Place ROSH_HAAYIN = new Place("ROSH_HAAYIN", 32.0950, 34.9520, 15.86);
	public static final Place RISHON_LEZION = new Place("RISHON_LEZION", 31.9730, 34.7925, 61.91);
	public static final Place REHOVOT = new Place("REHOVOT", 31.8948, 34.8096, 23.76);
	public static final Place RAMLA = new Place("RAMLA", 31.9290, 34.8660, 13.39);
	public static final Place RAMAT_GAN = new Place("RAMAT_GAN", 32.0834, 34.8106, 16.39);
	public static final Place RAMAT_HASHARON = new Place("RAMAT_HASHARON", 32.15, 34.84, 16.73);
	public static final Place RAANANA = new Place("RAANANA", 32.1833, 34.8700, 14.86);
	public static final Place SDEROT = new Place("SDEROT", 31.5250, 34.5930, 10.66);
	public static final Place SHEFER = new Place("SHEFER", 32.9370, 35.4400, 0.35);
	public static final Place TEL_AVIV = new Place("TEL_AVIV", 32.0853, 34.7818, 57.14);
	public static final Place UMAN = new Place("UMAN", 48.7519, 30.2193, 10.0);
	public static final Place AMSTERDAM = new Place("AMSTERDAM", 52.3676, 4.9041, 219.0);
	public static final Place BANGKOK = new Place("BANGKOK", 13.7563, 100.5018, 1569.0);
	public static final Place BUDAPEST = new Place("BUDAPEST", 47.4979, 19.0402, 525.0);
	public static final Place DUBAI = new Place("DUBAI", 25.2048, 55.2708, 4114.0);
	public static final Place LONDON = new Place("LONDON", 51.5074, -0.1278, 1572.0);
	public static final Place NEW_YORK = new Place("NEW_YORK", 40.7128, -74.0060, 1214.0);
	public static final Place PARIS = new Place("PARIS", 48.8566, 2.3522, 105.4);
	public static final Place KYIV = new Place("KYIV", 50.4501, 30.5234, 839.0);
	public static final Place ROME = new Place("ROME", 41.9028, 12.4964, 1285.0);
}
