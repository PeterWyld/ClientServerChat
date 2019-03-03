package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ClientGUI implements ClientUI, ActionListener{
	private PrintWriter output;
	private JFrame frame = new JFrame("ClientServerChat");
	private JButton sendBtn;
	private JTextArea messageBox, msgHistory, groupHistory;
	private JScrollPane msgBox, msgHist, grpMsgHist;
	
	private String username;
	private Socket server;
	private PrintWriter serverOut;
	private BufferedReader serverIn;
	
	public ClientGUI(String address, int port, String username) {
		this.username = username;
		try {
			server = new Socket(address, port);
			serverOut = new PrintWriter(server.getOutputStream(), true);
			serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println(address + " is not a valid address.");
			System.exit(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		serverOut.println(username);
		
	}
	
	public void drawGUI() {
		sendBtn = new JButton("Send");
		sendBtn.setBounds(10,375,460,80);
		frame.add(sendBtn);
		
		messageBox = new JTextArea();
		messageBox.setText("");
		messageBox.setAlignmentX(messageBox.RIGHT_ALIGNMENT);
		messageBox.setAlignmentY(messageBox.TOP_ALIGNMENT);
		messageBox.setBounds(10,300,460,75);
		msgBox = new JScrollPane(messageBox);
		msgBox.setBounds(10,300,460,75);
		msgBox.setVisible(true);
		frame.add(msgBox);
		
		msgHistory = new JTextArea();
		msgHistory.setText("");
		msgHistory.setAlignmentX(messageBox.RIGHT_ALIGNMENT);
		msgHistory.setAlignmentY(messageBox.TOP_ALIGNMENT);
		msgHistory.setBounds(250,0,220,270);
		msgHistory.setEnabled(false);
		msgHist = new JScrollPane(msgHistory);
		msgHist.setBounds(250,0,220,270);
		msgHist.setVisible(true);
		frame.add(msgHist);
		
		groupHistory = new JTextArea();
		groupHistory.setText("");
		groupHistory.setAlignmentX(messageBox.LEFT_ALIGNMENT);
		groupHistory.setAlignmentY(messageBox.TOP_ALIGNMENT);
		groupHistory.setBounds(10,0,230,270);
		groupHistory.setEnabled(false);
		grpMsgHist = new JScrollPane(groupHistory);
		grpMsgHist.setBounds(10,0,230,270);
		grpMsgHist.setVisible(true);
		frame.add(grpMsgHist);
		
		sendBtn.addActionListener(this);

		frame.setSize(500,500);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		messageBox.getText();
		sendMessage(messageBox.getText());
		msgHistory.setText(msgHistory.getText() + "\n" + messageBox.getText());
		messageBox.setText("");
	}
	
	public void recieveMessage(String msg) {
		groupHistory.setText(groupHistory.getText() + "\n" + msg);
	}
	
	public void sendMessage(String msg) {
		output.println(msg);
	}
	
	public static void main(String[] args) {
		ClientGUI gui = new ClientGUI("localhost", 14001, "bargles");
	}

	@Override
	public String recieveMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
