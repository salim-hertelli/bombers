package bombers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public Server() throws IOException {
		start();
	}
	
	private void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(0);
		//serverSocket.bind(new InetSocketAddress("127.0.0.1",0));
		try {
			System.out.println(serverSocket.getInetAddress());
			System.out.println("Server successfully started.\nServer listening at port " + serverSocket.getLocalPort());
			while (true) {
				Socket acceptedSocket = serverSocket.accept();
				System.out.println("Connection established with new host.");
				
				PrintWriter out = new PrintWriter(acceptedSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(acceptedSocket.getInputStream()));
				new Thread(new ClientHandler(in, out)).start();
			}
		} finally {
			serverSocket.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		new Server();
	}
}
