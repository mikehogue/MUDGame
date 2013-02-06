package Network.Commands;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Inventory Command is a command that a Client executes when they wish to see their
 * equipped items and all other items that they are holding. After compiling the list, 
 * a Note is sent with all items that the player has.
 * @author Matt Latura
 */
public class Inventory implements Command{

	/**
	 * Create a list of the equipped items and all other items and send the Client a Note
	 * containing all of the information.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		cm.send(new Note(cm.getPlayer().printInventory()));		
	}

}
