package gameobjects;

import java.io.Serializable;

import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

/**
 * 
 * @author Team6_Assignment3
 * @version 1.0
 * @since	1.0
 * 
 * This class is the brick object of the game.
 * All the functionalities of the brick are defined in this class.
 */
public class Brick {
	public Rectangle getBrickRectangle() {
		return brickRectangle;
	}

	public void setBrickRectangle(Rectangle brickRectangle) {
		this.brickRectangle = brickRectangle;
	}

	private Rectangle brickRectangle;
	private double width = 40;
	private double height = 40;
	private double x = 400;
	private double y = 200;
	public Brick() {
		brickRectangle = new Rectangle(width, height);
		brickRectangle.setX(x);
		brickRectangle.setY(y);
		brickRectangle.setFill(Color.GRAY);
		brickRectangle.setEffect(new Glow(10.0));
		brickRectangle.getTransforms().add(new Rotate(45, 45, 50));
	}
	
	public Rectangle getRectangle() {
		return this.brickRectangle;
	}
	
	public void explode() {
		brickRectangle.setFill(Color.TRANSPARENT);
	}

	
	public void update() {
		// TODO Auto-generated method stub
		
	}

	
	public void draw() {
		// TODO Auto-generated method stub
		
	}

	
	public void start() {
		brickRectangle.setFill(Color.GRAY);
	}
	


	public void pause() {
		// TODO Auto-generated method stub
		
	}


	public void resume() {
		// TODO Auto-generated method stub
		
	}


	public void undo() {
		// TODO Auto-generated method stub
		
	}


	public void replay() {
		// TODO Auto-generated method stub
		
	}

	
	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public double getY() {
		// TODO Auto-generated method stub
		return 0;
	}

	
}