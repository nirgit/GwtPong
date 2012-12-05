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
import com.nm.gwt.pong.client.model.GameMessage;
import com.nm.gwt.pong.client.model.OvalPad;
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
	
	private final OvalPad computer ;
	private final OvalPad player ;
	
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
		int middleY 	= (canvas.getCoordinateSpaceHeight() - Pad.getHeight())/2;
		int endX 		= canvas.getCoordinateSpaceWidth() - Pad.getWidth() ;
		this.player 	= new OvalPad(2*OvalPad.WIDTH, middleY) ; 
		this.computer 	= new OvalPad(endX-OvalPad.WIDTH, middleY) ;
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
					showResults() ;
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
	 * Show the results of the game.
	 */
	private void showResults() {
		if(scoreBoard.didPlayerWin()) {
			showMessage("You win!") ;
		} else if(scoreBoard.didComputerWin()) {
			showMessage("You lost!") ;
		}
		showRestart() ;
	}
	
	/**
	 * Displays a message on the screen.
	 * @param message
	 */
	private void showMessage(String message) {
		GameMessage gameMessage = new GameMessage(message);
		gameMessage.draw(canvas.getContext2d()) ;
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
		if(player.getX() + player.getWidth() >= ball.getX() && player.isMiss(ball)) {
			scoreBoard.incComputerScore() ;
		}
		// if the ball hits the computer's side
		if(computer.getX() - computer.getWidth() <= ball.getX() && computer.isMiss(ball)) {
			scoreBoard.incPlayerScore() ;
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
		if(computerYCoordinate + Pad.getHeight() <= ball.getTargetY()) {
			computer.setY(computerYCoordinate + level.getComputerSpeed()) ;
		} else if (computerYCoordinate >= ball.getTargetY()) {
			computer.setY(computerYCoordinate - level.getComputerSpeed()) ;
		}
	}

	/**
	 * Updates the coordinates of the ball.
	 */
	private void updateBallCoordinates(Ball ball) {
		int direction = ball.getDirection() ;
		int nextX = ball.getX() + direction * ball.getSpeed() ;
		if((nextX < player.getX() + player.getWidth() && player.isMiss(ball)) || player.isHit(ball)) {
			ball.setDirectionToComputer() ;
			nextRandomYTarget(ball) ;
			// ensure the ball does not stay in the "scoring" area
		} else if((nextX > computer.getX() - computer.getWidth() && computer.isMiss(ball)) || computer.isHit(ball)){
			ball.setDirectionToPlayer() ;
			nextRandomYTarget(ball) ;
			// ensure the ball does not stay in the "scoring" area
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
