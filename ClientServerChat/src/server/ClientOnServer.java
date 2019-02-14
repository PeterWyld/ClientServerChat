package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientOnServer implements Runnable{
	private Socket socket;
	private BufferedReader clientIn;
	private PrintWriter clientOut;
	private ArrayList<String> chat;
	private String ClientName = "ANONYMOUS";
	
	public ClientOnServer(Socket socket) {
		try {
			clientIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			clientOut = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		String userInput;
		try {
			while((userInput = clientIn.readLine()) != "cmd:exit") {
				chat.add(ClientName + ": " + userInput); 
				//NOTE be careful about adding strings into array as it will be putting in their references
			}
			
		} catch (IOException e) {
			sendErrorMessages("Error in reading message");
		}
		
	}
	
	public void sendMessages(String str) {
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
	
}
