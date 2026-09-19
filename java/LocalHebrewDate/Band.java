package LocalHebrewDate;

public class Band {
	public final Event start;
	public final Event end;
	public final Integer id;
	public final boolean stripe;

	public Band(Event start, Event end) {
		this(start, end, null, false);
	}

	public Band(Event start, Event end, Integer id) {
		this(start, end, id, false);
	}

	public Band(Event start, Event end, Integer id, boolean stripe) {
		this.start = start;
		this.end = end;
		this.id = id;
		this.stripe = stripe;
	}
}
