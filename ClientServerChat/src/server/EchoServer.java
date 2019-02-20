package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoServer {

	// accept a connection
	// respond to the request 
	
	private ServerSocket in;
	
	public EchoServer(int port) {
		try {
			in = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void go() {
		try {
			System.out.println("Server listening");
			Socket s = in.accept();
			System.out.println("Server accepted connection on " + in.getLocalPort() + " ; " + s.getPort() );
			InputStreamReader r = new InputStreamReader(s.getInputStream());
			BufferedReader clientIn = new BufferedReader(r);
			PrintWriter clientOut = new PrintWriter(s.getOutputStream(), true);
			
			while(true) {
				String userInput = clientIn.readLine();
				clientOut.println(userInput);
			}		
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new EchoServer(14001).go();
	}
}