package com.virtualsushi.sitizens.backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

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

public class Extends_Canvas {
	public static final String DEST = "src/main/resources/pdf_files/extends_canvas.pdf";
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		File f = new File(DEST);
		f.getParentFile().mkdir();
		new Extends_Canvas().createPdf(DEST);
	}
	
	public void createPdf(String dest) throws IOException {
		PdfDocument pdf = new PdfDocument(new PdfWriter(dest));
		PdfPage page = pdf.addNewPage();
		PdfCanvas pdfCanvas = new PdfCanvas(page);
		Rectangle rect = new Rectangle(36, 500, 100, 250);
		pdfCanvas.rectangle(rect);
		pdfCanvas.stroke();
		Canvas canvas = new Canvas(pdfCanvas, pdf, rect);
		MyCanvasRenderer mycanvasrender = new MyCanvasRenderer(canvas);
		canvas.setRenderer(mycanvasrender);
		PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
		PdfFont bold = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
		Text title =
		    new Text("The Strange Case of Dr. Jekyll and Mr. Hyde").setFont(bold);
		Text author = new Text("Robert Louis Stevenson").setFont(font);
		Paragraph p = new Paragraph().add(title).add(" by ").add(author);
		while (!mycanvasrender.isFull()) {
			canvas.add(p);
		}
		canvas.close();
		pdf.close();
	}

}
