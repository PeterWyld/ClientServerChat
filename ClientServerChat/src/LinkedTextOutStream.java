

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JTextArea;


/**
 * This is class that can be used as an outputstream by a printwriter to println to a textbox. In addition to
 * this it also prints a linkedChar to any linkedTextAreas whenever they appear in the stream
 * (E.g. if the linked char is a newline character then the class will print a newline to all textareas when it appears)
 * <br><br>
 * 
 * Code modified from:
 * https://www.codejava.net/java-se/swing/redirect-standard-output-streams-to-jtextarea
 * @author www.codejava.net 
 */
public class LinkedTextOutStream extends OutputStream {
	private JTextArea mainTextArea;
	private JTextArea[] linkedTextAreas;
	private char linkedChar;
	
	public LinkedTextOutStream(JTextArea mainTextArea, JTextArea[] linkedTextAreas, char linkedChar) {
		this.mainTextArea = mainTextArea;
		this.linkedTextAreas = linkedTextAreas;
		this.linkedChar = linkedChar;
	}
	
	/**
	 * Writes every character to the mainTextArea and if the character == the linkedChar then it prints it to all textAreas
	 */
	@Override
	public void write(int b) throws IOException {
		if (b != 0) {
			mainTextArea.append(String.valueOf((char) b));
			mainTextArea.setCaretPosition(mainTextArea.getDocument().getLength());
			
			if ((char) b == linkedChar)  {
				for(int i = 0; i <= linkedTextAreas.length -1; i++) {
					linkedTextAreas[i].append(String.valueOf(linkedChar));
					linkedTextAreas[i].setCaretPosition(linkedTextAreas[i].getDocument().getLength());
				}
			}
		} 
	}

}
