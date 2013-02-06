package Network.Commands;

import GameModel.Character;
import GameModel.Notification;
import GameModel.Player;
import GameModel.World;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Retreat Command is executed when a Client wishes to get further from another Character.
 * Retreating from Mobs can change which Attack is used when Attacking for both sides of 
 * the Fight. For example, the Dragon will have different attacks at distance three than it does
 * at distance one. 
 * @author Matt Latura
 */
public class Retreat implements Command{

	/**
	 * Attempt to retreat on the Character specified. If the Character is not in the same
	 * Area as the requester, alert the Client that there is nobody in the Area. Otherwise, 
	 * retreat further from the Character and alert the Client that they have retreated
	 * successfully.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		Player mover = cm.getPlayer();
		Character target = w.getArea(mover.getLocation()).hasCharacter(message);
		if(target != null){
			w.retreat(mover, target);
		}
		else{
			w.sendNotification(new Notification(mover, "There is no one here by that name", false));
		}		
	}

}
