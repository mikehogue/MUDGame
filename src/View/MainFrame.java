package View;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Network.Client;

/**
 * The main frame for the Whatever MUD. Displays all the elements of the GUI, including
 * the TitleView and the MUDView.
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class MainFrame extends JFrame {
	
	/**
	 * The JPanel which holds the cardlayout of panels to display
	 */
	public JPanel body;
	//The stack of panels which body uses to change views
	private Stack<JPanel> panels;
	//The panel which is currently being displayed
	private JPanel currentPanel;
	
	/**
	 * The main method which runs the Client GUI.
	 * @param args
	 */
	public static void main(String[] args) {
		new MainFrame();
	}
	
	/**
	 * Creates a new MainFrame, which holds the stack of panels flipped through in changeViews and
	 * starts off displaying the TitleView.
	 */
	public MainFrame() {  
		
		this.setTitle("Whatever!");
		
		panels = new Stack<JPanel>();
		currentPanel = new TitleView(this);
		
		body = new JPanel();
		body.setLayout(new CardLayout());
		
		body.add(currentPanel, "TITLE");
		panels.push(currentPanel);
		
		this.add(body, BorderLayout.CENTER);
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.setLocation(0, 0);
		this.setSize(1035, 700);

		this.setVisible(true);
		setUpMenuBar();
	}
	
	/**
	 * Creates the JMenuBar which is displayed at the top of the frame, including
	 * File and Help menus.
	 */
	private void setUpMenuBar() {
		JMenuBar jmb = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem exit = new JMenuItem("Exit");
		JMenu help = new JMenu("Help");
		file.add(exit);
		exit.addActionListener(new ExitListener());
		
		JMenuItem about = new JMenuItem("About");
		about.addActionListener(new AboutListener());
		
		JMenuItem commands = new JMenuItem("Commands");
		commands.addActionListener(new CommandListener());

		help.add(commands);
		help.add(about);
		jmb.add(file);
		jmb.add(help);
		this.add(jmb, BorderLayout.NORTH);
	}
	
	/**
	 * Action listener for the exit buttons in the title page and file menu.
	 * @author Chris
	 *
	 */
	private class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}

	/**
	 * Action listener which displays the about page.
	 * @author Chris
	 *
	 */
	private class AboutListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "MUD by Team Whatever\n\nBackground art by Raphelt. raphelt.deviantart.com\n");
		}
	}
	
	/**
	 * Action listener which displays the available commands and formats.
	 * @author Chris
	 *
	 */
	private class CommandListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, "Commands\n\nooc <message>: send message to global chat\n" +
					"move <n,s,e,w>: move in the specified compass direction\n" +
					"          (aka: go <n,s,e,w>)\n" +
					"look: Look around the area\n" +
					"say <message>: Send message to local room chat\n" +
					"tell <player> <message>: Send message privately to player\n" +
					"who: List the players currently logged into the server\n" +
					"attack <name>: Start combat with <name>\n" +
					"equip <item>: Equip item from your inventory\n" +
					"give <item> <player>: Give specified item to player in your room\n" +
					"drop <item>: Drops item from your inventory\n" +
					"take <item> <mob>: Picks up an item in the room. If a Mob is specified, it takes the Item from the Mob\n" +
					"inventory: List all of the items in your inventory\n" +
					"shutdown <password>: Shutdown the games server\n" +
					"commands: List all of the possible commands on the server\n" + 
					"quit: Logout of the game and go back to the Title Screen\n" + 
					"increase <stat>: Increase the specified stat. Usable after reaching 1000 experience\n" +
					"score: List the statistics of your Player\n" + 
					"use <item>: Use an item, such as a Key, Bomb or Potion, on your Player or the environment\n" +
					"advance <character>: Advance on a Player or Mob for combat\n" + 
					"retreat <character>: Retreat further from a Player or Mob for combat\n" + 
					"dance: Alert the Players in your room that you are dancing\n" + 
					"highfive <Player>: Alert the Players in your room that you have high-fived a Player\n" + 
					"wave <Player>: Alert the Players in your room that you have waved at a Player\n" + 
					"slap <Player>: Alert the Players in your room that you have slapped a Player\n" + 
					"giggle <Player>: Alert the Players in your room that you have giggled at a Player\n");
		}
	}
	
	/**
	 * Switches views between the title screen and the MUD screen.
	 * @param v
	 * 			The view that is supposed to be displayed.
	 * @param o
	 * 			The client for the player whos game is being displayed.
	 */
	public void changeViews(Views v, Object o) {
		switch (v) {
			case MUD:
				currentPanel = new MUDView((Client) o);
				panels.push(currentPanel);
				body.add(currentPanel, v.name());
				CardLayout cl1 = (CardLayout) body.getLayout();
				cl1.show(body, v.name());
				for (Component c : body.getComponents()) {
					if (c == currentPanel) {
						c.requestFocusInWindow();
					}
				}
				break;
			case TITLE:
				currentPanel = new TitleView(this);
				panels.push(currentPanel);
				body.add(currentPanel, v.name());
				CardLayout cl2 = (CardLayout) body.getLayout();
				cl2.show(body, v.name());
				for (Component c : body.getComponents()) {
					if (c == currentPanel) {
						c.requestFocusInWindow();
					}
				}
				break;
			default:
				return;
		}
	}
	
}
