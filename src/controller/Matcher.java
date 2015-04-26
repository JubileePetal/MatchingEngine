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
			
			if(buyOrder.getQuantity() > sellOrder.getQuantity()) {
				matchFromBuy(buyOrder, sellOrder);
			}
			// if buyOrder Quantity is larger than sellOrder quantity
				// sell is finished
				// split buy into two orders, one with sell amount and one with remaining amount
				// put one buy with remaining amount back
				// create trade
			// if sellorder quantity is larger than buyOrder quantity
				// buy is finished
				// split sell into two orders, one with buy amount and one with remaining amount
				// put one sell with remaining amount back
			// else if they have the same quantity just fricken match
		}
	}
	
	private void matchFromBuy(Order buyOrder, Order sellOrder) {
		
		int sellOrderQuantity = sellOrder.getQuantity();
		int originalBuyOrderQuantity = buyOrder.getQuantity();
		
		// create a copy of the buy order, but set it's buy quantity
		// to the amount being sold
		Order buyOrderCopy = (Order) buyOrder.clone();		
		buyOrderCopy.setOrderQuantity(sellOrderQuantity);
		
		// reduce the quantity of the original buy order with
		// an amount equal to that being bought, and put it back
		// in the orderBook
		buyOrder.setOrderQuantity(originalBuyOrderQuantity - sellOrderQuantity);
		orderBook.addToBuyOrders(buyOrder);
		
		tradeProcessor.createTrade(buyOrderCopy, sellOrder);
	
	}

}
