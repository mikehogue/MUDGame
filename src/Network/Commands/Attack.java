package Network.Commands;

import GameModel.Notification;
import GameModel.World;
import Network.Server;
import Network.Server.ClientManager;
import GameModel.Character;

/**
 * One of the commands that a Client can execute.
 * An Attack Command is executed when the Client wishes to attack another Character. 
 * Attacking another Character is a crucual part of the game: without it, certain Items may not 
 * be found, and the game may not be playable. After Attacking Mobs, their health will 
 * go down, unless the Player misses.
 * @author Matt Latura
 */
public class Attack implements Command{

	/**
	 * First, search if there is a Character in the Area that has the name that the Client 
	 * wishes to attack. If there is not, alert the Client that there is nobody to attack.
	 * If the Character is there, attack the player and alert the Player of the results!
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		Character defender = w.getArea(cm.getPlayer().getLocation()).hasCharacter(message);
		if(defender != null){
			w.attack(cm.getPlayer(), defender);
		}
		else{
			w.sendNotification(new Notification(cm.getPlayer(), "There is no one by that name here", false));
		}
		
	}

}
