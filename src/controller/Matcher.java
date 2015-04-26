package controller;

import model.Librarian;
import model.OrderBook;
import models.Order;

public class Matcher implements Runnable {
	
	private Librarian librarian;
	private TradeProcessor tradeProcessor;
	private OrderBook orderBook;
	
	public Matcher(Librarian librarian) {
		this.librarian = librarian;
	}
	
	public void setTradeProcessor(TradeProcessor tradeProcessor) {
		this.tradeProcessor = tradeProcessor;
	}

	@Override
	public void run() {
		
		//TODO Should be while true loop
		String key = librarian.getFirstInQueue(); //Synchronized
		System.out.println("Got this from the queue:" + key);
		orderBook = librarian.getOrderBook(key);
		// CHECK MARKET ORDERS
		// CHECK LIMIT ORDERS
		if(orderBook.matchIsPossible()) {
			runMatchingAlgorithm();
		}
	}
	
	private void runMatchingAlgorithm() {
		
		while(orderBook.matchIsPossible()) {
			Order buyOrder = orderBook.getFirstBuy();
			Order sellOrder = orderBook.getFirstSell();
		}
	}

}
