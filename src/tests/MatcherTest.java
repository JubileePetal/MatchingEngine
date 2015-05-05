package tests;

import static org.junit.Assert.*;

import model.Librarian;
import model.OrderBook;
import models.Instrument;
import models.Order;

import org.junit.Before;
import org.junit.Test;

import communications.Greeter;

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
		
		TradeProcessor tradeProcessor = new TradeProcessor();
		tradeProcessor.setGreeter(new Greeter());
		//matcher = new Matcher();
		matcher.setLibrarian(librarian);
		matcher.setTradeProcessor(tradeProcessor);
		
		assertNotNull(matcher.getLibrarian());
		assertNotNull(matcher.getTradeProcessor());
	}

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
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		obA.addToQueue(simpleBuy);
		
		// if the current orderbook has one pending order
		// then matcher should process the order
		matcher.processOrderBook();
		verify(matcher, times(1)).processOrder();
	}
	
	@Test
	public void testTwoPendingsExistDoProcess() {
		
		Order simpleBuyOne = OrderCollections.simpleBuyOrder();
		Order simpleBuyTwo = OrderCollections.simpleBuyOrder();
		obA.addToQueue(simpleBuyOne);
		obA.addToQueue(simpleBuyTwo);
		// if the current orderbook has two pending orders
		// then matcher should process two orders
		matcher.processOrderBook();
		verify(matcher, times(2)).processOrder();
	}
	
	@Test
	public void testMatchIsCalledSell() {
		
		// test that match is called ONCE if we have one matching
		// buy and sell with the SAME quantity
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		obA.addToBuyOrders(simpleBuy);
		Order simpleSell = OrderCollections.simpleSellOrder();		
		obA.addToQueue(simpleSell);	
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(1)).match(simpleSell);
		matcher.returnOrderBook();
	}

	@Test
	public void testMatchIsCalledBuy() {
		
		// test that match is called ONCE if we have one matching
		// buy and sell with the SAME quantity
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		obA.addToQueue(simpleBuy);
		Order simpleSell = OrderCollections.simpleSellOrder();		
		obA.addToSellOrders(simpleSell);	
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(1)).match(simpleBuy);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testMatchIsNotCalledDifferentPrice() {

		// test that match is not called when there is no
		// match to be made (high sell, low buy)
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		simpleBuy.setPrice(10.0);
		obA.addToBuyOrders(simpleBuy);
		Order simpleSell = OrderCollections.simpleSellOrder();
		simpleSell.setPrice(20.0);
		obA.addToQueue(simpleSell);	
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(0)).match(simpleSell);
		matcher.returnOrderBook();
	}
	

	
	@Test
	public void testMatchIsNotCalledIfSellEmpty() {
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		obA.addToQueue(simpleBuy);
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(0)).match(simpleBuy);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testMatchIsNotCalledIfBuyEmpty() {
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		obA.addToQueue(simpleSell);	
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(0)).match(simpleSell);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testNoRemainsIfEqual() {
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		
		matcher.borrowOrderBook();
		assertFalse(matcher.match(simpleBuy));
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testNoRemainsIfMyQuantityIsLess() {
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		simpleSell.setOrderQuantity(20);
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		simpleBuy.setOrderQuantity(10);
		
		matcher.borrowOrderBook();
		assertFalse(matcher.match(simpleBuy));
		matcher.returnOrderBook();		
	}

	@Test
	public void testRemainsExistIfMyQuantityIsLarger() {
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		simpleSell.setOrderQuantity(10);
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		simpleBuy.setOrderQuantity(20);
		
		matcher.borrowOrderBook();
		assertTrue(matcher.match(simpleBuy));
		matcher.returnOrderBook();		
	}
	
/*	
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
	*/

}
