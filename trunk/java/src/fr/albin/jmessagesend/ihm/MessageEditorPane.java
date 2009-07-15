package fr.albin.jmessagesend.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.albin.jmessagesend.conf.Configuration;
import fr.albin.jmessagesend.message.generic.Message;
import fr.albin.jmessagesend.message.generic.MessageFormatter;
import fr.albin.jmessagesend.message.generic.MessageType;

public class MessageEditorPane extends JTextPane implements HyperlinkListener, ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public MessageEditorPane() {
		super();
		this.setContentType("text/html");
		this.messages = new ArrayList();
		
		this.popupMenu = new JPopupMenu();
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				showPopup(e);
		    }

		    public void mouseReleased(MouseEvent e) {
		    	showPopup(e);
		    }
		});
		
		clearHistoryMenuItem = new JMenuItem("Clear History");
		this.popupMenu.add(clearHistoryMenuItem);
		clearHistoryMenuItem.addActionListener(this);		
		
		this.addHyperlinkListener(this);
		this.setEditable(false);
	}
			

	public synchronized void addMessage(Message message) {
		this.messages.add(message);
		
		this.render();
	}
	
	public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
        	LOGGER.debug("Activating hyperlink");
            Runtime runtime = Runtime.getRuntime();
            try {
            	String link = e.getURL().toString();
            	LOGGER.debug("Link to open : " + link);
				runtime.exec(Configuration.getInstance().getSystemBrowser() + " " + link);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        }
        else {
        	LOGGER.debug("Other hyperlinking features.");
        }
    }
	
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.clearHistoryMenuItem) {
			MessageEditorPane.this.clearHistory();
		}
	}
	
	protected void showPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
            this.popupMenu.show(e.getComponent(), e.getX(), e.getY());
        }
	}
	
	protected void clearHistory() {
		this.messages.clear();
		this.setText("");
	}
	
	private synchronized void render() {
		
		HTMLDocument document = (HTMLDocument) this.getDocument();
		HTMLEditorKit editor = (HTMLEditorKit) this.getEditorKit();
		
		String result = "";
		MessageFormatter formatter = new MessageFormatter();
		Iterator iterator = this.messages.iterator();
		
		while (iterator.hasNext()) {
			Message message = (Message) iterator.next();
			String text = "<font color=\"" ;
			if (message.getType() == MessageType.RECEIVED) {
				text += MESSAGE_RECEIVED_COLOR;
			}
			else {
				text += MESSAGE_SENT_COLOR;
			}
			text += "\">" + formatter.asHtml(message);
			text += "</font>";
			result += text;
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Messages : \n" + result);
		}
		
		this.messages.clear();
		
		try {
			editor.insertHTML(document, document.getLength(), result, 0, 0, null);
		} catch (Exception e) {
			LOGGER.error("Error", e);
		}
		
		System.gc();
	}

	private List messages;
	private JPopupMenu popupMenu;
	private JMenuItem clearHistoryMenuItem;
	
	private static final String MESSAGE_SENT_COLOR = "black";
	private static final String MESSAGE_RECEIVED_COLOR = "blue";
	private static final Log LOGGER = LogFactory.getLog(MessageEditorPane.class);
	
}
