package client;

import java.io.BufferedReader;
import java.io.PrintWriter;

public interface ClientUI {
	
	public abstract String recieveMessage();
	
	public void sendMessage(String msg);
}
