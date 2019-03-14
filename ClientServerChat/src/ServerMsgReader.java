

import java.util.ArrayList;

/**
 * A class that for a thread to continuously check the msgQueue and send any messages to clients if there are any
 */
public class ServerMsgReader implements Runnable {
	private ArrayList<ClientThread> clients;
	private SyncedMsgQueue messages;
	private boolean closing = false;
	
	public ServerMsgReader(ArrayList<ClientThread> clients, SyncedMsgQueue messages) {
		this.messages = messages;
		this.clients = clients;
	}

	/**
	 * A method to which will continuously read messages and send them to clients
	 * The method will wait for a notification from inside the msgQueue before reading any messages in the server
	 */
	public void run() {
		Message currentMessage;
		while(!closing) {
			try {
				messages.waitForMessage(); //waiting for notification
			} catch (InterruptedException e) {
				closing = true;
			}
			while((currentMessage = messages.nextMessage()) != null) {
				if(currentMessage.getMessage().equals("EXIT")) {
					for (int i = 0; i <= clients.size()-1; i++) {
						if(clients.get(i).hashCode() == currentMessage.getID()) {
							clients.get(i).interrupt();
							clients.get(i).close();
							clients.remove(i);
						} else {
							clients.get(i).sendServerMessage(currentMessage.getUsername() + " has left the server.");
						}
					}
				} else {
					for (int i = 0; i <= clients.size()-1; i++) {
						clients.get(i).sendMessage(currentMessage);
					}
				}
			}
		}
	}
}
