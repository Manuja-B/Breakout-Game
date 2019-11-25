package gameobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;


/**
 * 
 * @author Team6_Assignment3
 * @version 1.0
 * @since	1.0
 * 
 * This class is the ball object of the game.
 * All the functionalities of the ball are defined in this class.
 */
public class Ball  {
	private Circle ballCircle;
	private double radius = 8;
	private double ballCenterX = 30;
	private double ballCenterY = 30; 
	private double initialSpeedX = 2, initialSpeedY = -3;
	private double speedX = initialSpeedX;
	private double speedY = initialSpeedY;
	private Queue<List<Double>> replayQueue = new LinkedList<>();
	private Queue<List<Double>> replayQueue2 = new LinkedList<>();
	private Clock clock;
	private double undoTime;

	// Construct a Ball object with the arbitrary values
	public Ball() {
        ballCircle = new Circle();
        ballCircle.setRadius(radius);
        ballCircle.setCenterX(ballCenterX);
        ballCircle.setCenterY(ballCenterY);
        ballCircle.setFill(Color.HOTPINK);
        
    }
	
	// update the position, call draw() method, and record the current queue into queue

	public void update() {
		ballCenterX += speedX;
		ballCenterY += speedY;
		// If ball hits upper or bottom
		if(ballCenterY - radius + initialSpeedY <= 0 || ballCenterY + radius - initialSpeedY >= 530) {
			setSpeedY(speedY * -1);
		}
		// IF ball hits left or right walls
		if(ballCenterX - radius - initialSpeedX <= 0 || ballCenterX + radius + initialSpeedX >= 700) {
			setSpeedX(speedX * -1);
		}
		
		draw();
		
		List<Double> coord = new ArrayList<>();
		coord.add(ballCenterX);
		coord.add(ballCenterY);
		coord.add(clock.getTimer());
		replayQueue.add(coord);
		replayQueue2.add(coord);
	}

	
	public void draw() {
		ballCircle.setCenterX(ballCenterX);
        ballCircle.setCenterY(ballCenterY);
		ballCircle.setFill(Color.HOTPINK);
	}
	

	public void start() {
		// When restarted, the queue should be emptied
		if (!replayQueue.isEmpty()) {
			replayQueue.clear();
			replayQueue2.clear();
		}
		radius = 8;
		ballCenterX = 30;
		ballCenterY = 30;
		speedX = initialSpeedX;
		speedY = initialSpeedY; 
		update();
	}
	

	public void pause() {
	}

	
	public void resume() {
		if (replayQueue2.isEmpty()) {
			for (List<Double> coord : replayQueue) {
				replayQueue2.add(coord);
			}
		}
		update();
	}

	
	public void undo() {
		if (replayQueue2.isEmpty()) {
			for (List<Double> coord : replayQueue) {
				replayQueue2.add(coord);
			}
		}
		if (!replayQueue.isEmpty()) {
			List<Double> coord = (((LinkedList<List<Double>>)replayQueue)).getLast();
			ballCenterX = coord.get(0);
			ballCenterY = coord.get(1);
			undoTime = coord.get(2);
			// Removing the last recorded queue from replayQueue after undo one step
			((LinkedList<List<Double>>)replayQueue).removeLast();	
			((LinkedList<List<Double>>)replayQueue2).removeLast();
			draw();
		}
	}
	
	public void replay() {
			if (!replayQueue2.isEmpty()) {
				List<Double> coord = (((LinkedList<List<Double>>)replayQueue2)).poll();
				ballCenterX = coord.get(0);
				ballCenterY = coord.get(1);
				undoTime = coord.get(2);
				clock.getClockLabel().setText(String.valueOf(undoTime));
				draw();
			}
	}

	
	public double getX() {
		return ballCenterX;
	}


	public double getY() {
		return ballCenterY;
	}
	
	public void setSpeedX(double s) {
		speedX = s;
	}
	
	public double getBallCenterX() {
		return ballCenterX;
	}

	public void setBallCenterX(double ballCenterX) {
		this.ballCenterX = ballCenterX;
	}

	public double getBallCenterY() {
		return ballCenterY;
	}

	public void setBallCenterY(double ballCenterY) {
		this.ballCenterY = ballCenterY;
	}

	public void setSpeedY(double s) {
		speedY = s;
	}
	
	public double getSpeedX() {
		return this.speedX;
	}
	
	public double getSpeedY() {
		return this.speedY;
	}
	
	public Circle getCircle() {
		return ballCircle;
	}
	
	public void getClockFromMain(Clock c) {
		this.clock = c;
	}
	
	public double getUndoTime() {
		return this.undoTime;
	}

	public boolean collideWith(Shape shape) {
		Shape inter = Shape.intersect(ballCircle, shape);
		if(inter.getLayoutBounds().getHeight() >= 0 || inter.getLayoutBounds().getWidth() >= 0)
			return true;
		else
			return false;
	}
	
	public int getReplayQueueSize() {
		return this.replayQueue.size();
		
	}
	
	public Queue<List<Double>> getReplayQueue1()
	{
		return replayQueue;
	}
	
	public Queue<List<Double>> getReplayQueue2()
	{
		return replayQueue2;
	}
}