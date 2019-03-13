

public class ChatClient {
	
	/**
	 * Uses the arguments given in the command to construct the appropriate client
	 * @param args
	 */
	public static void main(String[] args) {
		int port = 14001;
		String host = "localhost";
		boolean gui = false;

		// Iterating over the arguments given
		for (int i = 0; i <= args.length - 1; i++) {
			if (args[i].charAt(0) == '-') {
				
				switch (args[i]) {
					case "-ccp":
						if (args.length-1 > i) {
							try {
								port = Integer.parseInt(args[i + 1]);
								i++;
								if (port < 1024 || port > 65535) {
									System.out.println("Invalid port.");
									System.exit(0);
								}
							} catch (NumberFormatException e) {
								System.out.println("Port requires an integer arguement");
								System.exit(0);
							}
						} else {
							System.out.println("The tag '-ccp' requires an arguement");
							System.exit(0);
						}
						break;
					
					case "-cca":
						if (args.length-1 > i) {
							host = args[i + 1];
							i++;
						} else {
							System.out.println("The tag '-cca' requires an arguement");
							System.exit(0);
						}
						break;
					
					case "-gui":
						gui = true;
						break;
						
					default:
						System.out.println("Tag not recognised: " + args[i]);
						break;
				}
			}
		}
		
		if(gui) {
			new ClientGUI(host, port).go();
		} else {
			new ClientConsole(host, port).go();
		}
	}
}
