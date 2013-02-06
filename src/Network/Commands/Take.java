package Network.Commands;

import java.util.Arrays;
import java.util.Iterator;
import GameModel.Item;
import GameModel.World;
import GameModel.mobs.Mob;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Take Command is executed with two possible arguments. If there is only one argument,
 * Then the command is used to pick up an Item that is in the same area that the Player is.
 * If there are two arguments provided, the last argument must be a Mob. The Player will then
 * steal the Item from the Mob.
 * @author Mike Hogue
 *
 */
public class Take implements Command{

	/**
	 * The execute command will determine what action the Client is trying to do depending on their 
	 * arguments. If a Mob is included as the last argument, we will attempt to steal the Item
	 * from the Mob. If the Mob has the item, take the Item from the Mob and give it to the Player.
	 * If the Mob does not have the Item, then alert the Player that the Item is not there. 
	 * If there is not a Mob as the last argument, we know they are attempting to 
	 * pick up the Item from the room. Check if the Area has the Item; if So, give the Item  to the Player.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		String[] args = s.getArgs(message);
		String[] mobs = {"dragon", "giant rat", "goblin", "goblin shaman", "grue", 
				"imp", "skeleton archer", "skeleton warrior", "troll", "giant spider", "baby spider"
		};
		
		String lastTwoWords = "";
		
		try { 
			lastTwoWords = args[args.length - 2].toLowerCase() + " " + args[args.length - 1].toLowerCase();
		} catch (ArrayIndexOutOfBoundsException e) {
			w.takeItem(cm.getPlayer(), message);
			cm.send(cm.getPlayer().getInventory());
			return;
		}
		String lastWord = args[args.length - 1].toLowerCase();

		if (Arrays.asList(mobs).contains(lastWord)) {
			Iterator<Mob> moblist = w.getArea(cm.getPlayer().getLocation()).getMobs();
			String itemName = "";
			for (int i = 0; i < args.length - 1; i++) {
				itemName += args[i] + " ";
			}
			itemName = itemName.substring(0, itemName.length() - 1);
			
			while (moblist.hasNext()) {
				Mob m = moblist.next();
				if (m.getName().equalsIgnoreCase(lastTwoWords)) {
					Item hadItem = m.hasItem(itemName);
					if (hadItem != null) {
						cm.getPlayer().giveItem(hadItem);
						m.getInventory().remove(hadItem);
						cm.send(new Note("You got the " + itemName + " from the " + m.getName() + "!"));
						cm.send(cm.getPlayer().getInventory());
						return;
					} else {
						cm.send(new Note("The " + m.getName() + " did not have the " + itemName  + "!"));
						return;
					}
				} 
			}
			cm.send(new Note("Sorry, you are not in the same room as this Mob."));
			
		} else if (Arrays.asList(mobs).contains(lastTwoWords)) {
	
			Iterator<Mob> moblist = w.getArea(cm.getPlayer().getLocation()).getMobs();
			String itemName = "";
			for (int i = 0; i < args.length - 2; i++) {
				itemName += args[i] + " ";
			}
			itemName = itemName.substring(0, itemName.length() - 1);
			
			while (moblist.hasNext()) {
				Mob m = moblist.next();
				if (m.getName().equalsIgnoreCase(lastTwoWords)) {
					Item hadItem = m.hasItem(itemName);
					if (hadItem != null) {
						cm.getPlayer().giveItem(hadItem);
						m.getInventory().remove(hadItem);
						cm.send(new Note("You got the " + itemName + " from the " + m.getName() + "!"));
						cm.send(cm.getPlayer().getInventory());
						return;
					} else {
						cm.send(new Note("The " + m.getName() + " did not have the " + itemName  + "!"));
						return;
					}
				} 
			}
			cm.send(new Note("Sorry, you are not in the same room as this Mob."));
		} else {
			w.takeItem(cm.getPlayer(), message);
			cm.send(cm.getPlayer().getInventory());
		}
	}
}
