package bombers;

import java.net.*; 
import java.io.*; 


public class Client {
	private BufferedReader input = null;
	private PrintWriter output = null;
	private BufferedReader toSend = null; 
	
	public class Reader extends Thread {
		public void run() { 
			String msg = "";
			while(!msg.equals("exit")) {
				try {
					msg = input.readLine(); 
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
			while (!msg.equals("exit")) {
				while (!(msg.equals("Send")||msg.equals("exit"))) { 
					try
					{ 
						msg = toSend.readLine(); 
						output.println(msg);
					} catch(IOException i) { 
	                	System.out.println(i); 
					}
				}
				output.flush();
			}
		}
	}
	
	public Client(String destIp, int destPort) throws IOException, InterruptedException {
		Socket socket = new Socket(destIp, destPort);
		//socket.bind(new InetSocketAddress("192.168.137.1", 0));
		
		System.out.println("Connection established.");
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream(), true); 
		toSend = new BufferedReader(new InputStreamReader(System.in));
		
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
		Client a = new Client("192.168.137.89", 49746);
	}	
}
