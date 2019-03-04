package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConsole extends Thread {
	private BufferedReader serverIn;
	private PrintWriter serverOut;
	private BufferedReader userIn;
	private Socket server;
	private String username;

	public ClientConsole(String address, int port, String username) {
		this.username = username;
		try {
			server = new Socket(address, port);
			userIn = new BufferedReader(new InputStreamReader(System.in));
			serverOut = new PrintWriter(server.getOutputStream(), true);
			serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println(address + " is not a valid address.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void go() {
		String userInput = "";
		this.start();
		try {
			serverOut.println(username);
			while ((userInput = userIn.readLine()) != null) {
				serverOut.println(userInput);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				server.close();
				// closing the socket
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		boolean uninterrupted = true;
		String serverRes = "";
		while (uninterrupted) {
			
			try {
				serverRes = serverIn.readLine();
				if (serverRes == null) {
					uninterrupted = false;
				}

			} catch (IOException e) {
				uninterrupted = false;
				serverOut.println("EXIT");
				System.out.println("You have been disconnected from the server");
				System.exit(0);
			}
			System.out.println(serverRes);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				uninterrupted = false;
			}
		}

	}
}
