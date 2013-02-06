package Network;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JOptionPane;

import View.MainFrame;
import View.Views;

import GameModel.Item;
import GameModel.Player;

/**
 * A Client is used to connect to the server. The client will receive and send data from
 * the user to the game server. It processes everything and sends it to it's observer,
 * MUDView. Whenever Client receives anything, it parses it and lets MUDView know what to do.
 * @author Michael Hogue
 *
 */
public class Client extends Observable{

	private ObjectInputStream read;
	private ObjectOutputStream write;
	private Thread receivingThread;
	private String clientName;
	private boolean isConnected = false;
	private MainFrame mf;
	private boolean hasQuit = false;
	private Socket socket;
	
	/**
	 * Construct a new Client and attempt to connect to the server. If connected, a LoginAttempt is sent 
	 * immediately. A thread will be started to receive data.
	 * @param clientName - A string representation of the clients attempted username for login.
	 * @param password - A string representation of the clients attempted password for login.
	 * @param serverIP - The attempted IP of the server that the client wishes to connect to.
	 * @param serverPort - The attempted port on the server that the client wishes to connect to.
	 * @param mf - The clients MainFrame -- will be used to change views. 
	 * 
	 */
	public Client(String clientName, String password, String serverIP, Integer serverPort, MainFrame mf) {
		this.mf = mf;
		this.clientName = clientName;
		this.hasQuit = false; 
		try {
			
			socket = new Socket();
		    SocketAddress sockaddr = new InetSocketAddress(serverIP, serverPort);
			socket.connect(sockaddr);
		    

			write = new ObjectOutputStream(socket.getOutputStream());
			read = new ObjectInputStream(socket.getInputStream());
			LoginAttempt login = new LoginAttempt(clientName, password);
			write.writeObject(login);
			isConnected = true;
		
		} catch (UnknownHostException e) {
		
			JOptionPane.showMessageDialog(null, "The requested host is unknown. Please retry later or try with a different server.", "Host Unreachable!", JOptionPane.ERROR_MESSAGE);
			setChanged();
			notifyObservers("Host not found");
			
		} catch (IOException e) {
			
			JOptionPane.showMessageDialog(null, "There was an error connecting. It seems like the server may not be running.", "IO Error!", JOptionPane.ERROR_MESSAGE);
			setChanged();
			notifyObservers("Unable to connect to server!");
		}
		
		receivingThread = new Thread(new ReceiveThread());
		receivingThread.start();
	}
	
	/**
	 * Check if the client is connected to a server. Used to know whether or not the MUDView
	 * should be displayed, and in the ReceiveThread to avoid errors. 
	 * @return
	 * The clients connected state to the server. 
	 */
	public boolean isConnected() {
		return isConnected;
	}

	private class ReceiveThread implements Runnable {

		@SuppressWarnings("unchecked")
		@Override
		
		public void run() {
			
			while (true) {
				if (isConnected) {
					try {
						Object getData = read.readObject();
						/* We will handle the LoginFailed and LoggedInAlready here so we 
						 * can just go back to the titlescreen ASAP
						 */
						if(getData != null){
							if (getData.toString().equals("LoginFailed")) {
								JOptionPane.showMessageDialog(null, "Invalid Password.");
								mf.changeViews(Views.TITLE, null);
							}						
							else if (getData.toString().equals("ServerShutdown")) {
								hasQuit = true; 
								JOptionPane.showMessageDialog(null, "The server has been shutdown by an administrator. Please login later.");
								mf.changeViews(Views.TITLE, null);
							}
							else if (getData.toString().equals("ServerReset")) {
								hasQuit = true;
								JOptionPane.showMessageDialog(null, "The server has been reset by an administrator. All game data has been erased and you must start new.");
								mf.changeViews(Views.TITLE, null);
							}
							else if (getData.toString().equals("Banned")) {
								JOptionPane.showMessageDialog(null, "You are banned from logging in!");
								hasQuit = true;
								mf.changeViews(Views.TITLE, null);
							}						
							else if (getData.toString().equals("Kicked")) {
								JOptionPane.showMessageDialog(null, "You have been kicked!");
								hasQuit = true;
								mf.changeViews(Views.TITLE, null);
							}						
							else if (getData.toString().equals("LoggedInAlready")) {
								JOptionPane.showMessageDialog(null, "Sorry, this user is already logged in.");
								mf.changeViews(Views.TITLE, null);
							}							
							else if(getData instanceof Player){
								Player p = (Player)getData;
								if(p.isDead()){
									JOptionPane.showMessageDialog(null, "You have been slain!");
									hasQuit = true;
									socket.close();
									mf.changeViews(Views.TITLE, null);
								}
								else{
									setChanged();
									notifyObservers((Player) getData);
								}
								
							}
							else if(getData instanceof Note){
								Note note = (Note)getData;
								setChanged();
								notifyObservers(note);
							} else if (getData instanceof ArrayList) {
								setChanged();
								notifyObservers((ArrayList<Item>) getData);
							}
							else{
								setChanged();
								notifyObservers((String) getData.toString() + "\n");
							}
						}						
					} 
					catch (IOException e) {
						if (!hasQuit)
							JOptionPane.showMessageDialog(null,	"The connection with the server has been interrupted. Please try to reconnect.");
						
						isConnected = false;
						try {
							socket.close();
						} catch (IOException e1) {
							
						}
						
						mf.changeViews(Views.TITLE, null);
					} catch (ClassNotFoundException e) {
						JOptionPane.showMessageDialog(null, "There was a critical error with the application and it needs to be ended.", "Class not found!", JOptionPane.ERROR_MESSAGE);
						System.exit(0);
					}
				} 
			} //end of while loop
			
		} //end of run class
	}
	
	/**
	 * Send data to the server. Since the user can only type messages, only Strings
	 * will be sent to the server. The server will parse the string to figure out
	 * exactly what this client is attempting to do. 
	 * @param sendData
	 * The message that the server will receive from the client. The message is always 
	 * preceded by the clients name, a colon and a space. 
	 */
	public void writeObject(String sendData) {
		try {
			if (sendData.equals("quit")) {
				hasQuit = true; 
			}
			write.writeObject(clientName + ": " + sendData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

