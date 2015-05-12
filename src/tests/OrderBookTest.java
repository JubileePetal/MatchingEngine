package tests;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.ArrayList;

import model.OrderBook;
import models.Instrument;
import models.OpCodes;
import models.Order;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class OrderBookTest {

	private OrderBook orderBook = spy(new OrderBook());
	
	@Before
	public void setUp() throws Exception {
		

	}
	
	@Test
	public void testCanMatchTrue(){
		
		/*Note that these two will match */
		Order buyOrder 		= OrderCollections.simpleBuyOrder();
		Order sellOrder		= OrderCollections.simpleSellOrder();
		
		orderBook.addToBuyOrders(buyOrder);
		
		assertTrue(orderBook.canMatch(sellOrder, OpCodes.SELL_ORDER));
		
	}
	
	@Test
	public void testCanMatchFalse(){
		
		/*Note that these two will match */
		Order buyOrder 				= OrderCollections.simpleBuyOrder();
		Order sellOrderNoMatch		= OrderCollections.simpleSellOrderNewPrice();
		
		orderBook.addToBuyOrders(buyOrder);
		
		assertFalse(orderBook.canMatch(sellOrderNoMatch, OpCodes.SELL_ORDER));
		
	}

	@Test
	public void testNoMatch(){
		
		Order buyOrder = OrderCollections.simpleBuyOrder();
		assertFalse(orderBook.canMatch(buyOrder, OpCodes.BUY_ORDER));
		
		
	}
	
	@Test
	public void testFiveMatchesTrue(){
		
		
		ArrayList<Order> buyOrders 		= OrderCollections.fiveEqualBuys();
		ArrayList<Order> sellOrders 	= OrderCollections.fiveEqualSell();
		
		assertEquals(buyOrders.size(),sellOrders.size());
		
		
		for(int i = 0; i < buyOrders.size(); i++){
			
			orderBook.addToBuyOrders(buyOrders.get(i));
			
		}
		
		for (int j = 0; j < sellOrders.size(); j++) {
			
			assertTrue(orderBook.canMatch(sellOrders.get(j), OpCodes.SELL_ORDER));
			
		}
			
	}
	
	@Test
	public void testFiveMatchesFalse(){
		
		
		ArrayList<Order> buyOrders 		= OrderCollections.fiveEqualBuysNewPrice();
		ArrayList<Order> sellOrders 	= OrderCollections.fiveEqualSell();
		
		assertEquals(buyOrders.size(),sellOrders.size());
		
		
		for(int i = 0; i < buyOrders.size(); i++){
			
			orderBook.addToBuyOrders(buyOrders.get(i));
			
		}
		
		for (int j = 0; j < sellOrders.size(); j++) {
	
			assertFalse(orderBook.canMatch(sellOrders.get(j), OpCodes.SELL_ORDER));
		}
		
		
	}	
	
	@Test	
	
	public void testAddBuyOrder(){
		
		Order buyOrder 		=  OrderCollections.simpleBuyOrder();
		
		assertNull(orderBook.getBuyOrders().pollFirst());
		orderBook.addToBuyOrders(buyOrder);
		assertEquals(buyOrder, orderBook.getBuyOrders().pollFirst());
	}

	@Test
	public void testAddSellOrder(){
		
		Order sellOrder 	= OrderCollections.simpleSellOrder();
		assertNull(orderBook.getSellOrders().pollFirst());
		orderBook.addToSellOrders(sellOrder);
		assertEquals(sellOrder, orderBook.getSellOrders().pollFirst());
		
		
	}
	
	@Test
	public void testGetFirstBuy(){
		
		Order buyOrder 		=  OrderCollections.simpleBuyOrder();
		orderBook.addToBuyOrders(buyOrder);
		assertEquals(orderBook.getFirstBuy(),buyOrder);
		
		
	}
		
	@Test
	public void testGetFirstSell(){
		
		Order sellOrder 		=  OrderCollections.simpleSellOrder();
		orderBook.addToSellOrders(sellOrder);
		assertEquals(orderBook.getFirstSell(),sellOrder);
	
	}
	

}
