package com.nm.gwt.pong.client;

import java.util.HashSet;
import java.util.Set;

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
import com.google.gwt.user.client.ui.Panel;
import com.nm.gwt.pong.client.model.Ball;
import com.nm.gwt.pong.client.model.GameLevel;
import com.nm.gwt.pong.client.model.Pad;
import com.nm.gwt.pong.client.model.ScoreBoard;

/**
 * Date: 27/11/2012
 * Description: <b>Game</b> - is the class responsible for initializing the game elements, and controlling the game flow. 
 * @author Nir Moav
 */
public class Game {

	private final static int FPS 	= 25 ; 
	private GameLevel level 		= null ;

	private final Panel container;
	private final Canvas canvas;

	// a restart button, to use when the game is finished.
	private Button restart ;
	private ScoreBoard scoreBoard ;
	
	private final Pad computer ;
	private final Pad player ;
	
	private Set<Ball> balls ;
	// a flag indicating if the game is stopped.
	private boolean isStopped ;
	
	/**
	 * c'tor
	 * @param container the container panel of the game.  
	 * @param canvas the canvas where the game is played.
	 */
	public Game(Panel container, Canvas canvas, GameLevel level) {
		this.container	= container ;
		this.canvas 	= canvas;
		this.level		= level ;
		this.isStopped	= false ;
		// Initialize game elements.
		int middleY 	= (canvas.getCoordinateSpaceHeight()-Pad.getHeight())/2;
		int endX 		= canvas.getCoordinateSpaceWidth() - Pad.getWidth() ;
		this.player 	= new Pad(0, middleY) ; 
		this.computer 	= new Pad(endX, middleY) ;
		initRestartButton();
		// bind logics.
		bind(canvas) ;
	}
	
	public void setLevel(GameLevel level) {
		this.level = level;
	}

	/**
	 * Initializes the balls at X & Y coordinates.
	 * @param x
	 * @param y
	 */
	private void initBalls(int x, int y) {
		int lowestBallSpeed = level.getHighestBallsSpeed()/2 ;
		this.balls = new HashSet<Ball>() ;
		for(int i=0 ; i < level.getNumberOfBalls() ; i++) {
			Ball ball = new Ball(x, y, Random.nextInt(lowestBallSpeed) + lowestBallSpeed) ;
			balls.add(ball) ;
		}
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
	private void nextRandomYTarget(Ball ball) {
		ball.setTargetY(Random.nextInt(canvas.getCoordinateSpaceHeight())) ;
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
		initGame();
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
				} else if (isStopped){
					return false ;
				} else {
					return true ;
				}
			}
		}, 1000 / FPS) ;
	}

	private void initGame() {
		this.isStopped 	= false ; 
		// clear canvas.
		this.canvas.getContext2d().clearRect(0, 0, canvas.getCoordinateSpaceWidth(), canvas.getCoordinateSpaceHeight()) ;
		// remove restart button
		restart.removeFromParent() ;
		int middleY 	= (canvas.getCoordinateSpaceHeight()-Pad.getHeight())/2;
		int endX 		= canvas.getCoordinateSpaceWidth() - Pad.getWidth() ;
		initBalls(endX/2, middleY);
		this.scoreBoard = new ScoreBoard(level.getNumberOfBalls() * 3) ;
		// randomize the next Y value target for all balls.
		for(Ball b : this.balls)
			nextRandomYTarget(b);
	}
	
	public void stop() {
		this.isStopped = true ;
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
				start() ;
			}
		}) ;
	}
	
	
	/**
	 * updates the state of the ball & computer's pad.
	 */
	private void updateState() {
		Ball closestBall = null ;
		int x = -1 ;
		for(Ball ball : this.balls) {
			// update the ball's X & Y coordinates.
			updateBallCoordinates(ball) ;
			if(ball.getX() > x) { 
				x = ball.getX() ;
				closestBall = ball ;
			}
		}
		// play the computer's move.
		updateComputerPad(closestBall) ;
		// Check coordinates to see if a score update is needed.
		for(Ball ball : this.balls)
			checkCoords(ball) ;
	}

	/**
	 * Checks if computer or player scored.
	 * @param ball 
	 */
	private void checkCoords(Ball ball) {
		// if the ball hits the player's side
		if(ball.getX() <= player.getX() + Pad.getWidth() && !player.isHit(ball.getY())) {
			scoreBoard.incComputerScore() ;
			// make sure ball goes to the other direction.
			ball.setDirectionToComputer() ;
			ball.setX(player.getX() + Pad.getWidth()) ;
		}
		// if the ball hits the computer's side
		if(ball.getX() >= canvas.getCoordinateSpaceWidth() - Pad.getWidth() && !computer.isHit(ball.getY())) {
			scoreBoard.incPlayerScore() ;
			// make sure ball goes to the other direction.
			ball.setDirectionToPlayer() ;
			ball.setX(computer.getX() - Pad.getWidth()) ;
		}
	}

	/**
	 * This is the computer playing.
	 */
	private void updateComputerPad(Ball ball) {
		// check first if the ball is heading towards its pad. don't do anything if not.
		if(!ball.isComputerDirection()) return ;
		// the computer needs to aspire to get to the target Y, where the ball is heading.
		int computerYCoordinate = computer.getY();
		if(computerYCoordinate + Pad.getHeight() < ball.getTargetY()) {
			computer.setY(computerYCoordinate + level.getComputerSpeed()) ;
		} else if (computerYCoordinate > ball.getTargetY()) {
			computer.setY(computerYCoordinate - level.getComputerSpeed()) ;
		}
	}

	/**
	 * Updates the coordinates of the ball.
	 */
	private void updateBallCoordinates(Ball ball) {
		int direction = ball.getDirection() ;
		int nextX = ball.getX() + direction * ball.getSpeed() ;
		if(nextX < Pad.getWidth()) {
			ball.setDirectionToComputer() ;
			nextRandomYTarget(ball) ;
			nextX = Pad.getWidth() ;
		} else if(nextX > canvas.getCoordinateSpaceWidth() - Pad.getWidth()){
			ball.setDirectionToPlayer() ;
			nextRandomYTarget(ball) ;
			nextX = canvas.getCoordinateSpaceWidth() - Pad.getWidth() ;
		}
		// calculate next Y of the ball... some algebra...
		double targetX	= (ball.isComputerDirection() ? canvas.getCoordinateSpaceWidth() - Pad.getWidth() : Pad.getWidth()) ;
		double tilt		= (double)(ball.getTargetY() - ball.getY()) / (targetX - ball.getX()) ;
		double nextY	= ball.getTargetY() - tilt * (targetX - nextX) ;
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
		for(Ball ball : this.balls)
			ball.draw(ctx) ;
		scoreBoard.draw(ctx) ;
	}
}
