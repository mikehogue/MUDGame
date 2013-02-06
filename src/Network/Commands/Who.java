package Network.Commands;

import java.util.Iterator;

import GameModel.Player;
import GameModel.World;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Who Command is a Command that a Client enters when they wish to know all of the Players
 * that are currently connected to the game. The Server will compile a list of the connected
 * Players and then return it to the Client's chat box with a Note.
 * @author Michael Hogue
 *
 */
public class Who implements Command{

	/** 
	 * Execute a command which will alert the Client of all of the logged in Players. 
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		String p_string = "";
		
		Iterator<Player> players = w.getAllPlayers();
		while (players.hasNext()) {
			Player p = players.next();
			p_string += p.getName() + ", ";
		}
		
		if (p_string.indexOf(',') != -1) {
			p_string = p_string.substring(0, p_string.length() -2 );
		}
		
		cm.send(new Note("Players online: " + p_string, true));
		
		
	}

}
