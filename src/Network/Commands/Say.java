package Network.Commands;

import GameModel.World;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Say Command means that the CLient wishes to send a Note to all Players that are in the
 * same Area as them. After making sure that the Client has input the correct arguments,
 * send the note to every player in the room. If the arguments are invalid, send a Note
 * back to the sender and alert them to enter a message. 
 * @author Michael Hogue and Matt Latura. 
 *
 */
public class Say implements Command{

	/**
	 * The execute method will make sure that the message is not empty or only containing whitespace.
	 * If it does not meet the criteria, alert the sender that they must enter a message to send to 
	 * the room's occupants. If the criteria is met, send a Notification to every Player that
	 * has the same location as the sender.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		if (!message.equals("") && message.trim().length() != 0) {
			w.say(cm.getPlayer(), message, false);
		} else {
			cm.send(new Note("You must enter text to say to the room!", true));
		}
	}

}
