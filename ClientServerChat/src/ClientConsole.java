

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * A client that will read user input from the console 
 * and output messages sent from a connected server
 *
 * Uses a separate thread for taking in messages from the server
 */
public class ClientConsole {
	private Socket server;
	private BufferedReader serverIn;
	private PrintWriter serverOut;
	private BufferedReader userIn;

	private Thread msgReaderThrd;

	/**
	 * The constructor for this class attempts to connect to the server (using the address and port parameters). <br>
	 * The method will end the program if it is unable to connect to the server for whatever reason.
	 * @param address The IP address of the server
	 * @param port The port of the server
	 */
	public ClientConsole(String address, int port) {
		try {
			server = new Socket(address, port);
			userIn = new BufferedReader(new InputStreamReader(System.in));
			serverOut = new PrintWriter(server.getOutputStream(), true);
			serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println(address + " is not a valid address.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Could not connect to server");
			System.exit(0);
		}
	}

	/**
	 * Begins the communication between the server and the client
	 * 
	 * The method sets up and starts the msgReader thread which will print out the messages from the server. <br>
	 * The method will then continuously get user input until the user enters either a null or "EXIT"
	 */
	public void go() {
		String userInput = "";
		
		msgReaderThrd = new Thread(new ClientMsgReceiver(serverIn, serverOut, new PrintWriter(System.out, true)));
		msgReaderThrd.start();
		
		try {
			while ((userInput = userIn.readLine()) != null && !userInput.equals("EXIT")) {
				serverOut.println(userInput);
			}
		} catch (IOException e) {
			System.out.println("Issue with reading input from console");
		} finally {
			msgReaderThrd.interrupt();
			try {
				server.close();
				serverIn.close();
				serverOut.close();
				userIn.close();
			} catch (IOException e) {
			}
		}
	}
}
