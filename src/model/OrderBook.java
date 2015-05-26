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

	final static private Double RISK_FREE_RATE = 0.1;
	final static private int TESTSIZE = 10;
	final static private Double LAMBDA = 0.94;

	private TreeSet<Order> 	buyOrders;
	private TreeSet<Order>	 	sellOrders;
	private LinkedList<Order>	pendingOrders;
	private ArrayList<Option>	myOptions;
	private LinkedList<Double> spotPrices;
	
	private Double currentEwmaVol;
	private Double currentRateOfReturn;
	private String instrumentName;
	
	public OrderBook() {
		
		pendingOrders 	= new LinkedList<Order>();
		buyOrders 		= TreeSetCreator.createBuyOrderSet();
		sellOrders		= TreeSetCreator.createSellOrderSet();
		spotPrices = new LinkedList<Double>();
		
		currentEwmaVol = null;
		currentRateOfReturn = null;
		
		

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
		
		Double current = spotPrices.getLast();
		if(spotPrices.size() > 1) {
			Double previous = spotPrices.get(spotPrices.size() - 2);
			currentRateOfReturn = (current - previous) / previous;
		}
		try {
			currentEwmaVol = Analyzer.EWMA(currentEwmaVol, currentRateOfReturn, LAMBDA);
		} catch(NullPointerException e) {
			if(currentRateOfReturn != null) {
				currentEwmaVol = currentRateOfReturn;
			}
		}
		
		//System.out.println("Spot price: " + current);
		//System.out.println("Rate of return: " + currentRateOfReturn);
		//System.out.println("EWMA vol: " + currentEwmaVol);
		
		if(spotPrices.size() > 10) {
			
			Analytics analytics = new Analytics();
			
			analytics.setSpotPrice(current);
			analytics.setRateOfReturn(currentRateOfReturn);
			analytics.setEwmaVol(currentEwmaVol);
			
			Double SMA = Analyzer.simpleMovingAverage((LinkedList<Double>) spotPrices.clone(), TESTSIZE);
			analytics.setSMA(SMA);
			//System.out.println("SMA: " + SMA);
			
			Double simpleVol = Analyzer.simpleVolatility((LinkedList<Double>) spotPrices.clone(), TESTSIZE);
			analytics.setSimpleVol(simpleVol);
			//System.out.println("Simple Vol: " + simpleVol);
			
			for(Option option : myOptions) {
				
				Double price, delta, gamma;
				
				if(option.getType() == OpCodes.CALL_OPTION) {
					
					price = Analyzer.callOptionPrice(current, currentEwmaVol, RISK_FREE_RATE, option);
					option.setTheoreticPrice(price);
					delta = Analyzer.callDelta(current, option.getStrikePrice(), RISK_FREE_RATE, currentEwmaVol, option.getTimeToMaturity());
					option.setDelta(delta);
					gamma = Analyzer.gamma(current, option.getStrikePrice(), RISK_FREE_RATE, currentEwmaVol, option.getTimeToMaturity());
					option.setGamma(gamma);
					System.out.println("Call Option strike: " + option.getStrikePrice() + " T: " + option.getTimeToMaturity() + " price: " + price + " delta: " + delta + " gamma: " + gamma);

				} else {

					Double callPrice = Analyzer.callOptionPrice(current, currentEwmaVol, RISK_FREE_RATE, option);
					price = Analyzer.putOptionPrice(callPrice, current, option.getStrikePrice(), RISK_FREE_RATE, option.getTimeToMaturity());
					option.setTheoreticPrice(price);
					delta = Analyzer.putDelta(current, option.getStrikePrice(), RISK_FREE_RATE, currentEwmaVol, option.getTimeToMaturity());
					option.setDelta(delta);
					gamma = Analyzer.gamma(current, option.getStrikePrice(), RISK_FREE_RATE, currentEwmaVol, option.getTimeToMaturity());
					option.setGamma(gamma);
					System.out.println("Put Option strike: " + option.getStrikePrice() + " T: " + option.getTimeToMaturity() + " price: " + price + " delta: " + delta + " gamma: " + gamma);
				}

			}
			analytics.setOptions(myOptions);
<<<<<<< HEAD
			System.out.println(instrumentName);
=======

>>>>>>> 54d0936d50a212a9a655c21a6f539f6ba726dbae
			analytics.setInstrumentName(instrumentName);
			//System.out.println("--------------------------------------------------------------------------");
			
			return analytics;
		}
		
		return null;
		
	}

	public String getInstrumentName() {
		return instrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		
		this.instrumentName = instrumentName;
		
	}
	
}
