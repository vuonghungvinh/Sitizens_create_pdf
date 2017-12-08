package com.virtualsushi.sitizens.backend;

import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.ColorConstants;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.PdfCanvasConstants;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.Leading;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.CanvasRenderer;
import com.itextpdf.layout.renderer.IRenderer;
import com.itextpdf.signatures.PdfSignatureAppearance.RenderingMode;
import com.virtualsushi.sitizens.model.Event;
import com.virtualsushi.sitizens.model.EventDescription;
import com.virtualsushi.sitizens.model.Media;
import com.virtualsushi.sitizens.model.Organizer;
import com.virtualsushi.sitizens.model.OrganizerInfo;
import com.virtualsushi.sitizens.model.Phone;
import com.virtualsushi.sitizens.model.Price;
import com.virtualsushi.sitizens.model.PrintRequest;
import com.virtualsushi.sitizens.model.PrintRequest.Language;
import com.virtualsushi.sitizens.model.Timing;
import com.virtualsushi.sitizens.model.WeekSchema;

import javafx.scene.paint.ColorBuilder;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.inject.Inject;
import javax.swing.GroupLayout.Alignment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Created by jefw on 7/24/17.
 */
public class PdfEngine {

    /*public String MONTSERRAT = "Montserrat";
    public static final String FONT_BLACK = "./src/main/webapp/fonts/%s-Black.ttf";
    public static final String FONT_BLACK_ITALIC = "./src/main/webapp/fonts/%s-BlackItalic.ttf";
    public static final String FONT_LIGHT = "./src/main/webapp/fonts/%s-Light.ttf";
    public static final String FONT_LIGHT_ITALIC = "./src/main/webapp/fonts/%s-LightItalic.ttf";
    public static final String FONT_MEDIUM = "./src/main/webapp/fonts/%s-Medium.ttf";
    public static final String FONT_MEDIUM_ITALIC = "./src/main/webapp/fonts/%s-MediumItalic.ttf";
    public static final String FONT_BOLD = "/fonts/%s-Bold.ttf";
    public static final String FONT_BOLD_ITALIC = "./src/main/webapp/fonts/%s-BoldItalic.ttf";*/

    private static final Logger log = Logger.getLogger(PdfEngine.class.getName());

    public ByteArrayOutputStream createPdf(PrintRequest printRequest) throws IOException {
        ByteArrayOutputStream pdfContent = null;
        Event event = printRequest.getEvent();
        PrintRequest.Template template = printRequest.getTemplate();
        if (printRequest.getPrintType().equals(PrintRequest.PrintType.FLYER)) {
            switch (template) {
                case ONE:
                    pdfContent = getFlyerOne(event, printRequest.getLanguage(), PageSize.A5, printRequest.getFont());
                    break;
                case TWO:
                    pdfContent = getFlyerTwo(event, printRequest.getLanguage(), PageSize.A5, printRequest.getFont());
                    break;
                case THREE:
                    pdfContent = getFlyerThree(event, printRequest.getLanguage(), PageSize.A5, printRequest.getFont());
                    break;
            }
        } else {
            switch (template) {
                case ONE:
                    pdfContent = getPosterOne(event, printRequest.getLanguage(), PageSize.A2, printRequest.getFont());
                    break;
                case TWO:
                    pdfContent = getPosterTwo(event, printRequest.getLanguage(), PageSize.A2, printRequest.getFont());
                    break;
                case THREE:
                    pdfContent = getPosterThree(event, printRequest.getLanguage(), PageSize.A2, printRequest.getFont());
                    break;
            }
        }
        return pdfContent;
    }

    private ByteArrayOutputStream getFlyerOne(Event event, Language language, PageSize ps, String font) throws IOException {
        return getFlyerTwo(event, language, ps, font);
    }
    
    private String createPathFile(String name, String type) {
    	DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
    	String basePath = "src/main/resources/pdf_files";
    	return String.format("%s/%s/%s_%s.pdf", basePath, type, dateFormat.format(date), name.replaceAll(" ", "_"));
    }

