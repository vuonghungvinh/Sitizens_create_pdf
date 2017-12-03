package com.virtualsushi.sitizens.model;

import java.util.List;

public class EventDescription {
	private String name;
	private String performers;
	private String website;
	private List<String> emails;
	private List<Phone> phones;
	private String short_description;
	private String long_description;
	
	public EventDescription(String name, String performers, String website, List<String> emails, List<Phone> phones,
			String short_description, String long_description) {
		super();
		this.name = name;
		this.performers = performers;
		this.website = website;
		this.emails = emails;
		this.phones = phones;
		this.short_description = short_description;
		this.long_description = long_description;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPerformers() {
		return performers;
	}
	public void setPerformers(String performers) {
		this.performers = performers;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
	public String getShort_description() {
		return short_description;
	}
	public void setShort_description(String short_description) {
		this.short_description = short_description;
	}
	public String getLong_description() {
		return long_description;
	}
	public void setLong_description(String long_description) {
		this.long_description = long_description;
	}
	
}
