package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;

import models.OpCodes;
import models.Order;


public class OrderBook  {

	private TreeSet<Order> pendingOrders;
	private TreeSet<Order> marketOrders;
	
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
        
        marketOrders = new TreeSet<Order>(comparator);
		pendingOrders = new TreeSet<Order>(comparator);
		
		buyOrders = new HashMap<Double,Order>();
		sellOrders = new HashMap<Double,Order>();
		
		orderGuide = new OrderGuide(this);
	}
	
	public void handleMarketOrders() {
		
		boolean workExists = marketOrders.isEmpty() ? false : true;
		
		while(workExists) {
			Order order = marketOrders.pollFirst();
			workExists = false;
		}
	}
	
	public Double getMaxBuy() {
		
		return 0.0;
	}
	
	public void match() {
	/*	
		Entry<Double, Order> min = null;
		for (Entry<String, Double> entry : map.entrySet()) {
		    if (min == null || min.getValue() > entry.getValue()) {
		        min = entry;
		    }
		}
		double maxBuy = Collections.max(buyOrders.values());
		*/
	}
	
	public void handOffOrder(Order order) {
		orderGuide.handOffOrder(order);
	}
	
	public void addToMarketOrders(Order order) {
		
		synchronized(marketOrders) {
			marketOrders.add(order);
		}
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
