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

	private TreeSet<Order> marketOrders;
	private TreeSet<Order> buyOrders;
	private TreeSet<Order> sellOrders;
	
	public OrderBook() {
		
		buyOrders = TreeSetCreator.createBuyOrderSet();
		sellOrders = TreeSetCreator.createSellOrderSet();
		marketOrders = TreeSetCreator.createMarketOrderSet();

	}
	

/*	
	public void handleMarketOrders() {
		
		boolean workExists = marketOrders.isEmpty() ? false : true;
		
		while(workExists) {
			Order order = marketOrders.pollFirst();
			workExists = false;
		}
	}
*/	
	/*
	public boolean isMatchPossible() {
		
		//double maxBuy = getMaxBuy().doubleValue();
		double minSell = getMinSell().doubleValue();
		
		//return maxBuy > minSell ? true : false;
	}
	*/
	
	public void addToMarketOrders(Order order) {
		
		synchronized(marketOrders) {
			marketOrders.add(order);
		}
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
	

}
