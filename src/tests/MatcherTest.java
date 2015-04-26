package tests;

import static org.junit.Assert.*;

import model.Librarian;
import model.OrderBook;
import model.TreeSetCreator;
import models.Instrument;
import models.Order;

import org.junit.Before;
import org.junit.Test;

import controller.Matcher;

public class MatcherTest {

	private Matcher matcher;
	OrderBook ob;
	
	@Before
	public void setUp() throws Exception {
		
		Instrument instrument = new Instrument();
		instrument.setAbbreviation("ERB");
		
		Librarian librarian = new Librarian();
		librarian.addOrderBook(instrument);
		
		ob = librarian.getOrderBook(instrument.getAbbreviation());
		
		Order a = new Order();
		a.setTimeEnteredOrderBook(0);
		a.setPrice(50.0);
		Order b = new Order();
		b.setTimeEnteredOrderBook(6);
		b.setPrice(40.0);
		Order c = new Order();
		c.setTimeEnteredOrderBook(2);
		c.setPrice(60.0);
		Order d = new Order();
		d.setTimeEnteredOrderBook(3);
		d.setPrice(70.0);
		Order e = new Order();
		e.setTimeEnteredOrderBook(4);
		e.setPrice(45.0);
		
		ob.addToSellOrders(a);
		ob.addToSellOrders(b);
		ob.addToSellOrders(c);
		ob.addToSellOrders(d);
		ob.addToSellOrders(e);
		
		Order f = new Order();
		f.setTimeEnteredOrderBook(0);
		f.setPrice(10.0);
		Order g = new Order();
		g.setTimeEnteredOrderBook(6);
		g.setPrice(50.0);
		Order h = new Order();
		h.setTimeEnteredOrderBook(2);
		h.setPrice(20.0);
		Order i = new Order();
		i.setTimeEnteredOrderBook(3);
		i.setPrice(50.0);
		Order j = new Order();
		j.setTimeEnteredOrderBook(4);
		j.setPrice(40.0);
		
		ob.addToBuyOrders(f);
		ob.addToBuyOrders(g);
		ob.addToBuyOrders(h);
		ob.addToBuyOrders(i);
		ob.addToBuyOrders(j);
		
		matcher = new Matcher(librarian);
	}
	
	@Test
	public void test() {
		assertTrue(ob.matchIsPossible());
	}
	
	@Test
	public void testA() {
		matcher.run();
	}

}