    private ByteArrayOutputStream getFlyerTwo(Event event, Language language, PageSize ps, String font) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        EventDescription eventDescription = (language == Language.FR) ? event.getFr() : event.getNl();
//        PdfWriter writer = new PdfWriter(baos);
        System.out.println("creating...");
        String filepath = createPathFile(eventDescription.getName(), "Flyer");
        File f = new File(filepath);
        f.getParentFile().mkdirs();
        PdfWriter writer = new PdfWriter(filepath);
        PdfDocument pdf = new PdfDocument(writer);
        PdfPage page = pdf.addNewPage(ps);
        PdfCanvas canvas = new PdfCanvas(page);
        addBackgroundImage(canvas, ps.getWidth(), ps.getHeight(), "halftone-background.jpg");
        if (eventDescription != null)
            drawTitle(pdf, eventDescription.getName(), canvas, ps.getWidth(), ps.getHeight(), font);
        addImage(event.getMedias(), canvas, ps.getWidth(), ps.getHeight());
//        drawAxes(canvas, ps);
        Locale locale = new Locale(language.name().toLowerCase());
        String dayOfWeek = event.getDateStart().dayOfWeek().getAsText();
        String dayOfMonth = event.getDateStart().dayOfMonth().getAsText();
        String month = event.getDateStart().monthOfYear().getAsText();
        String _abstract = StringUtils.isNotBlank(eventDescription.getShort_description()) ? eventDescription.getShort_description() : shorten(eventDescription.getLong_description(), 120);
        drawInfo(pdf, canvas, ps, dayOfWeek, dayOfMonth, month, _abstract, font);
        Timing timing = null;
        if (dayOfWeek.toLowerCase().equals("monday")) {
        	timing = event.getWeekschema().getMonday();
        }
        if (dayOfWeek.toLowerCase().equals("tuesday")) {
        	timing = event.getWeekschema().getTuesday();
        }
        if (dayOfWeek.toLowerCase().equals("wednesday")) {
        	timing = event.getWeekschema().getWednesday();
        	System.out.println("herere");
        	System.out.println(timing);
        }
        if (dayOfWeek.toLowerCase().equals("thursday")) {
        	timing = event.getWeekschema().getThursday();
        }
        if (dayOfWeek.toLowerCase().equals("friday")) {
        	timing = event.getWeekschema().getFriday();
        }
        if (dayOfWeek.toLowerCase().equals("saturday")) {
        	timing = event.getWeekschema().getSaturday();
        }
        if (dayOfWeek.toLowerCase().equals("sunday")) {
        	timing = event.getWeekschema().getSunday();
        }
        String time = "none";
        if (timing != null) {
        	time = timing.getStart().toString();
        }
        String price = "none";
        String type = "none";
        List<Price> prices = event.getPrices();
        if (prices != null && prices.size() > 0) {
        	price = prices.get(0).getValue().toString();
        	if (language == Language.FR) {
        		type = prices.get(0).getFr();
        	} else {
        		type = prices.get(0).getNl();
        	}
        }
        String address = "";
        String city = "";
        String website = "";
        String zip = "";
        String phone = "";
        Organizer organizer = event.getOrganizer();
        OrganizerInfo ori = null;
        if (organizer != null) {
        	if (language == Language.FR) {
        		ori = organizer.getFr();
            } else {
            	ori = organizer.getNl();
            }
        }
        if (ori != null) {
        	address = ori.getAddress_line_1();
        	city = ori.getCity();
        	website = ori.getWebsite();
        	zip = ori.getZip();
        	if (ori.getPhones().size() > 0) {
        		phone = ori.getPhones().get(0).getNumber();
        	}
        }
        drawContact(pdf, canvas, ps, address, city, zip, website, phone, font);
        drawTime(pdf, canvas, time, price, type, font, ps);
        
