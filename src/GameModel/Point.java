package GameModel;

import java.io.Serializable;
/**
 * Point is a pair of x and y coordinates used to locate an object's location in World's 2d array of Areas.
 * @author Matthew Latura
 */
public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	private int x;
	private int y;
	
	/**
	 * Default constructor, sets the passed ints to the x and y values of the Point
	 * @param x - x value of Point
	 * @param y - y value of Point
	 */
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x value of this point
	 * @return the x value of this point
	 */
	public int getX(){
		return x;
	}
	
	/**
	 * Returns the y value of this point
	 * @return the y value of this point
	 */
	public int getY(){
		return y;
	}
	
	
	/**
	 * Compares this Point with the passed Point. If the x and y values are equal than it returns true, otherwise returns false
	 * @param p2 - The Point to compare this Point with
	 * @return - if the Point's x and y values are equal
	 */
	public boolean equalTo(Point p2){
		if(this.x == p2.getX() && this.y == p2.getY()){
			return true;
		}
		return false;
	}
}
