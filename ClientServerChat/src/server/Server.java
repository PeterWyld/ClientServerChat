package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

public class Server {
	private ServerSocket in;
	private List<ClientThread> clients = new LinkedList<ClientThread>();
	
	public Server(int port) {
		try {
			in = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void go() {

		try {
			System.out.println("Server listening");
			clients.add(new ClientThread(in.accept()));
			System.out.println("Server accepted connection on " + in.getLocalPort() + " ; " + clients.get(clients.size() -1).getSocket().getPort());
			clients.get(clients.size()-1).start();

			
			while(true) {
				
				//send all other clients the message
				for (int i = 0; i <= clients.size()-1; i++) {
					String currentMessage = clients.get(i).popNextMessage();
					if(currentMessage != null) {
						if(currentMessage.equals("cmd:Exit")) {
							//remove client
							currentMessage = clients.get(i).getName() + "has left the server";
						}
					
						for (int j = 0; j <= i -1; j++) {
							clients.get(j).sendMessage(currentMessage);
						}
						
						for (int j = i+1; j <= clients.size() -1; j++) {
							clients.get(j).sendMessage(currentMessage);
						}
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Server(14006).go();
	}
}
