package tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import models.Order;
import models.Trade;

import org.junit.Before;
import org.junit.Test;

import communications.Greeter;
import controller.TradeProcessor;

public class TradeProcessorTest {
	
	TradeProcessor tradeProcessor; 
	Greeter greeter;
	
	@Before
	public void setUp(){
		
		tradeProcessor 		= spy(new TradeProcessor());
		greeter				= new Greeter();
		
		tradeProcessor.setGreeter(greeter);
	}
	
	
	@Test
	public void testCreateTrade() {
		
		Order buyOrder 		= OrderCollections.simpleBuyOrder();
		Order sellOrder		= OrderCollections.simpleSellOrder();
		
		Trade  trade = tradeProcessor.createTrade(buyOrder, sellOrder);
		assertNotNull(trade);
		verify(tradeProcessor,times(1)).createPartialTrade(sellOrder,
													trade.getTradeID());
	}
	
	
	@Test
	public void testOrderOrder(){
		
		Order buyOrder1 		= OrderCollections.simpleBuyOrder();
		Order sellOrder1		= OrderCollections.simpleSellOrder();
		
		buyOrder1.setId(1);
		sellOrder1.setId(2);
		
		Order buyOrder2 		= OrderCollections.simpleBuyOrder();
		Order sellOrder2		= OrderCollections.simpleSellOrder();
		
		buyOrder2.setId(2);
		sellOrder2.setId(1);
		
		
		
		Trade trade1 = tradeProcessor.createTrade(buyOrder1, sellOrder1);
		assertEquals(trade1.getBuyPartial().getOrder().getId(),1);
		assertEquals(trade1.getSellPartial().getOrder().getId(),2);
		
		Trade trade2 = tradeProcessor.createTrade(sellOrder2, buyOrder2);
		assertEquals(trade2.getBuyPartial().getOrder().getId(),2);
		assertEquals(trade2.getSellPartial().getOrder().getId(),1);
		
		
		
		
	}

}
