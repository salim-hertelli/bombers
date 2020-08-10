package bombers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler implements Runnable {
	private static Set<BufferedWriter> clients = new HashSet();
	private BufferedReader in;
	private BufferedWriter out;
	
	public ClientHandler(InputStream in, OutputStream out) {
		this.in = new BufferedReader(new InputStreamReader(in));
		this.out = new BufferedWriter(new OutputStreamWriter(out));
	}
	
	public void run() {
		try {
			out.write("Welcome to bombers");
			while (true) {
				String nextLine = in.readLine();
				System.out.println(nextLine);
			} 
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}