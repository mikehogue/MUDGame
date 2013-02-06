package Network.Commands;

import java.util.Iterator;
import GameModel.Notification;
import GameModel.Player;
import GameModel.World;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * An OOC Command means that the Client wishes to send a chat message to all clients that
 * are connected to the server. After making sure that the arguments are valid, send
 * the note to every player in the game.
 * @author Michael Hogue and Matt Latura
 */
public class Ooc implements Command {

	/**
	 * The execute command will search through all of the Players in the World.
	 * As it iterates through the players, it sends a Notification to each Player.
	 * If the message is blank or only consists of whitespace, the sender is alerted
	 * to insert a message.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		Iterator<Player> i = w.getAllPlayers();
		if (!message.equals("") && message.trim().length() != 0) {
			w.sendNotification(new Notification(i, "(ooc) " + cm.getPlayer().getName() + ": " + message, true));
		} else {
			w.sendNotification(new Notification(cm.getPlayer(), "Please enter a message to send to all players after ooc.", true));	
		}
	}


}
