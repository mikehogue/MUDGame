package GameModel;

/**
 * This is an enum representing the 4 cardinal directions used for movement in
 * the MUD game.
 * 
 * @author Matthew Latura
 * 
 */
public enum Direction {
	/**
	 * The Direction we are moving to, or the direction that we want the
	 * opposite of, is North.
	 */
	NORTH,
	/**
	 * The Direction we are moving to, or the direction that we want the
	 * opposite of, is East.
	 */
	EAST,
	/**
	 * The Direction we are moving to, or the direction that we want the
	 * opposite of, is South.
	 */
	SOUTH,
	/**
	 * The Direction we are moving to, or the direction that we want the
	 * opposite of, is West.
	 */
	WEST;

	/**
	 * Calculates a point one unit in this Direction from the passed Point
	 * 
	 * @param p
	 *            - the Point used as a starting location
	 * @return one unit in this Direction from the passed Point
	 */
	public Point move(Point p) {
		if (this == NORTH) {
			return new Point(p.getX(), p.getY() + 1);
		} else if (this == EAST) {
			return new Point(p.getX() + 1, p.getY());
		} else if (this == SOUTH) {
			return new Point(p.getX(), p.getY() - 1);
		} else {
			return new Point(p.getX() - 1, p.getY());
		}
	}

	/**
	 * Returns the opposite of this Direction
	 * 
	 * @return the opposite of this Direction
	 */
	public Direction opposite() {
		if (this == NORTH) {
			return SOUTH;
		} else if (this == EAST) {
			return WEST;
		} else if (this == SOUTH) {
			return NORTH;
		} else {
			return EAST;
		}
	}
}
