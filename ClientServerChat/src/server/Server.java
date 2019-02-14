package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Server {
	private ServerSocket in;
	private List<ClientOnServer> Clients = new LinkedList<ClientOnServer>();
	
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
			Clients.add(new ClientOnServer(in.accept()));
			System.out.println("Server accepted connection on " + in.getLocalPort() + " ; " + s.getPort() );

			
			while(true) {
				
				//send all other clients the message
				clientOut.println(userInput);
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
		new Server(14002).go();
	}
}
