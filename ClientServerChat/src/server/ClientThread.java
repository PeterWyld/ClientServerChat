package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

public class ClientThread extends Thread{
	private Socket socket;
	private BufferedReader clientIn;
	private PrintWriter clientOut;
	private LinkedList<String> msgQueue = new LinkedList<String>();
	private String clientName = "ANONYMOUS";
	private static String EXIT_STRING = "cmd:Exit";
	
	public ClientThread(Socket socket) {
		this.socket = socket;
		try {
			this.clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientOut = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			msgQueue.add(EXIT_STRING);
		} finally {
			this.close();
		}
	}
	
	public ClientThread(Socket socket, String clientName) {
		this.socket = socket;
		this.clientName = clientName;
		try {
			this.clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.clientOut = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			msgQueue.add(EXIT_STRING);
		} finally {
			this.close();
		}
	}
	
	@Override
	public void run() {
		String userInput = "";
		while(userInput != EXIT_STRING /*&& user is still present*/) {
			try {
				userInput = clientIn.readLine();
				synchronized (msgQueue) {
					msgQueue.add(clientName + ": " + userInput);
				}
			} catch (IOException e) {
				sendErrorMessages("Error in reading message");
			}
			//NOTE be careful about adding strings into array as it will be putting in their references
		}
			

		
	}
	
	public String popNextMessage() {
		if (msgQueue.isEmpty()) {
			return null;
		} else {
			System.out.println("printing");
			synchronized (msgQueue) {
				return msgQueue.pop();
			}
		}
	}
	
	public void sendMessage(String str) {
		clientOut.println(str);
	}
	
	public void sendErrorMessages(String str) {
		//reformat text to make clear it is an error
		clientOut.println(str);
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Socket getSocket() {
		return socket;
	}
}
