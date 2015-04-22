package communications;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Librarian;
import models.Instrument;

import com.google.gson.Gson;


public class Greeter implements Runnable {
	
	private HashMap<String,ClientHandler> admins;
	private HashMap<String,ClientHandler> traders;
	private HashMap<String,ClientHandler> isvrs;
	private HashMap<String,ClientHandler> regulators;
	
	private Librarian librarian;
	private ArrayList<Instrument> instruments;
	
	
	private ServerSocket welcomeSocket;

	
	public Greeter(Librarian librarian) {
		
		this.librarian = librarian;
		
		admins 		= new HashMap<String,ClientHandler>();
		traders 	= new HashMap<String,ClientHandler>();	
		isvrs 		= new HashMap<String,ClientHandler>();	
		regulators	= new HashMap<String,ClientHandler>();	
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
	
	private Socket waitForNewClients(){
		
		Socket socket = null;
		try {
			System.out.println("Waiting for clients...");
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

	public ArrayList<Instrument> getInstruments() {
		return instruments;
	}

	public void setInstruments(ArrayList<Instrument> instruments) {
		this.instruments = instruments;
	}
	
	
}
