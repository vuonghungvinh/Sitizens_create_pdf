package com.virtualsushi.sitizens.backend;

import java.io.File;
import java.io.FileNotFoundException;

import com.itextpdf.io.IOException;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

public class Canvas_Cut {
	public static final String DEST = "/home/vinh/Documents/vinh/uw/dataproject/java/pdf_file/sub1/canvas_cut.pdf";
	public static void main(String[] args) throws IOException, java.io.IOException {
		// TODO Auto-generated method stub
		File f = new File(DEST);
		f.getParentFile().mkdir();
		new Canvas_Cut().createPdf(DEST);
	}
	
	public void createPdf(String dest) throws java.io.IOException {
		PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
		PdfPage page = pdf.addNewPage();
		PdfCanvas pdfCanvas = new PdfCanvas(page);
		Rectangle rect = new Rectangle(36, 750, 100, 50);
		pdfCanvas.rectangle(rect);
		pdfCanvas.stroke();
		Canvas canvas = new Canvas(pdfCanvas, pdf, rect);
		PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
		PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
		Text title =
		    new Text("The Strange Case of Dr. Jekyll and Mr. Hyde").setFont(bold);
		Text author = new Text("Robert Louis Stevenson").setFont(font);
		Paragraph p = new Paragraph().add(title).add(" by ").add(author);
		canvas.add(p);
		canvas.close();
		pdf.close();
	}
}
