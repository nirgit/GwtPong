package com.nm.gwt.pong.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.Random;
import com.nm.gwt.pong.client.model.Ball;
import com.nm.gwt.pong.client.model.Pad;

/**
 * @author NirLap
 */
public class Game {

	private final static int FPS = 25 ; 
	private final static int BALL_SPEED = 12 ;
	
	private final Context2d ctx ;
	private final Pad computer ;
	private final Pad player ;
	
	private final Ball ball ;
	private Canvas canvas;
	
	private int targetY = 0 ;
	
	
	/**
	 * c'tor
	 * @param ctx
	 */
	public Game(Canvas canvas) {
		this.canvas 	= canvas;
		this.ctx 		= canvas.getContext2d() ;
		// Initialize game elements.
		int middleY 	= (canvas.getCoordinateSpaceHeight()-Pad.getHeight())/2;
		this.player 	= new Pad(0, middleY) ; 
		int endX 		= canvas.getCoordinateSpaceWidth() - Pad.getWidth() ;
		this.computer 	= new Pad(endX-Pad.getWidth()/2, middleY) ;
		this.ball		= new Ball(endX/2, middleY) ;
		// randomize the next Y value target.
		nextRandomYTarget();
		// init canvas style
		initCanvas(canvas) ;
		// bind logics.
		bind(canvas) ;
	}

	private void nextRandomYTarget() {
		this.targetY = Random.nextInt(canvas.getCoordinateSpaceHeight()) ;
	}

	private void initCanvas(Canvas canvas) {
		canvas.addStyleName("game-canvas") ;
	}

	/**
	 * Bind controls
	 * @param canvas
	 */
	private void bind(final Canvas canvas) {
		canvas.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.isDownArrow()) {
					player.down() ;
					int LOWER_BOUND = canvas.getCoordinateSpaceHeight()-Pad.getHeight();
					player.setY(Math.min(player.getY(), LOWER_BOUND)) ;
				} else if(event.isUpArrow()) {
					player.up() ;
					player.setY(Math.max(player.getY(), 0)) ;
				}
			}
		}) ;
	}

	/**
	 * Start the game.
	 */
	public void start() {
		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
			@Override
			public boolean execute() {
				// the game loop
				updateState() ;
				// render
				render() ;
				return true;
			}
		}, 1000 / FPS) ;
	}

	
	/**
	 * updates the state of the ball & computer's pad.
	 */
	private void updateState() {
		int direction = ball.getDirection() ;
		int nextX = ball.getX() + direction * BALL_SPEED ;
		if(nextX < Pad.getWidth()) {
			ball.setDirectionToComputer() ;
			nextRandomYTarget() ;
			nextX = Pad.getWidth() ;
		} else if(nextX > canvas.getCoordinateSpaceWidth() - Pad.getWidth()){
			ball.setDirectionToPlayer() ;
			nextRandomYTarget() ;
			nextX = canvas.getCoordinateSpaceWidth() - Pad.getWidth() ;
		}
		// calculate next Y
		double targetX	= (ball.isComputerDirection() ? canvas.getCoordinateSpaceWidth() - Pad.getWidth() : Pad.getWidth()) ;
		double tilt		= (double)(targetY - ball.getY()) / (targetX - ball.getX()) ;
		double nextY	= targetY - tilt * (targetX - nextX) ;
		// set next Y & X
		ball.setY((int)nextY) ;
		ball.setX(nextX) ;
	}
	
	/**
	 * Render game elements.
	 */
	private void render() {
		computer.draw(ctx) ;
		player.draw(ctx) ;
		ball.draw(ctx) ;
	}
	
}
