

import java.util.LinkedList;

/**
 * A class which stores users messages before they are sent off by the ServerMsgReader
 * 
 * It acts the same as a list except it only allows you to check if it's empty, add a message or pop a message
 * The addMessage and popMessage functions are synchronized to prevent two messsages being added/popped at the same time
 * Messages in the class consist of three strings: the username, the message and an ID
 */
public class SyncedMsgQueue {
	private LinkedList<Message> messages = new LinkedList<Message>();
	
	public SyncedMsgQueue() {
		super();
	}
	
	/**
	 * Adds a message to the messages list parameters are for the three parts of the message.
	 * It then notifies the all threads using the object.
	 * @param username
	 * @param message
	 * @param ID
	 */
	public synchronized void addMessage(String username, String message, int ID) {
		messages.add(new Message(username, message, ID));
		notifyAll();
	}
	
	/**
	 * pops a message off of the front of the list
	 * @return a message
	 */
	public synchronized Message nextMessage() {
			if (messages.isEmpty()) {
				return null;
			} else {
				return messages.pop();
			}
			
	}
	
	public boolean isEmpty() {
		return messages.isEmpty();
	}
	
	/**
	 * Method used by the serverMsgReader to wait until a message is added
	 */
	public synchronized void waitForMessage() throws InterruptedException{
		while(messages.isEmpty()) {
			wait();
		}
	}
}
