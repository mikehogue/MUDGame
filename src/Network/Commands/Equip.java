package Network.Commands;

import GameModel.World;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * An Equip Command is used when a Client wishes to equip or unequip an Equippable Item 
 * that they have in their inventory.
 * @author Matt Latura
 *
 */
public class Equip implements Command{

	/**
	 * Attempt to (un)equip the Item, and send the response back to the Client.
	 * Send the Player and their Inventory back to the Client as well to update the stat
	 * panels.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		w.sendNotification(cm.getPlayer().equipItem(message));
		w.sendPlayer(cm.getPlayer());
		cm.send(cm.getPlayer().getInventory());
	}

}
