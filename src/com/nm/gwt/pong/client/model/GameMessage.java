package com.nm.gwt.pong.client.model;

import com.google.gwt.canvas.dom.client.Context2d;

/**
 * Date: Dec 5, 2012
 * Description: <b>GameMessage</b> - 
 * @author Nir Moav
 */
public class GameMessage implements IDrawable {

	private final static int FONT_SIZE		= 80 ;
	private final static String FONT 		= "bold " + FONT_SIZE + "px Calibri" ;
	private final static String COLOR_WHITE = "white" ;
	private final static int MAX_WIDTH 		= 250 ;
	
	private String message ;
	private int maxMessageWidth;
	private boolean isTextFilled ;
	
	/**
	 * C'tor 
	 * @param message the message to show. 
	 * @param maxMessageWidth the max width of the message.
	 * @param isTextFilled true iff the text should be filled.
	 */
	public GameMessage(String message, int maxMessageWidth, boolean isTextFilled) {
		this.message 			= message ;
		this.maxMessageWidth 	= maxMessageWidth;
		this.isTextFilled 		= isTextFilled;
	}
	
	public GameMessage(String message) {
		this(message, MAX_WIDTH, false) ;
	}

	public void setMessage(String message) {
		this.message = message ;
	}
	
	/**
	 * @see com.nm.gwt.pong.client.model.IDrawable#draw(com.google.gwt.canvas.dom.client.Context2d)
	 */
	@Override
	public void draw(Context2d ctx) {
		String[] lines = message.split("\n") ;
		ctx.save() ;
		
		double middleOfScreenX = (ctx.getCanvas().getWidth() - maxMessageWidth) / 2;
		double middleOfScreenY = ctx.getCanvas().getHeight() / 4 ;
		ctx.translate(middleOfScreenX, middleOfScreenY) ;
		if(isTextFilled)
			ctx.setFillStyle(COLOR_WHITE) ;
		else
			ctx.setStrokeStyle(COLOR_WHITE) ;
		ctx.setFont(FONT) ;
		for(int i=0; i < lines.length; i++) {
			if(isTextFilled)
				ctx.fillText(lines[i], 0, 0 + i * FONT_SIZE + 10, maxMessageWidth) ;
			else
				ctx.strokeText(lines[i], 0, 0 + i * FONT_SIZE + 10, maxMessageWidth) ;
		}
		
		ctx.restore() ;
	}

}
