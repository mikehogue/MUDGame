package Network;

import java.io.Serializable;

/**
 * A LoginAttempt is what a Client sends when attempting to login to the server.
 * If the LoginAttempt is successful, they will be granted access to the game. If not,
 * then the game will alert them what is wrong. Possible problems include an incorrect password
 * or a banned username. 
 * @author Michael Hogue
 *
 */
@SuppressWarnings("serial")
public class LoginAttempt implements Serializable {
	private String user;
	private String password;
	
	/**
	 * Constructs a LoginAttempt so it can be sent to the server. 
	 * @param user 
	 * The username that the client is trying to login to.
	 * @param password
	 * The password of the username that the client wishes to login to.
	 */
	public LoginAttempt(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	/**
	 * Retrieve the attempted login username.
	 * @return
	 * The username that this LoginAttempt is associated with.
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * Retrieve the attempted login password.
	 * @return
	 * The password associated with this LoginAttempt.
	 */
	public String getPass() {
		return password;
	}
}
