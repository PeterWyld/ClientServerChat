package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ClientMsgReceiver implements Runnable {
	private BufferedReader serverIn;
	private PrintWriter serverOut;
	
	public ClientMsgReceiver(BufferedReader serverIn, PrintWriter serverOut) {
		this.serverIn = serverIn;
		this.serverOut = serverOut;
	}

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
