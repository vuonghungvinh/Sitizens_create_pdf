package com.virtualsushi.sitizens.backend;

import com.google.common.io.BaseEncoding;
import com.google.common.io.Files;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
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
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.virtualsushi.sitizens.model.Event;
import com.virtualsushi.sitizens.model.EventDescription;
import com.virtualsushi.sitizens.model.Media;
import com.virtualsushi.sitizens.model.Phone;
import com.virtualsushi.sitizens.model.Price;
import com.virtualsushi.sitizens.model.PrintRequest;
import com.virtualsushi.sitizens.model.PrintRequest.Language;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
        /*PdfFont bold = PdfFontFactory.createFont(String.format(FONT_BOLD, MONTSERRAT), PdfEncodings.IDENTITY_H);
        PdfFont medium = PdfFontFactory.createFont(String.format(FONT_MEDIUM, MONTSERRAT), PdfEncodings.IDENTITY_H);*/
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        EventDescription eventDescription = (language == Language.FR) ? event.getFr() : event.getNl();
//        PdfWriter writer = new PdfWriter(baos);
        System.out.println("sjdfjsdfjdfsjdfs");
        String filepath = createPathFile(eventDescription.getName(), "Flyer");
        File f = new File(filepath);
        f.getParentFile().mkdirs();
        PdfWriter writer = new PdfWriter(filepath);
        PdfDocument pdf = new PdfDocument(writer);
        PdfPage page = pdf.addNewPage(ps);
        PdfCanvas canvas = new PdfCanvas(page);

        //Replace the origin of the coordinate system to the center of the page
        canvas.concatMatrix(1, 0, 0, 1, ps.getWidth() / 2, ps.getHeight() / 2);

        addImage(event.getMedias(), canvas, ps.getWidth(), ps.getHeight());
//        drawAxes(canvas, ps);
        if (eventDescription != null)
            drawTitle(pdf, eventDescription.getName(), canvas, ps.getWidth(), ps.getHeight(), font);
        Locale locale = new Locale(language.name().toLowerCase());
        String dayOfWeek = event.getDateStart().dayOfWeek().getAsText(locale);

        String dayOfMonth = event.getDateStart().dayOfMonth().getAsText();
        String month = event.getDateStart().monthOfYear().getAsText(locale);
//        String hour = getStartHour(event, locale, language);
        String hour = "08";
        drawDate(pdf, dayOfWeek, dayOfMonth, month, hour, canvas, ps.getWidth(), ps.getHeight(), font);
        String _abstract = StringUtils.isNotBlank(eventDescription.getShort_description()) ? eventDescription.getShort_description() : shorten(eventDescription.getLong_description(), 120);
        drawDescription(pdf, _abstract, canvas, ps.getWidth(), ps.getHeight(), font);
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

