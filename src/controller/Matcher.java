package controller;

import model.Librarian;
import model.OrderBook;
import models.Analytics;
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
		
		while(currentOrderBook.canMatch(order, type) && quantityRemains) {
			quantityRemains = match(order);
		}
		
		if(quantityRemains) {

			if(type == OpCodes.BUY_ORDER) {
				currentOrderBook.addToBuyOrders(order);
				
			} else if(type == OpCodes.SELL_ORDER) {
				currentOrderBook.addToSellOrders(order);
			}
			tradeProcessor.orderPlacedInBook(order);
			BookStatus bookStatus = processMarketData(order.getInstrument().getName());
			tradeProcessor.broadCastMarketData(bookStatus);
		}
	}
	
	public BookStatus processMarketData(String instrumentName) {

		BookStatus bookStatus = new BookStatus(instrumentName);
		bookStatus.generateBuyLevels(currentOrderBook.getBuyOrders());
		bookStatus.generateSellLevels(currentOrderBook.getSellOrders());
		return bookStatus;
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
		
		
		Order orderClone;
		
		if(myQuantity == matchedQuantity) {
			
			
			equalMatch(myOrder, matchedOrder);
			
			myQuantityRemains = false;
			
		} else {
			
			if(myQuantity > matchedQuantity) {
				orderClone = getClonedOrder(myOrder, matchedQuantity);
				equalMatch(orderClone, matchedOrder);
				
				int remainingQuantity = myQuantity - matchedQuantity;
				myOrder.setOrderQuantity(remainingQuantity);
				
				myQuantityRemains = true;
				
			} else if(myQuantity < matchedQuantity) {
				orderClone = getClonedOrder(matchedOrder, myQuantity);
				equalMatch(myOrder, orderClone);
				
				int remainingQuantity = matchedQuantity - myQuantity;
				matchedOrder.setOrderQuantity(remainingQuantity);
				
				putMatchedOrderBack(matchedOrder);
				myQuantityRemains = false;
				
			} 		
		}
		BookStatus bookStatus = processMarketData(myOrder.getInstrument().getName());
		tradeProcessor.broadCastMarketData(bookStatus);
		return myQuantityRemains;
	}

	public void equalMatch(Order myOrder, Order matchedOrder) {
		// Need to price promote my order with that of the
		// order in the orderbook
		myOrder.setPrice(matchedOrder.getPrice());
		Trade trade = tradeProcessor.createTrade(myOrder, matchedOrder);
		trade.setTradeMadeTime(System.currentTimeMillis());

		tradeProcessor.sendTrade(trade);
		currentOrderBook.tradeMade(trade.getBuyPartial().getOrder().getPrice());
		Analytics analytics = currentOrderBook.generateAnalytics();
		if(analytics != null) {
			tradeProcessor.sendAnalytics(analytics);
		}
		
		
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
	
	public OrderBook getCurrentOrderBook() {
		return currentOrderBook;
	}
	
	public String getCurrentInstrument() {
		return currentInstrument;
	}
	
	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}
	
	public Librarian getLibrarian() {
		return librarian;
	}
	
	public void setTradeProcessor(TradeProcessor tradeProcessor) {
		this.tradeProcessor = tradeProcessor;
	}
	
	public TradeProcessor getTradeProcessor() {
		return tradeProcessor;
	}
}
	
	
	