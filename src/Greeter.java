import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;


public class Greeter {
	
	private ServerSocket serverSocket;
	private String hostname;
	private int portNumber;
	
	public Greeter() {
		// TODO Auto-generated constructor stub
	}
	
	
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
	            System.out.println(order.message);
	            
	            
	            
			}	
			
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
}
