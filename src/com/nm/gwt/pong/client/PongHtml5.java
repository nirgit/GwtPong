package com.nm.gwt.pong.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class PongHtml5 implements EntryPoint {

	private FlowPanel panel = new FlowPanel() ;
	private Canvas canvas = null ;
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Label title = new Label("Pong powered by HTML5 GWT");
		
		canvas = Canvas.createIfSupported() ;
		canvas.setCoordinateSpaceWidth(1000) ;
		canvas.setCoordinateSpaceHeight(500) ;
		
		canvas.setPixelSize(1000, 500) ;
		
		panel.add(title) ;
		panel.add(canvas) ;
		RootLayoutPanel.get().add(panel) ;
		
		final Game game = new Game(canvas) ;
		
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {
			@Override
			public void execute() {
				canvas.setFocus(true) ;
				game.start() ;
			}
		}) ;
	}
}
