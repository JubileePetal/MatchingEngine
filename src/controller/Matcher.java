package controller;

import model.Librarian;
import model.OrderBook;
import models.BookStatus;
import models.OpCodes;
import models.Order;
import models.Trade;

public class Matcher implements Runnable {
	
	private Librarian librarian;
	private TradeProcessor tradeProcessor;
	private OrderBook currentOrderBook;
	private String currentInstrument;

	@Override
	public void run() {
		
		while(true) {
			processOrderBook();
		}

	}
	
	public void processOrderBook() {
		
		if(borrowOrderBook()) {
			//System.out.println("Got this from the queue:" + currentInstrument);

			while(currentOrderBook.ordersInQueue()) {
				processOrder();
			}
			
			returnOrderBook();
		}

	}
	
	public void processOrder() {
		
		
		Order order = currentOrderBook.getFirstPending();
		int type = order.isBuyOrSell();
		boolean quantityRemains = true;
		
		System.out.println("got order: price: " + order.getPrice() + " quantity: " + order.getQuantity() + "buy or sell: " + order.isBuyOrSell());

		
		while(currentOrderBook.canMatch(order, type) && quantityRemains) {
			System.out.println("match is possible");
			quantityRemains = match(order);
		}
		
		if(quantityRemains) {

			if(type == OpCodes.BUY_ORDER) {
				currentOrderBook.addToBuyOrders(order);
				
			} else if(type == OpCodes.SELL_ORDER) {
				currentOrderBook.addToSellOrders(order);
			}
			tradeProcessor.orderPlacedInBook(order);
			processMarketData(order);
		}
	}
	
	public void processMarketData(Order order) {
		
		BookStatus bookStatus = new BookStatus(order.getInstrument().getName());
		bookStatus.generateBuyLevels(currentOrderBook.getBuyOrders());
		bookStatus.generateSellLevels(currentOrderBook.getSellOrders());
		tradeProcessor.broadCastMarketData(bookStatus);
		System.out.println("Sent market data, buy: " + currentOrderBook.getBuyOrders().size() + " sell: " + currentOrderBook.getSellOrders().size());
	}
	
	public Order getMatchedOrder(int myType) {
		
		Order matchedOrder;
		
		if(myType == OpCodes.BUY_ORDER) {
			matchedOrder = currentOrderBook.getFirstSell();
		} else {
			matchedOrder = currentOrderBook.getFirstBuy();
		}
		
		return matchedOrder;
	}
	
	public Order getClonedOrder(Order orderToClone, int newQuantity) {
		Order newOrder = (Order) orderToClone.clone();
		newOrder.setOrderQuantity(newQuantity);
		return newOrder;
	}
	
	public void putMatchedOrderBack(Order matchedOrder) {
		
		int matchedType = matchedOrder.isBuyOrSell();
		
		if(matchedType == OpCodes.BUY_ORDER) {
			currentOrderBook.addToBuyOrders(matchedOrder);
		} else {
			currentOrderBook.addToSellOrders(matchedOrder);
		}
	}
	
	public boolean match(Order myOrder) {

		boolean myQuantityRemains = false;
		int myQuantity = myOrder.getQuantity();
		int myType = myOrder.isBuyOrSell();
		
		Order matchedOrder = getMatchedOrder(myType);
		int matchedQuantity = matchedOrder.getQuantity();
		
		System.out.println("matchedOrder: price: " + matchedOrder.getPrice() + " quantity: " + matchedOrder.getQuantity());
		
		Order orderClone;
		
		if(myQuantity == matchedQuantity) {
			
			System.out.println("Equal quantities");
			
			equalMatch(myOrder, matchedOrder);
			
			myQuantityRemains = false;
			
		} else {
			
			if(myQuantity > matchedQuantity) {
				System.out.println("My quantity > matched quantity");
				orderClone = getClonedOrder(myOrder, matchedQuantity);
				equalMatch(orderClone, matchedOrder);
				
				int remainingQuantity = myQuantity - matchedQuantity;
				myOrder.setOrderQuantity(remainingQuantity);
				
				myQuantityRemains = true;
				
			} else if(myQuantity < matchedQuantity) {
				System.out.println("My Quantity < matched quantity");
				orderClone = getClonedOrder(matchedOrder, myQuantity);
				equalMatch(myOrder, orderClone);
				
				int remainingQuantity = matchedQuantity - myQuantity;
				matchedOrder.setOrderQuantity(remainingQuantity);
				
				putMatchedOrderBack(matchedOrder);
				myQuantityRemains = false;
				
			} 		
		}
		processMarketData(myOrder);
		return myQuantityRemains;
	}

	public void equalMatch(Order myOrder, Order matchedOrder) {
		myOrder.setPrice(matchedOrder.getPrice());
		Trade trade = tradeProcessor.createTrade(myOrder, matchedOrder);
		tradeProcessor.sendTrade(trade);
	}
	
	public boolean borrowOrderBook() {
		
		currentInstrument = librarian.getFirstInQueue();
		
		if(currentInstrument != null) {
			currentOrderBook = librarian.getOrderBook(currentInstrument);
			return true;
		} else {
			currentOrderBook = null;
			return false;
		}
		
	}
	
	public void returnOrderBook() {

		librarian.putInQueue(currentInstrument);
	}

	
	public Librarian getLibrarian() {
		return librarian;
	}
	public TradeProcessor getTradeProcessor() {
		return tradeProcessor;
	}
	
	public OrderBook getCurrentOrderBook() {
		return currentOrderBook;
	}
	
	public String getCurrentInstrument() {
		return currentInstrument;
	}
	
	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}
	
	public void setTradeProcessor(TradeProcessor tradeProcessor) {
		this.tradeProcessor = tradeProcessor;
	}
	
}
	
	
	