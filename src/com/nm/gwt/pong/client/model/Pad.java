package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.ui.Image;
import com.nm.gwt.pong.client.resources.imgs.ImageResources;

/**
 * @author NirLap
 *
 */
public class Pad implements IDrawable {

	private final static Image padImg ;

	private static final int PAD_HEIGHT	= 200 ;
	private static final int PAD_WIDTH	= 50;
	private static final int STEP		= 10;

	
	static {
		SafeUri safeURI = ImageResources.INSTANCE.pad().getSafeUri() ;
		padImg = new Image(safeURI) ;
	}
	
	private int x ;
	private int y ;

	/**
	 * C'tor
	 * @param x
	 * @param y
	 */
	public Pad(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public void draw(Context2d ctx) {
		ctx.save() ;
		ctx.clearRect(0, 0, 2 * PAD_WIDTH, ctx.getCanvas().getHeight()) ;
		ctx.translate(x, y) ;
		ctx.drawImage(ImageElement.as(padImg.getElement()), 0, 0) ;
		ctx.restore() ;
	}

	public static int getHeight() {
		return PAD_HEIGHT ;
	}

	public static int getWidth() {
		return PAD_WIDTH ;
	}
	
	public void down() {
		y = y + STEP ;
	}
	
	public void up() {
		y = y - STEP ;
	}

}
