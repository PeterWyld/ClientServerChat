package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server extends Thread{
	private ServerSocket in;
	private List<ClientThread> clients = new LinkedList<ClientThread>();
	private SyncedMsgQueue messages = new SyncedMsgQueue();
	
	public Server(int port) {
		try {
			in = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void go() {
		while(true) {
			try {
				System.out.println("Server listening");
				
//				clients.add(new ClientThread(in.accept()));

				Socket s = in.accept();
				InputStreamReader r = new InputStreamReader(s.getInputStream());
				BufferedReader clientIn = new BufferedReader(r);
				PrintWriter clientOut = new PrintWriter(s.getOutputStream(), true);
				clients.add(new ClientThread(clientOut, clientIn, messages));
				
				System.out.println("Server accepted connection on " + 
						in.getLocalPort() + " ; " + clients.get(clients.size() -1).getSocket().getPort());
				clients.get(clients.size()-1).start();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}
		
	}
	
	@Override
	public void run() {
		String currentMessage;
		while(true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				//Shutdown the server
				e.printStackTrace();
			}
			
			currentMessage = messages.nextMessage();
			if(currentMessage != null) {
				for (int i = 0; i <= clients.size()-1; i++) {
					clients.get(i).sendMessage(currentMessage);
				}
			}
		}
	}
	
	private void addClient() {
		
	}
	
	public static void main(String[] args) {
		new Server(14001).go();
	}
}
