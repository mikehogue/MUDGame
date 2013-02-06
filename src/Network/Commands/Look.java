package Network.Commands;

import java.util.Iterator;

import GameModel.Notification;
import GameModel.Player;
import GameModel.World;
import Network.Note;
import Network.Server;
import Network.Server.ClientManager;
import GameModel.Character;

/**
 * One of the Commands a Client can execute.
 * A Look Command is used when the Client wishes to get a description of something.
 * If no argument is provided, the room description is sent back. If an argument is provided,
 * such as an Item or Character, the description of the item or Character is returned.
 * @author Matt Latura
 *
 */
public class Look implements Command {

	/**
	 * If there are no arguments provided, return the room description and alert the other
	 * occupants of the same Area that the Player has looked around the room. If an argument is
	 * provided, return the description of the argument, unless there is nothing in the Area that
	 * has the same name as the argument.
	 */
	public void execute(Server s, String message, ClientManager cm) {
		World w = s.getWorld();
		Player p = cm.getPlayer();
		Iterator<Player> i = w.getArea(p.getLocation()).getAllPlayersExcept(p);
		String otherPlayersSee;
		if(message.equals("")){
			otherPlayersSee = p.getName() + " takes a quick glance around the room";
			cm.send(new Note(w.getArea(cm.getPlayer().getLocation()).getDescription(cm.getPlayer()), false));
			w.sendNotification(new Notification(i, otherPlayersSee, false));
		}
		else{
			Note note = new Note("There is nothing by that name here");
			if(p.hasItem(message) != null){
				note.setMessage(p.hasItem(message).getDescription());
			}
			else if(w.getArea(p.getLocation()).hasItem(message) != null){
				note.setMessage(w.getArea(p.getLocation()).hasItem(message).getDescription());
			}
			else if(w.getArea(p.getLocation()).hasCharacter(message) != null){
				Character ch = w.getArea(p.getLocation()).hasCharacter(message);
				if(ch instanceof Player){
					note.setMessage(((Player)ch).getDescription());
				}
				else{
					note.setMessage(ch.getDescription());
				}
			}
			cm.send(note);
		}
	}
}
