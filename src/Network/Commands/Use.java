package Network.Commands;
import GameModel.Bomb;
import GameModel.Consumable;
import GameModel.Direction;
import GameModel.Item;
import GameModel.Player;
import GameModel.World;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Use Command is entered by the Client when they wish to use a Consumable (such as a Potion)
 * or an Item like a Key in the game. The Server will check if the Player owns the Item and
 * if the Item is a Consumable or a Key. After attempting to use the Item, send the Client a 
 * Note that contains information on the Item being used. 
 * @author Michael Hogue and Matt Latura
 *
 */
public class Use implements Command {

	@Override
	public void execute(Server s, String message, ClientManager cm) {
		Player player = cm.getPlayer();
		World w = s.getWorld();
		
		Item i = player.hasItem(message.toLowerCase());
		Direction d = w.getArea(player.getLocation()).unlockDoor(i);
		if (i != null) {
			if (i instanceof Consumable) {
				((Consumable) i).use(player);
				cm.send(new Note("You have used the " + message, false));
				cm.send(cm.getPlayer());
				cm.send(cm.getPlayer().getInventory());
			}
			else if(i instanceof Bomb)	{
				((Bomb) i).use(player, w);
				cm.send(cm.getPlayer().getInventory());
			}
			else{
				if(d != null){
					player.getInventory().remove(i);
					cm.send(new Note("You have unlocked the " + d.toString().toLowerCase() + " exit"));
					cm.send(cm.getPlayer().getInventory());
				}
				else{
					cm.send(new Note("Sorry, that item is not useable at the moment"));
				}
			}	
		}
		else {
			cm.send(new Note("You do not have that item to use!"));
		
		}
	}
}
