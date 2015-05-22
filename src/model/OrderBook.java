package model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeSet;

import analytics.Analyzer;

import models.Analytics;
import models.OpCodes;
import models.Option;
import models.Order;
import models.Trade;
import models.TreeSetCreator;


public class OrderBook  {

	private TreeSet<Order> 	buyOrders;
	private TreeSet<Order>	 	sellOrders;
	private LinkedList<Order>	pendingOrders;
	private ArrayList<Option>	myOptions;
	private LinkedList<Double> spotPrices;
	
	private Double currentEwmaVol;
	private Double currentRateOfReturn;
	
	public OrderBook() {
		
		pendingOrders 	= new LinkedList<Order>();
		buyOrders 		= TreeSetCreator.createBuyOrderSet();
		sellOrders		= TreeSetCreator.createSellOrderSet();
		spotPrices = new LinkedList<Double>();
		
		currentEwmaVol = 0d;
		currentRateOfReturn = 0d;

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

	public ArrayList<Option> getMyOptions() {
		return myOptions;
	}

	public void setMyOptions(ArrayList<Option> myOptions) {
		this.myOptions = myOptions;
	}

	public void tradeMade(double price) {
		spotPrices.add(price);
	}
	
	public Analytics generateAnalytics() {
		
		int testSize = 10;
		Double lambda = 0.9;
		
		Analytics analytics = new Analytics();
		Double SMA = Analyzer.simpleMovingAverage((LinkedList<Double>) spotPrices.clone(), testSize);
		analytics.setSMA(SMA);
		System.out.println("SMA: " + SMA);
		
		Double simpleVol = Analyzer.simpleVolatility((LinkedList<Double>) spotPrices.clone(), testSize);
		analytics.setSimpleVol(simpleVol);
		System.out.println("Simple Vol: " + simpleVol);
		
		currentEwmaVol = Analyzer.EWMA(currentEwmaVol, currentRateOfReturn, lambda);
		
		if(spotPrices.size() > 1) {
			Double current = spotPrices.getLast();
			Double previous = spotPrices.get(spotPrices.size() - 2);
			currentRateOfReturn = (current - previous) / previous;
		} else {
			currentRateOfReturn = 0d;
		}
		
		return null;
	}
	
}
