package communications;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.google.gson.Gson;

import models.Message;

public class Sender {
	
	private ClientHandler clientHandler;
	private Socket socket;
	private DataOutputStream outToClient;
	private Gson gson;
	
	
	public Sender(ClientHandler clientHandler) {
		this.clientHandler = clientHandler;
		gson = new Gson();
		
	}

	public void setSocket(Socket socket){
		this.socket = socket;
		gson = new Gson();
	}
	
	public boolean initiateStream(){
		
		outToClient = null;
		
		try {
			outToClient =
					new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not set up DataOutputStream "
					+ "outToClient in Sender");
			return false;
		}
		
		return true;
		
	}
	
	public synchronized void sendToClient(int opCode, String json){
		
		Message message = new Message();
		message.setType(opCode);
		
		if(json != null){
			
			message.setJson(json);
		}
		
		String toSend = gson.toJson(message);
		
		writeToSocket(toSend);
		
	}
	
	private void writeToSocket(String stringMessage) {
		
		try {
			outToClient.writeBytes(stringMessage + '\n');
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not writeToClient in ClientHandler");

		}
		
	}

	

}
