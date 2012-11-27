package com.nm.gwt.pong.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.nm.gwt.pong.client.model.Ball;
import com.nm.gwt.pong.client.model.Pad;
import com.nm.gwt.pong.client.model.ScoreBoard;

/**
 * Date: 27/11/2012
 * Description: <b>Game</b> - is the class responsible for initializing the game elements, and controlling the game flow. 
 * @author Nir Moav
 */
public class Game {

	private final static int FPS 			= 25 ; 
	private final static int BALL_SPEED 	= 12 ;
	private final static int COMPUTER_SPEED = 3 ;
	
	private final FlowPanel container;
	private final Canvas canvas;

	// a restart button, to use when the game is finished.
	private Button restart ;
	private ScoreBoard scoreBoard ;
	
	private final Pad computer ;
	private final Pad player ;
	private final Ball ball ;
	
	// the Y coordinate which the ball is heading to.
	private int targetY = 0 ;
	
	
	/**
	 * c'tor
	 * @param container the container panel of the game.  
	 * @param canvas the canvas where the game is played.
	 */
	public Game(FlowPanel container, Canvas canvas) {
		this.container	= container ;
		this.canvas 	= canvas;
		// Initialize game elements.
		int middleY 	= (canvas.getCoordinateSpaceHeight()-Pad.getHeight())/2;
		this.player 	= new Pad(0, middleY) ; 
		int endX 		= canvas.getCoordinateSpaceWidth() - Pad.getWidth() ;
		this.computer 	= new Pad(endX, middleY) ;
		this.ball		= new Ball(endX/2, middleY) ;
		this.scoreBoard = new ScoreBoard() ;
		initRestartButton();
		// bind logics.
		bind(canvas) ;
	}

	/**
	 * initializes the Restart button.
	 */
	private void initRestartButton() {
		this.restart = new Button("Restart") ;
		this.restart.setStyleName("restart-button") ;
	}

	/**
	 * Picks a random Y coordinate for the ball to head to.
	 */
	private void nextRandomYTarget() {
		this.targetY = Random.nextInt(canvas.getCoordinateSpaceHeight()) ;
	}

	/**
	 * Bind controls
	 * @param canvas
	 */
	private void bind(final Canvas canvas) {
		// bind mouse controls
		bindMouseControls(canvas) ;
		// bind a restart button.
		bindRestartButton() ;
	}

	/**
	 * Binds a mouse control to the canvas to control the player's pad.
	 * @param canvas
	 */
	private void bindMouseControls(final Canvas canvas) {
		canvas.addMouseMoveHandler(new MouseMoveHandler() {
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				int yCoordinate = event.getY() ;
				// correction so pad doesn't go lower than the game's screen.
				if(yCoordinate > canvas.getCoordinateSpaceHeight()-Pad.getHeight())
					yCoordinate = canvas.getCoordinateSpaceHeight()-Pad.getHeight() ;
				player.setY(yCoordinate) ;
			}
		}) ;
	}

	/**
	 * Start the game.
	 */
	public void start() {
		// randomize the next Y value target.
		nextRandomYTarget();
		// start the game
		Scheduler.get().scheduleFixedPeriod(new RepeatingCommand() {
			@Override
			public boolean execute() {
				// the game loop
				updateState() ;
				// render
				render() ;
				// while game is not over, continue.
				boolean isGameOver = scoreBoard.isGameOver() ;
				if(isGameOver) { 
					showRestart() ;
					return false ;
				} else {
					return true ;
				}
			}
		}, 1000 / FPS) ;
	}

	/**
	 * Show a restart button to play again.
	 */
	private void showRestart() {
		// add to container.
		container.add(restart) ;
	}

	private void bindRestartButton() {
		restart.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				scoreBoard.reset() ;
				start() ;
				restart.removeFromParent() ;
			}
		}) ;
	}
	
	
	/**
	 * updates the state of the ball & computer's pad.
	 */
	private void updateState() {
		// update the ball's X & Y coordinates.
		updateBallCoordinates() ;
		// play the computer's move.
		updateComputerPad() ;
		// Check coordinates to see if a score update is needed.
		checkCoords() ;
	}

	/**
	 * Checks if computer or player scored.
	 */
	private void checkCoords() {
		// if the ball hits the player's side
		if(ball.getX() <= player.getX() + Pad.getWidth() && !player.isHit(ball.getY())) {
			scoreBoard.incComputerScore() ;
			// make sure ball goes to the other direction.
			ball.setDirectionToComputer() ;
			ball.setX(player.getX() + Pad.getWidth()) ;
			// render score board!
			scoreBoard.draw(canvas.getContext2d()) ;
		}
		// if the ball hits the computer's side
		if(ball.getX() >= canvas.getCoordinateSpaceWidth() - Pad.getWidth() && !computer.isHit(ball.getY())) {
			scoreBoard.incPlayerScore() ;
			// make sure ball goes to the other direction.
			ball.setDirectionToPlayer() ;
			ball.setX(computer.getX() - Pad.getWidth()) ;
			// render score board!
			scoreBoard.draw(canvas.getContext2d()) ;
		}
	}

	/**
	 * This is the computer playing.
	 */
	private void updateComputerPad() {
		// check first if the ball is heading towards its pad. don't do anything if not.
		if(!ball.isComputerDirection()) return ;
		// the computer needs to aspire to get to the target Y, where the ball is heading.
		int computerYCoordinate = computer.getY();
		if(computerYCoordinate + Pad.getHeight() < targetY) {
			computer.setY(computerYCoordinate + COMPUTER_SPEED) ;
		} else if (computerYCoordinate > targetY) {
			computer.setY(computerYCoordinate - COMPUTER_SPEED) ;
		}
	}

	/**
	 * Updates the coordinates of the ball.
	 */
	private void updateBallCoordinates() {
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
		// calculate next Y of the ball... some algebra...
		double targetX	= (ball.isComputerDirection() ? canvas.getCoordinateSpaceWidth() - Pad.getWidth() : Pad.getWidth()) ;
		double tilt		= (double)(targetY - ball.getY()) / (targetX - ball.getX()) ;
		double nextY	= targetY - tilt * (targetX - nextX) ;
		// set next Y & X
		ball.setY((int)nextY) ;
		ball.setX(nextX) ;
	}
	
	/**
	 * Render the game elements.
	 */
	private void render() {
		Context2d ctx = canvas.getContext2d() ;
		computer.draw(ctx) ;
		player.draw(ctx) ;
		ball.draw(ctx) ;
	}
}
