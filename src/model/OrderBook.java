package model;


import java.util.LinkedList;
import java.util.TreeSet;
import models.OpCodes;
import models.Order;
import models.TreeSetCreator;


public class OrderBook  {

	private TreeSet<Order> 		 buyOrders;
	private TreeSet<Order>	 	 sellOrders;
	private LinkedList<Order>	 pendingOrders;
	
	public OrderBook() {
		
		pendingOrders 	= new LinkedList<Order>();
		buyOrders 		= TreeSetCreator.createBuyOrderSet();
		sellOrders		= TreeSetCreator.createSellOrderSet();

	}
	
	public boolean canMatch(Order order, int type) {
		
		boolean canMatch = false;
		
		double myPrice = order.getPrice();
			
		if(type == OpCodes.BUY_ORDER) {
			if(!sellOrders.isEmpty()) {
				double minSell = sellOrders.first().getPrice();			
				canMatch = myPrice >= minSell;
			}
		} else if(type == OpCodes.SELL_ORDER) {
			
			if(!buyOrders.isEmpty()) {
				double maxBuy = buyOrders.first().getPrice();			
				canMatch = myPrice <= maxBuy;
			}
		}
		
		return canMatch;
	}
	
	public boolean ordersInQueue() {
		return !pendingOrders.isEmpty();
	}
	
	public Order getFirstPending() {
		Order order = null;
		
		synchronized(pendingOrders) {
			order = pendingOrders.poll();
		}
		return order;
	}
	
	public Order getFirstBuy() {		
		Order order = null;
		
		synchronized(buyOrders) {
			order = buyOrders.pollFirst();
		}
		
		return order;
	}
	
	public Order getFirstSell() {
		Order order = null;
		
		synchronized(sellOrders) {
			order = sellOrders.pollFirst();
		}
		
		return order;
	}

	public void addToBuyOrders(Order order) {
		
		synchronized(buyOrders) {
			buyOrders.add(order);
		}
	}
	
	public void addToSellOrders(Order order) {
		
		synchronized(sellOrders) {
			sellOrders.add(order);
		}
	}

	public void addToQueue(Order order) {
		
		synchronized(pendingOrders) {
			pendingOrders.add(order);
		}
		
	}

	public TreeSet<Order> getBuyOrders() {
		return (TreeSet<Order>) buyOrders.clone();
	}

	public TreeSet<Order> getSellOrders() {

		return (TreeSet<Order>) sellOrders.clone();
	}
	

}
