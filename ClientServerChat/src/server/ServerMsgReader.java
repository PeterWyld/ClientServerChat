package server;

import java.util.ArrayList;

public class ServerMsgReader implements Runnable {
	ArrayList<ClientThread> clients;
	SyncedMsgQueue messages;
	
	public ServerMsgReader(ArrayList<ClientThread> clients, SyncedMsgQueue messages) {
		this.messages= messages;
		this.clients =clients;
	}

	public void run() {
		String[] currentMessage;
		boolean uninterrupted = true;
		while(uninterrupted) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				uninterrupted = false;
			}
			
			currentMessage = messages.nextMessage();
			if(currentMessage != null) {
				for (int i = 0; i <= clients.size()-1; i++) {
					clients.get(i).sendMessage(currentMessage);
				}
			}
		}
	}
}
