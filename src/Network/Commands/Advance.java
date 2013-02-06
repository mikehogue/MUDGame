package Network.Commands;

import GameModel.Notification;
import GameModel.Player;
import GameModel.World;
import Network.Server;
import Network.Server.ClientManager;
import GameModel.Character;

/** 
 * One of the commands that a Client can execute.
 * An Advance Command is executed when a Client wishes to get closer to another Character.
 * Advancing on Mobs can change which Attack is used when Attacking for both sides of 
 * the Fight. For example, a Player cannot use their fists unless they advance to one distance
 * away from the target.
 * @author Matt Latura
 *
 */
public class Advance implements Command{

	/**
	 * Attempt to advance on the Character specified. If the Character is not in the same
	 * Area as the requester, alert the Client that there is nobody in the Area. Otherwise, 
	 * advance closer to the Character and alert the Client that they have advanced successfully.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		Player mover = cm.getPlayer();
		Character target = w.getArea(mover.getLocation()).hasCharacter(message);
		if(target != null){
			w.advance(mover, target);
		}
		else{
			w.sendNotification(new Notification(mover, "There is no one here by that name", false));
		}
		
	}

}
