package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import GameModel.Armor;
import GameModel.Bomb;
import GameModel.Consumable;
import GameModel.Item;
import GameModel.Player;
import GameModel.Weapon;
import Network.Client;
import Network.Note;
import Network.Commands.CommandList;

/**
 * The main view for the MUD Game. Displays the server text box, chat text box, entry boxes
 * for both, stat/inventory pane, and minimap, and updates based on data written from the
 * server.
 * 
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class MUDView extends JPanel implements Observer {
	
	private JTextArea chatArea = new JTextArea();
	private JTextArea serverArea = new JTextArea(); 
	private static JTextField commandTextField = new JTextField();
	private static JTextField chatTextField = new JTextField();
	private JButton commandSend = new JButton("Send");
	private JButton chatSend = new JButton("Send");
	private JTabbedPane statPanel;
	private JPanel stats;
	private JTextArea invArea = new JTextArea();
	private MapPanel mapPanel;
	private volatile static Client client;
	private MapSheet map;
	
	/**
	 * Constructs a new MUDView, setting up each panel in the view so that update
	 * can fill in their contents.
	 * 
	 * @param mf
	 * 			The MainFrame in which the MUDView will be displayed.
	 * @param cl
	 * 			The client which is writing to and recieving from this MUDView.
	 */
	public MUDView(Client cl) {

		cl.addObserver(this);
		MUDView.client = cl;
		
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		chatArea.setEditable(false);
		JScrollPane chatScrollPane = new JScrollPane(chatArea);
		chatScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		chatScrollPane.setPreferredSize(new Dimension(200, 350));
		
		commandTextField.setPreferredSize(new Dimension(390, 25));
		commandTextField.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
		
		chatTextField.setPreferredSize(new Dimension(130, 25));
		chatTextField.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
		
		serverArea.setLineWrap(true);
		serverArea.setWrapStyleWord(true);
		serverArea.setEditable(false);
		JScrollPane serverScrollPane = new JScrollPane(serverArea);
		serverScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		serverScrollPane.setPreferredSize(new Dimension(460, 530));
		commandTextField.addKeyListener(new CommandEnterListener());
		chatTextField.addKeyListener(new ChatEnterListener());
		
		commandSend.addActionListener(new commandSendListener());
		chatSend.addActionListener(new chatSendListener());
		
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//Server text box
		c.anchor = GridBagConstraints.PAGE_START;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 0;
		c.gridheight = 2;
		c.gridwidth = 1;
		c.insets = new Insets(2, 0, 5, 0);
		this.add(serverScrollPane, c);
		
		//Chat text box
		c.anchor = GridBagConstraints.LAST_LINE_START;
		c.gridx = 0;
		c.gridy = 1;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(5, 0, 5, 5);
		this.add(chatScrollPane, c);
		
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 1;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0, 0, 0, 0);
		this.add(commandTextField, c);

		c.anchor = GridBagConstraints.SOUTHEAST;
		this.add(commandSend, c);
		
		c.anchor = GridBagConstraints.SOUTHWEST;
		c.gridx = 0;
		c.gridy = 2;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.insets = new Insets(0,0,0,0);
		this.add(chatTextField, c);
		
		c.anchor = GridBagConstraints.SOUTHEAST;
		c.insets = new Insets(0, 0, 0, 5);
		this.add(chatSend, c);
		
		setUpStatPanel();
		
		c.gridx = 2;
		c.gridy = 1;
		c.gridheight = 2;
		c.gridwidth = 1;
		c.insets = new Insets(5,5,0,5);
		this.add(statPanel, c);
		
		setUpMapPanel();
		
		c.gridy = 0;
		c.gridheight = 1;
		c.insets = new Insets(0,5,0,5);
		this.add(mapPanel, c);
	}
	
	/**
	 * Sets up the tabbed pane containing the stat and inventory panels.
	 */
	private void setUpStatPanel()	{
		statPanel = new JTabbedPane();
		statPanel.setPreferredSize(new Dimension(200, 380));
		
		stats = new JPanel(new BorderLayout());
		stats.setBackground(Color.WHITE);
		stats.setPreferredSize(new Dimension(200, 380));
		stats.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
		
		invArea.setEditable(false);		

		JScrollPane inventory = new JScrollPane(invArea);
		inventory.setBackground(Color.WHITE);
		inventory.setPreferredSize(new Dimension(200, 380));
		inventory.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
		inventory.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		inventory.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		statPanel.add("Statistics", stats);
		statPanel.add("Inventory", inventory);
	}
	
	/**
	 * A JPanel which holds a buffered image of a room to display on the MiniMap.
	 * @author Chris
	 *
	 */
	private class MapPanel extends JPanel	{
		
		private BufferedImage roomImage;
		
		/**
		 * Creates a 200x200 gray MapPanel with a BorderLayout and black border.
		 */
		public MapPanel()	{
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(200,200));
			this.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.black));
			this.setBackground(Color.gray);
			this.roomImage = null;
		}
		
		/**
		 * Paints the current roomImage to the MapPanel. Called whenever the roomImage
		 * changes.
		 */
		public void paintComponent(Graphics g)	{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			if (roomImage != null)
				g2.drawImage(roomImage, null, 10, 10);
		}

		/**
		 * Sets the MapPanel to display the given BufferedImage.
		 * @param roomImage
		 * 			The BufferedImage to be displayed.
		 */
		public void setRoomImage(BufferedImage roomImage)
		{
			this.roomImage = roomImage;
			repaint();
		}
	}
	
	/**
	 * Prepares the Map Panel.
	 */
	private void setUpMapPanel()	{
		mapPanel = new MapPanel();
		map = new MapSheet();
	}
	
	/**
	 * KeyListener for the server command text box.
	 */
	private class CommandEnterListener implements KeyListener{

		public void keyPressed(KeyEvent arg0) {
			int keyCode = arg0.getKeyCode();
			if(keyCode == KeyEvent.VK_ENTER){
				if (!commandTextField.getText().isEmpty()) {
					MUDView.client.writeObject(commandTextField.getText());
					MUDView.commandTextField.setText("");
				}					
			}
			
		}

		public void keyReleased(KeyEvent arg0) { }
		public void keyTyped(KeyEvent arg0) { }
		
	}
	
	private static boolean contains(String test) {

	    for (CommandList c : CommandList.values()) {
	        if (c.name().equals(test)) {
	            return true;
	        }
	    }

	    return false;
	}
	
	/**
	 * KeyListener for the chat text box.
	 *
	 */
	private class ChatEnterListener implements KeyListener{

		public void keyPressed(KeyEvent arg0) {
			int keyCode = arg0.getKeyCode();
			if(keyCode == KeyEvent.VK_ENTER){
				if (!chatTextField.getText().isEmpty()) {
					String[] args = chatTextField.getText().split(" ");
					
					if (args[0].equalsIgnoreCase ("ooc") || args[0].equalsIgnoreCase ("tell") || args[0].equalsIgnoreCase ("say") ||args[0].equalsIgnoreCase ("who") || args[0].equalsIgnoreCase ("dance") || args[0].equalsIgnoreCase ("giggle") || args[0].equalsIgnoreCase ("slap") || args[0].equalsIgnoreCase ("wave") || args[0].equalsIgnoreCase ("highfive")) {
						client.writeObject(chatTextField.getText());
						MUDView.chatTextField.setText("");
					} else if (!contains(args[0])) {
						client.writeObject("say " + chatTextField.getText());
						MUDView.chatTextField.setText("");
					} else {
						JOptionPane.showMessageDialog(null, "The only supported commands are OOC, SAY, TELL, WHO, DANCE, SLAP, GIGGLE, WAVE, HIGHFIVE");
					}
				}					
			}
			
		}

		public void keyReleased(KeyEvent arg0) { }
		public void keyTyped(KeyEvent arg0) { }
		
	}
	
	/**
	 * ActionListener for the server command send button.
	 */
	private class commandSendListener implements ActionListener	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!commandTextField.getText().isEmpty()) {
				client.writeObject(commandTextField.getText());
				MUDView.commandTextField.setText("");
			}
		}
	}
	
	/**
	 * ActionListener for the chat command send button.
	 */
	private class chatSendListener implements ActionListener	{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (!chatTextField.getText().isEmpty()) {
				client.writeObject(chatTextField.getText());
				MUDView.chatTextField.setText("");
			}
		}
	}

	/**
	 * Updates the MUDView based on information recieved from the client. If it recieves a player,
	 * than the location or stats have changed. If ir recieves an ArrayList, then the player's
	 * inventory has changed. If it recieves a notification, that notification should be displayed
	 * in the appropriate text window.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (stats == null) {
			while (stats == null) {
				//Make sure whole GUI is constructed
			}
		}
		if (arg1 != null) {
			
				if (arg1 instanceof Player) {
					Player player = (Player)arg1;
					if(player.isDead()){
						
					}
					
					String name = player.getName();
					String health = "Health: " + player.getCurrentHealth() + "/" + player.getMaxHealth() + "\n";
					
					JLabel nameLabel = new JLabel(name);
					nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
					
					JTextArea statArea = new JTextArea();
					statArea.setEditable(false);
					statArea.setOpaque(false);
					
					statArea.append("___________________________\n");
					statArea.append("BASE STATISTICS\n");
					statArea.append(health);
					statArea.append("Strength: " + player.strength + "\n");
					statArea.append("Dexterity: " + player.dexterity + "\n");					
					statArea.append("Precision: " + player.precision + "\n\n");
					
					statArea.append("COMBAT STATISTICS\n");
					statArea.append("Base Damage: " + player.getDamage() + "\n");
					statArea.append("Damage Reduction: " + player.armor + "%\n");
					statArea.append("Attack Delay: " + player.getAttackDelay() + " seconds\n\n");
					
					statArea.append("EXPERIENCE\n");
					statArea.append("Experience Points: " + player.getExperience() + "\n");
					statArea.append("Stat Increases Available: " + player.getStatIncreases());
					//statArea.setForeground(Color.WHITE);
					
					while(stats.getComponentCount() > 0){
						stats.remove(0);
						stats.validate();
					}
					stats.add(nameLabel, BorderLayout.NORTH);
					stats.add(statArea, BorderLayout.CENTER);
					
					statPanel.repaint();
					if(map == null)	{
						map = new MapSheet();
					}
					mapPanel.setRoomImage(map.getRoom(parseRoomLocation(player)));
				} 
				else if(arg1 instanceof ArrayList)	{
					@SuppressWarnings("unchecked")
					ArrayList <Item> list = (ArrayList <Item>) arg1;
					ArrayList <String> consumables = new ArrayList<String>();
					ArrayList <String> weapons = new ArrayList<String>();
					ArrayList <String> armors = new ArrayList<String>();
					for(int i = 0; i < list.size(); i++)	{
						if(list.get(i) instanceof Consumable)	{
							Consumable cons = (Consumable) list.get(i);
							consumables.add(cons.getName());
						}
						else if(list.get(i) instanceof Bomb)	{
							consumables.add("Bomb");
						}
						else if(list.get(i) instanceof Armor)	{
							Armor a = (Armor) list.get(i);
							armors.add(a.getName());
						}
						else if(list.get(i) instanceof Weapon)	{
							Weapon w = (Weapon) list.get(i);
							weapons.add(w.getName());
						}
						else if(list.get(i).getName().compareTo("Glowing Blue Orb") == 0)	{
							consumables.add("Glowing Blue Orb");
						}
						else if(list.get(i).getName().compareTo("Royal Key") == 0)	{
							consumables.add("Royal Key");
						}
						else if(list.get(i).getName().compareTo("Lever Key") == 0)	{
							consumables.add("Lever Key");
						}
					}
					Collections.sort(consumables);
					Collections.sort(weapons);
					Collections.sort(armors);
					
					invArea.setText("");
					
					invArea.append("------------------Weapons------------------\n");
					for(int i = 0; i < weapons.size(); i++)	{
						invArea.append(weapons.get(i));
						invArea.append("\n");
					}
					invArea.append("\n");
					
					invArea.append("--------------------Armor--------------------\n");
					for(int i = 0; i < armors.size(); i++)	{
						invArea.append(armors.get(i));
						invArea.append("\n");
					}
					invArea.append("\n");
					
					invArea.append("--------------------Items---------------------\n");
					for(int i = 0; i < consumables.size(); i++)	{
						invArea.append(consumables.get(i));
						invArea.append("\n");
					}
					invArea.append("\n");
					
					//while(inventory.getComponentCount() > 0)	{
					//	inventory.remove(0);
					//	inventory.validate();
					//}
					
					statPanel.repaint();
				}
				else if (arg1 instanceof Note) {
					Note note = (Note)arg1;
					
					if (!note.isChatMessage()) {
						this.serverArea.append("\n" + note.getMessage() + "\n");
						serverArea.setCaretPosition(serverArea.getDocument().getLength());
					} else {
						this.chatArea.append("\n" + note.getMessage() + "\n");
						chatArea.setCaretPosition(chatArea.getDocument().getLength());
					}
					
				}
				
		}
	}
	
	/**
	 * Takes a player, and parses his location to determine which exits should
	 * be shown on the minimap.
	 * 
	 * @param p
	 * @return A string representation of the exits in the room, in N>E>S>W order.
	 */
	private String parseRoomLocation(Player p)	{
		int x = p.getLocation().getX();
		int y = p.getLocation().getY();
		if((x == 10 && y == 0) || (x == 12 && y == 0) || (x == 9 && y == 2))	{
			return "N";
		}
		else if((x == 9 && y == 1) || (x == 8 && y == 3) || (x == 10 && y == 5) || (x == 9 && y == 8))	{
			return "E";
		}
		else if((x == 9 && y == 4))	{
			return "S";
		}
		else if((x == 12 && y == 5) || (x == 17 && y == 8))	{
			return "W";
		}
		else if((x == 12 && y == 2) || (x == 11 && y == 4) || (x == 11 && y == 6))	{
			return "NS";
		}
		else if((x == 11 && y == 1) || (x == 10 && y == 3) || (x == 13 && y == 8) || (x == 15 && y == 9) || (x == 15 && y == 7))	{
			return "EW";
		}
		else if((x == 10 && y == 7) || (x == 14 && y == 7))	{
			return "NE";
		}
		else if((x == 12 && y == 8) || (x == 14 && y == 9))	{
			return "ES";
		}
		else if((x == 12 && y == 3) || (x == 10 && y == 8) || (x == 16 && y == 9))	{
			return "SW";
		}
		else if((x == 12 && y == 7) || (x == 16 && y == 7))	{
			return "NW";
		}
		else if((x == 16 && y == 8))	{
			return "NES";
		}
		else if((x == 11 && y == 3))	{
			return "NEW";
		}
		else if((x == 12 && y == 1) || (x == 14 && y == 8))
			return "NSW";
		else if((x == 10 && y == 1) || (x == 11 && y == 7))	{
			return "ESW";
		}
		else if((x == 9 && y == 3) || (x == 11 && y == 5))
			return "NESW";
		else
			return null;
	}
	
	/**
	 * Paints the background image on the MUDView.
	 */
	@Override
	public void paintComponent (Graphics g)	{
		super.paintComponent(g);
	
		super.paintComponent(g);
		ImageIcon icon = new ImageIcon("Images/Large_Dungeon_Entrance.jpg");
		g.drawImage(icon.getImage(), 0, 0, null);
	}
}
