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

	private TreeSet<Order> buyOrdersT;
	
	private OrderGuide orderGuide;
	
	public OrderBook() {
		
        Comparator<Order> buyOrderComp = new Comparator<Order>() {
            @Override
            public int compare(Order a, Order b) {
            	
            	double priceA = a.getPrice();
            	long timeA = a.getTimeEnteredOrderBook();
            	
            	double priceB = b.getPrice();
            	long timeB = b.getTimeEnteredOrderBook();
            	
            	if(priceA > priceB) {
            		return -1;
            	} else if(priceA == priceB) {
            		if(timeA < timeB) {
            			return -1;
            		} else {
            			return 1;
            		}
            	} else {
            		return 1;
            	}
            	
            }
        };
		buyOrdersT = new TreeSet<Order>(buyOrderComp);
		
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
	
	public Order getBuyOrder(Double key) {
		
		
		
		return null;
	}
	
	public void handleMarketOrders() {
		
		boolean workExists = marketOrders.isEmpty() ? false : true;
		
		while(workExists) {
			Order order = marketOrders.pollFirst();
			workExists = false;
		}
	}
	
	public Double getMaxBuy() {
		
		Entry<Double, Order> max = null;
		
		synchronized(buyOrders) {
			
			for (Entry<Double, Order> entry : buyOrders.entrySet()) {
			    if (max == null || max.getKey() < entry.getKey()) {
			        max = entry;
			    }
			}
		}

		return max.getKey();
	}
	
	public Double getMinSell() {
		
		Entry<Double, Order> min = null;
		
		synchronized(sellOrders) {
			
			for (Entry<Double, Order> entry : sellOrders.entrySet()) {
			    if (min == null || min.getKey() > entry.getKey()) {
			        min = entry;
			    }
			}
		}

		return min.getKey();
	}
	
	public boolean isMatchPossible() {
		
		double maxBuy = getMaxBuy().doubleValue();
		double minSell = getMinSell().doubleValue();
		
		return maxBuy > minSell ? true : false;
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
	
	public void addToBuy(Order order) {

		synchronized(buyOrdersT) {
			buyOrdersT.add(order);
		}
	}
	
	public Order getMaxBuyT() {

		Order order = null;
		
		synchronized(buyOrdersT) {
			order = buyOrdersT.pollFirst();
		}
		return order;
	}
	
	public void addToSellHash(Order order) {
		
		Double price = order.getPrice();
		
		synchronized(sellOrders) {
			sellOrders.put(price, order);
		}
	}
	

}
