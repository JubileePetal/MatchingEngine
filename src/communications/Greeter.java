package communications;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import model.Librarian;
import models.BookStatus;
import models.Instrument;
import models.Option;

public class Greeter implements Runnable {
	
	
	private HashMap<String,ClientHandler> admins;
	private HashMap<String,ClientHandler> traders;
	private HashMap<String,ClientHandler> isvrs;
	private HashMap<String,ClientHandler> regulators;
	
	private Librarian 				librarian;
	private ArrayList<Instrument> 	instruments;
	private ArrayList<Option>		options;
	
	private HashMap<String, BookStatus> marketData;
	
	long orderIDCounter;
	long tradeIDCounter;
	
	private ServerSocket welcomeSocket;
	
	public Greeter() {
		
		marketData = new HashMap<String, BookStatus>();
		
		admins 			= new HashMap<String,ClientHandler>();
		traders 		= new HashMap<String,ClientHandler>();	
		isvrs 			= new HashMap<String,ClientHandler>();	
		regulators		= new HashMap<String,ClientHandler>();	
		orderIDCounter 	= 0;
		tradeIDCounter  = 0;
	}	
	
	public void addBookStatus(BookStatus bookStatus) {
		synchronized(marketData) {
			marketData.put(bookStatus.getInstrumentName(), bookStatus);
		}
	}

	public void createWelcomeSocket(){
		
		try {
			welcomeSocket = new ServerSocket(1337);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not create welcomeSocket in Greeter.");
			System.exit(-1);
		}
			

	}
	
	@Override
	public void run() {
		
		createWelcomeSocket();
		System.out.println("Server is up!");
		
		while(true){
			
			Socket newClientSocket = waitForNewClients();
			
			if(newClientSocket != null){
				ClientHandler newClientHandler = 
						new ClientHandler(newClientSocket, this);
				
				newClientHandler.addObserver(librarian);
			
				(new Thread(newClientHandler)).start();
				
			}
		}	
	}
	
	public Librarian getLibrarian() {
		return librarian;
	}

	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
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
	
	public void addTrader(String username, ClientHandler client){
		
		traders.put(username, client);
		
	}
	
	public void addAdmin(String username, ClientHandler client){
		
		admins.put(username, client);
	}

	public void addIsvr(String username, ClientHandler client){
		
		isvrs.put(username, client);
	}

	public void addRegulator(String username, ClientHandler client){

		
		regulators.put(username, client);
	}

//	public void addAlgoBot(String username, ClientHandler client){
//		
//		
//		
//	}

	
	public ArrayList<Instrument> getInstruments() {
		return instruments;
	}

	public void setInstruments(ArrayList<Instrument> instruments) {
		this.instruments = instruments;
		
		for(Instrument instrument : instruments) {
			marketData.put(instrument.getName(), null);
		}
	}
	
	public void setOptions(ArrayList<Option> options){
		this.options = options;
	}
	
	public ArrayList<Option> getOptions(){
		
		return options;
	}
	
	public synchronized long getUniqueOrderID(){
		
		long newOrderID;	
		orderIDCounter++;
		newOrderID = orderIDCounter;
		return newOrderID;
	}
	
	public synchronized long getUniqueTradeID(){
		
		long newTradeID;	
		tradeIDCounter++;
		newTradeID = tradeIDCounter;
		return newTradeID;
	}
	
	public ClientHandler getTrader(String traderUsername){
		
		return traders.get(traderUsername);
	}

	public ArrayList<ClientHandler> getAllHandlers() {
		
		ArrayList<ClientHandler> handlers = new ArrayList<ClientHandler>();
		for(ClientHandler handler : traders.values()) {
			handlers.add(handler);
		}
		for(ClientHandler handler : admins.values()) {
			handlers.add(handler);
		}
		for(ClientHandler handler : isvrs.values()) {
			handlers.add(handler);
		}
		for(ClientHandler handler : regulators.values()) {
			handlers.add(handler);
		}
		return handlers;
	}

	public BookStatus[] getMarketData() {
		BookStatus[] statusOfBooks;
		
		synchronized(marketData) {
			Object[] statuses 	= marketData.values().toArray();
			statusOfBooks 		= Arrays
					.copyOf(statuses, statuses.length, BookStatus[].class);
		}

		return statusOfBooks;
	}
	
	

	public void addAlgoBot(String username, ClientHandler client) {
		traders.put(username, client);
		
	}
	
}
