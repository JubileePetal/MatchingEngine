package tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.Socket;

import model.Librarian;

import org.junit.Before;
import org.junit.Test;

import communications.ClientHandler;
import communications.Greeter;
import controller.Archiver;

public class GreeterTest {

	Greeter greeter;
	Librarian librarian;
	ClientHandler ch;
	Archiver archiver;
	
	String username;
	
	
	
	@Before
	public void setUp(){
		
		Socket socket = mock(Socket.class);
		
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
		
		assertNotEquals(greeter.getUniqueOrderID(), greeter.getUniqueOrderID());
		assertNotEquals(greeter.getUniqueTradeID(), greeter.getUniqueTradeID());
		
	}
	
	@Test
	public void testGetTrader(){
		
		greeter.addTrader(ch.getUsername(), ch);
		assertNull(greeter.getTrader("Dumbo the Elephant"));
		assertNotNull(greeter.getTrader(username));
		assertEquals(greeter.getTrader(username),ch);
	}

}
