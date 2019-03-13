

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Runnable class for a thread to continously check to see if another client is trying to connect and then add them to the list of clients
 */
public class ClientAdder implements Runnable {
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private SyncedMsgQueue messages = new SyncedMsgQueue();
	private ServerSocket in;
	
	public ClientAdder(ArrayList<ClientThread> clients, SyncedMsgQueue messages, ServerSocket in) {
		this.clients = clients;
		this.messages = messages;
		this.in = in;
	}
	
	/**
	 * Continuously accepts clients until interrupted.
	 * When a client is accepted the program creates a new clientThread with the relevant readers and
	 * writers and adds it to the list of clients (which is the same as in the server class)
	 */
	public void run() {
		boolean uninterrupted = true;
		
		while(uninterrupted) {
			try {
				System.out.println("Server listening");
				
				//the readers and writers are all created here because otherwise it causes an error for reasons I don't know
				Socket s = in.accept();
				InputStreamReader r = new InputStreamReader(s.getInputStream());
				BufferedReader clientIn = new BufferedReader(r);
				PrintWriter clientOut = new PrintWriter(s.getOutputStream(), true);
				clients.add(new ClientThread(clientOut, clientIn, messages, s));
				
				System.out.println("Server accepted connection on " + 
						in.getLocalPort() + " ; " + s.getPort());
				clients.get(clients.size()-1).start();
				try {
					Thread.sleep(10);
				} catch(InterruptedException e) {
					uninterrupted = false;
				}

			} catch (IOException e) {
				uninterrupted = false;
			}
		}
	}

}
