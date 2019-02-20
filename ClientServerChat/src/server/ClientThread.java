package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientThread extends Thread{
	private BufferedReader clientIn;
	private PrintWriter clientOut;
	private String clientName = "ANONYMOUS";
	private static String EXIT_STRING = "cmd:Exit";
	private SyncedMsgQueue msgQueue;
	
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
					msgQueue.addMessage(clientName + ": " + userInput, Integer.toString(this.hashCode()));
					//NOTE: be careful about adding strings into array as it will be putting in their references
				} catch (InterruptedException e) {
					try {
						msgQueue.addMessage(EXIT_STRING, Integer.toString(this.hashCode()));
					} catch (InterruptedException e1) {
					}
					interrupted = true;
				}
			} catch (IOException e) {
				//likely unexpected client exit
				try {
					msgQueue.addMessage(EXIT_STRING, Integer.toString(this.hashCode()));
				} catch (InterruptedException e1) {
				}
				sendErrorMessages("Error in reading message");
				interrupted = true;
			}
			
		}
			

		
	}
	
	public void sendMessage(String[] message) {
		if(!message[1].equals(Integer.toString(this.hashCode()))) {
			clientOut.println(message[0]);
		}
		
	}
	
	public void sendErrorMessages(String str) {
		clientOut.println("ERROR: " + str);
	}
	
//	public void close() {
//		try {
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public Socket getSocket() {
//		return socket;
//	}
}
