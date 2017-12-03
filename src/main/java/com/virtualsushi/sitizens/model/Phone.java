package com.virtualsushi.sitizens.model;

import com.virtualsushi.sitizens.model.Phone.Type;

public class Phone {
	private String number;
	private Type type;
	
	public Phone(String number, Type type) {
		super();
		this.number = number;
		this.type = type;
	}
	

	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public Type getType() {
		return type;
	}


	public void setType(Type type) {
		this.type = type;
	}


	public enum Type {

        CONTACT, BOOKING, FAX, MOBILE;
		
        public static Type fromString(String value) {
            return Type.valueOf(value.toUpperCase());
        }

    }
}
