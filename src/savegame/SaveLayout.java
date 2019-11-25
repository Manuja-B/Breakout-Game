package savegame;

/**
 * 
 * @author Team6
 * @version 1.0
 * @since	1.0
 * This class is instance of a game. The object of this class will be persisted for saving a game.
 */

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SaveLayout implements Serializable
{
	private double ballX;
	private double ballY;
	private double batX;
	private double time;
	
	public double getBallX() {
		return ballX;
	}

	public void setBallX(double ballX) {
		this.ballX = ballX;
	}

	public double getBallY() {
		return ballY;
	}

	public void setBallY(double ballY) {
		this.ballY = ballY;
	}

	public double getBatX() {
		return batX;
	}

	public void setBatX(double batX) {
		this.batX = batX;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
	}

	public SaveLayout(double ballX,double ballY,double batX,double time)
	{
		this.ballX = ballX;
		this.ballY = ballY;
		this.batX = batX;
		this.time = time;
	}
	
		
	public SaveLayout() {
	}
	

}
