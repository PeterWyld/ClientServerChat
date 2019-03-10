

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 * The server class holds all the threads and deals with the shutdown of the server
 */
public class Server{
	private ServerSocket in;
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private SyncedMsgQueue messages = new SyncedMsgQueue();
	private Thread clientAdder;
	private Thread messageReader;

	public Server(int port) {
		try {
			in = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sets up the required threads and then takes input from the console
	 */
	public void go() {
		clientAdder = new Thread(new ClientAdder(clients, messages, in));
		clientAdder.start();
		messageReader = new Thread(new ServerMsgReader(clients, messages));
		messageReader.start();
		
		InputStreamReader r = new InputStreamReader(System.in);
		BufferedReader serverIn = new BufferedReader(r);
		String userInput;
		try {
			while(!(userInput = serverIn.readLine()).equals("EXIT")) {
			
				//place to put responses to other inputs (non listed in spec)
				switch (userInput) {
				default: 
					System.out.println("Unrecognised Command");
				}
			}
			
				

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
		
		this.close();
		System.exit(0);
		
	}
	
	/**
	 * Interrupts all Threads and then closes the server
	 */
	private void close() {
		for(int i = 0; i <= clients.size() -1; i++) {
			clients.get(i).interrupt();
		}

		clientAdder.interrupt();
		messageReader.interrupt();
		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates a new server taking an argument to set the port (default 14001)
	 */
	public static void main(String[] args) {
		int port = 14001;
		
		for(int i = 0; i <= args.length -1; i++) {
			if (args[i].charAt(0) == '-') {
				switch(args[i]) {
					case "-csp": 
						try {
							port = Integer.parseInt(args[i+1]);
						} catch (NumberFormatException e) {
							System.out.println("-csp requires an integer arguement");
							System.exit(0);
						}
						break;
						
					default: 
						System.out.println("Tag not recognised: " + args[i]);
						break;
				}
			}
		}
		new Server(port).go();
	}
}
