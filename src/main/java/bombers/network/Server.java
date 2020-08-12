package bombers.network;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	static private final String DEFAULT_IP_ADDRESS = "127.0.0.1";
	static private final int DEFAULT_PORT = 0;
	
	private String ipAddress;
	private int portNumber;
	
	public Server(String ipAddress, int portNumber) {
		this.ipAddress = ipAddress;
		this.portNumber = portNumber;
	}
	
	public Server() {
		ipAddress = DEFAULT_IP_ADDRESS;
		portNumber = DEFAULT_PORT;
	}

	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket();
			serverSocket.bind(new InetSocketAddress(ipAddress,portNumber));
			
			System.out.println(serverSocket.getInetAddress());
			System.out.println("Server successfully started.\nServer listening at port " + serverSocket.getLocalPort());
			while (true) {
				Socket acceptedSocket = serverSocket.accept();
				System.out.println("Connection established with new host.");
				
				PrintWriter out = new PrintWriter(acceptedSocket.getOutputStream(), true);
				BufferedReader in = new BufferedReader(new InputStreamReader(acceptedSocket.getInputStream()));
				new Thread(new ClientHandler(in, out)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
		new Server("192.168.137.1",0).start();
	}
}
