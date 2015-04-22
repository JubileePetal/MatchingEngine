package communications;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Observable;

import models.Message;
import models.OpCodes;
import models.Order;
import models.User;

import com.google.gson.Gson;



public class ClientHandler extends Observable implements Runnable {

	private Socket socket;
	private Gson gson;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	private boolean connected;
	private Greeter greeter;
	private String username;
	
	
	public ClientHandler(Socket clientSocket, Greeter greetedBy){
		
		greeter = greetedBy;
		socket = clientSocket;
		
	}
		
	public boolean setupCommunicationTools(){
			
		gson = new Gson();
			
		inFromClient = null;
		outToClient  = null;
		
		boolean readerOK = setupClientReader();
		boolean writerOK = setupClientWriter();
			
		return (readerOK && writerOK);
	}

	public boolean setupClientWriter() {
        
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

	public boolean setupClientReader() {
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
		
        return true;
	}

	public Message receiveMessage(){
		
		String clientMessage;
		Message message = null;
		clientMessage = readFromClient();
		
		if(clientMessage == null){
			
			connected = false;
			System.out.println("Client disconnected.");
			
		}else{
			
			message = unpackMessage(clientMessage);
			
		}
		return message;
		
	}

	public Message unpackMessage(String clientMessage){
		
		return gson.fromJson(clientMessage,Message.class);
	}

	public void addUserToGreeter(Message message){
		
		User user =  gson.fromJson(message.getJson(),User.class);
		this.username = user.getUsername();
		
		/** add user to the right list in greeter*/
		switch (user.getUserType()) {
			case OpCodes.TRADER:
				greeter.addTrader(user.getUsername(), this);
				System.out.println("Trader accepted.");
				loginResponse(OpCodes.LOG_IN_ACCEPTED);
				break;
				
			case OpCodes.REGULATOR:
				greeter.addRegulator(user.getUsername(), this);
				loginResponse(OpCodes.LOG_IN_ACCEPTED);
				break;
				
			case OpCodes.ADMIN:
				greeter.addAdmin(user.getUsername(), this);
				loginResponse(OpCodes.LOG_IN_ACCEPTED);
				break;
			
			case OpCodes.ISVR:
				greeter.addIsvr(user.getUsername(), this);
				loginResponse(OpCodes.LOG_IN_ACCEPTED);
				break;
			
			default:
				loginResponse(OpCodes.LOG_IN_REJECTED);
				break;
			}
		
			
			
	}
	
	private void loginResponse(int loginStatus){
		
		Message message = new Message();
		message.setType(loginStatus);
		message.setJson(getInstruments());
		String stringMessage = gson.toJson(message);
		
		writeToClient(stringMessage);
		

		
	}
	
	public String getInstruments(){
		
		String instrumentMessage = gson.toJson(greeter.getInstruments());
		
		
		return instrumentMessage;
	}
	
	private void writeToClient(String stringMessage) {
			
		try {
			outToClient.writeBytes(stringMessage + '\n');
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not writeToClient in ClientHandler");

		}
		
	}
	
	private String readFromClient() {
		
		String clientMessage = null;
		
		try {
			
			 clientMessage = inFromClient.readLine();
			 System.out.println("New message.");
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not read message from client "
					+ "in ClientHandler.");
		}
		return clientMessage;
	}

	public void update(){
		
		setChanged();
		notifyObservers(this);
	}
	
	public void getUniqeOrderID(){
		
		
	}
	
	public void newOrder(Message message){
		
		Order order =  gson.fromJson(message.getJson(),Order.class);
		
		
	}
	
	public void run() {
		
		if(setupCommunicationTools()){
			
			System.out.println("Communication Tools up!");
			
			connected = true;
			while(connected){
				
				
				Message message = receiveMessage();
				
				if(connected){
					
					/*********PRINT STUFF********/
//					System.out.println("Message type : "
//					+ 	message.getType());
//					System.out.println("Message message:  "
//					+ message.getJson());	
					/*****************************/
					
					
					switch (message.getType()) {
						
						case OpCodes.LOG_IN:
							addUserToGreeter(message);
							break;
							
						case OpCodes.LOG_OUT:
								//TODO Call logout method
							break;
							
						case OpCodes.ORDER:
								//TODO Call Order method
							System.out.println("Got an order......");
							
								//TODO  Notify Observers
							break;
					
						default:
							break;
					}	
					
					/***********TEST*************/
					
					
					
					
					/****************************/
					
				}//if


				
				
				
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
