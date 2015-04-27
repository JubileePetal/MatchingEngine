package communications;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

import models.Message;
import models.OpCodes;
import models.Order;
import models.User;

import com.google.gson.Gson;

import controller.Trade;



public class ClientHandler extends Observable implements Runnable {

	private Socket socket;
	private Gson gson;
	private boolean connected;
	private Greeter greeter;
	private String username;
	
	private Sender sender;
	private Receiver receiver;
	private ArrayList<Order> myOrders;
	
	public ClientHandler(Socket clientSocket, Greeter greetedBy){
		
		myOrders	 = new ArrayList<Order>();
		greeter 	 = greetedBy;
		socket 		 = clientSocket;
		sender 		 = new Sender(this);
		receiver  	 = new Receiver(this);
		gson 		 = new Gson();
		
	}
	
	public boolean setupCommunicationTools(){
			
		sender.setSocket(socket);
		receiver.setSocket(socket);
		
		return (sender.initiateStream() && receiver.initiateStream());
	}
	
	public int addUserToGreeter(Message message){
		
		User user =  gson.fromJson(message.getJson(),User.class);
		this.username = user.getUsername();
		
		int userStatus;
	
		switch (user.getUserType()) {
			case OpCodes.TRADER:
				greeter.addTrader(username, this);
				System.out.println("Trader accepted.");
				userStatus = OpCodes.LOG_IN_ACCEPTED;
				break;
				
			case OpCodes.REGULATOR:
				greeter.addRegulator(username, this);
				userStatus = OpCodes.LOG_IN_ACCEPTED;
				break;
				
			case OpCodes.ADMIN:
				greeter.addAdmin(username, this);
				userStatus = OpCodes.LOG_IN_ACCEPTED;
				break;
			
			case OpCodes.ISVR:
				greeter.addIsvr(username, this);
				userStatus = OpCodes.LOG_IN_ACCEPTED;
				break;
			
			default:
				userStatus = OpCodes.LOG_IN_REJECTED;
				break;
			}
		
		return userStatus;
	}		
	
	public String getValidInstruments(){
		return gson.toJson(greeter.getInstruments());
	}
	
	public void update(Order order){
		
		setChanged();
		notifyObservers(order);
	}
	
	public Order unpackOrder(Message message){
		
		Order order =  gson.fromJson(message.getJson(),Order.class);
		myOrders.add(order);
		order.setId(greeter.getUniqueOrderID());
		return order;
	}
	
	public void sendTrade(Trade trade) {
		
		String json = gson.toJson(trade);
		sender.sendToClient(OpCodes.TRADE, json);
		
	}
	
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public void run() {
		
		if(setupCommunicationTools()){
			
			System.out.println("Communication Tools up!");
			
			connected = true;
			while(connected){
				
				
				Message message = receiver.readFromClient();
				
				if(connected){
				
					switch (message.getType()) {
						
						case OpCodes.LOG_IN:
							
							int userStatus = addUserToGreeter(message);	
							sender.sendToClient(userStatus, getValidInstruments());						
							break;
							
						case OpCodes.LOG_OUT:
								//TODO Call logout method
							break;
							
						case OpCodes.ORDER:
							System.out.println("Got an order.");
							Order order = unpackOrder(message);
							update(order);
							sender.confirmOrder(order.getId());
							break;
					
						default:
							break;
					}	

					
				}//if
				
			}//while
			
		}
		
	}


}