        pdf.close();
        return baos;
    }

    private ByteArrayOutputStream getFlyerThree(Event event, Language language, PageSize ps, String font) throws IOException {
        return getFlyerTwo(event, language, ps, font);
    }

    private ByteArrayOutputStream getPosterOne(Event event, Language language, PageSize ps, String font) throws IOException {
        return getFlyerTwo(event, language, ps, font);
    }

    private ByteArrayOutputStream getPosterTwo(Event event, Language language, PageSize ps, String font) throws IOException {
        return getFlyerTwo(event, language, ps, font);
    }

    private ByteArrayOutputStream getPosterThree(Event event, Language language, PageSize ps, String font) throws IOException {
        return getFlyerTwo(event, language, ps, font);
    }
    
    private String fontPath(String font) {
    	return String.format("%s/%s", "src/main/resources/fonts", font);
    }
    
    private void drawInfo(PdfDocument pdf, PdfCanvas canvas, PageSize ps, String dayofweek, String dayofmonth,
    		String month, String short_desscription, String font_s) throws IOException {
    	float width = ps.getWidth();
    	float height = ps.getHeight();
    	font_s = fontPath(font_s);
    	FontProgram fontProgram = FontProgramFactory.createFont(font_s);
    	PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);
    	Rectangle rect = new Rectangle(0, height * 0.23f, (float)width, (float)(height * 0.15));
    	float h = height * 0.15f;
    	float font_dow = (float) (h / 0.75) / dayofweek.length();
    	canvas.rectangle(rect);
    	canvas.stroke();
    	Canvas cv = new Canvas(canvas, pdf, rect);
    	float mediumFontSize = 26;
        float smallFontSize = 18;
        canvas.setFillColor(Color.BLACK).rectangle(rect).fillStroke();
        float w_second_cell = width * 0.2f;
        float font_dayofmonth = 10f;
        while (font.getWidth(dayofmonth, font_dayofmonth + 5f) * dayofmonth.length() < w_second_cell) {
        	font_dayofmonth += 1f;
        }
        Text dayofweek_t = new Text(dayofweek).setFont(font).setFontColor(Color.WHITE).setFontSize(font_dow);
        Paragraph p = new Paragraph().add(dayofweek_t).setRotationAngle(Math.PI/2)
        		.setTextAlignment(TextAlignment.LEFT).setVerticalAlignment(VerticalAlignment.MIDDLE);
        Table table = new Table(new float[] {font_dow, w_second_cell, width - font_dow - w_second_cell});
        table.setBorder(Border.NO_BORDER);
        table.setWidthPercent(100);
        table.setHeight(height * 0.15f);
        Cell cell = new Cell();
        cell.setHeight(height * 0.15f);
        cell.setBorder(Border.NO_BORDER);
        cell.setVerticalAlignment(VerticalAlignment.BOTTOM);
        cell.setPaddingLeft(12f);
        cell.setPaddingBottom(font_dayofmonth*1.5f*0.1f);
        cell.add(p);
        table.addCell(cell);
        //dayofmonth and moth
        
        float font_month = (float)(w_second_cell / 0.65) / month.length();
        Table tb2 = new Table(1);
        tb2.setBorder(Border.NO_BORDER);
        tb2.setWidthPercent(100);
        tb2.setHeight(height * 0.15f);
        tb2.setMargin(0);
        tb2.setPadding(0);
        Cell c = new Cell();
        c.setHeight(w_second_cell-font_month * 1.5f);
        Color dom_color = new DeviceRgb(204, 92, 90);
        Text t = new Text(dayofmonth).setFont(font).setFontColor(dom_color).setFontSize(font_dayofmonth);
        t.setWordSpacing(0f);
        Paragraph pg = new Paragraph().setFontSize(font_dayofmonth).setFont(font)
        		.setTextAlignment(TextAlignment.CENTER).setFontColor(dom_color);
        pg.setBorder(Border.NO_BORDER);
        pg.setPadding(0);
        pg.setMargin(0);
        pg.setFixedLeading(0f);
        pg.setMultipliedLeading(0f);
        pg.add(t);
        c.setMargin(0);
        c.setPadding(0);
        c.setVerticalAlignment(VerticalAlignment.BOTTOM);
        c.add(pg);
        tb2.addCell(c);
        
        c = new Cell();
        c.setBorder(Border.NO_BORDER);
        c.setHeight(font_month*1.5f);
