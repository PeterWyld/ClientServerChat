package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JTextField;

public class SendButtonListener implements ActionListener{
	PrintWriter output;
	JTextField textBox;
	
	public SendButtonListener(PrintWriter output, JTextField textBox) {
		this.output = output;
		this.textBox = textBox;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		output.println(textBox.getText());
		System.out.println(textBox.getText());
		textBox.setText("");
		
	}

}
