package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * @author NirLap
 *
 */
public class Ball implements IDrawable {

	private final static double RADIUS = 20 ;
	private final static double BORDER_WIDTH = 3 ;
	
	private int direction = -1 ;
	
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
		ctx.beginPath() ;
		ctx.arc(x, y, RADIUS, 0, 2 * Math.PI) ;
		ctx.setFillStyle("yellow") ;
		ctx.fill() ;
		ctx.setLineWidth(BORDER_WIDTH) ;
		ctx.setStrokeStyle("silver") ;
		ctx.stroke() ;
		ctx.closePath() ;
		// update old coordinates.
		oldX = x ;
		oldY = y ;
		// restore original context.
		ctx.restore() ;
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
