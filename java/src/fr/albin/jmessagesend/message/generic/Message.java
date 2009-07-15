package fr.albin.jmessagesend.message.generic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.albin.jmessagesend.conf.Configuration;

public class Message {

	public Message() {
		this.date = new Date();
		this.toUsers = new ArrayList();
		this.message = "";
		this.fromUser = "";
		this.type = MessageType.SENT;
		this.origin = MessageOrigin.WINDOWS;
		this.ifSameUser = false; 
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public List getUsers() {
		return this.toUsers;
	}
	
	public void setUsers(List users) {
		this.toUsers = users;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setFromUser(String user) {
		this.fromUser = user;
		this.ifSameUser = Configuration.getInstance().getUser().equalsIgnoreCase(user);
	}
	
	public String getFromUser() {
		return this.fromUser;
	}
	
	public String getBulkMessage() {
		return bulkMessage;
	}

	public void setBulkMessage(String bulkMessage) {
		this.bulkMessage = bulkMessage;
	}
	
	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
	
	public MessageOrigin getOrigin() {
		return origin;
	}
	
	public void setOrigin(MessageOrigin origin) {
		this.origin = origin;
	}
	
	public boolean isSameUser() {
		return this.ifSameUser;
	}
	
	protected boolean ifSameUser;
	protected String message;
	protected List toUsers;
	protected String fromUser;
	protected Date date;
	protected String bulkMessage;
	protected MessageType type;
	protected MessageOrigin origin;

}
