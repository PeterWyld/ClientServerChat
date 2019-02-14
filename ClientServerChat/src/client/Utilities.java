package client;

public class Utilities {
	public static int parseIntDefault(String newInt, int defaultInt) {
		try {
			return Integer.parseInt(newInt);
		} catch (NumberFormatException e) {
			return defaultInt;
		}
	}
	
}
