package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ChatClient {
	public static void main(String[] args) {
		int port = 14001;
		String host = "localhost";
		boolean gui = false;

		// Iterating over the arguements given
		for (int i = 0; i <= args.length - 1; i++) {
			if (args[i].charAt(0) == '-') {
				switch (args[i]) {
				case "-ccp":
					if (args.length > i) {
						try {
							port = Integer.parseInt(args[i + 1]);
							i++;
						} catch (NumberFormatException e) {
							System.out.println("Port requires an integer arguement");
						}
					} else {
						System.out.println("The tag '-ccp' requires an arguement");
					}
					break;
				case "-cca":
					if (args.length > i) {
						host = args[i + 1];
						i++;
					} else {
						System.out.println("The tag '-cca' requires an arguement");
					}
				case "-gui":
					gui = true;
				default:
					System.out.println("Tag not recognised: " + args[i]);
					break;
				}
			}
		}

		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("What is your username?");
		String user;
		try {
			user = input.readLine();
		} catch (IOException e) {
			user = "ANONYMOUS";
		}
		
		if(gui) {
			new ClientGUI(host, port, user);
		} else {
			new ClientConsole(host, port, user).go();
		}
	}
}
