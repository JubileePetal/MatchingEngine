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


}
