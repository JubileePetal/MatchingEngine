package tests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;

import java.net.Socket;
import java.util.ArrayList;

import model.Librarian;

import org.junit.Before;
import org.junit.Test;

import communications.ClientHandler;
import communications.Greeter;
import controller.Archiver;

public class GreeterTest {

	Greeter greeter;
	Socket socket;
	Librarian librarian;
	ClientHandler ch;
	Archiver archiver;
	
	String username;
	
	
	
	@Before
	public void setUp(){
		
		socket = mock(Socket.class);
		
		username = "Starlight";
		
		greeter 	= spy(new Greeter());
		librarian 	= new Librarian();
		ch 			= new ClientHandler(socket,greeter);
		archiver 	= new Archiver();
		
		ch.setUsername(username);
		greeter.setLibrarian(librarian);
		greeter.setInstruments(archiver.retrieveInstruments());
		
	}
	
	@Test
	public void testAddTrader() {
		

		assertNull(greeter.getTrader(username));
		greeter.addTrader(username,ch);
		assertEquals(greeter.getTrader(username),ch);
		
	}
	
	
	@Test
	public void testGetIDs(){
		assertThat(greeter.getUniqueOrderID(), is(not(greeter.getUniqueOrderID())));
		assertThat(greeter.getUniqueOrderID(), is(not(greeter.getUniqueTradeID())));
		
	}
	
	@Test
	public void testGetTrader(){
		
		greeter.addTrader(ch.getUsername(), ch);
		assertNull(greeter.getTrader("Dumbo the Elephant"));
		assertNotNull(greeter.getTrader(username));
		assertEquals(greeter.getTrader(username),ch);
	}

	@Test
	public void testGetAllHandlers(){
		
		ClientHandler handler1 = new ClientHandler(socket,greeter);
		ClientHandler handler2 = new ClientHandler(socket,greeter);
		ClientHandler handler3 = new ClientHandler(socket,greeter);
		ClientHandler handler4 = new ClientHandler(socket,greeter);
		
		handler1.setUsername("Ariel");
		handler2.setUsername("Pocahontas");	
		handler3.setUsername("Jasmine");
		handler4.setUsername("Belle");
		
		
		greeter.addTrader(handler1.getUsername(), handler1);
		greeter.addTrader(handler2.getUsername(), handler2);
		greeter.addTrader(handler3.getUsername(), handler3);
		greeter.addTrader(handler4.getUsername(), handler4);
		
		
		ArrayList<ClientHandler> allHandlers = greeter.getAllHandlers();
		assertNotNull(allHandlers);
		assertEquals(allHandlers.size(), 4);
		
		
		
		
	}
}
