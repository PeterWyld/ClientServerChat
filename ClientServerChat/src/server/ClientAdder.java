package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ClientAdder implements Runnable {
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private SyncedMsgQueue messages = new SyncedMsgQueue();
	private ServerSocket in;
	
	public ClientAdder(ArrayList<ClientThread> clients, SyncedMsgQueue messages, ServerSocket in) {
		this.clients = clients;
		this.messages = messages;
		this.in = in;
	}
	
	public void run() {
		boolean uninterrupted = true;
		while(uninterrupted) {
			try {
				System.out.println("Server listening");
				
//				clients.add(new ClientThread(in.accept()));

				Socket s = in.accept();
				InputStreamReader r = new InputStreamReader(s.getInputStream());
				BufferedReader clientIn = new BufferedReader(r);
				PrintWriter clientOut = new PrintWriter(s.getOutputStream(), true);
				clients.add(new ClientThread(clientOut, clientIn, messages));
				
				System.out.println("Server accepted connection on " + 
						in.getLocalPort() + " ; " + s.getPort());
				clients.get(clients.size()-1).start();
				try {
					Thread.sleep(10);
				} catch(InterruptedException e) {
					uninterrupted = false;
				}

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

}
