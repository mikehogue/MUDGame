package GameModel;

import java.util.ArrayList;
import java.util.Iterator;

import Network.Note;

/**
 * Notifications are used to send Note objects to specific Players. They can be sent to any number of Players
 * 
 * @author Matthew Latura
 */
public class Notification {	
	//An Iterator of the players that need to be notified
	private Iterator<Player> toBeNotified;
	
	//The String to be printed in a user's GUI
	private Note note;
	
	/**
	 * The default constructor for Notification takes a Player Iterator parameter and sets it to the toBeNotified field.
	 * The Notification's note is has a default message and is not a chat message
	 * @param tBN - The Player Iterator contaning the Players to send the note
	 */
	public Notification(Iterator<Player> tBN){
		note = new Note("Nothing has changed", false);
		toBeNotified = tBN;
	}
	
	
	/**
	 * A more powerful constructor takes a Player Iterator containing the Players to send the note to as well as a String
	 * to set as the Note's message
	 * @param tBN - The Player Iterator containing the Players to send the Note to
	 * @param note - The string to set as the Note's message
	 */
	public Notification(Iterator<Player> tBN, String note){
		this(tBN);
		this.note.setMessage(note);
	}
	
	/**
	 * A more powerful constructor takes a Player Iterator containing the Players to send the note to as well as a String
	 * to set as the Note's message and a boolean as to whether the note is a chat message or not.
	 * @param tBN - The Player Iterator containing the Players to send the Note to
	 * @param note - The string to set as the Note's message
	 * @param isChat - Whether or not the note is a chat message
	 */
	public Notification(Iterator<Player> tBN, String note, boolean isChat){
		this(tBN, note);
		this.note.isChatMessage(isChat);
	}
	
	/**
	 * This constructor takes a single Player as a parameter rather than an iterator holding multiple Players. It also allows 
	 * you to set the message and whether or not it is a chat message
	 * @param playerToRecieve - The Player to receive the Note
	 * @param message - The string to set as the Note's message
	 * @param isChat - Whether or not the note is a chat message
	 */
	public Notification(Player playerToRecieve, String message, boolean isChat){
		ArrayList<Player> temp = new ArrayList<Player>();
		temp.add(playerToRecieve);
		toBeNotified = temp.iterator();
		this.note = new Note(message, isChat);
	}
	
	/**
	 * A minimal constructor that takes a single Player parameter to send the Note to and a boolean that determines
	 * whether or not the Note is a chat message. The Note's message is set to an empty String. 
	 * @param player - Player to send the note to
	 * @param b - Whether the note is a chat message
	 */
	public Notification(Player player, boolean b) {
		this(player, "", b);		
	}


	/**
	 * Returns the message of the Note contained in this Notification
	 * @return the message of the Note contained in this Notification
	 */
	public String getString(){
		return note.getMessage();
	}
	
	
	/**
	 * Returns the Note object contained by this Notification
	 * @return the Note object contained by this Notification
	 */
	public Note getNote(){
		return note;
	}
	
	/**
	 * Sets the Note object contained by this Notification
	 * @param note - the Note object that will be contained by this Notificaiton
	 */
	public void setNote(Note note){
		this.note = note;
	}
	
	
	/**
	 * Sets the message held by the Note object in this Notification
	 * @param message - the message to be held by the Note object in this Notification
	 */
	public void setString(String message){
		this.note.setMessage(message);
	}
	
	/**
	 * Returns the iterator of Players that will be sent the Note
	 * @return The iterator of Players that will be sent the Note
	 */
	public Iterator<Player> getPlayersToNotify(){
		return toBeNotified;
	}
	
	/**
	 * Returns whether or not this Notification's Note is a chat message
	 * @return whether or not this Notification's Note is a chat message
	 */
	public boolean isChatMessage(){
		return note.isChatMessage();
	}
}
