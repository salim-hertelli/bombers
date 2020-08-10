package bombers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable {
	private static Set<BufferedWriter> clients = new HashSet();
	private BufferedReader in;
	private PrintWriter out;
	
	public ClientHandler(BufferedReader in, PrintWriter out) {
		this.in = in;
		this.out = out;
	}
	
	public void run() {
		try {
			out.println("Welcome to bombers chat server");
			out.println("Choose your id");
			while (true) {
				String nextLine = in.readLine();
				System.out.println(nextLine);
			} 
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}