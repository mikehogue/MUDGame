package Network;

import java.io.Serializable;

/**
 * A Note is an object with two fields:
 * A string that holds a message that the server wishes to send to the client. 
 * A boolean which lets the client know if it is a chat message or a game message.
 * A Note is the most common thing that a client will receive from the server. 
 * @author Michael Hogue and Matt Latura 
 *
 */
public class Note implements Serializable{

	private static final long serialVersionUID = 1L;
	private String message;
	private boolean isChatMessage;
	
	/**
	 * Default constructor with no arguments. A note with this constructor is 
	 * defaulted to be not a chat message, as these Notes are sent more often.
	 * @param message
	 * The message that the server wishes to tell a specific client.
	 */
	public Note(String message){
		this.message = message;
		isChatMessage = false;
	}

	/** 
	 * Construct a Note with a boolean so we can mark it as a chat message. 
	 * @param message
	 * The message that the server wishes to tell a specific client.
	 * @param isChatMessage
	 * If true, it will mark it as a chat message, and will go into the clients chat box. 
	 * If false, it will mark it as a server message, and it will go into the clients server box.
	 */
	public Note(String message, boolean isChatMessage){
		this(message);
		this.isChatMessage = isChatMessage;
	}
	
	/**
	 * Get the message associated with this Note that the Server needs to send.
	 * @return
	 * The String that the Note is attempting to send to the client.
	 */
	public String getMessage(){
		return message;
	}
	
	/**
	 * Change the message associated with this Note.
	 * @param newMessage
	 * The new message that this Note will contain. 
	 */
	public void setMessage(String newMessage){
		message = newMessage;
	}
	
	/**
	 * Check to see if the Note is a chat message or a server message.
	 * @return
	 * If the Note is a chat message or a server message.
	 */
	public boolean isChatMessage(){
		return isChatMessage;
	}
	
	/**
	 * Change the Note to either be a chat message or a server message depending on the boolean.
	 * @param isChatMessage
	 * The value to set if this is a chat message.
	 * If true, the Note is a chat message.
	 * If false, the Note is a server message.
	 */
	public void isChatMessage(boolean isChatMessage){
		this.isChatMessage = isChatMessage;
	}
	
}


