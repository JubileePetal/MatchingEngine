package tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.net.Socket;

import model.Librarian;
import model.OrderBook;
import models.Instrument;
import models.Order;

import org.junit.Before;
import org.junit.Test;

import communications.ClientHandler;
import communications.Greeter;
import controller.Matcher;

public class LibrarianTest {


	private final Librarian librarian = spy(new Librarian());

	private String first; 
	private String second;
	private String third;
	
	
	
	
	@Before
	public void setUp() throws Exception {
		
		first 	= "First";
		second	= "Second";
		third 	= "Third";
		
		librarian.putInQueue(first);
		librarian.putInQueue(second);
		librarian.putInQueue(third);
		
		
		
	}
	
	
	@Test
	public void testPutInQueue() {
		
		assertTrue("First".equals(librarian.getFirstInQueue()));

	}
	
	
	@Test
	public void testGetFirstInQueue(){
		
		assertTrue(first.equals(librarian.getFirstInQueue()));
		assertTrue(second.equals(librarian.getFirstInQueue()));
		assertTrue(third.equals(librarian.getFirstInQueue()));
		
	}
	
	@Test
	public void testAddOrderBook(){
		
		Instrument instrumentA = new Instrument();
		instrumentA.setAbbreviation("ERB");
		librarian.addOrderBook(instrumentA);
		verify(librarian, times(1)).putInQueue(instrumentA.getAbbreviation());
		assertNotNull(librarian.getFirstInQueue());
		
	}
	
	@Test
	public void testUpdate(){
		
		Socket socket 		= mock(Socket.class);
		Greeter greeter 	= new Greeter();
		ClientHandler ch	= new ClientHandler(socket,greeter);
		
		
		Order order 		= new Order();
		
		ch.addObserver(librarian);
		
		
		ch.update(order);
		verify(librarian, times(1)).update(ch, order);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
