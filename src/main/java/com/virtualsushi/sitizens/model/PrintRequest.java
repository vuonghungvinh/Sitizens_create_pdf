package com.virtualsushi.sitizens.model;

import com.virtualsushi.sitizens.model.PrintRequest.Language;
import com.virtualsushi.sitizens.model.PrintRequest.PrintType;
import com.virtualsushi.sitizens.model.PrintRequest.Template;

public class PrintRequest {
	private Long id;
	private PrintType printType;
	private Template template;
	private Language language;
	private Event event;
	private String font;
	
    public PrintRequest(Long id, PrintType printType, Template template, Language language, Event event, String font) {
		super();
		this.id = id;
		this.printType = printType;
		this.template = template;
		this.language = language;
		this.event = event;
		this.font = font;
	}
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PrintType getPrintType() {
		return printType;
	}

	public void setPrintType(PrintType printType) {
		this.printType = printType;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public enum PrintType {
        POSTER, FLYER
    }

    public enum Template {
        ONE, TWO, THREE
    }

    public enum Language {
        FR, NL
    }
}
