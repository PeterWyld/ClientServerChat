package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
	private Socket socket;
	private BufferedReader clientIn;
	private PrintWriter clientOut;
	private String clientName = "ANONYMOUS";
	private static String EXIT_STRING = "cmd:Exit";
	private SyncedMsgQueue msgQueue;
	//private int ID;
	
//	public ClientThread(Socket socket) {
//		this.socket = socket;
//		try {
//			this.clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			this.clientOut = new PrintWriter(socket.getOutputStream(), true);
//		} catch (IOException e) {
//			msgQueue.add(EXIT_STRING);
//		} finally {
//			this.close();
//		}
//	}
//	
//	public ClientThread(Socket socket, String clientName) {
//		this.socket = socket;
//		this.clientName = clientName;
//		try {
//			this.clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//			this.clientOut = new PrintWriter(socket.getOutputStream(), true);
//		} catch (IOException e) {
//			msgQueue.add(EXIT_STRING);
//		} finally {
//			this.close();
//		}
//	}
	
	public ClientThread(PrintWriter clientOut, BufferedReader clientIn, SyncedMsgQueue msgQueue) {
		this.clientIn = clientIn;
		this.clientOut = clientOut;
		this.msgQueue = msgQueue;
		//this.ID = ID;
	}
	
	@Override
	public void run() {
		String userInput = "";
		boolean interrupted = false;
		while(userInput != EXIT_STRING && !interrupted) {
			try {
				userInput = clientIn.readLine();
				try {
					Thread.sleep(10);
					msgQueue.addMessage(clientName + ": " + userInput);
					//NOTE: be careful about adding strings into array as it will be putting in their references
				} catch (InterruptedException e) {
					userInput = EXIT_STRING;
					interrupted = true;
				}
			} catch (IOException e) {
				sendErrorMessages("Error in reading message");
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
