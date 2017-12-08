package com.virtualsushi.sitizens.model;

public class Organizer {
	private OrganizerInfo fr;
	private OrganizerInfo nl;
	
	public Organizer(OrganizerInfo fr, OrganizerInfo nl) {
		super();
		this.fr = fr;
		this.nl = nl;
	}

	public OrganizerInfo getFr() {
		return fr;
	}

	public void setFr(OrganizerInfo fr) {
		this.fr = fr;
	}

	public OrganizerInfo getNl() {
		return nl;
	}

	public void setNl(OrganizerInfo nl) {
		this.nl = nl;
	}
}
