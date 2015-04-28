package tests;

import static org.junit.Assert.*;

import model.Librarian;
import model.OrderBook;
import models.Instrument;
import models.Order;

import org.junit.Before;
import org.junit.Test;

import controller.Matcher;
import controller.TradeProcessor;

import static org.mockito.Mockito.*;

public class MatcherTest {

	private final Matcher matcher = spy(new Matcher());
	OrderBook obA;
	OrderBook obB;
	
	
	
	@Before
	public void setUp() throws Exception {
		
		Instrument instrumentA = new Instrument();
		instrumentA.setAbbreviation("ERB");
		Instrument instrumentB = new Instrument();
		instrumentB.setAbbreviation("HMA");
		
		Librarian librarian = new Librarian();
		librarian.addOrderBook(instrumentA);
		librarian.addOrderBook(instrumentB);
		
		obA = librarian.getOrderBook(instrumentA.getAbbreviation());
		obB = librarian.getOrderBook(instrumentB.getAbbreviation());
		
		//matcher = new Matcher();
		matcher.setLibrarian(librarian);
		matcher.setTradeProcessor(new TradeProcessor());
		
		assertNotNull(matcher.getLibrarian());
		assertNotNull(matcher.getTradeProcessor());
	}
/*
	@Test
	public void testCanBorrow() {
		matcher.borrowOrderBook();
		assertNotNull(matcher.getCurrentOrderBook());
	}
	
	@Test
	public void testCannotBorrow() {
		
		// librarian is set up with two books, 
		// borrowing a third shouldn't work
		matcher.borrowOrderBook();
		matcher.borrowOrderBook();
		matcher.borrowOrderBook();
		assertNull(matcher.getCurrentOrderBook());
	}
	
	@Test
	public void testCanReturn() {
		matcher.processOrderBook();
		verify(matcher, times(1)).returnOrderBook();
	}
	
	@Test
	public void testCannotReturn() {
		// if the matcher can't borrow a book,
		// then return will never be called
		matcher.borrowOrderBook();
		matcher.borrowOrderBook();
		matcher.processOrderBook();
		verify(matcher, times(0)).returnOrderBook();
	}

	@Test
	public void testNoPendingsNoProcess() {
		// if the current orderbook has no pending orders
		// then no processing of an order can be made
		matcher.processOrderBook();
		verify(matcher, times(0)).processOrder();
	}
	
	@Test
	public void testOnePendingExistDoProcess() {
		
		Order simpleSell = new Order();
		obA.addToQueue(simpleSell);
		
		// if the current orderbook has one pending order
		// then matcher should process the order
		matcher.processOrderBook();
		verify(matcher, times(1)).processOrder();
	}
	
	@Test
	public void testTwoPendingsExistDoProcess() {
		
		Order simpleSell = new Order();
		obA.addToQueue(simpleSell);
		Order simpleSell2 = new Order();
		obA.addToQueue(simpleSell2);
		
		// if the current orderbook has two pending orders
		// then matcher should process two orders
		matcher.processOrderBook();
		verify(matcher, times(2)).processOrder();
	}
	
	@Test
	public void testMatchIsCalledSell() {
		
		Order simpleBuy = new Order();
		simpleBuy.setPrice(10);
		simpleBuy.setOrderQuantity(20);
		simpleBuy.setToBuyOrder();
		
		obA.addToBuyOrders(simpleBuy);
		
		Order simpleSell = new Order();
		simpleSell.setPrice(10);
		simpleSell.setOrderQuantity(20);
		simpleSell.setToSellOrder();
		
		obA.addToQueue(simpleSell);	
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher).match(simpleSell);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testMatchIsCalledBuy() {
		
		Order simpleSell = new Order();
		simpleSell.setPrice(10);
		simpleSell.setOrderQuantity(20);
		simpleSell.setToSellOrder();
		
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = new Order();
		simpleBuy.setPrice(10);
		simpleBuy.setOrderQuantity(20);
		simpleBuy.setToBuyOrder();
		
		obA.addToQueue(simpleBuy);
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher).match(simpleBuy);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testMatchIsNotCalledIfSellEmpty() {
		
		Order simpleBuy = new Order();
		simpleBuy.setPrice(10);
		simpleBuy.setOrderQuantity(20);
		simpleBuy.setToBuyOrder();
		
		obA.addToQueue(simpleBuy);
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(0)).match(simpleBuy);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testMatchIsNotCalledIfBuyEmpty() {
		
		Order simpleSell = new Order();
		simpleSell.setPrice(10);
		simpleSell.setOrderQuantity(20);
		simpleSell.setToSellOrder();
		
		obA.addToQueue(simpleSell);	
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(0)).match(simpleSell);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testNoRemainsIfEqual() {
		
		Order simpleSell = new Order();
		simpleSell.setPrice(10);
		simpleSell.setOrderQuantity(20);
		simpleSell.setToSellOrder();
		
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = new Order();
		simpleBuy.setPrice(10);
		simpleBuy.setOrderQuantity(20);
		simpleBuy.setToBuyOrder();
		
		matcher.borrowOrderBook();
		assertFalse(matcher.match(simpleBuy));
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testNoRemainsIfPendindQuantityIsLess() {
		
		Order simpleSell = new Order();
		simpleSell.setPrice(10);
		simpleSell.setOrderQuantity(20);
		simpleSell.setToSellOrder();
		
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = new Order();
		simpleBuy.setPrice(10);
		simpleBuy.setOrderQuantity(10);
		simpleBuy.setToBuyOrder();
		
		matcher.borrowOrderBook();
		assertFalse(matcher.match(simpleBuy));
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testRemainsExistIfPendingQuantityIsLarger() {
		
		Order simpleSell = new Order();
		simpleSell.setPrice(10);
		simpleSell.setOrderQuantity(20);
		simpleSell.setToSellOrder();
		
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = new Order();
		simpleBuy.setPrice(10);
		simpleBuy.setOrderQuantity(30);
		simpleBuy.setToBuyOrder();
		
		matcher.borrowOrderBook();
		assertTrue(matcher.match(simpleBuy));
		matcher.returnOrderBook();		
	}
	*/
	
	@Test
	public void testMatch() {
		for(Order order : OrderCollections.getOrderListA()) {
			order.setToBuyOrder();
			obA.addToBuyOrders(order);
			System.out.println("buy order: price: " + order.getPrice() + " quantity: " + order.getQuantity());
		}
		
		for(Order order : OrderCollections.getOrderListB()) {
			order.setToSellOrder();
			obA.addToQueue(order);
			System.out.println("pending sell order: price: " + order.getPrice() + " quantity: " + order.getQuantity());
		}
		matcher.processOrderBook();
		System.out.println("order in book price: " + obA.getFirstSell().getPrice());
	}

}
