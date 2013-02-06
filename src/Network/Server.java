package Network;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import GameModel.GameSimulation;
import GameModel.Notification;
import GameModel.Point;
import GameModel.World;
import GameModel.Player;
import Network.Commands.*;

/**
 * The Server is one of the main components of the game. The Server is what runs the entire
 * game in the background. All Clients will connect here and play in the World that exists on this
 * Server. The Server contains the following items:
 * a userList, which contains all of the passwords mapped to a username
 * a clientManagers list, which contains all of the ClientManagers that we can use to send Notes to Clients
 * a players list, which maps the Players to their username
 * a GameSimultaion, which runs all of the Threads of the Mobs and their actions
 * a bannedUsers list, which contains a list of all unallowed Usernames
 * a World which contains all of the Players, Mobs, Items and etc
 * @author Michael Hogue, Matt Latura, Mazen Shihab, and Chris Conway
 *
 */
@SuppressWarnings("serial")
public class Server extends JFrame implements Observer, Serializable {


	private transient ServerSocket serverSocket;
	private transient LinkedBlockingQueue<ClientManager> removal;
	private volatile transient LinkedList<ClientManager> clientManagers;
	private JPanel panel;
	private JTextArea textArea;
	private transient int connectedClients;
	private Server s;
	private Map<String, String> userList;
	private Map<String, Player> players;
	private World world;
	private GameSimulation gs;
	private LinkedList<String> bannedUsers;
	
	/**
	 * The main method which will start the Server. It will attempt to read a server.dat file in the
	 * same location as the Server. If it does successfully read it, it will read the file, and construct
	 * a new Server with the data read from Server.dat. If the file is not found, or there is any sort of
	 * error, construct a new Server with null as the argument, which will create a new World, Userlist,
	 * etc.
	 * @param args
	 * Not used. 
	 */
	public static void main(String[] args) {
		ObjectInputStream ois = null;

		try {
			ois = new ObjectInputStream(new FileInputStream("server.dat"));
			Object o = ois.readObject();
			ois.close();
			ois = null;
			System.gc();
			new Server((Server) o);
		} catch (FileNotFoundException e1) {
			ois = null;
			System.gc();
			new Server(null);
		} catch (IOException e1) {
			ois = null;
			System.gc();
			new Server(null);
		} catch (ClassNotFoundException e) {
			System.exit(0);
		}
	}

	/**
	 * Construct a new Server. The construction will do the following things:
	 * If there was a passed in Server and it is not null, we should do the following things:
	 * 1. Set the this.s to the passed in Server so we can save it later.
	 * 2. Get the world from the passed in Server and set it to this.world
	 * 3. Get the userList from the passed in Server and set it to this.userList
	 * 4. Get the bannedUsers from the passed in Server and set it to this.bannedUsers
	 * 5. Get the players from the passed in Server and set it to this.players
	 * 6. Get the GameSimulation from the passed in Server and set it to this.gs
	 * If there was not a passed in Server, we must construct a new one of each of the previous things.
	 * After constructing/setting these things, we can do the following things:
	 * 1. Add the Server as an Observer of the World
	 * 2. Start the Game Simulation
	 * 3. Set up the GUI
	 * 4. Start listening for connections!
	 * @param s
	 */
	public Server(Server s) {
		
		if (s != null) {
			this.s = s;
			this.world = s.getWorld();
			this.userList = s.getUserList();
			this.bannedUsers = s.getBannedUsers();
			this.players = s.getPlayers();
			this.gs = s.getGs();
		} else {
			this.players = new HashMap<String, Player>();
			this.bannedUsers = new LinkedList<String>();
			userList = new HashMap<String, String>();
			this.world = new World();
			gs = new GameSimulation(world);
			this.s = this;

		}
		gs.start();
		world.addObserver(this);
		new LinkedBlockingDeque<String>();
		clientManagers = new LinkedList<ClientManager>();
		removal = new LinkedBlockingQueue<ClientManager>();

		setUpGUI();
		
		try {
			serverSocket = new ServerSocket(4000);
			this.textArea.append("Server started.\n");
		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "There is already a server running on this port.");
			System.exit(0);
		}
		
