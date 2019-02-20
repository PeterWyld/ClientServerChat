package server;

import java.util.LinkedList;

public class SyncedMsgQueue {
	private LinkedList<String[]> messages = new LinkedList<String[]>();
	
	public SyncedMsgQueue() {
		super();
	}
	
	public void addMessage(String message, String ID) throws InterruptedException {
		Thread.sleep(10);
		synchronized(this) {
			messages.add(new String[] {message, ID});
		}
	}
	
	public String[] nextMessage() {
		synchronized(this) {
			if (messages.isEmpty()) {
				return null;
			} else {
				return messages.pop();
			}
			
		}
	}
	
	public boolean isEmpty() {
		return messages.isEmpty();
	}
}
