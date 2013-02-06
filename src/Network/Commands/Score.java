package Network.Commands;
import GameModel.Player;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Score command is executed when a Client wishes to see their available stats.
 * Stats include things such as health, strength, dexterity, precision, armor and more.
 * The Score Command does not take any arguments, so there is no need to check it's data;
 * simply send a note to the Client who requested their Score.
 * @author Michael Hogue and Chris Conway
 *
 */
public class Score implements Command {

	/**
	 * The execute method will get the Client's Player and build a String that contains
	 * all of the Player's statistics. After compiling the String, send a Note to the Client
	 * with the data. 
	 */
	public void execute(Server s, String message, ClientManager cm) {
		Player player = cm.getPlayer();
		String stats = "";
		stats+= "Player name: " + player.getName() + "\n";
		String health = "Health: " + player.getCurrentHealth() + "/" + player.getMaxHealth() + "\n";
		
		stats += health;
		stats +="Strength: " + player.strength + "\n";
		stats +="Dexterity: " + player.dexterity + "\n";
		stats +="Armor: " + player.armor + "\n";
		stats +="Precision: " + player.precision;
		
		cm.send(new Note(stats, false));
	}

}
