package com.virtualsushi.sitizens.model;

import java.util.List;

public class OrganizerInfo {
	private String address_line_1;
	private String city;
	private String zip;
	private String website;
	private List<Phone> phones;
	public OrganizerInfo(String address_line_1, String city, String zip, String website,
			List<Phone> phones) {
		super();
		this.address_line_1 = address_line_1;
		this.city = city;
		this.zip = zip;
		this.website = website;
		this.phones = phones;
	}
	public String getAddress_line_1() {
		return address_line_1;
	}
	public void setAddress_line_1(String address_line_1) {
		this.address_line_1 = address_line_1;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public List<Phone> getPhones() {
		return phones;
	}
	public void setPhones(List<Phone> phones) {
		this.phones = phones;
	}
}
