package com.nm.gwt.pong.client.model;

/**
 * Date: Nov 28, 2012
 * Description: <b>GameLevel</b> - represents a level in the game. 
 * @author Nir Moav
 */
public enum GameLevel {

	VERY_EASY("Very Easy", 1, 12, 5),
	EASY("Easy", 1, 14, 8),
	MEDIUM("Medium", 2, 14, 8),
	HARD("Hard", 4, 14, 12),
	VERY_HARD("Very Hard", 5, 16, 16),
	;
	
	private final String title;
	private final int numberOfBalls ;
	private final int highestBallsSpeed ;
	private final int computerSpeed ;
	
	/**
	 * C'tor 
	 * @param title the title of the level.
	 * @param numberOfBalls the number of balls participating.
	 * @param highestBallsSpeed the highest ball's speed.
	 * @param computerSpeed the speed of the computer's pad.
	 */
	private GameLevel(String title, int numberOfBalls, int highestBallsSpeed, int computerSpeed) {
		this.title				= title ;
		this.numberOfBalls		= numberOfBalls;
		this.highestBallsSpeed	= highestBallsSpeed;
		this.computerSpeed 		= computerSpeed;
	}

	public String getTitle() {
		return title;
	}

	public int getNumberOfBalls() {
		return numberOfBalls;
	}

	public int getHighestBallsSpeed() {
		return highestBallsSpeed;
	}

	public int getComputerSpeed() {
		return computerSpeed;
	}
}
