

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A Class that represents a client on the server
 * <br> It has a thread for dealing with getting client input
 * <br> As well as methods for sending messages to the client
 */
public class ClientThread extends Thread{
	private BufferedReader clientIn;
	private PrintWriter clientOut;
	private String clientName = "ANONYMOUS";
	private static String EXIT_STRING = "EXIT";
	private SyncedMsgQueue msgQueue;
	private Socket socket;
	
	public ClientThread(PrintWriter clientOut, BufferedReader clientIn, SyncedMsgQueue msgQueue, Socket socket) {
		this.clientIn = clientIn;
		this.clientOut = clientOut;
		this.msgQueue = msgQueue;
		this.socket = socket;
	}
	
	/**
	 * This method attempts to read messages in from the client and put them into the msgQueue <br>
	 * until either it is interrupted or the client disconnects (and causes an IOException). <br>
	 */
	@Override
	public synchronized void run() {
		String userInput = "";
		boolean interrupted = false;
		
		try {
			//getting the clients username
			clientName = getInfo("What is your username?");
			clientOut.println("Hi! " + clientName);
			
			//continuously reading their messages
			while(!(userInput = clientIn.readLine()).equals(EXIT_STRING) && !interrupted && userInput != null) {
				try {
					Thread.sleep(10);
					msgQueue.addMessage(clientName, userInput, this.hashCode());
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
			
			//adds a message to let other users know that this user has disconnected
			msgQueue.addMessage(clientName, EXIT_STRING, this.hashCode());
			
		} catch (IOException e) {
			//likely unexpected client exit
			msgQueue.addMessage(clientName, EXIT_STRING, this.hashCode());
			sendServerMessage("Error in reading message");
		}
		
		sendServerMessage("You have been disconnected from the server");
		close();
		
	}
	
	/**
	 * Sends a message to the user and returns the response
	 * @param msg The message to be sent
	 * @return Their response
	 */
	private String getInfo(String msg) throws IOException{
		clientOut.println(msg);
		return clientIn.readLine();
	}
	
	/**
	 * Sends a message to the client
	 * @param message An array of Strings (length of array should be either 1 or 3)
	 */
	public void sendMessage(Message message) {
		if(!(message.getID() == this.hashCode())) {
			clientOut.println(message.getUsername() + ": " + message.getMessage());
		}
	}
	
	/**
	 * A method to be used to send the user messages directly from the server (errors, disconnection message, info requests
	 */
	public void sendServerMessage(String message) {
		clientOut.println(message);
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
		}
		clientOut.close();
		try {
			clientIn.close();
		} catch (IOException e) {
		}
	}
	
}
