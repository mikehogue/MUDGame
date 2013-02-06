package Network.Commands;

import GameModel.World;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A SocialCommand is a Command that the Client can execute to alert the Players
 * in the room as them that they are doing something. The command is flexible enough
 * that any action can be implemented without the creation of a separate class. 
 * Using the Say Command's framework to send a Note to all of the occupants of an Area,
 * we can simply pass an argument that it is a SocialCommand so the String does not begin with
 * the Player's name followed by a colon. For example, if we wanted to send all of the occupants
 * of an Area a message that you were crying, we could simply pass "[PlayerName] cries!" as the message
 * argument. 
 * @author Michael Hogue
 */
public class SocialCommand implements Command{

	/**
	 * The execute command is executed whenever a SocialCommand should take place.
	 * If the SocialCommand is executing, we know that the optional Character argument that has been
	 * passed to the SocialCommand, such as highfive Player1, where Player1 is the Character's name argument,
	 * we know that Player1 is actually in the same room as the sender. Because of this, once we have
	 * reached this point, we can simply execute a Say Command and pass it true for the isSocialCommand
	 * argument.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		w.say(cm.getPlayer(), message, true);
	}

}
