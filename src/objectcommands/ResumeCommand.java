package objectcommands;

import gameobjects.Ball;
import gameobjects.Brick;
import gameobjects.Clock;
import gameobjects.Paddle;

/**
 * 
 * @author Team6_Assignment3
 * @version 1.0
 * @since	1.0
 * 
 * This class is instance of the game for the resume functionality of each objects of the game
 */
public class ResumeCommand implements Command {

	private Ball ball;
	private Paddle paddle;
	private Clock clock;
	private Brick brick;
	
	public ResumeCommand(Ball b, Paddle p, Clock c, Brick br) {
		this.ball = b;
		this.paddle = p;
		this.clock = c;			
		this.brick = br;
	}
	
	@Override
	public void execute() {
		ball.resume();
		paddle.resume();
		clock.resume();
		brick.resume();
	}
}
