package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;

/**
 * Date: 27/11/2012
 * Description: Represents a Pad in the game.
 * @author Nir Moav
 */
public class Pad implements IDrawable {

	private static final String COLOR_BLACK		= "black" ;
	private static final String COLOR_SILVER 	= "silver" ;
	private static final String COLOR_WHITE 	= "white" ;

	private static final int PAD_HEIGHT	= 200 ;
	private static final int PAD_WIDTH	= 50;
	private static final int STEP		= 10;

	// the last coordinates of the pad.
	private int oldX ;
	private int oldY ;

	private int x ;
	private int y ;

	/**
	 * C'tor
	 * @param x the x coordinate of the pad.
	 * @param y the y coordinate of the pad.
	 */
	public Pad(int x, int y) {
		this.x 		= x;
		this.oldX 	= x;
		
		this.y 		= y;
		this.oldY 	= y;
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

	/**
	 * @see com.nm.gwt.pong.client.model.IDrawable#draw(com.google.gwt.canvas.dom.client.Context2d)
	 */
	@Override
	public void draw(Context2d ctx) {
		ctx.save() ;
		// clear left overs from before.
		ctx.clearRect(oldX-1, oldY-1, PAD_WIDTH+2, PAD_HEIGHT+2) ;
		// move to new position.
		ctx.translate(x, y) ;
		// draw the pad's body.
		drawPadBody(ctx);
		drawPadBorder(ctx);
		// update the rendered ("old") coordinates
		oldX = x ;
		oldY = y ;
		ctx.restore() ;
	}

	private void drawPadBorder(Context2d ctx) {
		ctx.beginPath() ;
		ctx.setLineCap(LineCap.ROUND) ;
		ctx.setStrokeStyle(COLOR_WHITE) ;
		ctx.setLineWidth(2) ;
		ctx.strokeRect(0, 0, PAD_WIDTH, PAD_HEIGHT) ;
		ctx.closePath() ;
	}

	private void drawPadBody(Context2d ctx) {
		ctx.beginPath() ;
		CanvasGradient gradient = ctx.createLinearGradient(0, 0, PAD_WIDTH, 0) ;
		gradient.addColorStop(0f, 	COLOR_SILVER) ;
		gradient.addColorStop(0.5f, COLOR_BLACK) ;
		gradient.addColorStop(1, 	COLOR_SILVER) ;
		ctx.setFillStyle(gradient) ;
		ctx.fillRect(0, 0, PAD_WIDTH, PAD_HEIGHT) ;
		ctx.closePath() ;
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

	/**
	 * @param y
	 * @return true iff the given y is in the range of the pad.
	 */
	public boolean isHit(int y) {
		return this.y < y && this.y + Pad.PAD_HEIGHT > y ;
	}

}
