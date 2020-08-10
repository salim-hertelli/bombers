package bombers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ClientHandler implements Runnable {
	private static Map<String, PrintWriter> clients = new HashMap();
	private BufferedReader in;
	private PrintWriter out;
	private String username;
	
	public ClientHandler(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	private static boolean usernameAvailable(String username) {
		return !clients.containsKey(username);
	}
	
	public void run() {
		try {
			out.println("Welcome to bombers chat server");
			out.println("Choose your id");
			String username = "";
			while (true) {
				username = in.readLine();
				if (usernameAvailable(username)) {
					break;
				} else {
					out.println("Sorry this username is already used.");
					out.println("Choose another username");
				}
			}
			this.username = username;
			clients.put(this.username, out);
			
			while (true) {
				String nextLine = in.readLine();
				String message = java.time.LocalTime.now() + " " + this.username + ": " + nextLine;
				for (String otherUsername : clients.keySet()) {
					if (!otherUsername.equals(this.username)) {
						clients.get(otherUsername).println(message);
					}
				}
			} 
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}