package model;

import java.util.Comparator;
import java.util.TreeSet;

import models.Order;

public final class TreeSetCreator {
	
	private TreeSetCreator() {
		
	}
	
	public static TreeSet<Order> createMarketOrderSet() {
		
        Comparator<Order> marketComparator = new Comparator<Order>() {
            @Override
            public int compare(Order a, Order b) {
                return a.getTimeEnteredOrderBook() < b.getTimeEnteredOrderBook() ? -1 : 1;
            }
        };
        
        return new TreeSet<Order>(marketComparator);
	}
	
	public static TreeSet<Order> createSellOrderSet() {
		
        Comparator<Order> sellOrderComp = new Comparator<Order>() {
            @Override
            public int compare(Order a, Order b) {
            	
            	double priceA = a.getPrice();
            	long timeA = a.getTimeEnteredOrderBook();
            	
            	double priceB = b.getPrice();
            	long timeB = b.getTimeEnteredOrderBook();
            	
            	if(priceA < priceB) {
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
        
		return new TreeSet<Order>(sellOrderComp);

	}
	
	public static TreeSet<Order> createBuyOrderSet() {
		
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
        
		return new TreeSet<Order>(buyOrderComp);

	}
	
}
