package server;

import java.util.LinkedList;

public class SyncedMsgQueue {
	LinkedList<String> messages = new LinkedList<String>();
	//not using extends because:
	//I don't want to have to consider other list methods to be synched
	//If I want to add some more data about messages it would be easier in this way
	
	public SyncedMsgQueue() {
		super();
	}
	
	public void addMessage(String message) throws InterruptedException {
		Thread.sleep(10);
		synchronized(this) {
			messages.add(message);
		}
	}
	
	public String nextMessage() {
		synchronized(this) {
			return messages.pop();
		}
	}
	
	public boolean isEmpty() {
		return messages.isEmpty();
	}
}
