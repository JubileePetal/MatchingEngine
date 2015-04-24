package tests;

import static org.junit.Assert.*;

import model.OrderBook;
import models.Order;

import org.junit.Before;
import org.junit.Test;

public class OrderBookTest {

	private OrderBook orderBook;
	
	@Before
	public void setUp() throws Exception {
		orderBook = new OrderBook();
	}

	@Test
	public void testMaxBuy() {
		addBuyOrders();
		Order max = orderBook.getMaxBuyT();
		System.out.println(max.getPrice());
		System.out.println(max.getTimeEnteredOrderBook());
		//assertEquals(50.0, max.doubleValue(),0.5);
	}
	
	@Test
	public void testMinSell() {
		addSellOrders();
		Double min = orderBook.getMinSell();
		assertEquals(10.0, min.doubleValue(),0.5);
	}
	
	private void addSellOrders() {
		Order a = new Order();
		a.setTimeEnteredOrderBook(0);
		a.setPrice(10.0);
		Order b = new Order();
		b.setTimeEnteredOrderBook(1);
		b.setPrice(50.0);
		Order c = new Order();
		c.setTimeEnteredOrderBook(2);
		c.setPrice(20.0);
		Order d = new Order();
		d.setTimeEnteredOrderBook(3);
		d.setPrice(50.0);
		Order e = new Order();
		e.setTimeEnteredOrderBook(4);
		e.setPrice(40.0);
		
		orderBook.addToSellHash(a);
		orderBook.addToSellHash(b);
		orderBook.addToSellHash(c);
		orderBook.addToSellHash(d);
		orderBook.addToSellHash(e);
	}
	
	private void addBuyOrders() {
		Order a = new Order();
		a.setTimeEnteredOrderBook(0);
		a.setPrice(10.0);
		Order b = new Order();
		b.setTimeEnteredOrderBook(6);
		b.setPrice(50.0);
		Order c = new Order();
		c.setTimeEnteredOrderBook(2);
		c.setPrice(20.0);
		Order d = new Order();
		d.setTimeEnteredOrderBook(3);
		d.setPrice(50.0);
		Order e = new Order();
		e.setTimeEnteredOrderBook(4);
		e.setPrice(40.0);
		
		orderBook.addToBuy(a);
		orderBook.addToBuy(b);
		orderBook.addToBuy(c);
		orderBook.addToBuy(d);
		orderBook.addToBuy(e);
	}
	
	private void removeOrders() {

	}

}
