package LocalHebrewDate;

import Date.DateTime;

public class Event {
	public final DateTime datetime;
	public final Zman zman;

	public LocalHebrewDateTime localHebrewDateTime;

	public Event(DateTime datetime) {
		this(datetime, null);
	}

	public Event(DateTime datetime, Zman zman) {
		this.datetime = datetime;
		this.zman = zman;
	}
}
