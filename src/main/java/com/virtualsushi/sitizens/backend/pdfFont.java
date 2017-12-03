package com.virtualsushi.sitizens.backend;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.test.annotations.WrapToTest;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.io.IOException;

@WrapToTest
public class pdfFont {
	public static final String DEST = "/home/vinh/Documents/vinh/uw/dataproject/java/pdf_file/text_paragraph.pdf";
	 
    public static void main(String args[]) throws IOException {
//        File file = new File(DEST);
//        file.getParentFile().mkdirs();
        DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        Date date = new Date();
        System.out.println(String.format("src/%s/%s_%s.pdf", "Flyer", dateFormat.format(date), "demo"));
//        new pdfFont().createPdf(DEST);
    }
 
    public void createPdf(String dest) throws IOException {
        // Initialize PDF document
        PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
 
        // Initialize document
        Document document = new Document(pdf);
 
        // Add content
        PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
        PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
        Text title = new Text("The Strange Case of Dr. Jekyll and Mr. Hyde").setFont(bold);
        Text author = new Text("Robert Louis Stevenson").setFont(font);
        Paragraph p = new Paragraph().add(title).add(" by ").add(author);
        document.add(p);
 
        //Close document
        document.close();
    }
}