		listen();
	}
	
	private void listen() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				//Dont need to do anything
			}

			this.textArea.append("A new client has connected.\n");
			connectedClients++;

			this.setTitle("MUD Server - Connected Clients: " + connectedClients);
			ClientManager client = new ClientManager(socket);
			clientManagers.add(client);
		}
	}
	
	private void setUpGUI() {
		this.textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(textArea);
		chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatScrollPane.setPreferredSize(new Dimension(600, 400));
		this.textArea.setBackground(Color.black);
		this.textArea.setForeground(Color.green);
		this.setTitle("MUD Server - Connected Clients: 0");

		JButton kick = new JButton("Kick");
		kick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String userToKick = JOptionPane
						.showInputDialog("Who do you want to kick?");
				for (ClientManager cm : clientManagers) {
					if (cm.getPlayer().getName().equalsIgnoreCase(userToKick)) {
						cm.send("Kicked");
						try { cm.socket.close(); } catch (IOException e) { }
						
						JOptionPane.showMessageDialog(null, userToKick
								+ " has been kicked.");
						return;
					}
				}

				JOptionPane.showMessageDialog(null, userToKick
						+ " is not in the game.");
			}

		});

		JButton shutdown = new JButton("Shutdown");
		shutdown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				JOptionPane.showMessageDialog(null,
						"Shutting down and saving the game.");
				close();
			}

		});

		JButton reset = new JButton("Reset");
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int response = JOptionPane
						.showConfirmDialog(
								null,
								"Do you wish to reset all clients and the entire world?",
								"Confirm", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE);

				if (response == JOptionPane.YES_OPTION) {
					reset();
				}
			}

		});

		JButton banByName = new JButton("Ban By Name");
		banByName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String userToBan = JOptionPane
						.showInputDialog("What name do you wish to ban? (Re-adding the same user removes them)");
				
				if (!bannedUsers.contains(userToBan)) {
					bannedUsers.add(userToBan);
					JOptionPane.showMessageDialog(null, userToBan + " has been banned.");
				} else { 
					bannedUsers.remove(userToBan);
					JOptionPane.showMessageDialog(null, userToBan + " has had their ban lifted.");
				}
				
			}

		});

		this.panel = new JPanel();
		//this.panel.add(textArea, BorderLayout.CENTER);
		this.panel.add(chatScrollPane);
		this.panel.setBackground(Color.black);
		
		this.add(panel);
		this.setLocation(200, 100);
		this.setSize(640, 480);
		this.setVisible(true);

		this.panel.add(kick);
		this.panel.add(shutdown);
		this.panel.add(reset);
		this.panel.add(banByName);

		this.textArea.append("Waiting for connections...\n");

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});

	}

	private LinkedList<String> getBannedUsers() {
		return bannedUsers;
	}

	private boolean inPlayersRoom(Player p, String name) {
		Iterator<GameModel.Character> characters = world.getArea(
				p.getLocation()).getOccupants();
		while (characters.hasNext()) {
			if (characters.next().getName().equalsIgnoreCase(name)) {
				return true;
			}
		}

		return false;
	}

	private Map<String, String> getUserList() {
		return this.userList;
	}

	private Map<String, Player> getPlayers() {
		return this.players;
	}

	private GameSimulation getGs() {
		return this.gs;
	}
	
	/**
	 * Returns the World that the Server has the game running on. Many Commands that the
	 * Client will execute will need to access the World, and the Constructor will also
	 * need to utilize this.
	 * @return
	 * The World that the Server has the game running on.
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * Split a string up into a String Array that has one word per array index. Used to parse
	 * the commands that the Client sends to the Server.
	 * @param message
	 * The String that needs to be split into a String array.
	 * @return
	 * A String Array containing one word per array index.
	 */
	public String[] getArgs(String message) {
		return message.split(" ");
	}

	/**
	 * A ClientManager provides an easy way to hold a collection of Clients on the Server.
	 * Every Client has a ClientManager, which stores the Socket that they are connected to,
	 * the Player that they are using in the game, and a thread to receive and send messages. 
	 * @author Michael Hogue
	 *
	 */
	public class ClientManager implements Serializable {

		/**
		 * The Socket that the Client has connected to the Server with.
		 */
		public transient Socket socket;
		private transient ObjectInputStream inStream;
		private transient ObjectOutputStream outStream;
		transient Thread getThread;
		private Player player;

		/**
		 * Get the Player that this Client is playing in the World with.
		 * @return
		 * The ClientManager's Player
		 */
		public Player getPlayer() {
			return player;
		}

		private ClientManager(Socket sock) {
			socket = sock;

			try {
				outStream = new ObjectOutputStream(socket.getOutputStream());
				inStream = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			getThread = new Thread(new GetThread());
			getThread.start();
		}

		/**
		 * A GetThread is exactly what it sounds like -- a Thread that has the sole purpose
		 * of trying to receive data until data is received. Whenever data is received, we 
		 * need to parse it to understand what the Client is attempting to do.
		 * @author Michael Hogue
		 *
		 */
		public class GetThread implements Runnable {

			/**
			 * The point of the run method is to be the infinite loop that the GetThread is running
			 * on. It will keep attempting to read data from the InputStream until something has been 
			 * sent.
			 */
			public void run() {

				while (true) {
					try {

						Object message = inStream.readObject();

						if (message instanceof LoginAttempt) {

							LoginAttempt login = (LoginAttempt) message;
							String name = login.getUser();
							if (bannedUsers != null) {
								if (bannedUsers.contains(name)) {
									outStream.writeObject("Banned");
								}
							}

							String pass = login.getPass();

							if (userList.containsKey(name)) {
								if (pass.equals(userList.get(name))) {

									/*
									 * Make sure this player isn't already
									 * playing
									 */
									Iterator<Player> clients = world
											.getAllPlayers();
									while (clients.hasNext()) {
										Player p = clients.next();
										if (p.getName().equals(name)) {
											outStream
													.writeObject("LoggedInAlready");
											try { socket.close(); } catch (IOException e) { }
											return;
										}
									}

									/* User successfully logged in */
									player = players.get(name);

									/*
									 * Make sure they aren't already in the
									 * world...
									 */
									world.getArea(player.getLocation())
											.removeOccupant(player);
									world.addCharacter(player);

									String description = world.getArea(
											player.getLocation())
											.getDescription(player);

									outStream.writeObject(player);
									outStream.writeObject(new Note(description, false));
									outStream.writeObject(player.getInventory());
								} else {
									/* Invalid password */
									outStream.writeObject("LoginFailed");
									return;
								}
							} else {
								/* create user */
								player = new Player(name, pass);
								player.setLocation(new Point(10, 0));
								world.addCharacter(player);

								userList.put(name, pass);
								players.put(name, player);

								String description = world.getArea(
										player.getLocation()).getDescription(player);
								
								outStream.writeObject(player);
								outStream.writeObject(new Note(description, false));
								outStream.writeObject(player.getInventory());
							}
						} else {
							parse((String) message, outStream);
						}

					} catch (IOException e) {

						textArea.append(player.getName()
								+ " has disconnected.\n");
						world.getArea(player.getLocation()).removeOccupant(
								player);

						connectedClients--;
						setTitle("MUD Server - Connected Clients: "
								+ connectedClients);

						try {
							ClientManager.this.socket.close();
							clientManagers.remove(this);
						} catch (IOException e1) {
							e1.printStackTrace();
						}

						/* Alert all players that they disconnected */
						sendToAllClients(player.getName()
									+ " has disconnected.", false);

						break;

					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}

					while (!removal.isEmpty()) {
						clientManagers.remove(removal.poll());
					}
				}
			}
		}

		/**
		 * The parse message is one of the most important methods on the Server. The parse method 
		 * takes a string and attempts to understand what the Client is trying to do. If the first
		 * word is in the CommandList, we should process it. If it isn't in the CommandList, alert the
		 * Client that they must use an appropriate command. If it is in the CommandList, execute
		 * the Command that is associated with it's textual representation in the CommandList.
		 * @param message
		 * The message that needs to be parsed -- contains everything, including the command and all arguments.
		 * @param o
		 * The OutputStream that we can use to communicate with the Client when we need to send them data.
		 */
		public void parse(String message, ObjectOutputStream o) {
			String fullCommand = "";
			String command = "";
			String args = "";

			fullCommand = message.substring(message.indexOf(':') + 2);

			if (fullCommand.indexOf(' ') != -1) {
				command = fullCommand.substring(0, fullCommand.indexOf(' '));
				args = fullCommand.substring(fullCommand.indexOf(' ') + 1);
			} else {
				command = fullCommand;
			}

			try {

				CommandList c = CommandList.valueOf(command.toUpperCase());

				switch (c){ 
				
				/* Chat Commands */
				case OOC: {
					new Ooc().execute(s, args, this);
					break;
				}
				case SAY: {
					new Say().execute(s, args, this);
					break;
				}

					/* Social Commands */
				case GIGGLE: {
					String temp = player.getName() + " giggles";

					if (!args.equals("")) {
						if (inPlayersRoom(player, args)) {
							temp += " at " + args;
						}
					}

					temp += "!";
					new SocialCommand().execute(s, temp, this);
					break;
				}

				case DANCE: {
					new SocialCommand().execute(s, player.getName()
							+ " dances!", this);
					break;
				}

				case HIGHFIVE: {
					String temp = player.getName() + " high fives ";

					if (!args.equals("")) {
						if (inPlayersRoom(player, args)) {
							temp += args + "!";
							new SocialCommand().execute(s, temp,
									this);
						} else {
							o.writeObject(new Note(
									"This player is not in this room to high-five!",
									true));
						}
					} else {
						o.writeObject(new Note(
								"Please supply a player to high-five!", true));

					}

					break;
				}

				case WAVE: {
					String temp = player.getName() + " waves at ";

					if (!args.equals("")) {
						if (inPlayersRoom(player, args)) {
							temp += args + "!";
							new SocialCommand().execute(s, temp,
									this);
						} else {
							o.writeObject(new Note(
									"This player is not in this room to wave at!",
									true));
						}
					} else {
						o.writeObject(new Note(
								"Please supply a player to wave at!", true));
					}

					break;
				}

				case SLAP: {
					String temp = player.getName() + " slaps ";

					if (!args.equals("")) {
						if (inPlayersRoom(player, args)) {
							temp += args + "!";
							new SocialCommand().execute(s, temp,
									this);
						} else {
							o.writeObject(new Note(
									"This player is not in this room to slap!",
									true));
						}
					} else {
						o.writeObject(new Note(
								"Please supply a player to slap!", true));
					}

					break;
				}

					/* Take */
				case GRAB:
				case GET:
				case TAKE: {
					new Take().execute(s, args, this);
					break;
				}

					/* Look */
				case L:
				case LOOK: {

					new Look().execute(s, args, this);
					break;
				}

					/* Inventory */
				case INV:
				case INVENTORY: {
					new Inventory().execute(s, null, this);
					break;
				}

				case D:
				case DROP: {
					new Drop().execute(s, args, this);
					break;
				}

				case USE: {
					new Use().execute(s, args, this);
					break;
				}

				case G:
				case GIVE: {
					new Give().execute(s, args, this);
					break;
				}

					/* Equip */
				case EQ:
				case EQUIP: {
					new Equip().execute(s, args, this);
					break;
				}

					/* Attack */
				case A:
				case ATT:
				case ATTACK: {
					new Attack().execute(s, args, this);
					break;
				}

					/* Move Commands */
				case MOVE:
				case M:
				case GO: {
					new Move().execute(s, args, this);
					break;
				}
				case N: {
					new Move().execute(s, "north", this);
					break;
				}
				case E: {
					new Move().execute(s, "east", this);
					break;
				}
				case S: {
					new Move().execute(s, "south", this);
					break;
				}
				case W: {
					new Move().execute(s, "west", this);
					break;
				}

				case ADV:
				case ADVANCE: {
					new Advance().execute(s, args, this);
					break;
				}

				case RET:
				case RETREAT: {
					new Retreat().execute(s, args, this);
					break;
				}

				case WHO: {
					new Who().execute(s, null, this);
					break;
				}

				case COMMANDS: {
					new Commands().execute(s, null, this);
					break;
				}
				
				case INCREASE: {
					new Increase().execute(s, args, this);
					break;
				}

				case QUIT: {
					new Quit().execute(s, null, this);
					break;
				}

				case TELL: {
					new Tell().execute(s, args, this);
					break;
				}

				case SCORE: {
					new Score().execute(s, args, this);
					break;
				}

					/* Shutdown -- password protected */
				case SHUTDOWN: {
					if (args.equals("abc123")) {
						s.close();
					} else {
						o.writeObject(new Note("You do not have permission!",
								false));
					}
					break;
				}

					/* Command not found */
				default:
					/* Exception will be thrown... we will just handle it there */
				}
			} catch (IOException e) {
			} catch (IllegalArgumentException e) {
				try {
					o.writeObject(new Note(
							"Your command was not recognized; please try again.",
							false));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

			textArea.append(message + "\n");
			//textArea.setCaretPosition(textArea.getDocument().getLength());

		}

		/** 
		 * Used to send data from the Server to the Client. Examples can be anything from their Player
		 * to update statistics, their Inventory to update the inventory panel, or a Note from another
		 * Player.
		 * @param message
		 * The Object that we are sending to the Client.
		 */
		synchronized public void send(Object message) {
			try {
				outStream.reset();
				outStream.writeObject(message);
			} catch (IOException e) {
				removal.add(this);
			}
		}

	}

	/** 
	 * The update method will handle data that has been sent to the Server from the World.
	 * Because the World is constantly Changing, they World needs a way to send the Server information
	 * when important things happen, such as Notifications to send to Players or a Player so
	 * they can be removed from the Server if they have died.
	 * @param arg0
	 * The Observable Sender who sent us this notification.
	 * @param arg1
	 * The message that we have been updated with.
	 */
	public void update(Observable arg0, Object arg1) {
		if (arg1 instanceof Notification) {
			Iterator<Player> playersToNotify = ((Notification) arg1)
					.getPlayersToNotify();
			while (playersToNotify.hasNext()) {
				Player checkPlayer = playersToNotify.next();
				for (int i = 0; i < clientManagers.size(); i++) {
					if (checkPlayer == clientManagers.get(i).getPlayer()) {
						clientManagers.get(i).send(((Notification) arg1).getNote());
					}
				}
			}
		} else if (arg1 instanceof Player) {
			Player p = (Player)arg1;
			for (int i = 0; i < clientManagers.size(); i++) {
				if (clientManagers.get(i).getPlayer() == p) {
						clientManagers.get(i).send(p);
						if(p.isDead()){
							clientManagers.remove(this);
							players.remove(p);
							userList.remove(p.getName());
						}
				}
			}
		}

	}
	
	private void reset() {

		sendAlert("ServerReset");
		
		new File("server.dat").delete();
		this.players = new HashMap<String, Player>();
		this.bannedUsers = new LinkedList<String>();
		userList = new HashMap<String, String>();
		this.world = new World();
		gs = new GameSimulation(world);
		this.s = this;
		
		gs.start();
		world.addObserver(this);
		new LinkedBlockingDeque<String>();
		clientManagers = new LinkedList<ClientManager>();
		removal = new LinkedBlockingQueue<ClientManager>();
	}

	private void close() {
		
		sendAlert("ServerShutdown");
		
		ObjectOutputStream oos;
		try {

				Iterator<Player> ps = world.getAllPlayers();
				while (ps.hasNext()) {
					Player p = ps.next();
					world.getArea(p.getLocation()).removeOccupant(p);
				}

				oos = new ObjectOutputStream(new FileOutputStream("server.dat"));
				oos.writeObject(this);
				oos.close();
				System.exit(0);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	private void sendAlert(String message) {
		for (ClientManager manager : clientManagers) {
			manager.send(message);
			try { manager.socket.close(); } catch (IOException e) { }
		}
	}
	
	private void sendToAllClients(String message, boolean isChat) {
		for (ClientManager manager : clientManagers)
			manager.send(new Note(message, isChat));
	}
}
