package fr.albin.jmessagesend.ihm;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.jdic.tray.SystemTray;
import org.jdesktop.jdic.tray.TrayIcon;

import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.conf.UserGroupsProperties;
import fr.albin.jmessagesend.conf.UsersProperties;
import fr.albin.jmessagesend.message.capturer.CaptureMessageThread;
import fr.albin.jmessagesend.message.event.MessageEvent;
import fr.albin.jmessagesend.message.event.MessageListener;
import fr.albin.jmessagesend.message.generic.Message;
import fr.albin.jmessagesend.message.generic.MessageFactory;
import fr.albin.jmessagesend.message.generic.MessageOrigin;
import fr.albin.jmessagesend.message.sender.MessageSender;
import fr.albin.jmessagesend.message.sender.TCPMessageSender;
import fr.albin.jmessagesend.message.sender.WindowsMessageSender;
import fr.albin.jmessagesend.scheduler.Scheduler;
import fr.albin.jmessagesend.scheduler.Task;
import fr.albin.jmessagesend.timer.DurationTimer;
import fr.albin.jmessagesend.user.User;
import fr.albin.jmessagesend.user.UserGroup;
import fr.albin.jmessagesend.user.UserGroups;

public class MainFrame extends JFrame implements MessageListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public MainFrame() {
		super("JNetSend");
		Container mainPanel = this.getContentPane();
		GridBagLayout gridBagLayout = new GridBagLayout();
		mainPanel.setLayout(gridBagLayout);
		
    	// Retrieves the groups.
		UserGroupsProperties groupsProps = new UserGroupsProperties();
		UserGroups userGroups = groupsProps.getUserGroupsList();
				
		// Retrieves the toUsers and assign them to groups.
		UsersProperties userProps = new UsersProperties();
		userProps.setUserGroups(userGroups);
		Vector allUsers = (Vector)userProps.getUserList();
		
		// No group detected. Creation of a default group.
		if (userGroups.getList().size() == 0) {
			UserGroup defaultUserGroup = new UserGroup("Default", "Default");
			// Adds all the toUsers to it.
			defaultUserGroup.setList(allUsers);
			userGroups.add(defaultUserGroup);
		}
		
		// ----Menu bar
		JMenuBar menuBar = new JMenuBar();
		
		JMenu helpMenu = menuBar.add(new JMenu("Help"));
		JMenuItem helpMenuItem = helpMenu.add(new JMenuItem("About"));

		helpMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String version = "JMessageSend by Albin Vigier\nVersion : ";
				version += Configuration.getInstance().getApplicationVersion();
				JOptionPane.showMessageDialog(null, version);
			}
		});
		
		this.setJMenuBar(menuBar);
		
		// ----Left panel
		
		// Tabs to represent the groups and toUsers.
		userGroupTabs = new UserGroupsTabbedPane(userGroups);
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.weightx = 2.0;
		constraints.gridheight = 6;
		constraints.gridwidth = 1;
		gridBagLayout.setConstraints(userGroupTabs, constraints);
		mainPanel.add(userGroupTabs);
		
		// ----Top right panel
		
		// Incoming message area.
		this.incomingMessagePane = new MessageEditorPane();
		this.incomingMessagePane.setBackground(Color.LIGHT_GRAY);
		this.incomingMessageScrollPane = new JScrollPane(this.incomingMessagePane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		constraints.gridheight = 3;
		constraints.weighty = 1.0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.incomingMessageScrollPane, constraints);
		mainPanel.add(incomingMessageScrollPane);
		
		// ----Bottom right panel
		
		// Outgoing message area.
		this.outgoingMessageTextArea = new TextArea("", 4, 4, TextArea.SCROLLBARS_VERTICAL_ONLY);
		constraints.gridheight = 2;
		constraints.weighty = 0.0;
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.outgoingMessageTextArea, constraints);
		mainPanel.add(this.outgoingMessageTextArea);
		
		// Time label
		this.timeLabel = new JLabel("Time (s)");
		
		// Time text field
		this.timeSpinner = new NumberSpinner();
		
		// Crypted checkbox
		this.cryptedCheckBox = new JCheckBox("Crypté");
		this.cryptedCheckBox.setSelected(true);
		
		// Send button.
		this.sendButton = new JButton("Envoyer");
		
		// Panel to add live option and send button.
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		gridBagLayout.setConstraints(this.timeLabel, constraints);
		mainPanel.add(this.timeLabel);
		
		gridBagLayout.setConstraints(this.timeSpinner, constraints);
		mainPanel.add(this.timeSpinner);
		
		gridBagLayout.setConstraints(this.cryptedCheckBox, constraints);
		mainPanel.add(this.cryptedCheckBox);
		
		constraints.gridwidth = GridBagConstraints.REMAINDER;
		gridBagLayout.setConstraints(this.sendButton, constraints);
		mainPanel.add(this.sendButton);
		
		// Adds the send button listener.
		this.sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMessage();
			}
		});
		
		// Adds the text area listener. When the user press return,
		// the message is sent.
		this.outgoingMessageTextArea.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if ((e.getKeyCode() == KeyEvent.VK_ENTER) &&
					(e.getModifiers() == 0)) {
					sendMessage();
				}
			}
		});
		
		// Setting the tray icon.
		trayIcon = null;
		try {
			// Gets the default paths
			String paths = System.getProperties().getProperty("java.library.path");
			// Gets the dll lib path from the config file.
			String dllPath = Configuration.getInstance().getDllPath();
			// Adds the dll lib path
			if (paths != null) {
				File path = new File(dllPath);
				paths += ";";
				paths += path.getAbsolutePath();
				System.setProperty("java.library.path", paths);
				LOGGER.debug("Paths used to load the DLLs : " + paths);
			}
			
			this.setIconImage(IMAGE_NORMAL.getImage());
			
			// Only loads the tray icon feature if enabled.
			if (Configuration.getInstance().isUsingTrayIconFeature()) {
				trayIcon = new TrayIcon(IMAGE_NORMAL, "JSend");
				trayIcon.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SystemTray.getDefaultSystemTray().removeTrayIcon(trayIcon);
						setVisible(true);
						setState(Frame.NORMAL);
					}
				});
			}
		}
		catch (Exception e) {
			LOGGER.error("Error when trying to manage tray icon : " +  e.getMessage());
		}
		
		// Message capturers
		CaptureMessageThread captureMessageThread = new CaptureMessageThread(CaptureMessageThread.WINDOWS); 
		captureMessageThread.addMessageListener(this);
		
		CaptureMessageThread captureMessageThread2 = new CaptureMessageThread(CaptureMessageThread.TCP); 
		captureMessageThread2.addMessageListener(this);
		
		// Message senders
		this.tcpMessageSender = new TCPMessageSender();
		this.tcpMessageSender.addMessageListener(this);
		this.windowsMessageSender = new WindowsMessageSender();
		this.windowsMessageSender.addMessageListener(this);
		
		// To create messages
		messageFactory = new MessageFactory();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);	
			}
			public void windowIconified(WindowEvent event) {
				if (trayIcon != null) {
					trayIcon.setIcon(IMAGE_NORMAL);
					SystemTray.getDefaultSystemTray().addTrayIcon(trayIcon);
					setVisible(false);
				}
			}
		});

		this.setSize(500, 350);
		this.setResizable(true);
	}
	
	/**
	 * Sends a NetBios message.
	 */
	private void sendMessage() {
		LOGGER.debug("Click on the send button.");
		String messageString = outgoingMessageTextArea.getText();

		List vector = userGroupTabs.getSelectedUsersList();
		if (vector.size() != 0) {
			Message message = messageFactory.createOutgoingMessage(messageString, vector, this.cryptedCheckBox.isSelected());
			
			// Sends a live message.
			int timeValue = this.timeSpinner.getIntValue();
			if ( timeValue == 0 ) {
				LOGGER.debug("Live sending.");
				if (this.cryptedCheckBox.isSelected()) {
					tcpMessageSender.send(message);
				}
				else {
					windowsMessageSender.send(message);
				}
				// To display a non crypted message in the incoming box
				message = messageFactory.createOutgoingMessage(messageString, vector, false);
				this.incomingMessagePane.addMessage(message);
			}
			// Sends a differate message.
			else {
				LOGGER.debug("Differate sending.");
				Scheduler scheduler = Scheduler.getInstance();
				scheduler.addTask(new Task(message, new DurationTimer(timeValue * 1000)));
			}
			outgoingMessageTextArea.setText("");
		}
	}
	
	private void scrollDownIncomingMessagePane() {
		if (this.incomingMessageScrollPane.getVerticalScrollBar().isShowing()) {
			LOGGER.debug("There is a scroll bar, we scroll down.");
		    this.incomingMessagePane.setCaretPosition(this.incomingMessagePane.getDocument().getLength());
		}
	    this.repaint();
	}
	
	/**
	 * When a message is received, we append it into the incoming message pane.
	 * We scroll the scroll pane to back.
	 * We change the icon status.
	 */
	public void messageReceived(MessageEvent e) {
		// A message has been received 
		if (e.getSource() instanceof CaptureMessageThread) {
			LOGGER.debug("MessageReceived event received");
			this.incomingMessagePane.addMessage(e.getMessage());
			
		    // Change the icon status if the tray icon is used and present in the tray bar.
		    if ( (!this.isVisible()) && (this.trayIcon != null) ) {
		    	LOGGER.debug("Setting tray icon to notify incoming message.");
		    	this.trayIcon.setIcon(IMAGE_INCOMING_MESSAGE);
		    	SystemTray.getDefaultSystemTray().addTrayIcon(trayIcon);
		    }
		}
		// A message has been sent
		else if (e.getSource() instanceof MessageSender) {
			// Gets the source object
			MessageSender messageSender = (MessageSender) e.getSource();
			List users = messageSender.getUncontactedUsersList();
			Iterator iterator = users.iterator();
			String userNames = "";
			while (iterator.hasNext()) {
				User user = (User) iterator.next();
				if (userNames.length() != 0) {
					userNames += ", ";
				}
				else {
					userNames = "Impossible de contacter les utilisateurs suivants : \n";
				}
				userNames += user.toString();
			}
			// There are uncontacted users
			if (users.size() != 0) {
				// TCP uncontacted users
				// We try to contact them by netbios (windows)
				if (messageSender.getMessage().getOrigin() == MessageOrigin.TCP) {
					Message message = messageFactory.createOutgoingMessage(messageSender.getMessage().getMessage(), users, false);
					this.windowsMessageSender.send(message);
				}
				// Else, we show an error from contacting users.
				else {
					JOptionPane.showMessageDialog(this, userNames);
				}
			}
		}
	}
	
	private UserGroupsTabbedPane userGroupTabs;
	private TextArea outgoingMessageTextArea;
	private MessageEditorPane incomingMessagePane;
	private JButton sendButton;
	private JCheckBox cryptedCheckBox;
	private JLabel timeLabel;
	private NumberSpinner timeSpinner;
	private JScrollPane incomingMessageScrollPane;
	private TCPMessageSender tcpMessageSender;
	private WindowsMessageSender windowsMessageSender;
	private MessageFactory messageFactory;
	
	private TrayIcon trayIcon;
	
	private static final Log LOGGER = LogFactory.getLog(MainFrame.class);
	private static final String IMAGE_NORMAL_PATH = "images/tray_icon_normal.png";
	private static final String IMAGE_INCOMING_MESSAGE_PATH = "images/tray_icon_incoming_message.png";	
	
	private static final ImageIcon IMAGE_NORMAL = new ImageIcon(IMAGE_NORMAL_PATH);
	private static final ImageIcon IMAGE_INCOMING_MESSAGE = new ImageIcon(IMAGE_INCOMING_MESSAGE_PATH);
}

