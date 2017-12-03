package com.virtualsushi.sitizens.model;

public class Price {
	private String nl;
	private String fr;
	private Double value;
	public Price(String nl, String fr, Double value) {
		super();
		this.nl = nl;
		this.fr = fr;
		this.value = value;
	}
	public String getNl() {
		return nl;
	}
	public void setNl(String nl) {
		this.nl = nl;
	}
	public String getFr() {
		return fr;
	}
	public void setFr(String fr) {
		this.fr = fr;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
}
