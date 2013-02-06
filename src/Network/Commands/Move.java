package Network.Commands;

import GameModel.Direction;
import GameModel.World;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Move Command is executed when a Client wishes to enter another Area in the game.
 * The server will attempt to move the Client in the specified direction. If the direction does
 * not exist, we will alert them to specify a legal direction. If the player successfully inputs a 
 * correct direction, we will move them if possible, alert them that a key is needed,
 * or alert them that they cannot move in that direction. 
 * @author Michael Hogue
 *
 */
public class Move implements Command {

	/**
	 * The execute command is executed when the Client wishes to move. The move command takes many
	 * abbreviations, such that GO, MOVE, M, N, E, S, W, NORTH, EAST, SOUTH, WEST are all valid
	 * move commands. Attempt to move the player in the specified direction and alert them the 
	 * result. 
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		if (message.equalsIgnoreCase("e") || message.equalsIgnoreCase("east")) 
			cm.send(new Note(w.moveCharacter(cm.getPlayer(), Direction.EAST)));
		else if (message.equalsIgnoreCase("w") || message.equalsIgnoreCase("west")) 
			cm.send(new Note(w.moveCharacter(cm.getPlayer(), Direction.WEST)));
		else if (message.equalsIgnoreCase("n") || message.equalsIgnoreCase("north")) 
			cm.send(new Note(w.moveCharacter(cm.getPlayer(), Direction.NORTH)));
		else if (message.equalsIgnoreCase("s") || message.equalsIgnoreCase("south")) 
			cm.send(new Note(w.moveCharacter(cm.getPlayer(), Direction.SOUTH)));
		else 
			cm.send(new Note("Invalid move direction! North, East, South and West are valid arguments."));
	}

}
