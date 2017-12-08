package com.virtualsushi.sitizens.model;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public class Event {
	private Long id;
	private DateTime dateStart;
	private DateTime dateEnd;
	private EventDescription nl;
	private EventDescription fr;
	private List<Price> prices;
	private List<Media> medias;
	private WeekSchema weekschema;
	private Organizer organizer;
	public Event(Long id, DateTime dateStart, DateTime dateEnd, EventDescription nl, EventDescription fr,
			List<Price> prices, List<Media> medias, WeekSchema weekschema, Organizer organizer) {
		super();
		this.id = id;
		this.dateStart = dateStart;
		this.dateEnd = dateEnd;
		this.nl = nl;
		this.fr = fr;
		this.prices = prices;
		this.medias = medias;
		this.weekschema = weekschema;
		this.organizer = organizer;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public DateTime getDateStart() {
		return dateStart;
	}
	public void setDateStart(DateTime dateStart) {
		this.dateStart = dateStart;
	}
	public DateTime getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(DateTime dateEnd) {
		this.dateEnd = dateEnd;
	}
	public EventDescription getNl() {
		return nl;
	}
	public void setNl(EventDescription nl) {
		this.nl = nl;
	}
	public EventDescription getFr() {
		return fr;
	}
	public void setFr(EventDescription fr) {
		this.fr = fr;
	}
	public List<Price> getPrices() {
		return prices;
	}
	public void setPrices(List<Price> prices) {
		this.prices = prices;
	}
	public List<Media> getMedias() {
		return medias;
	}
	public void setMedias(List<Media> medias) {
		this.medias = medias;
	}
	public WeekSchema getWeekschema() {
		return weekschema;
	}
	public void setWeekschema(WeekSchema weekschema) {
		this.weekschema = weekschema;
	}
	public Organizer getOrganizer() {
		return organizer;
	}
	public void setOrganizer(Organizer organizer) {
		this.organizer = organizer;
	}
}
