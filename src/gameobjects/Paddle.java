package gameobjects;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Team6_Assignment3
 * @version 1.0
 * @since	1.0
 * 
 * This class is the paddle object of the game.
 * All the functionalities of the paddle are defined in this class.
 */
public class Paddle extends Pane {
	private Rectangle paddleRectangle;
	
	private double initialX = 0, initialY = 200;
	private double paddleX = initialX, paddleY = initialY;
	private double speed = 10;
	private double width = 120, height = 10;
	private Queue<Double> replayQueue = new LinkedList<>();
	private Queue<Double> replayQueue2 = new LinkedList<>();
	
	public Paddle() {
		paddleRectangle = new Rectangle();
		paddleRectangle.setWidth(width);
		paddleRectangle.setHeight(height);
		paddleRectangle.setFill(Color.BLACK);
		paddleRectangle.setX(paddleX);
		paddleRectangle.setY(paddleY);
	}

	public Queue<Double> getReplayQueue() {
		return replayQueue;
	}

	public void setReplayQueue(Queue<Double> replayQueue) {
		this.replayQueue = replayQueue;
	}

	public Queue<Double> getReplayQueue2() {
		return replayQueue2;
	}

	public void setReplayQueue2(Queue<Double> replayQueue2) {
		this.replayQueue2 = replayQueue2;
	}

	
	public void update() {	
		replayQueue.add(paddleX);
		replayQueue2.add(paddleX);
		paddleRectangle.setOnKeyPressed(pressEvent -> {
            if (pressEvent.getCode() == KeyCode.LEFT && paddleX - speed >= 0) {
                paddleX -= speed;
            } else if (pressEvent.getCode() == KeyCode.RIGHT && paddleX + width + speed <= 700) {
            	paddleX += speed;
            }
        });
		paddleRectangle.requestFocus();
		draw();
	}

	public double getPaddleX() {
		return paddleX;
	}

	public void setPaddleX(double paddleX) {
		this.paddleX = paddleX;
	}

	
	public void draw() {
		paddleRectangle.setLayoutX(paddleX);
		paddleRectangle.setFill(Color.BLACK);
	}

	
	
	public void start() {
		// When restarted, the queue should be emptied
		if (!replayQueue.isEmpty()) {
			replayQueue.clear();
			replayQueue2.clear();
		}
		paddleX = initialX;
		draw();
		update();
	}

	
	
	public void pause() {
	}

	
	public void resume() {
		// If it has replayed once, queue2 would be empty, so need to copy queue back to queue2
		if (replayQueue2.isEmpty()) {
			for (Double coord : replayQueue) {
				replayQueue2.add(coord);
			}
		}
		update();
	}
	

	
	public void undo() {
		// If it has replayed once, queue2 would be empty, so need to copy queue back to queue2
		if (replayQueue2.isEmpty()) {
			for (Double coord : replayQueue) {
				replayQueue2.add(coord);
			}
		}
		if (!replayQueue.isEmpty()) {
			Double coord = (((LinkedList<Double>)replayQueue)).getLast();
			paddleX = coord;
			((LinkedList<Double>)replayQueue).removeLast();
			((LinkedList<Double>)replayQueue2).removeLast();
			draw();
		}
	}

	
	public double getX() {
		return paddleX;
	}

	
	public double getY() {
		return paddleY;
	}

	
	public void replay() {
		if (!replayQueue2.isEmpty()) {
			Double coord = (((LinkedList<Double>)replayQueue2)).poll();
			paddleX = coord;
			draw();
		} 	
	}
	
	public Rectangle getRectangle() {
		return this.paddleRectangle;
	}
}