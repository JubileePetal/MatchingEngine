import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import models.Order;

import com.google.gson.Gson;
import com.sun.tools.javac.util.List;


public class ClientHandler implements Runnable {

	private Socket socket;
	private Gson gson;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	
	
	
	
	public ClientHandler(Socket clientSocket){
		
		
		socket = clientSocket;
		
	}
	
	
	public boolean setupCommunicationTools(){
			
		gson = new Gson();
			
			inFromClient = null;
			outToClient  = null;
			
        try {
			inFromClient =
					new BufferedReader
						(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not set up BufferedReader inFromClient"
					+ " in ClientHandler");
			return false;
		}

        try {
			outToClient =
					new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not set up DataOutputStream "
					+ "outToClient in ClientHandler");
			return false;
		}
		
		return true;
	}



	@Override
	public void run() {
		
		if(setupCommunicationTools()){
			
			System.out.println("Communication Tools up!");
			while(true){
				
	/*******************TEST CODE******************/
				
				
				
				String clientMessage = null;
				
				try {
					
					 clientMessage = inFromClient.readLine();
					 System.out.println("New message.");
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
		
				Order order = gson.fromJson(clientMessage,Order.class);
				System.out.println("Client message: " + clientMessage);
				
				
	/*********************************************/
				
				
			}//while
			
		}
		
	}
}
