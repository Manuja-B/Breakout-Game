package gameobjects;

import javafx.scene.control.Label;

/**
 * 
 * @author Team6_Assignment3
 * @version 1.0
 * @since	1.0
 * 
 * This class is the clock of the game.
 * All the functionalities of the clock are defined in this class.
 */
public class Clock  {
	private double time = 0;
	private double delta = 1; // The counter to increment time in milliseconds
	private Label clockLabel;
	
	public Clock() {
		clockLabel = new Label("0.0");
	}
	
	public void setLabel(Label l) {
		this.clockLabel = l;
	}

	public double getTimer() {
		return this.time;
	}
	
	public void setTime(double newTime) {
		time = newTime;
	}
	

	public void update() {
		time += delta;
		clockLabel.setText(String.valueOf(time));
	}
	
	
	public void draw() {
		
	}


	public void pause() {
	}

	
	public void resume() {
	}

	
	public void undo() {
		if (time >= 5) {
			time -= delta;
			clockLabel.setText(String.valueOf(time));
		} else {
			clockLabel.setText(String.valueOf(0));
		}
	}

	
	public double getX() {
		return 0; // Clock doesn't need a getX function
	}

	
	public double getY() {
		return 0; // Clock doesn't need a getY function
	}

	
	public void start() {
		this.time = 0;
		
	}

	
	public void replay() {
	}
	
	public Label getClockLabel() {
		return this.clockLabel;
	}

}