package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.nm.gwt.pong.client.model.IDrawable;

/**
 * Date: 27/11/2012
 * Description: Represents a Pad in the game.
 * @author Nir Moav
 */
public class OvalPad implements IDrawable {

	public static final int HEIGHT	= 150 ;
	public static final int WIDTH	= 20;

	private static final String COLOR_BLACK		= "black" ;
	private static final String COLOR_WHITE 	= "white" ;

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
	public OvalPad(int x, int y) {
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
		ctx.clearRect(oldX-WIDTH-1, oldY-WIDTH-1, 2+WIDTH*2, 2+HEIGHT+WIDTH*2) ;
		// move to new position.
		ctx.translate(x, y) ;
		// draw the pad's body.
		drawPadBody(ctx);
		// update the rendered ("old") coordinates
		oldX = x ;
		oldY = y ;
		ctx.restore() ;
	}

	private void drawPadBody(Context2d ctx) {
		ctx.beginPath() ;
		ctx.setLineCap(LineCap.ROUND) ;
		ctx.setStrokeStyle(COLOR_WHITE) ;
		ctx.setLineWidth(2) ;
		ctx.arc(0, 0, WIDTH, Math.PI , 0, false) ; 
		ctx.lineTo(WIDTH, HEIGHT) ;
		ctx.arc(0, HEIGHT, WIDTH, 0, Math.PI, false) ;
		ctx.lineTo(-WIDTH, 0) ;
		CanvasGradient gradient = ctx.createLinearGradient(-WIDTH, 0, WIDTH, 0) ;
		gradient.addColorStop(0f, 	COLOR_WHITE) ;
		gradient.addColorStop(0.5f, COLOR_BLACK) ;
		gradient.addColorStop(1, 	COLOR_WHITE) ;
		ctx.setFillStyle(gradient) ;
		ctx.fill() ;
		ctx.stroke() ;
		ctx.closePath() ;
	}

	public int getHeight() {
		return HEIGHT ;
	}

	public int getWidth() {
		return WIDTH ;
	}
	
	public void down() {
		y = y + STEP ;
	}
	
	public void up() {
		y = y - STEP ;
	}

	public boolean isHitY(int y) {
		return this.y - getHeight() <= y && this.y + getHeight() >= y ;
	}

	public boolean isHitX(int x) {
		return this.x - getWidth() <= x && x <= this.x + getWidth() ;
	}
	
	public boolean isHit(Ball ball) {
		return isHitY(ball.getY()) && isHitX(ball.getX()) ;
	}

	public boolean isMiss(Ball ball) {
		return !isHit(ball) ;
	}
}
