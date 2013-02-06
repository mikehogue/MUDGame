package View;

/**
 * The possible Views that the Client can see, and are used for switching between the two.
 * There are two views, TITLE and MUD.
 * @author Michael Hogue
 */
public enum Views {
	/**
	 * This view is used when the Client has not logged into the game yet, or has been logged out
	 * for various reasons. They are able to quit and attempt to join a game.
	 */
	TITLE, 
	/**
	 * This view is used when the Client is already logged into the game. The game GUI is 
	 * visible to the Client so they can play the game.
	 */
	MUD;
}