//        t = new Text(month).setFont(font).setFontColor(dom_color).setFontSize(font_month - 2f);
        pg = new Paragraph().add(month).setTextAlignment(TextAlignment.CENTER).setFontColor(dom_color).setFont(font)
        		.setFontSize(font_month);
        c.setVerticalAlignment(VerticalAlignment.TOP);
        c.setFontSize(font_dayofmonth).add(pg);
        tb2.addCell(c);
        tb2.setPadding(0);
        
        cell = new Cell();
        cell.setBorder(Border.NO_BORDER);
        cell.setVerticalAlignment(VerticalAlignment.BOTTOM);
        cell.setPadding(0);
        cell.add(tb2);
        table.addCell(cell);
        float w = width - font_dow - w_second_cell;
        float font_des = 8f;
        if (ps == PageSize.A5) {
        	font_des = ps.getWidth() / short_desscription.length() * 8f / 0.75f;
        	if (font_des > 16f) {
        		font_des = 16f;
        	}
        } else {
        	font_des = ps.getWidth() / short_desscription.length() * 23f / 2.38f;
        	if (font_des > 40f) {
        		font_des = 40f;
        	}
        }
        t = new Text(short_desscription).setFont(font).setFontColor(Color.WHITE).setFontSize(font_des);
        pg = new Paragraph().setFontSize(font_des).setFont(font)
        		.setTextAlignment(TextAlignment.CENTER).setFontColor(Color.WHITE).add(t);
        pg.setKeepTogether(true);
        pg.setHeightPercent(100);
        cell = new Cell();
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setPadding(0);
        cell.add(pg);
        table.addCell(cell);
        cv.add(table);
    }
    
    private void drawTime(PdfDocument pdf, PdfCanvas canvas, String time, String value, String type, String font_s, PageSize ps) throws IOException {
    	font_s = fontPath(font_s);
    	FontProgram fp = FontProgramFactory.createFont(font_s);
    	PdfFont font = PdfFontFactory.createFont(fp, PdfEncodings.WINANSI, true);
    	String text = "start at " + time + " ticket " + value + "$ - " + type;
    	text = text.toUpperCase();
    	float fontsize = 10f;
    	while ((font.getWidth(text, fontsize) + ps.getWidth() * 0.11f) < ps.getWidth()) {
    		fontsize += 1f;
    	}
    	Rectangle rect = new Rectangle(0, ps.getHeight() * 0.1f, (float)ps.getWidth(), fontsize * 1.5f);
    	canvas.rectangle(rect).restoreState();
    	Canvas cv = new Canvas(canvas, pdf, rect);
    	Paragraph pg = new Paragraph(text).setFixedLeading(fontsize).setTextAlignment(TextAlignment.CENTER)
    			.setFont(font).setFontSize(fontsize * 0.9f).setFontColor(Color.WHITE);
    	cv.add(pg).close();
    }
    
    private void drawContact(PdfDocument pdf, PdfCanvas canvas, PageSize ps, 
    		String address, String city, String zip, String website, String phone, String font_s) throws IOException {
    	String text = address;
    	if (text.length() > 0) {
    		text = text + ", " + city;
    	} else {
    		text = city;
    	}
    	if (text.length() > 0) {
    		text = text + ", " + zip;
    	} else {
    		text = zip;
    	}
    	if (text.length() > 0) {
    		text = text + " - " + website;
    	} else {
    		text = website;
    	}
    	if (text.length() > 0) {
    		text = text + " - " + phone;
    	} else {
    		text = phone;
    	}
    	font_s = fontPath(font_s);
    	FontProgram fp = FontProgramFactory.createFont(font_s);
    	PdfFont font = PdfFontFactory.createFont(fp, PdfEncodings.WINANSI, true);
    	float fontsize = 6f;
    	while ((font.getWidth(text, fontsize) + ps.getWidth() * 0.11f) < ps.getWidth()) {
    		fontsize += 1f;
    	}
    	Rectangle rect = new Rectangle(0, 0, (float)ps.getWidth(), fontsize * 1.5f);
    	canvas.rectangle(rect).fillStroke();
    	Canvas cv = new Canvas(canvas, pdf, rect);
    	Paragraph pg = new Paragraph(text).setTextAlignment(TextAlignment.CENTER).setFontColor(Color.WHITE)
    			.setFont(font).setFontSize(fontsize);
    	cv.add(pg).close();
    }
    
    @SuppressWarnings("resource")
	private void drawTitle(PdfDocument pdf, String title, PdfCanvas canvas, float width, float height, String font_s) throws IOException {
        font_s = fontPath(font_s);
        title = title.toUpperCase();
        FontProgram fontProgram = FontProgramFactory.createFont(font_s);
    	PdfFont font = PdfFontFactory.createFont(fontProgram, PdfEncodings.WINANSI, true);
//    	float fontSize = (float) (width / 0.65) / title.length();
    	float fontSize = 10f;
        while ((font.getWidth(title, fontSize) + width * 0.1f) < width) {
        	fontSize += 1f;
        }
        float topStart = height/100;
        
        float paragraphWidth = width;
        float leftStart = 0;
        Color greyColor = new DeviceRgb(37, 37, 37);
        Rectangle rect = new Rectangle(3, height - fontSize, paragraphWidth-10, fontSize);

        Paragraph p = new Paragraph(title).setFixedLeading(fontSize).setTextAlignment(TextAlignment.CENTER)
        		.setFontColor(Color.WHITE);
        canvas.saveState().moveTo(0, 0).stroke();
        new Canvas(canvas, pdf, rect)
	        .setFont(font)
	        .setFontSize(fontSize)
	        .add(p).close();
//        new Canvas(canvas, pdf, rect)
//            .setFont(font)
//            .setFontSize(fontSize)
//            .add(p.setFixedPosition(0, height * 0.95f, width)).close();
    }

    private String getUrlImageLib(String name) {
    	return String.format("src/main/resources/pic_lib/%s", name);
    }
    
    private void addBackgroundImage(PdfCanvas canvas, float w, float h, String src) throws MalformedURLException {
    	ImageData imgbg = ImageDataFactory.create(getUrlImageLib(src));
    	float pageRatio = h / w;
        float imageRatio = imgbg.getHeight() / imgbg.getWidth();
        float scaleFactor = 1.20f;
        if (pageRatio <= imageRatio)
            scaleFactor = (float) 1.20 * w / imgbg.getWidth();
        else
            scaleFactor = (float) 1.20 * h / imgbg.getHeight();
        float x = (imgbg.getWidth() * scaleFactor) / 2;
	    float y = (imgbg.getHeight() * scaleFactor) / 2;
	    float totalWidth = imgbg.getWidth() * scaleFactor;
	    canvas.addImage(imgbg, 0, 0, totalWidth, true);
    }
    
    private void addImage(List<Media> media, PdfCanvas canvas, float width, float height) throws MalformedURLException {
        URL url = null;
        if (!media.isEmpty()) {
            url = new URL(media.get(0).getUrl());
        }
        if (url == null)
            url = new URL("https", "s3-eu-west-1.amazonaws.com", "/brussels-images/content/gallery/visit/minisites/comicsfestival/gaston-close-up2_sq_640.jpg");
        ImageData imageData = ImageDataFactory.create(url);
        float pageRatio = height / width;
        float imageRatio = imageData.getHeight() / imageData.getWidth();
        //Calculate the scale factor for the background image that is 20% bigger then the page
        float scaleFactor = 1.20f;
        if (pageRatio <= imageRatio)
            scaleFactor = (float) 1.20 * width / imageData.getWidth();
        else
            scaleFactor = (float) 1.20 * height / imageData.getHeight();

        canvas.saveState();
        float smallScaleFactor = (height / 2) / imageData.getHeight();

        if (imageRatio < 0.70)
            smallScaleFactor = (float) (width - 40) / imageData.getWidth();
        else
            smallScaleFactor = (float) (height / 2) / imageData.getHeight();
        float imageWidth = imageData.getWidth() * smallScaleFactor;
        float imageHeight = imageData.getHeight() * smallScaleFactor;
        canvas.addImage(imageData, (width-imageWidth)/2, height * 0.43f, imageWidth, true);
    }

    public String createTestPdf() throws IOException {
        //String html = getPosterContent(member);
        byte[] pdf = convertToPdf();
        String based = BaseEncoding.base64().encode(pdf);
        return based;
    }

    private byte[] convertToPdf() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        Text redText = new Text("This text is red. ")
            .setFontColor(Color.RED)
            .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA));

        Paragraph p1 = new Paragraph(redText).setMargin(0);
        document.add(p1);
        document.close();
        return baos.toByteArray();
    }

    private String shorten(String longText, int length) {
        if(StringUtils.isBlank(longText)) return "";
        if(longText.length()<=length) return longText;
        if(longText.indexOf(" ")<0) return longText.substring(0, length);
        return longText.substring(0, longText.indexOf(" ", length)) + " ...";
    }
    
    private EventDescription getEventDescription(Object obj) {
    	JSONObject object = (JSONObject)obj;
    	List<String> emails = new ArrayList<String>();
    	List<Phone> phones = new ArrayList<Phone>();
    	if (object.get("emails") != null) {
    		JSONArray email_arr = (JSONArray)object.get("emails");
    		for (Object e_obj : email_arr) {
    			emails.add(e_obj.toString());
    		}
    	}
    	if (object.get("phones") != null) {
    		JSONArray phone_arr = (JSONArray)object.get("phones");
    		for (Object phone_obj : phone_arr) {
    			JSONObject p_obj = (JSONObject)phone_obj;
    			phones.add(new Phone(p_obj.get("number").toString(), 
    					Phone.Type.fromString(p_obj.get("type").toString())));
    		}
    	}
    	String name = object.get("name") != null ? object.get("name").toString() : "";
    	String performers = object.get("performers") != null ? object.get("performers").toString() : "";
    	String website = object.get("website") != null ? object.get("website").toString() : "";
    	String short_description = object.get("short_description") != null ? object.get("short_description").toString() : "";
    	String long_description = object.get("long_description") != null ? object.get("long_description").toString() : "";
    	return new EventDescription(name, performers, website, emails, phones, short_description, 
    			long_description);
    }
    
    private List<Event> convertJsonToArray() throws IOException, ParseException {
    	List<Event> events = new ArrayList<Event>();
    	FileReader filereader = new FileReader("event-web.json");
    	JSONParser parser = new JSONParser();
    	JSONObject myjson = (JSONObject) parser.parse(filereader);
    	JSONArray jsonarray = (JSONArray) myjson.get("events");
    	EventDescription nl = null;
    	EventDescription fr = null;
    	WeekSchema weekschema = null;
    	for (Object object : jsonarray) {
    		JSONObject obj = (JSONObject)object;
    		List<Price> prices = new ArrayList<Price>();
    		List<Media> medias = new ArrayList<Media>();
        	if (obj.get("prices") != null) {
        		JSONArray price_arr = (JSONArray)obj.get("prices");
        		for (Object p_arr : price_arr) {
        			JSONObject p_obj = (JSONObject)p_arr;
        			prices.add(new Price(p_obj.get("nl").toString(), p_obj.get("nl").toString(), 
        					(Double)p_obj.get("value")));
        		}
        	}
        	if (obj.get("media") != null) {
        		JSONArray media_arr = (JSONArray)obj.get("media");
        		for (Object m_arr : media_arr) {
        			JSONObject m_obj = (JSONObject)m_arr;
        			if (m_obj.get("link").toString().equals("SRC")) {
        				medias.add(new Media(m_obj.get("link").toString(), Media.MediaTypes.SRC));
        			} else {
        				medias.add(new Media(m_obj.get("link").toString(), Media.MediaTypes.ED));
        			}
        		}
        	}
        	if (obj.get("fr") != null) {
        		fr =getEventDescription(obj.get("fr"));
        	}
        	if (obj.get("nl") != null) {
        		nl =getEventDescription(obj.get("nl"));
        	}
        	if (obj.get("week_schema") != null) {
        		Timing monday = null;
        		Timing tuesday = null;
        		Timing wednesday = null;
        		Timing thursday = null;
        		Timing friday = null;
        		Timing saturday = null;
        		Timing sunday = null;
        		JSONObject weeks = (JSONObject)obj.get("week_schema");
        		if (weeks.get("monday") != null) {
        			JSONArray mon_arr = (JSONArray)weeks.get("monday");
        			JSONObject mon = (JSONObject)mon_arr.get(0);
        			monday = new Timing(mon.get("start").toString(), mon.get("end").toString());
        		}
        		if (weeks.get("tuesday") != null) {
        			JSONArray tue_arr = (JSONArray)weeks.get("tuesday");
        			JSONObject tue = (JSONObject)tue_arr.get(0);
        			tuesday = new Timing(tue.get("start").toString(), tue.get("end").toString());
        		}
        		if (weeks.get("wednesday") != null) {
        			JSONArray wed_arr = (JSONArray)weeks.get("wednesday");
        			JSONObject wed = (JSONObject)wed_arr.get(0);
        			wednesday = new Timing(wed.get("start").toString(), wed.get("end").toString());
        		}
        		if (weeks.get("thursday") != null) {
        			JSONArray thu_arr = (JSONArray)weeks.get("thursday");
        			JSONObject thu = (JSONObject)thu_arr.get(0);
        			thursday = new Timing(thu.get("start").toString(), thu.get("end").toString());
        		}
        		if (weeks.get("friday") != null) {
        			JSONArray fri_arr = (JSONArray)weeks.get("friday");
        			JSONObject fri = (JSONObject)fri_arr.get(0);
        			friday = new Timing(fri.get("start").toString(), fri.get("end").toString());
        		}
        		if (weeks.get("saturday") != null) {
        			JSONArray sat_arr = (JSONArray)weeks.get("saturday");
        			JSONObject sat = (JSONObject)sat_arr.get(0);
        			saturday = new Timing(sat.get("start").toString(), sat.get("end").toString());
        		}
        		if (weeks.get("sunday") != null) {
        			JSONArray sun_arr = (JSONArray)weeks.get("sunday");
        			JSONObject sun = (JSONObject)sun_arr.get(0);
        			sunday = new Timing(sun.get("start").toString(), sun.get("end").toString());
        		}
        		weekschema = new WeekSchema(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
        	}
        	List<Phone> phones = new ArrayList<Phone>();
        	phones.add(new Phone("0974068920", Phone.Type.BOOKING));
        	OrganizerInfo oi = new OrganizerInfo("317 Au Co", "Da Nang", "12345", "google.com", phones);
        	Organizer organizer = new Organizer(oi, oi);
         	events.add(new Event(Long.parseLong(obj.get("id").toString()), DateTime.parse(obj.get("date_start").toString()), 
         			DateTime.parse(obj.get("date_end").toString()), nl, fr, prices, medias, weekschema, organizer));
        }
    	return events;
    }
    
    public static void main(String args[]) throws IOException {
    	PdfEngine pe = new PdfEngine();
    	List<Event> events = null;
    	try {
			events = pe.convertJsonToArray();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	PrintRequest pq = new PrintRequest((long) 1, PrintRequest.PrintType.FLYER, PrintRequest.Template.TWO, 
    			PrintRequest.Language.FR, events.get(2), "Vegan_Abattoir.ttf");
    	pe.createPdf(pq);
    	System.out.println("Done!");
    }
    
}