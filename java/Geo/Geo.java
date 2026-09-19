package Geo;

public final class Geo {
	private Geo() {}

	public static final double EARTH_WGS84_RADIUS = 6371000.0;
	public static final double EARTH_RADIUS = EARTH_WGS84_RADIUS;

	public static final Location DIASPORA_NORTH = new Location(33.317, 35.767);
	public static final Location DIASPORA_SOUTH = new Location(29.483, 34.900);
	public static final Location DIASPORA_EAST  = new Location(32.933, 35.883);
	public static final Location DIASPORA_WEST  = new Location(31.220, 34.268);
}
