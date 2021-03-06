

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * A runnable class that reads in messages from the server and then outputs them to the output of the PrintWriter
 * <br> Intended to be used with a Thread
 */
public class ClientMsgReceiver implements Runnable {
	private BufferedReader serverIn;
	private PrintWriter serverOut;
	private PrintWriter clientOutput;
	
	public ClientMsgReceiver(BufferedReader serverIn, PrintWriter serverOut, PrintWriter clientOutput) {
		this.serverIn = serverIn;
		this.serverOut = serverOut;
		this.clientOutput = clientOutput;
	}

	/**
	 * Continuously reads in messages from the server until either the server disconnects it, it's interrupted or there is an IOException
	 * <br> Messages read from the server are then printed using the printwriter
	 */
	public void run() {
		boolean uninterrupted = true;
		String serverMsg = "";
		
		try {
			while (uninterrupted && (serverMsg = serverIn.readLine()) != null) {

				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					uninterrupted = false;
				}
				
				clientOutput.println(serverMsg);
				
			}
		} catch (IOException e) {
		}
		
		//Closing connections and ending program.		
		serverOut.println("EXIT");
		System.out.println("You have been disconnected from the server");
		
		try {
			serverIn.close();
			serverOut.close();
			clientOutput.close();
		} catch (IOException e) {
		}
		
		System.exit(0);
	}
}