//    private String getStartHour(Event event, Locale locale, Language language) {
//        return String.format("%s%s%s%s", event.getDateStart().hourOfDay().getAsText(locale), language.equals(Language.NL) ? "u" : "h", minutes, "  ");
//    }
    private String fontPath(String font) {
    	return String.format("%s/%s", "src/main/resources/fonts", font);
    }
    
    private void drawTitle(PdfDocument pdf, String title, PdfCanvas canvas, float width, float height, String font_s) throws IOException {
        font_s = fontPath(font_s);
        PdfFont font = PdfFontFactory.createFont(font_s);
    	float fontSize = (float) (width / 0.6) / title.length();
        float topStart = (height / 2) - 2 * fontSize;
        
        float paragraphWidth = width;
        float leftStart = 0 - paragraphWidth / 2;
        //PdfFont bold = PdfFontFactory.createFont(String.format(FONT_BOLD, MONTSERRAT), PdfEncodings.IDENTITY_H);

        Color greyColor = new DeviceRgb(37, 37, 37);
        /*canvas.beginText()
                .setFontAndSize(font, fontSize)
                .setColor(greyColor, true)
                .setLeading(36)
                .moveText(leftStart, topStart);
            canvas.newlineShowText(title);
        canvas.endText();*/


        Rectangle rect = new Rectangle(leftStart, topStart, paragraphWidth, 2 * fontSize);

        Paragraph p = new Paragraph(title).setFixedLeading(fontSize).setTextAlignment(TextAlignment.CENTER);

        new Canvas(canvas, pdf, rect)
            .setFont(font)
            .setFontSize(fontSize)
            .add(p.setFixedPosition(leftStart, topStart, paragraphWidth)).close();


    }

    private void drawDescription(PdfDocument pdf, String description, PdfCanvas canvas, float width, float height, String font_s) throws IOException {
    	font_s = fontPath(font_s);
    	PdfFont font = PdfFontFactory.createFont(font_s);
        description = shorten(description, 300);
        float fontSize = 26;
        float paragraphWidth = (float) (width * 0.8);
        int rows = (int) (description.length() * fontSize * 0.6 / paragraphWidth) + 1;
        float paragraphHeight = rows * fontSize;
        float leftStart = 0 - paragraphWidth / 2;
        float topStart = 0 - height / 6 - 40 - paragraphHeight;

        Rectangle rect = new Rectangle(leftStart, topStart, paragraphWidth, paragraphHeight);

        Paragraph p = new Paragraph(description).setFixedLeading(fontSize).setTextAlignment(TextAlignment.CENTER);

        Color greyColor = new DeviceRgb(37, 37, 37);

        new Canvas(canvas, pdf, rect)
            .setFont(font)
            .setFontSize(fontSize)
            .setFontColor(greyColor)
            .add(p.setFixedPosition(leftStart, topStart, paragraphWidth)).close();
    }

    private void drawDate(PdfDocument pdf, String dayOfWeek, String dayOfMonth, String month, String hour, PdfCanvas canvas, float width, float height, String font_s) throws IOException {
        font_s = fontPath(font_s);
        PdfFont font = PdfFontFactory.createFont(font_s);
    	float bigFontSize = 80;
        float mediumFontSize = 40;
        float smallFontSize = 28;

        int bottomMargin = 20;

        float paragraphWidth = width / 5;
        float leftX = width / 2 - paragraphWidth - 30;
        float calenderHeight = 2 * smallFontSize + bigFontSize + mediumFontSize + bottomMargin;

        Color greyColor = new DeviceRgb(37, 37, 37);
        Color bluishColor = new DeviceRgb(54, 120, 160);


        Rectangle rect = new Rectangle(leftX - 10, 0, paragraphWidth + 10, calenderHeight + 10);

        Paragraph p = new Paragraph(dayOfWeek).setFixedLeading(smallFontSize).setTextAlignment(TextAlignment.CENTER);
        Paragraph p2 = new Paragraph(dayOfMonth).setFixedLeading(bigFontSize).setTextAlignment(TextAlignment.CENTER);
        Paragraph p3 = new Paragraph(month).setFixedLeading(smallFontSize).setTextAlignment(TextAlignment.CENTER);
        Paragraph p4 = new Paragraph(hour).setFixedLeading(mediumFontSize).setTextAlignment(TextAlignment.CENTER);

        canvas
            .setStrokeColor(Color.RED)
            .setFillColor(Color.WHITE)
            .setLineWidth(0.5f)
            .rectangle(rect)
            .fillStroke();

        new Canvas(canvas, pdf, rect)
            .setFont(font)
            .setFontSize(smallFontSize)
            .setFontColor(greyColor)
            .add(p.setFixedPosition(leftX, mediumFontSize + smallFontSize + bigFontSize + bottomMargin, paragraphWidth)).close();

        new Canvas(canvas, pdf, rect)
            .setFont(font)
            .setFontSize(bigFontSize)
            .setFontColor(bluishColor)
            .add(p2.setFixedPosition(leftX, mediumFontSize + smallFontSize + bottomMargin, paragraphWidth)).close();

        new Canvas(canvas, pdf, rect)
            .setFont(font)
            .setFontSize(smallFontSize)
            .setFontColor(greyColor)
            .add(p3.setFixedPosition(leftX, mediumFontSize + bottomMargin, paragraphWidth)).close();

        new Canvas(canvas, pdf, rect)
            .setFont(font)
            .setFontSize(mediumFontSize)
            .setFontColor(bluishColor)
            .add(p4.setFixedPosition(leftX, 10, paragraphWidth)).close();


    }


    /*private void drawDate(String dayOfWeek, String dayOfMonth, String month, String hour, PdfCanvas canvas, float width, float height) throws IOException {
        float bigFontSize = 80;
        float mediumFontSize = 40;
        float smallFontSize = 28;
        double length = mediumFontSize*hour.length();
        if(smallFontSize*dayOfWeek.length() > length)
            length = smallFontSize*dayOfWeek.length();
        if(bigFontSize*dayOfMonth.length() > length)
            length = bigFontSize*dayOfMonth.length();
        if(smallFontSize*month.length() > length)
            length = smallFontSize*month.length();
        double leftStart = width/2 - 0.6*length;
        double topStart = height/3;
        PdfFont bold = PdfFontFactory.createFont(String.format(FONT_BOLD, MONTSERRAT), PdfEncodings.IDENTITY_H);
        PdfFont light = PdfFontFactory.createFont(String.format(FONT_LIGHT, MONTSERRAT), PdfEncodings.IDENTITY_H);
        PdfFont medium = PdfFontFactory.createFont(String.format(FONT_MEDIUM, MONTSERRAT), PdfEncodings.IDENTITY_H);

        Color greyColor = new DeviceRgb(37, 37, 37);
        Color bluishColor = new DeviceRgb(54, 120, 160);
        canvas.beginText()
                .setFontAndSize(medium, smallFontSize)
                .setColor(greyColor, true)
                .moveText(leftStart, topStart);
            canvas.newlineShowText(dayOfWeek);
        canvas.beginText()
                .setFontAndSize(bold, bigFontSize)
                .setColor(bluishColor, true)
                .moveText(leftStart, topStart-0.9*(bigFontSize));
            canvas.newlineShowText(dayOfMonth);
        canvas.beginText()
                .setFontAndSize(medium, smallFontSize)
                .setColor(greyColor, true)
                .moveText(leftStart, topStart-0.9*(bigFontSize+smallFontSize));
            canvas.newlineShowText(month);
        canvas.beginText()
                .setFontAndSize(medium, mediumFontSize)
                .setColor(bluishColor, true)
                .moveText(leftStart, topStart-0.9*(bigFontSize+smallFontSize+mediumFontSize));
            canvas.newlineShowText(hour);
        canvas.endText();

    }*/

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
        PdfExtGState state = new PdfExtGState();
        state.setFillOpacity(0.25f);
        canvas.setExtGState(state);
        //Calculate top left coordinates for the background image
        float x = (0 - imageData.getWidth() * scaleFactor) / 2;
        float y = (0 - imageData.getHeight() * scaleFactor) / 2;
        float totalWidth = imageData.getWidth() * scaleFactor;
        canvas.addImage(imageData, x, y, totalWidth, true);

        //Now add the main image and set its height to half of the page height (*1.33 to convert Postscript units into pixels)
        float smallScaleFactor = (height / 2) / imageData.getHeight();

        if (imageRatio < 0.70)
            smallScaleFactor = (float) (width - 40) / imageData.getWidth();
        else
            smallScaleFactor = (float) (height / 2) / imageData.getHeight();
        float imageWidth = imageData.getWidth() * smallScaleFactor;
        float imageHeight = imageData.getHeight() * smallScaleFactor;
        canvas.restoreState();
        canvas.addImage(imageData, 0 - (imageWidth / 2), (height / 2) - imageHeight - 120, imageWidth, true);
    }


    private static void drawAxes(PdfCanvas canvas, PageSize ps) {
        //Draw X axis
        canvas.moveTo(-(ps.getWidth() / 2 - 15), 0)
            .lineTo(ps.getWidth() / 2 - 15, 0)
            .stroke();

        //Draw X axis arrow
        canvas.setLineJoinStyle(PdfCanvasConstants.LineJoinStyle.ROUND)
            .moveTo(ps.getWidth() / 2 - 25, -10)
            .lineTo(ps.getWidth() / 2 - 15, 0)
            .lineTo(ps.getWidth() / 2 - 25, 10).stroke()
            .setLineJoinStyle(PdfCanvasConstants.LineJoinStyle.MITER);

        //Draw Y axis
        canvas.moveTo(0, -(ps.getHeight() / 2 - 15))
            .lineTo(0, ps.getHeight() / 2 - 15)
            .stroke();

        //Draw Y axis arrow
        canvas.saveState()
            .setLineJoinStyle(PdfCanvasConstants.LineJoinStyle.ROUND)
            .moveTo(-10, ps.getHeight() / 2 - 25)
            .lineTo(0, ps.getHeight() / 2 - 15)
            .lineTo(10, ps.getHeight() / 2 - 25).stroke()
            .restoreState();

        //Draw X serif
        for (int i = -((int) ps.getWidth() / 2 - 61); i < ((int) ps.getWidth() / 2 - 60); i += 40) {
            canvas.moveTo(i, 5).lineTo(i, -5);
        }
        //Draw Y serif
        for (int j = -((int) ps.getHeight() / 2 - 57); j < ((int) ps.getHeight() / 2 - 56); j += 40) {
            canvas.moveTo(5, j).lineTo(-5, j);
        }
        canvas.stroke();
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
    	for (Object object : jsonarray) {
    		JSONObject obj = (JSONObject)object;
    		System.out.println(obj.get("fr" ));
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
         	events.add(new Event(Long.parseLong(obj.get("id").toString()), DateTime.parse(obj.get("date_start").toString()), 
         			DateTime.parse(obj.get("date_end").toString()), nl, fr, prices, medias));
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
    			PrintRequest.Language.NL, events.get(0), "Cardo-Bold.ttf");
    	pe.createPdf(pq);
    	System.out.println("here");
    }
    
}