package View;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Network.Client;



/**
 * The panel which contains the title screen of the MUD.
 * @author Chris
 *
 */
@SuppressWarnings("serial")
public class TitleView extends JPanel implements Observer {

	private JButton connect;
	private JButton exit;
	private MainFrame mf;
	private String name;
	private String password;
	private String address;
	private int port;
	
	/**
	 * Constructs a new titleView.
	 * @param mf
	 * 			The MainFrame that the TitleView is located in.
	 */
	public TitleView(MainFrame mf) {
		this.mf = mf;
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		connect = new JButton("Connect");
		connect.addActionListener(new ConnectListener());
		c.fill = GridBagConstraints.HORIZONTAL;
		/*
		c.anchor = GridBagConstraints.SOUTH;
		c.weighty = 1.0;
		c.insets = new Insets(650, 0, 0, 0);
		*/
		c.ipady = 40;
		c.ipadx = 300;
		c.gridx = 1;
		c.gridy = 0;
		this.add(connect, c);
		
		exit = new JButton("Exit");
		exit.addActionListener(new ActionListener()	{
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		/*
		exit.setBackground(new Color(0, 106, 78));
		exit.setForeground(Color.BLACK);
		exit.setBorderPainted(false);
		exit.setOpaque(false);
		*/
		//c.insets = new Insets(0, 0, 0, 0);
		c.ipady = 40;
		c.ipadx = 300;
		c.gridx = 1;
		c.gridy = 1;
		this.add(exit, c);
	}
	
	/**
	 * Action listener for the Connect button.
	 * @author Chris
	 *
	 */
	private class ConnectListener implements ActionListener {

		@SuppressWarnings("deprecation")
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JPanel inputPanel = new JPanel();
			inputPanel.setLayout(new BoxLayout(inputPanel, 1));
			
			inputPanel.add(new JLabel("Enter your name: "));
			JTextField nameField = new JTextField();
			nameField.setText("Hero");
			inputPanel.add(nameField);
			
			inputPanel.add(new JLabel("Enter your password: "));
			JPasswordField pwField = new JPasswordField();
			pwField.setText("password");
			inputPanel.add(pwField);
			
			inputPanel.add(new JLabel("Enter the IP Address of the MUD Server: "));
			JTextField addressField = new JTextField();
			addressField.setText("localhost");
			inputPanel.add(addressField);
			
			inputPanel.add(new JLabel("Enter the port of the" + " MUD Server: "));
			JTextField portField = new JTextField();
			portField.setText("4000");
			inputPanel.add(portField);
			
			int ret = JOptionPane.showConfirmDialog(mf, inputPanel, "Connection Options", JOptionPane.YES_NO_OPTION);
			if( ret == JOptionPane.YES_OPTION )	{
				while(nameField.getText().isEmpty() || pwField.getText().isEmpty() || addressField.getText().isEmpty() || portField.getText().isEmpty())	{
					JOptionPane.showMessageDialog(null, "All fields are required!", "Empty Field", JOptionPane.ERROR_MESSAGE);
					ret = JOptionPane.showConfirmDialog(mf, inputPanel, "Connection Options", JOptionPane.YES_NO_OPTION);
					if(ret == JOptionPane.NO_OPTION)
						return;
				}
				name = nameField.getText();
				if (name.indexOf(' ') != -1) {
					JOptionPane.showMessageDialog(null, "The username must be one word and not contain spaces.");
					return;
				}
				password = pwField.getText();
				address = addressField.getText();
				try { 
					port = Integer.parseInt(portField.getText());
				} catch (NumberFormatException e) {
					JOptionPane.showMessageDialog(null, "The port must be an integer!");
					return;
				}
				
				Client c = new Client(name, password, address, port, mf);
				if (c.isConnected()) {
					mf.changeViews(Views.MUD, c);
				}
			}
		}

	}

	/**
	 * Changes to this view when the user disconnects.
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		if (arg1 != null) {
			if (arg1.toString().equals("Disconnected")) {
				mf.changeViews(Views.TITLE, null);
			}
		}
	}
	
	/**
	 * Paints the title screen's background image.
	 */
	@Override
	public void paintComponent (Graphics g)	{
		super.paintComponents(g);
		ImageIcon icon = new ImageIcon("Images/Large_Dungeon_Entrance.jpg");
		g.drawImage(icon.getImage(), 0, 0, null);
	}

}
