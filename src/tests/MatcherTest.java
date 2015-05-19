package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.TreeSet;

import model.Librarian;
import model.OrderBook;
import models.BookStatus;
import models.Instrument;
import models.OpCodes;
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
		librarian.addOrderBook(instrumentA, OptionsCollections.smallOptionsSet());
		librarian.addOrderBook(instrumentB, OptionsCollections.smallOptionsSet());
		
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
	public void testGetClonedOrder() {
		int testQuantity = 79;
		Order buyOrder = OrderCollections.simpleBuyOrder();
		buyOrder.setOrderQuantity(20);
		Order clonedOrder = matcher.getClonedOrder(buyOrder, testQuantity);
		
		assertEquals(clonedOrder.getQuantity(), testQuantity);

	}

	public void testProcessMarketData() {
		
		Order buyOrderInBook = OrderCollections.simpleBuyOrder();
		Order sellOrderInBook = OrderCollections.simpleSellOrder();
		
		obA.addToBuyOrders(buyOrderInBook);
		obA.addToSellOrders(sellOrderInBook);
		
		matcher.borrowOrderBook();
		BookStatus bookStatus = matcher.processMarketData("Ericsson B");
		assertEquals(bookStatus.getInstrumentName(), "Ericsson B");
		assertTrue(bookStatus.getBuyLevels().get(0).getPrice() == buyOrderInBook.getPrice());
		assertTrue(bookStatus.getBuyLevels().get(0).getQuantity() == buyOrderInBook.getQuantity());
		assertTrue(bookStatus.getSellLevels().get(0).getPrice() == sellOrderInBook.getPrice());
		assertTrue(bookStatus.getSellLevels().get(0).getQuantity() == sellOrderInBook.getQuantity());
		matcher.returnOrderBook();
		
	}
	
	@Test
	public void testGetMatchedOrderSell() {
		
		int myType = OpCodes.SELL_ORDER;
		
		Order buyOrderInBook = OrderCollections.simpleBuyOrder();
		Order sellOrderInBook = OrderCollections.simpleSellOrder();
		
		obA.addToBuyOrders(buyOrderInBook);
		obA.addToSellOrders(sellOrderInBook);
		
		matcher.borrowOrderBook();
		Order matchedOrder = matcher.getMatchedOrder(myType);
		assertEquals(matchedOrder.isBuyOrSell(), OpCodes.BUY_ORDER);
		matcher.returnOrderBook();
	}
	
	@Test
	public void testGetMatchedOrderBuy() {
		
		int myType = OpCodes.BUY_ORDER;
		
		Order buyOrderInBook = OrderCollections.simpleBuyOrder();
		Order sellOrderInBook = OrderCollections.simpleSellOrder();
		
		obA.addToBuyOrders(buyOrderInBook);
		obA.addToSellOrders(sellOrderInBook);
		
		matcher.borrowOrderBook();
		Order matchedOrder = matcher.getMatchedOrder(myType);
		assertEquals(matchedOrder.isBuyOrSell(), OpCodes.SELL_ORDER);
		matcher.returnOrderBook();
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
	public void testCanReturnOB() {
		matcher.processOrderBook();
		verify(matcher, times(1)).returnOrderBook();
	}
	
	@Test
	public void testPutMatchedSellOrderBack() {
		
		// Test that matcher puts a buy order back to it's
		// proper place
		
		Order sellOrder = OrderCollections.simpleSellOrder();
		matcher.borrowOrderBook();
		matcher.putMatchedOrderBack(sellOrder);
		Order order = matcher.getCurrentOrderBook().getSellOrders().last();
		assertTrue(order.equals(sellOrder));
		matcher.returnOrderBook();
		
	}
	
	@Test
	public void testPutMatchedBuyOrderBack() {
		
		// Test that matcher puts a buy order back to it's
		// proper place
		
		Order buyOrder = OrderCollections.simpleBuyOrder();
		matcher.borrowOrderBook();
		matcher.putMatchedOrderBack(buyOrder);
		Order order = matcher.getCurrentOrderBook().getBuyOrders().last();
		assertTrue(order.equals(buyOrder));
		matcher.returnOrderBook();
		
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
	
	@Test
	public void testMarketDataProcessedOnceIfNoQuantityRemains() {
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		obA.addToQueue(simpleBuy);
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(1)).processMarketData(simpleBuy.getInstrument().getName());
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testMarketDataProcessedTwiceIfQuantityRemains() {
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		simpleSell.setOrderQuantity(10);
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		simpleBuy.setOrderQuantity(20);
		obA.addToQueue(simpleBuy);
		
		matcher.borrowOrderBook();
		matcher.processOrder();
		verify(matcher, times(2)).processMarketData(simpleBuy.getInstrument().getName());
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testMatchEqualMatch() {
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		
		matcher.borrowOrderBook();
		matcher.match(simpleBuy);
		verify(matcher, times(1)).equalMatch(simpleBuy, simpleSell);
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testMatchMyQuantLarger() {
		
		int theirQuant = 10;
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		simpleSell.setOrderQuantity(theirQuant);
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		simpleBuy.setOrderQuantity(20);
		
		matcher.borrowOrderBook();
		matcher.match(simpleBuy);
		verify(matcher, times(1)).getClonedOrder(simpleBuy, theirQuant);
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testMatchMyQuantSmaller() {
		
		int myQuant = 10;
		
		Order simpleSell = OrderCollections.simpleSellOrder();
		simpleSell.setOrderQuantity(20);
		obA.addToSellOrders(simpleSell);
		
		Order simpleBuy = OrderCollections.simpleBuyOrder();
		simpleBuy.setOrderQuantity(myQuant);
		
		matcher.borrowOrderBook();
		matcher.match(simpleBuy);
		verify(matcher, times(1)).getClonedOrder(simpleSell, myQuant);
		matcher.returnOrderBook();		
	}
	
	@Test
	public void testFiveEqualMatches() {
		
		ArrayList<Order> fiveEqualBuy = OrderCollections.fiveEqualBuys();
		ArrayList<Order> fiveEqualSell = OrderCollections.fiveEqualSell();
		
		for(Order order : fiveEqualBuy) {
			obA.addToBuyOrders(order);
		}
		
		for(Order order : fiveEqualSell) {
			obA.addToQueue(order);
		}
		int myQuant = 10;
		
		matcher.processOrderBook();
		for(int i = 0; i < 5; i++) {
			verify(matcher, times(1)).equalMatch(fiveEqualSell.get(i), fiveEqualBuy.get(i));
		}

	}

	@Test
	public void testMatchTwice() {

		
		Order myBuy = OrderCollections.simpleBuyOrder();
		myBuy.setOrderQuantity(20);
		obA.addToQueue(myBuy);
		
		Order sellOne = OrderCollections.simpleSellOrder();
		sellOne.setOrderQuantity(10);
		obA.addToSellOrders(sellOne);
		Order sellTwo = OrderCollections.simpleSellOrder();
		sellTwo.setOrderQuantity(10);
		obA.addToSellOrders(sellTwo);
		
		matcher.processOrderBook();
		
		verify(matcher, times(2)).match(myBuy);
		
	}
	
	@Test
	public void testPartialMatch() {

		
		Order myBuy = OrderCollections.simpleBuyOrder();
		myBuy.setOrderQuantity(10);
		obA.addToQueue(myBuy);
		
		Order sellOne = OrderCollections.simpleSellOrder();
		sellOne.setOrderQuantity(20);
		obA.addToSellOrders(sellOne);
		
		matcher.processOrderBook();
		
		verify(matcher, times(1)).match(myBuy);
		verify(matcher, times(1)).putMatchedOrderBack(sellOne);
		
	}
	
}
