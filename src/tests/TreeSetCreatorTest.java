package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.TreeSet;

import model.OrderBook;
import models.Order;
import models.TreeSetCreator;

import org.junit.Before;
import org.junit.Test;

public class TreeSetCreatorTest {

	TreeSet<Order> buyOrders;
	TreeSet<Order> sellOrders;
	
	@Before
	public void setUp() throws Exception {
		buyOrders = TreeSetCreator.createBuyOrderSet();
		sellOrders = TreeSetCreator.createSellOrderSet();
	}
	
	private void addSellOrders() {
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
		d.setPrice(10.0);
		Order e = new Order();
		e.setTimeEnteredOrderBook(4);
		e.setPrice(40.0);
		
		sellOrders.add(a);
		sellOrders.add(b);
		sellOrders.add(c);
		sellOrders.add(d);
		sellOrders.add(e);
		
	}
	
	private void clearSellOrders() {
		sellOrders.clear();
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
		
		buyOrders.add(a);
		buyOrders.add(b);
		buyOrders.add(c);
		buyOrders.add(d);
		buyOrders.add(e);
		
	}
	
	private void clearBuyOrders() {
		buyOrders.clear();
	}
	
	@Test
	public void testBuyPriceSort() {
		addBuyOrders();
		assertEquals(50.0, buyOrders.first().getPrice(), 0.5);
		clearBuyOrders();
	}
	
	@Test
	public void testBuyTimeSort() {
		addBuyOrders();
		assertEquals(3, buyOrders.first().getTimeEnteredOrderBook(), 0.5);
		clearBuyOrders();
	}
	
	@Test
	public void testSellPriceSort() {
		addSellOrders();
		assertEquals(10.0, sellOrders.first().getPrice(), 0.5);
		clearSellOrders();
	}
	
	@Test
	public void testSellTimeSort() {
		addSellOrders();
		assertEquals(0, sellOrders.first().getTimeEnteredOrderBook(), 0.5);
		clearSellOrders();
	}
	

	
	

}
