package Network.Commands;

import GameModel.Player;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * An Increase Command is executed after a Player has reached 1000 experience points.
 * After reaching 1000 experience points, they are given three Increases which they can
 * use on any of the folowing options: Strength, Dexterity, Health, and Precision. 
 * @author Matt Latura
 */
public class Increase implements Command{

	/**
	 * Execute the Increase command. If the Player has available stat increases, check for
	 * a correct argument. If the argument is correct, increase the stat and alert the Player
	 * that it has been increased. If the argument is wrong, alert them what the possible
	 * options are. If the Player does not have available stat increases, alert them that they
	 * do not have enough and give them information on how to get them.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		Player p = cm.getPlayer();
		if(p.getStatIncreases() >= 1){
			if(message.toLowerCase().equals("strength") || message.toLowerCase().equals("stre")){
				p.increaseStrength();
				cm.send(new Note("Is that a new muscle you've never seen before?\nYou now have " + p.getStatIncreases() + " stat increases remaining", false));
				cm.send(p);
			}
			else if(message.toLowerCase().equals("dexterity") || message.toLowerCase().equals("dex")){
				p.increaseDexterity();
				cm.send(new Note("Float like a butterfly, sting like a.... butterfly?\nYou now have " + p.getStatIncreases() + " stat increases remaining", false));
				cm.send(p);
			}
			else if(message.toLowerCase().equals("health") || message.toLowerCase().equals("max health")){
				p.increaseHealth();
				cm.send(new Note("All those chewy vitamins are really paying off!\nYou now have " + p.getStatIncreases() + " stat increases remaining", false));
				cm.send(p);
			}
			else if(message.toLowerCase().equals("precision") || message.toLowerCase().equals("pre")){
				p.increasePrecision();
				cm.send(new Note("The barn had better watch it's backside!\nYou now have " + p.getStatIncreases() + " stat increases remaining", false));
				cm.send(p);
			}
			else{
				cm.send(new Note("That is not a recognized base stat. You can increase Health, Strength, Dexterity, or Precision", false));
			}
		}
		else{
			cm.send(new Note("You don't have any stat increases, kill some more mobs to earn them", false));
		}
		
		
	}

}
