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
		
		while(true) {
			
			String key = librarian.getFirstInQueue();
			System.out.println("Got this from the queue:" + key);
			
			orderBook = librarian.getOrderBook(key);
			
			// TODO CHECK MARKET ORDERS

			if(orderBook.matchIsPossible()) {
				runMatchingAlgorithm();
			}
			
			librarian.putInQueue(key);
		}

	}
	
	public void runMatchingAlgorithm() {
		
		while(orderBook.matchIsPossible()) {
			System.out.println("Match is possible");
			Order buyOrder = orderBook.getFirstBuy();
			Order sellOrder = orderBook.getFirstSell();
			
			System.out.println("buy price: " + buyOrder.getPrice() + " quantity: " + buyOrder.getQuantity());
			System.out.println("sell price: " + sellOrder.getPrice() + " quantity: " + sellOrder.getQuantity());

			if(buyOrder.getQuantity() > sellOrder.getQuantity()) {
				System.out.println("buy quantity > sell quantity");
				matchFromBuy(buyOrder, sellOrder);
				
			} else if(sellOrder.getQuantity() > buyOrder.getQuantity()) {
				System.out.println("sell quantity > buy quantity");

				matchFromSell(buyOrder, sellOrder);
				
			} else {
				System.out.println("sell quantity == buy quantity");

				equalMatch(buyOrder, sellOrder);
			}
		}
	}
	
	public void matchFromBuy(Order buyOrder, Order sellOrder) {
		
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
	
	public void matchFromSell(Order buyOrder, Order sellOrder) {
		
		int originalSellOrderQuantity = sellOrder.getQuantity();
		int buyOrderQuantity = buyOrder.getQuantity();
		
		// create a copy of the sell order, but set it's sell quantity
		// to the amount being bought
		Order sellOrderCopy = (Order) sellOrder.clone();		
		sellOrderCopy.setOrderQuantity(buyOrderQuantity);
		
		// reduce the quantity of the original sell order with
		// an amount equal to that being sold, and put it back
		// in the orderBook
		sellOrder.setOrderQuantity(originalSellOrderQuantity - buyOrderQuantity);
		orderBook.addToSellOrders(sellOrder);
		
		tradeProcessor.createTrade(buyOrder, sellOrderCopy);
	
	}
	
	public void equalMatch(Order buyOrder, Order sellOrder) {
		tradeProcessor.createTrade(buyOrder, sellOrder);;
	}

}
