package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;

/**
 * Date: 27/11/2012
 * Description: Represents the score board of the game.
 * @author Nir Moav
 */
public class ScoreBoard implements IDrawable {

	private final static int FONT_SIZE		= 40 ;
	private final static String FONT	 	= "bold " + FONT_SIZE + "px Calibri";
	private final static String FONT_COLOR 	= "white";
	
	private int player ;
	private int computer ;
	private int pointsToWin;
	
	/**
	 * C'tor
	 * @param pointsToWin 
	 */
	public ScoreBoard(int pointsToWin) {
		this.pointsToWin = pointsToWin;
		reset() ;
	}
	
	public void reset() {
		this.player 	= 0 ;
		this.computer 	= 0 ;
	}

	public void incPlayerScore() {
		this.player++ ;
	}
	
	public void incComputerScore() {
		this.computer++ ;
	}

	public boolean isGameOver() {
		return this.player >= pointsToWin || this.computer >= pointsToWin ;
	}
	
	public boolean didPlayerWin() {
		return isGameOver() && player > computer ;
	}
	
	public boolean didComputerWin() {
		return isGameOver() && player < computer ;
	}
	
	@Override
	public void draw(Context2d ctx) {
		int positionY = 60 ;
		int positionX = ctx.getCanvas().getWidth() / 2 ;
		ctx.save() ;
		// clear board
		ctx.clearRect(positionX-FONT_SIZE, positionY-FONT_SIZE, 90, 45) ;
		ctx.translate(positionX, positionY) ;
		ctx.setStrokeStyle(FONT_COLOR) ;
		ctx.setFont(FONT) ;
		ctx.setTextAlign(TextAlign.CENTER) ;
		ctx.strokeText(player + " : " + computer, 0, 0) ;
		
		ctx.restore() ;
	}

}
