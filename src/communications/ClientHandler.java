package communications;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;

import models.Analytics;
import models.BookStatus;
import models.Message;
import models.OpCodes;
import models.Order;
import models.PartialTrade;
import models.User;

import com.google.gson.Gson;



public class ClientHandler extends Observable implements Runnable {

	private Socket 			socket;
	private Gson 			gson;
	private boolean 		connected;
	private Greeter 		greeter;
	private String 			username;
	private Sender 			sender;
	private Receiver 		receiver;
	private boolean			iAmBot;
	
	private ArrayList<Order> myOrders;
	
	public ClientHandler(Socket clientSocket, Greeter greetedBy){
		
		myOrders	 = new ArrayList<Order>();
		greeter 	 = greetedBy;
		socket 		 = clientSocket;
		sender 		 = new Sender(this);
		receiver  	 = new Receiver(this);
		gson 		 = new Gson();
		iAmBot		 = false;
	}
	
	public boolean setupCommunicationTools(){
			
		sender.setSocket(socket);
		receiver.setSocket(socket);
		
		return (sender.initiateStream() && receiver.initiateStream());
	}
	
	public int addUserToGreeter(Message message){
		
		User user 		=  gson.fromJson(message.getJson(),User.class);
		this.username 	=  user.getUsername();
		
		int userStatus;
	
		switch (user.getUserType()) {
			case OpCodes.TRADER:
				greeter.addTrader(username, this);
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
				
			case OpCodes.BOT:
				/**Do bots have to be in a list to work?*/
				userStatus = OpCodes.LOG_IN_ACCEPTED;
				break;
				
			case OpCodes.ALGO_BOT:
				/** Algo bot*/
				userStatus = OpCodes.LOG_IN_ACCEPTED;
				greeter.addAlgoBot(username, this);
				iAmBot = true;
				break;	
			
			default:
				userStatus = OpCodes.LOG_IN_REJECTED;
				break;
			}
		
		return userStatus;
	}		

	private String getMarketData() {
		String json = gson.toJson(greeter.getMarketData());
		return json;
	}
	
	public String getValidInstruments(){
		return gson.toJson(greeter.getInstruments());
	}
	
	public String getOptions(){
		String json = gson.toJson(greeter.getOptions());
		return json;
	}
	
	public void update(Order order){
		
		setChanged();
		notifyObservers(order);
	}
	
	public Order unpackOrder(Message message){

		Order order =  gson.fromJson(message.getJson(),Order.class);		
		order.setId(greeter.getUniqueOrderID());
		return order;
	}

	public void addOrder(Order order) {
		myOrders.add(order);
		String json = gson.toJson(order);
		sender.sendToClient(OpCodes.ORDER_ADDED, json);
	}	
	
	public void setConnected(boolean connected) {
		this.connected = connected;
	}
	
	public void run() {
		
		if(setupCommunicationTools()){
			
			connected = true;
			while(connected){	
				
				Message message = receiver.readFromClient();
				
				if(connected){
				
					switch (message.getType()) {
						
						case OpCodes.LOG_IN:
							
							int userStatus = addUserToGreeter(message);	
							sender.sendToClient(userStatus, getInitialization());						
							break;
							
						case OpCodes.LOG_OUT:
								//TODO Call logout method
							break;
							
						case OpCodes.ORDER:
							Order order = unpackOrder(message);
							order.setTimeEnteredSystem(System.currentTimeMillis());
							update(order);
							break;
					
						default:
							break;
					}	

					
				}
				
			}
			
		}
		
	}

	private String getInitialization() {
		

		String[] initData = new String[3];
		initData[0] = getValidInstruments();
		initData[1] = getMarketData();
		if(iAmBot){
			initData[2] = getOptions();
		}

		return gson.toJson(initData);
	}

	public void sendMarketData(BookStatus bookStatus) {
		String json = gson.toJson(bookStatus);
		sender.sendToClient(OpCodes.MARKET_DATA, json);
	}

	public void sendPartialTrade(PartialTrade partialTrade) {
		String json = gson.toJson(partialTrade);
		sender.sendToClient(OpCodes.PARTIAL_TRADE, json);
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void sendAnalytics(Analytics analytics) {
		System.out.println("Sending!");
		String json = gson.toJson(analytics);
		sender.sendToClient(OpCodes.ANALYTICS, json);
	}

	


}

