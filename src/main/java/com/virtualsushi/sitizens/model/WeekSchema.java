package com.virtualsushi.sitizens.model;

public class WeekSchema {
	private Timing monday;
	private Timing tuesday;
	private Timing wednesday;
	private Timing thursday;
	private Timing friday;
	private Timing saturday;
	private Timing sunday;
	public WeekSchema(Timing monday, Timing tuesday, Timing wednesday, Timing thursday, Timing friday, Timing saturday,
			Timing sunday) {
		super();
		this.monday = monday;
		this.tuesday = tuesday;
		this.wednesday = wednesday;
		this.thursday = thursday;
		this.friday = friday;
		this.saturday = saturday;
		this.sunday = sunday;
	}
	public Timing getMonday() {
		return monday;
	}
	public void setMonday(Timing monday) {
		this.monday = monday;
	}
	public Timing getTuesday() {
		return tuesday;
	}
	public void setTuesday(Timing tuesday) {
		this.tuesday = tuesday;
	}
	public Timing getWednesday() {
		return wednesday;
	}
	public void setWednesday(Timing wednesday) {
		this.wednesday = wednesday;
	}
	public Timing getThursday() {
		return thursday;
	}
	public void setThursday(Timing thursday) {
		this.thursday = thursday;
	}
	public Timing getFriday() {
		return friday;
	}
	public void setFriday(Timing friday) {
		this.friday = friday;
	}
	public Timing getSaturday() {
		return saturday;
	}
	public void setSaturday(Timing saturday) {
		this.saturday = saturday;
	}
	public Timing getSunday() {
		return sunday;
	}
	public void setSunday(Timing sunday) {
		this.sunday = sunday;
	}
	
}
