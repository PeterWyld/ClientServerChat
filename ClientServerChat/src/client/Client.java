package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private Socket server;

	public Client(String address, int port) {
		try {
			server = new Socket(address,port);
			System.out.println("done");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void go() {
		try {
			BufferedReader userIn = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter serverOut = new PrintWriter(server.getOutputStream(), true);
			BufferedReader serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
			
			while(true) {
				String userInput = userIn.readLine();
				serverOut.println(userInput);
				String serverRes = serverIn.readLine();
				System.out.println(serverRes);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				server.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		for(int i = 0; i <= args.length -1; i++) {
			if (args[i].charAt(0) == '-') {
				switch(args[i]) {
					case "-csp": 
						Utilities.parseIntDefault(args[i+1], 14006);
						i++;
				}
			}
		}
		new Client("localhost", 14006).go();
	}
}