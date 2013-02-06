package Network.Commands;
import GameModel.Item;
import GameModel.Player;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Drop command means that the player wishes to place an item that they have in their inventory
 * into the room that they are currently in. If they do not have the item, alert the player that 
 * they cannot drop it. If they do have the item, remove it from their inventory and place it into
 * the room. The player and inventory will be sent to the client to update their stats and inventory
 * panes. 
 * @author Michael Hogue
 *
 */
public class Drop implements Command {

	/** 
	 * Drop an Item into the Client's current Area. If the Client does not have this Item, 
	 * the Client should be alerted that they cannot drop it. 
	 */
	public void execute(Server s, String message, ClientManager cm) {
		Player player = cm.getPlayer();
		String compareTo = message.toLowerCase();		
		Item i = player.dropItem(compareTo);
		if (i != null) {
			s.getWorld().getArea(player.getLocation()).addItem(i);			
			cm.send(new Note(i.getName() + " has been dropped."));
			cm.send(cm.getPlayer());
			cm.send(cm.getPlayer().getInventory());
		} else {
			cm.send(new Note("You do not have that item to drop!"));
		}
	}

}
