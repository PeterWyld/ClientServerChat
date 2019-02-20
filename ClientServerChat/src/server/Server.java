package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server extends Thread{
	private ServerSocket in;
	private ArrayList<ClientThread> clients = new ArrayList<ClientThread>();
	private SyncedMsgQueue messages = new SyncedMsgQueue();
	private Thread clientAdder;
	private Thread messageReader;
	
	public Server(int port) {
		try {
			in = new ServerSocket(port);
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void go() {
		clientAdder = new Thread(new ClientAdder(clients, messages, in));
		clientAdder.start();
		messageReader = new Thread(new ServerMsgReader(clients, messages));
		messageReader.start();
		
		InputStreamReader r = new InputStreamReader(System.in);
		BufferedReader serverIn = new BufferedReader(r);
		
		while(true) {
			try {
				serverIn.readLine();
				
				

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
//				try {
//					in.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
			}
		}
		
	}
	
	@Override
	public void run() {
		
	}
	
	
	public static void main(String[] args) {
		new Server(14001).go();
	}
}
