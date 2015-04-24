package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;

import models.OpCodes;
import models.Order;


public class OrderBook  {

	private TreeSet<Order> pendingOrders;
	private HashMap<Double,Order> buyOrders;
	private HashMap<Double,Order> sellOrders;

	private OrderGuide orderGuide;
	
	public OrderBook() {
		
        Comparator<Order> comparator = new Comparator<Order>() {
            @Override
            public int compare(Order a, Order b) {
                return a.getTimeEnteredOrderBook() < b.getTimeEnteredOrderBook() ? -1 : 1;
            }
        };

		pendingOrders = new TreeSet<Order>(comparator);
		orderGuide = new OrderGuide(this);

		buyOrders = new HashMap<Double,Order>();
		sellOrders = new HashMap<Double,Order>();
	}
	
	public void handOffOrder(Order order) {
		orderGuide.handOffOrder(order);
	}
	
	public void addToPending(Order order) {
		
		synchronized(pendingOrders) {
			pendingOrders.add(order);
		}
	}

	public void addToBuyHash(Order order) {

		Double price = order.getPrice();
		
		synchronized(buyOrders) {
			buyOrders.put(price, order);
		}
	}
	
	public void addToSellHash(Order order) {
		
		Double price = order.getPrice();
		
		synchronized(sellOrders) {
			sellOrders.put(price, order);
		}
	}
	

}
