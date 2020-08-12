package bombers.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
				if (nextLine.trim().equalsIgnoreCase("get")) {
					clients.forEach((name,out) -> {
						out.println(name);
					});
					continue;
				};
				String message = java.time.LocalTime.now() + " " + this.username + ": " + nextLine;
				System.out.println(message);
				for (String otherUsername : clients.keySet()) {
					if (!otherUsername.equals(this.username)) {
						clients.get(otherUsername).println(message);
					}
				}
			} 
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			System.out.println("Player " + username + " disconnected.");
			out.close();
			clients.remove(username);
		}
	}
}