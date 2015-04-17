import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import com.google.gson.Gson;


public class Greeter implements Runnable {
	
	private HashMap<String,ClientHandler> admins;
	private HashMap<String,ClientHandler> traders;
	private HashMap<String,ClientHandler> isvrs;
	private HashMap<String,ClientHandler> regulators;
	
	private ServerSocket welcomeSocket;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	

	
	public Greeter() {
		
		if(createWelcomeSocket()){
			run();
		}
	}

	public boolean createWelcomeSocket(){
		
		try {
			welcomeSocket = new ServerSocket(1337);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not create welcomeSocket in Greeter.");
			return false;
		}
			
		return true;
	}
	
	public void createNewClientHandler(){
		
	}
	
	
	@Override
	public void run() {
		
		
		while(true){
			
			Socket newClientSocket = waitForNewClients();
			
			if(newClientSocket != null){
				
				ClientHandler newClientHandler = new ClientHandler();
				
			}

			
		}
		
		
	}
	
	
	
	public void Greet(){
		// TODO Greet until otherwise is true.
	}
	
	public void Goodbye(){
		//TODO Shut down
		
	}
	
	private Socket waitForNewClients(){
		
		Socket socket = null;
		try {
			 socket = welcomeSocket.accept();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not accept new "
					+ "connection from client in Greeter.");
		}
		
		return socket;
		
	}
	/**
	public void createSocket(){
	
		try {
			serverSocket = new ServerSocket(1337);
			
			while(true){
				
				Socket socket = serverSocket.accept();
				System.out.println("Katt");
				String clientMessage;
				String capitalizedSentence;
				
				Gson gson = new Gson();
									
				
				
	            BufferedReader inFromClient =
	               new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
	            clientMessage = inFromClient.readLine();
	            System.out.println("Received: " + clientMessage);
	            capitalizedSentence = clientMessage.toUpperCase() + '\n';
	            outToClient.writeBytes(capitalizedSentence);
	  
	            Order order = gson.fromJson(clientMessage,Order.class);
	            System.out.println(order.getMessage());
	            Currency curr = order.currency;
	            Instrument inst = order.instrument;
	            System.out.println(inst.name);
	            
	            
	            
			}	
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	} **/
}
