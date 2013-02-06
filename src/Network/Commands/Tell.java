package Network.Commands;

import java.util.Iterator;

import GameModel.Notification;
import GameModel.Player;
import GameModel.World;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;

/**
 * One of the commands that a Client can execute.
 * A Tell Command means that the Client wishes to send a Note to a specific Player. Only the Player
 * specified should receive the message, and they should be able to know that it was sent by a Tell
 * Command by the prefix "(Tell) ". We must first make sure that the arguments are proper and that the 
 * Player is logged in. 
 * @author Michael Hogue and Matt Latura
 *
 */
public class Tell implements Command {

	/**
	 * The execute function will make sure that the Player who is receiving the message exists
	 * and that the arguments are properly there. After making sure of this, alert the Player that
	 * they have received a Message, and also alert the sender so they know the message has been sent.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		
		String[] args = s.getArgs(message);
		String receiversName = args[0];
		String tellMessage = "(Tell) " + cm.getPlayer().getName() + ": ";
		
		if (receiversName == "") {
			cm.send(new Note("You must enter a receiver to send the note to.", true));
			return;
		}
		if (args.length == 1){
			cm.send(new Note("You must enter a message to tell " + receiversName + ".", true));
			return;
		}
		
		for (int i = 1; i < args.length; i++) {
			tellMessage += args[i] + " ";
		}
	
		
		World w = s.getWorld();
		if(!cm.getPlayer().getName().toLowerCase().equals(receiversName.toLowerCase())){
				Iterator<Player> i = w.getAllPlayers();
				boolean tellSent = false;
				while(i.hasNext()){
					Player p = i.next();
					if(p.getName().toLowerCase().equals(receiversName.toLowerCase())){
						w.sendNotification(new Notification(p, tellMessage, true));
						w.sendNotification(new Notification(cm.getPlayer(), tellMessage, true));
						tellSent = true;
					}
				}
				if(tellSent == false){
					w.sendNotification(new Notification(cm.getPlayer(), "There is no player online with that name", false));
				}
		}
		else{
			w.sendNotification(new Notification(cm.getPlayer(), "You can't send a tell to yourself", false));
		}
	}

}
