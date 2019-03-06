package client;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ClientGUI implements ClientUI{
	private PrintWriter output;
	private JFrame frame = new JFrame("ClientServerChat");
	private JPanel panel = new JPanel(new FlowLayout());
	private JButton sendBtn, exitBtn;
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
		
		SwingUtilities.invokeLater(() -> drawGUI());
		serverOut.println(username);
		
	}
	
	public void drawGUI() {
		sendBtn = new JButton("Send");
		sendBtn.setPreferredSize(new Dimension(230,80));
		
		exitBtn = new JButton("Exit");
		exitBtn.setPreferredSize(new Dimension(230,80));
			
		messageBox = new JTextArea();
		messageBox.setText("");
		messageBox.setAlignmentX(messageBox.RIGHT_ALIGNMENT);
		messageBox.setAlignmentY(messageBox.TOP_ALIGNMENT);
		messageBox.setBounds(10,300,460,75);
		msgBox = new JScrollPane(messageBox);
		msgBox.setPreferredSize(new Dimension(460,75));
		msgBox.setVisible(true);
				
		msgHistory = new JTextArea();
		msgHistory.setText("");
		msgHistory.setAlignmentX(messageBox.RIGHT_ALIGNMENT);
		msgHistory.setAlignmentY(messageBox.TOP_ALIGNMENT);
		msgHistory.setSize(220,270);
		msgHistory.setEnabled(false);
		msgHist = new JScrollPane(msgHistory);
		msgHist.setPreferredSize(new Dimension(220,270));
		msgHist.setVisible(true);
	
		groupHistory = new JTextArea();
		groupHistory.setText("");
		groupHistory.setAlignmentX(messageBox.LEFT_ALIGNMENT);
		groupHistory.setAlignmentY(messageBox.TOP_ALIGNMENT);
//		groupHistory.setBounds(10,0,230,270);
		groupHistory.setSize(220,270);
		groupHistory.setEnabled(false);
		grpMsgHist = new JScrollPane(groupHistory);
//		grpMsgHist.setBounds(10,0,230,270);
		grpMsgHist.setPreferredSize(new Dimension(220,270));
		grpMsgHist.setVisible(true);
		
		panel.add(grpMsgHist);
		panel.add(msgHist);
		panel.add(msgBox);
		panel.add(sendBtn);
		
		sendBtn.addActionListener(e -> sendMessage());
		
		panel.setSize(513,500);
		panel.setVisible(true);
		frame.add(panel);
		
		frame.setSize(525,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	
	public void sendMessage(ActionEvent e) {
		sendMessage(messageBox.getText());
		msgHistory.setText(msgHistory.getText() + "\n" + messageBox.getText());
		messageBox.setText("");
	}
	
	public void recieveMessage(String msg) {
		groupHistory.setText(groupHistory.getText() + "\n" + msg);
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
