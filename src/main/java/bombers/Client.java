package bombers;

import java.net.*; 
import java.io.*; 


public class Client {
	private DataInputStream input = null;
	private DataOutputStream output = null;
	private DataInputStream  toSend   = null; 
	
	public class Reader extends Thread {
		public void run() { 
			String msg = "";
			while(!msg.equals("exit")) {
				try {
					msg = new BufferedReader(new InputStreamReader(input)).readLine(); 
					System.out.println(msg);
				}catch(Exception i) {
					System.out.println(i);
				}
			}
	    }
	}
	
	public class Sender extends Thread {
		public void run() {
			String msg = "";
			while (!msg.equals("exit"))
				while (!(msg.equals("Send")||msg.equals("exit"))) { 
					try
					{ 
						msg = new BufferedReader(new InputStreamReader(toSend)).readLine(); 
						new BufferedWriter(new OutputStreamWriter(output)).write(msg);
						System.out.println("printed out: " + msg);
					}catch(IOException i) { 
	                	System.out.println(i); 
					}
				}try {
					output.flush();
				}catch (IOException e) {
					System.out.println(e);
				}
		}
	}
	
	public Client(String destIP, int destPort) throws IOException, InterruptedException {
		Socket socket = null;
		System.out.println(".");

		socket = new Socket();
		socket.bind(new InetSocketAddress("0.0.0.0", 0));
		socket.connect(new InetSocketAddress(destIP, destPort));
		
		System.out.println("Connection established.");
		input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		output = new DataOutputStream(socket.getOutputStream()); 
		toSend = new DataInputStream(System.in);
		
		Reader getter = new Reader();
		getter.start();
		
		Sender sender = new Sender();
		sender.start();
		
		getter.join();
		sender.join();
		
		socket.close();
		System.out.println("Connection closed.");	
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Client a = new Client("0.0.0.0", 54791);
	}
	
}
