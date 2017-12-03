package com.virtualsushi.sitizens.model;

public class Media {
	private String url;
	private MediaTypes type = MediaTypes.ED;
	
	public Media(String url, MediaTypes type) {
		super();
		this.url = url;
		this.type = type;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public MediaTypes getType() {
		return type;
	}

	public void setType(MediaTypes type) {
		this.type = type;
	}

	public enum MediaTypes {
        SRC, ED;
    }
}
