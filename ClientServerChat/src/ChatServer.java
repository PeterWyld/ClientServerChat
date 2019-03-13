
public class ChatServer {
	/**
	 * Creates a new server taking an argument to set the port (default 14001)
	 */
	public static void main(String[] args) {
		int port = 14001;
		
		for(int i = 0; i <= args.length -1; i++) {
			if (args[i].charAt(0) == '-') {
				switch(args[i]) {
					case "-csp": 
						if (args.length-1 > i) {
							try {
								port = Integer.parseInt(args[i+1]);
								i++;
								if (port < 1024 || port > 65535) {
									System.out.println("Invalid port.");
									System.exit(0);
								}
							} catch (NumberFormatException e) {
								System.out.println("-csp requires an integer arguement");
								System.exit(0);
							}
						} else {
							System.out.println("The tag '-csp' requires an arguement");
							System.exit(0);
						}
						break;
						
					default: 
						System.out.println("Tag not recognised: " + args[i]);
						break;
				}
			}
		}
		new Server(port).go();
	}
}
