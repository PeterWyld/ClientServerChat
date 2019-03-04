package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

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
	
	public ClientThread(PrintWriter clientOut, BufferedReader clientIn, SyncedMsgQueue msgQueue) {
		this.clientIn = clientIn;
		this.clientOut = clientOut;
		this.msgQueue = msgQueue;
	}
	
	public ClientThread(PrintWriter clientOut, BufferedReader clientIn, SyncedMsgQueue msgQueue, String clientName) {
		this.clientIn = clientIn;
		this.clientOut = clientOut;
		this.msgQueue = msgQueue;
		this.clientName = clientName;
	}
	
	/**
	 * This method attempts to read messages in from the client and put them into the msgQueue <br>
	 * until either it is interrupted or the client disconnects (and causes an IOException). <br>
	 */
	@Override
	public synchronized void run() {
		String userInput = "";
		boolean interrupted = false;
		while(userInput != EXIT_STRING && !interrupted) {
			try {
				userInput = clientIn.readLine();
				if (userInput == null) {
					interrupted = true;
				}
				try {
					Thread.sleep(10);
					msgQueue.addMessage(clientName, userInput, Integer.toString(this.hashCode()));
					//NOTE: be careful about adding strings into array as it will be putting in their references
				} catch (InterruptedException e) {
					msgQueue.addMessage(clientName, EXIT_STRING, Integer.toString(this.hashCode()));
					interrupted = true;
				}
			} catch (IOException e) {
				//likely unexpected client exit
				msgQueue.addMessage(clientName, EXIT_STRING, Integer.toString(this.hashCode()));
				sendServerMessage("Error in reading message");
				interrupted = true;
			}
			
		}
		sendServerMessage("You have been disconnected from the server");

		
	}
	
	/**
	 * Sends a message to the client
	 * @param message An array of Strings (length of array should be either 1 or 3)
	 */
	public void sendMessage(Message message) {
		if(!message.getID().equals(Integer.toString(this.hashCode()))) {
			clientOut.println(message.getUsername() + ": " + message.getMessage());
		}
	}
	
	/**
	 * A method to be used to send the user messages directly from the server (errors, disconnection message, info requests
	 */
	public void sendServerMessage(String message) {
		clientOut.println(message);
	}
	
}
