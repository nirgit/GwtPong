package com.nm.gwt.pong.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * This is the entry point class of GWT.
 * The game's will be initialized and started here.
 */
public class PongHtml5 implements EntryPoint {

	private final static String GAME_PANEL_STYLE_NAME 	= "game-panel";
	private final static Label GAME_TITLE 				= new Label("SpacePong powered by GWT's HTML5 API.");
	
	private FlowPanel panel = null ;
	private Canvas canvas 	= null ;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// create the game's panel.
		panel = new FlowPanel() ;
		// set game panel's style
		panel.addStyleName(GAME_PANEL_STYLE_NAME) ;
		// Create the canvas!
		canvas = Canvas.createIfSupported() ;
		// set the amount of coordinates in the canvas.
		canvas.setCoordinateSpaceWidth(1000) ;
		canvas.setCoordinateSpaceHeight(500) ;
		// set the size of the canvas in pixels.
		canvas.setPixelSize(1000, 500) ;
		// set canvas' style
		canvas.addStyleName("game-canvas") ;
		// add the contents to the game panel.
		panel.add(GAME_TITLE) ;
		panel.add(canvas) ;
		
		// add the game panel to the body of the HTML hosting page.
		RootLayoutPanel.get().add(panel) ;
		
		// Create the game
		final Game game = new Game(panel, canvas) ;
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				// set focus on the canvas & start the game!
				canvas.setFocus(true) ;
				game.start() ;
			}
		}) ;
	}
}
