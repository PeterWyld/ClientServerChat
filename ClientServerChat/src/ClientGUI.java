

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
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

/**
 * A client that utilises a basic GUI to send and receive messages from a connected server
 * 
 * Uses a separate thread to receive messages from the server 
 */
public class ClientGUI{
	private JFrame frame = new JFrame("ClientServerChat");
	private JPanel panel = new JPanel(new FlowLayout());
	private JPanel msgHistPanel = new JPanel(new FlowLayout());
	private JButton sendBtn, exitBtn;
	private JTextArea messageBox = new JTextArea();
	private JTextArea msgHistory = new JTextArea();
	private JTextArea groupHistory = new JTextArea();
	private JScrollPane msgBox, msgHist;
	
	private Socket server;
	private PrintWriter serverOut;
	private BufferedReader serverIn;
	
	private Thread msgReader;
	
	private PrintWriter userOut = new PrintWriter(new LinkedTextOutStream(msgHistory,
			new JTextArea[] {groupHistory}, '\n'), true);
	
	/**
	 * The constructor for this class attempts to connect to the server (using the address and port parameters). <br>
	 * The method will end the program if it is unable to connect to the server for whatever reason.
	 * @param address The IP address of the server
	 * @param port The port of the server
	 * 
	 * This code is a copy of the code in chat client because creating the socket in a different class seemed to
	 * cause problems (I don't know why) and it also seems neater that the connect is created in the class that uses
	 * it.
	 */
	public ClientGUI(String address, int port) {
		try {
			server = new Socket(address, port);
			serverOut = new PrintWriter(server.getOutputStream(), true);
			serverIn = new BufferedReader(new InputStreamReader(server.getInputStream()));
		} catch (UnknownHostException e) {
			System.out.println(address + " is not a valid address.");
			System.exit(0);
		} catch (IOException e) {
			System.out.println("Could not connect to server");
			System.exit(0);
		}
		
	}
	
	/**
	 * Adds the gui drawing to the EDT and starts the msgReader thread
	 */
	public void go() {
		SwingUtilities.invokeLater(() -> drawGUI());
		
		//giving the gui some time to be created before the receiving messages from the server
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		
		//Sets up the thread to print to the groupHistory and also print newlines in msgHistory whenever they appear in messages
		msgReader = new Thread(new ClientMsgReceiver(serverIn, serverOut,
				new PrintWriter(new LinkedTextOutStream(groupHistory, new JTextArea[] {msgHistory}, '\n'), true)));
		msgReader.start();

		
	}
	
	/**
	 * Draws the gui and adds actionListeners to the two buttons
	 */
	private void drawGUI() {
		sendBtn = new JButton("Send");
		sendBtn.setPreferredSize(new Dimension(200,80));
		
		exitBtn = new JButton("Exit");
		exitBtn.setPreferredSize(new Dimension(200,80));
		
		messageBox.setText("");
		messageBox.setAlignmentX(JTextArea.RIGHT_ALIGNMENT);
		messageBox.setAlignmentY(JTextArea.TOP_ALIGNMENT);
		messageBox.setBounds(10,300,460,75);
		msgBox = new JScrollPane(messageBox);
		msgBox.setPreferredSize(new Dimension(460,75));
		msgBox.setVisible(true);
				
		msgHistory.setText("");
		msgHistory.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
		msgHistory.setAlignmentY(JTextArea.TOP_ALIGNMENT);
		msgHistory.setColumns(23);
		msgHistory.setEnabled(false);
	
		groupHistory.setText("");
		groupHistory.setAlignmentX(JTextArea.LEFT_ALIGNMENT);
		groupHistory.setAlignmentY(JTextArea.TOP_ALIGNMENT);
		groupHistory.setColumns(23);
		groupHistory.setEnabled(false);
		
		msgHistPanel.add(msgHistory);
		msgHistPanel.add(groupHistory);
		
		msgHist = new JScrollPane(msgHistPanel);
		msgHist.setPreferredSize(new Dimension(500,270));
		msgHist.setVisible(true);
		
		panel.add(msgHist);
		panel.add(msgBox);
		panel.add(sendBtn);
		panel.add(exitBtn);
		
		sendBtn.addActionListener(e -> sendMessage());
		exitBtn.addActionListener(e -> disconnect());
		
		panel.setSize(513,500);
		panel.setVisible(true);
		frame.add(panel);
		
		frame.setSize(525,500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
	}
	
	/**
	 * Sends the message to the server and adds it to the user's text area
	 * <br> Used as an ActionListener by the send button
	 */
	private void sendMessage() {
		userOut.println(messageBox.getText());
		serverOut.println(messageBox.getText());
		messageBox.setText("");
	}
	
	/**
	 * Closes the connection, interrupts the message receiver and closes the program
	 * <br> Used as an ActionListener by the exit button
	 */
	private void disconnect() {
		msgReader.interrupt();
		serverOut.println("EXIT");
		
		try {
			server.close();
		} catch (IOException e) {
		}
		try {
			serverIn.close();
		} catch (IOException e) {
		}
		serverOut.close();
		
		System.exit(0);
	}
	
	/**
	 * This main class is just used for testing
	 */
	public static void main(String[] args) {
		ClientGUI gui = new ClientGUI("localhost", 14001);
		gui.go();
	}
}
