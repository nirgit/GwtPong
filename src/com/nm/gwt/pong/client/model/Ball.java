package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author NirLap
 *
 */
public class Ball implements IDrawable {
	
	private final static String COLOR_ORANGE	= "orange";
	private final static String COLOR_WHITE 	= "white";
	private final static String COLOR_GREEN		= "green";
	private final static String COLOR_LIGHTBLUE = "lightblue";

	private final static double RADIUS 			= 20 ;
	private final static double BORDER_WIDTH 	= 3 ;
	private final static double RADIANS 		= Math.PI / 180;
	
	private int angle		= 0 ;
	private int direction 	= -1 ;
	
	private int oldX ;
	private int oldY ;
	
	private int x ;
	private int y ;
	
	/**
	 * C'tor
	 */
	public Ball(int x, int y) {
		this.x = x ;
		this.oldX = x ;
		
		this.y = y ;
		this.oldY = y ;
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
		// save all context's transform data, etc... 
		ctx.save() ;
		// make changes...
		// Clear the last ball's position.
		ctx.clearRect(oldX-RADIUS-2, oldY-RADIUS-2, (2+RADIUS)*2, (2+RADIUS)*2) ;
		// paint the ball
		// draw the ball outline shape
		ctx.beginPath() ;
		ctx.arc(x, y, RADIUS, 0, 2 * Math.PI) ;
		ctx.closePath() ;
		// rotate the ball
		ctx.translate(x, y) ;
		ctx.rotate(angle * RADIANS) ;
		// fill the ball
		drawBallBody(ctx) ;
		// draw ball border
		drawBorder(ctx) ;
		// update old coordinates.
		oldX = x ;
		oldY = y ;
		// restore original context.
		ctx.restore() ;
		// increase the angle
		angle = (angle + 10)%360 ;
	}

	private void drawBallBody(Context2d ctx) {
		CanvasGradient gradient = ctx.createLinearGradient(0, 0, RADIUS/3, RADIUS/3) ;
		gradient.addColorStop(0, COLOR_ORANGE) ;
		gradient.addColorStop(0.1, COLOR_WHITE) ;
		gradient.addColorStop(0.3, COLOR_GREEN) ;
		gradient.addColorStop(0.6, COLOR_LIGHTBLUE) ;
		ctx.setFillStyle(gradient) ;
		ctx.fill() ;
	}

	private void drawBorder(Context2d ctx) {
		ctx.setLineWidth(BORDER_WIDTH) ;
		ctx.setStrokeStyle(COLOR_WHITE) ;
		ctx.stroke() ;
	}

	public void setDirectionToComputer() {
		this.direction = 1 ;
	}
	
	public void setDirectionToPlayer() {
		this.direction = -1 ;
	}

	public int getDirection() {
		return direction;
	}

	public boolean isComputerDirection() {
		return direction > 0 ;
	}
}
