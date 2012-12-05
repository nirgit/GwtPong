package com.nm.gwt.pong.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nm.gwt.pong.client.model.GameLevel;

/**
 * This is the entry point class of GWT.
 * The game's will be initialized and started here.
 */
public class PongHtml5 implements EntryPoint {

	private final static String GAME_PANEL_STYLE_NAME 	= "game-panel";
	private final static Label GAME_TITLE 				= new Label("SpacePong powered by GWT's HTML5 API.");
	private final ListBox levelPicker					= new ListBox() ;
	private final Button startButton					= new Button("Play") ;

	// the game instance.
	private Game game 		= null ;
	private Panel panel 	= null ;
	private Canvas canvas 	= null ;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		// create the game's panel.
		panel = new VerticalPanel() ;
		// set game panel's style
		panel.addStyleName(GAME_PANEL_STYLE_NAME) ;
		// init the level select box
		initSelectBox(levelPicker) ;
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
		panel.add(levelPicker) ;
		panel.add(startButton) ;
		panel.add(canvas) ;
		// add the game panel to the body of the HTML hosting page.
		RootLayoutPanel.get().add(panel) ;
	}

	/**
	 * @param levelPicker
	 */
	private void initSelectBox(final ListBox levelPicker) {
		for(GameLevel level : GameLevel.values())
			levelPicker.addItem(level.getTitle(), level.name()) ;
		startButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int selectedIndex		= levelPicker.getSelectedIndex() ;
				String levelName		= levelPicker.getValue(selectedIndex) ;
				GameLevel pickedLevel	= GameLevel.valueOf(levelName) ;
				startGame(pickedLevel) ;
			}
		}) ;
	}

	/**
	 * Starts a new game with a given level.
	 * @param gameLevel a game level.
	 */
	private void startGame(GameLevel gameLevel) {
		if(game == null)
			game = new Game(panel, canvas, gameLevel) ;
		// stop current game.
		this.game.stop() ;
		// set the level
		this.game.setLevel(gameLevel) ;
		// start it !
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
