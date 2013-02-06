package Network.Commands;
import java.util.Iterator;

import GameModel.Item;
import GameModel.Notification;
import GameModel.Player;
import GameModel.World;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Give Command means that the Client wishes to give one of the items in their inventory, 
 * or an equipped item, to a specified player. If the item is equipped, unequip the item from the player
 * and then remove it from the inventory. If the requested player is not logged in, do not attempt
 * to send the item; send a Note that explains that they can only give it to logged in players.
 * @author Michael Hogue and Matt Latura
 *
 */
public class Give implements Command {

	/** 
	 * The player wishes to give one of their items to another player.
	 * First, we must check if the receiver is logged in.
	 * Second, we must check if the sender has the item.
	 * If both are true, we will send the item over to the player and 
	 * alert both sides that the exchange has taken place.
	 */
	public void execute(Server s, String message, ClientManager cm) {
			
		Player player = cm.getPlayer();
		
		String[] args = s.getArgs(message);
		String item = "";
		String receiver = "";
		String sender = player.getName();
		
		if (args.length <= 1) {
			cm.send(new Note("You must have an item to send and a receiver to receive it!" , false));
			return;
		}
		
		try {	
			if (args.length > 2) {
				int i = 0;
				for (i = 0; i < args.length - 2; i++) {
					item += args[i].toLowerCase() + " ";
				}
				item += args[i];
				receiver = args[args.length - 1].toLowerCase();
			} else {
				item = args[0].toLowerCase();
				receiver = args[1].toLowerCase();
			}
		} catch (NullPointerException e) {
			cm.send(new Note("Invalid arguments for the give command. Use give <ItemName> <PlayerName>", false));
			return;
		}
		
		if (receiver.equalsIgnoreCase(sender)) {
			cm.send(new Note("You cannot give an item to yourself!", false));
			return;
		}
		
		World w = s.getWorld();
		Iterator<Player> itr = w.getAllPlayers();
		Player sendTo = null;
		while (itr.hasNext()) {
			Player p= itr.next();
			if (p.getName().equalsIgnoreCase(receiver)) {
				sendTo = p;
			}
		}
		
		
		if (sendTo == null) {
			cm.send(new Note("This player is not available to send an item to.", false));
		} else {
			Item i = cm.getPlayer().dropItem(item);
			if (i != null) {
				w.getArea(cm.getPlayer().getLocation()).removeItem(i);
				sendTo.giveItem(i);
				cm.send(new Note("You have given " + receiver + " your " + item, false));
				w.sendNotification(new Notification(sendTo, "You have received a " + item + " from " + sender, false));
			} else {
				cm.send(new Note("You do not have this item to give!", false));
			}
		}
		
		cm.send(cm.getPlayer().getInventory());
		
	}

}
