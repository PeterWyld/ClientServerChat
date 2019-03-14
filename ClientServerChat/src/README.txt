CHATCLIENT
	Arguments
		-cca (change client address)
			Changes the ip address from "localhost" 
			to the ip address specified by the user.
		-ccp (change client port)
			Changes the port it will try to connect
			to from the default port (14001) to an
			integer that the user specifies.
		-gui
			Tells the program to switch to the
			graphical user interface (instead of
			using the console UI).
	Commands
		EXIT and (CTRL D in linux console)
			When the user enters "EXIT" (which is
			case sensitive) the server will attempt
			to disconnect the user. When this happens
			other users will see "<username> has left
			the server" to show that you have disconnected.
	Misc
		You will not have your own messages
		sent back to you, only other users.

CHATSERVER
	Arguments
		-csp (change server port)
			Changes the port from which the server
			accepts clients from the default port 
			(14001) to an integer that the user 
			specifies.
	Commands
		EXIT
			When the user enters "EXIT" (which is
			case sensitive) the server will attempt
			to disconnect all users before shutting
			down.