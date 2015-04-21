import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import models.Message;
import models.OpCodes;
import models.Order;

import com.google.gson.Gson;
import com.sun.tools.javac.util.List;


public class ClientHandler implements Runnable {

	private Socket socket;
	private Gson gson;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private boolean connected;
	
	
	
	
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

	public Message getMessage(){
		
		String clientMessage = null;
		Message message = null;
		
		try {
			
			 clientMessage = inFromClient.readLine();
			 System.out.println("New message.");
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not read message from client "
					+ "in ClientHandler.");
		}
		
		if(clientMessage == null){
			
			connected = false;
			
		}else{
			
			message = gson.fromJson(clientMessage,Message.class);
			
		}
		
		
		
		return message;
		
	}


	@Override
	public void run() {
		
		if(setupCommunicationTools()){
			
			System.out.println("Communication Tools up!");
			
			connected = true;
			while(connected){
				
				
				Message message = getMessage();
				
				if(connected){
					
					System.out.println("Message type : "
					+ 	message.getType());
					System.out.println("Message message:  "
					+ message.getMessage());	
					
					switch (message.getType()) {
						
						case OpCodes.LOG_IN:
								//TODO Call login method
						
							break;
						case OpCodes.LOG_OUT:
								//TODO Call logout method
							break;
							
						case OpCodes.BUY_ORDER:
								//TODO Call Order method
							break;
							
						case OpCodes.SELL_ORDER:
								//TODO Call Order method
							break;
						
					
						default:
							break;
					}	
					
				}//if

					
				
				
			//TODO Get a user message with user types
	

				
			//TODO Add the user to the greeters list of users
				
			//TODO Wait for new messages from the user
				
				
				
	/*******************TEST CODE******************/
				
				
				/**
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
