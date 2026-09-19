package Date;

public class Time {
	public final int hour;
	public final int minute;
	public final int second;
	public final int serial;

	public Time() {
		this(0, 0, 0);
	}

	public Time(int hour, int minute, int second) {
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.serial = hour * 3600 + minute * 60 + second;
	}

	public static Time fromSerial(int serial) {
		int hour = serial / 3600;
		int remainder = serial % 3600;
		int minute = remainder / 60;
		int second = remainder % 60;
		return new Time(hour, minute, second);
	}

	public Time plus(int x) {
		return fromSerial(Math.floorMod(serial + x, 86400));
	}

	@Override
	public boolean equals(Object other) {
		return other instanceof Time && this.serial == ((Time) other).serial;
	}
}
