package Network.Commands;

import Network.Server;
import Network.Server.ClientManager;

/**
 * A command is something that the client is attempting to do on the server.
 * All possible commands are held in the CommandList. To make things simpler, 
 * all commands implement the same method with the same arguments, so they all implement
 * Command.
 * @author Michael Hogue
 */
public interface Command {
	
	/**
	 * The execute method is the default method for all of the commands in the CommandList.
	 * The execute method will invoke some action on the server, whether it be chatting with other 
	 * users, attacking a Mob, using a key, or using social commands.
	 * @param s
	 * The server that the Command is associated with. The server is sent because it contains
	 * everything from the world and anything else that the method may need.
	 * @param message
	 * The arguments that have been sent to this command. Not every command needs arguments, but
	 * many do. For example, if a client typed give iron sword Hero, the message argument would be
	 * iron sword Hero. The command should know how to parse these.
	 * @param cm
	 * The ClientManager that has executed this command. ClientManagers hold players and can allow
	 * the server to communicate with the client directly from the command. 
	 */
	public abstract void execute(Server s, String message, ClientManager cm);
	
}
