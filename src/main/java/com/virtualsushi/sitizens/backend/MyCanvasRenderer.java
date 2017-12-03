package com.virtualsushi.sitizens.backend;

import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.Property;
import com.itextpdf.layout.renderer.CanvasRenderer;
import com.itextpdf.layout.renderer.IRenderer;

public class MyCanvasRenderer extends CanvasRenderer {
	protected boolean full = false;
	public MyCanvasRenderer(Canvas canvas) {
		super(canvas);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void addChild(IRenderer renderer) {
		super.addChild(renderer);
		full = Boolean.TRUE.equals(getPropertyAsBoolean(Property.FULL));
	}
	
	public boolean isFull() {
		return full;
	}
}
