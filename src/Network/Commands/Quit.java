package Network.Commands;

import java.io.IOException;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Quit Command is a command that a Client can enter when he wishes to quit the game.
 * Upon sending it, the socket that they were connected to is closed and the user will return
 * to the Title Screen, where they can exit or log back in.
 * @author Michael Hogue
 */
public class Quit implements Command {

	@Override
	public void execute(Server s, String message, ClientManager cm) {
		cm.send(new Note("Logout", false));
		try {
			cm.socket.close();
		} catch (IOException e) {
			//Nothing we can do there...
		}
	}

}
