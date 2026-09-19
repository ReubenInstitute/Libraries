package Geo;

public class Location {
	public final double latitude;
	public final double longitude;

	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double distance(Location other) {
		double dLat = Math.toRadians(other.latitude - this.latitude);
		double dLon = Math.toRadians(other.longitude - this.longitude);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
				Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(other.latitude)) *
				Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return Geo.EARTH_RADIUS * c;
	}

	public double direction(Location other) {
		double lat1 = Math.toRadians(this.latitude);
		double lat2 = Math.toRadians(other.latitude);
		double dLon = Math.toRadians(other.longitude - this.longitude);
		double y = Math.sin(dLon) * Math.cos(lat2);
		double x = Math.cos(lat1) * Math.sin(lat2) -
				Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
		double bearing = Math.toDegrees(Math.atan2(y, x));
		return (bearing + 360.0) % 360.0;
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Location
				&& this.latitude == ((Location) other).latitude
				&& this.longitude == ((Location) other).longitude;
	}
}
