package communications;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import models.Message;

import com.google.gson.Gson;

public class Receiver {
	
	private ClientHandler clientHandler;
	private Socket socket;
	private BufferedReader inFromClient;
	private Gson gson;
	
	
	public Receiver(ClientHandler clientHandler) {		
		this.clientHandler = clientHandler;
		gson = new Gson();
		
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean initiateStream(){
        
		inFromClient = null;
		
		try {
			inFromClient =
					new BufferedReader
						(new InputStreamReader(socket.getInputStream()));
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not set up BufferedReader inFromClient"
					+ " in Receiver");
			return false;
		}
		
        return true;
	}
		
	public Message readFromClient(){
		
		Message newMessage;
		String clientJson = readFromSocket();
		if(clientJson == null){
			
			clientHandler.setConnected(false);
		}
		
		
		 newMessage = gson.fromJson(clientJson,Message.class);
		
		return newMessage;
	}
	
	private String readFromSocket(){
		
		String clientMessage = null;
		
		try {
			
			 clientMessage = inFromClient.readLine();
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not read message from client "
					+ "in Receiver.");
		}
		
		return clientMessage;
		
	}
		
	
	
}
