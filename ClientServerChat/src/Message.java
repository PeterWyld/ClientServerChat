

/**
 * Immutable object for messages which stores the username and ID of the sender and the contents of the message.
 */
public class Message {
	private String username;
	private String message;
	private String ID;
	
	public Message(String username, String message, String ID) {
		this.username = username;
		this.message = message;
		this.ID = ID;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getID() {
		return ID;
	}
}
