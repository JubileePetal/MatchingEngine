package model;

import models.OpCodes;
import models.Order;

public class OrderGuide {

	private OrderBook orderBook;
	
	public OrderGuide(OrderBook orderBook) {
		this.orderBook = orderBook;
	}
	
	public void handOffOrder(Order order) {
		
		if(order.isActive()) {
			takeActionDependentOnType(order);
		} else {
			placeInHibernation(order);
		}
	}
	
	public void takeActionDependentOnType(Order order) {
		
		int typeOfOrder = order.getTypeOfOrder();
		
		if(typeOfOrder == OpCodes.LIMIT_ORDER) {
			
			enterOrder(order);
			
			System.out.println("This is a Limit Order");
			
		} else if(typeOfOrder == OpCodes.MARKET_ORDER) {
			
			System.out.println("This is a Market Order");
			//TODO handle market order			
		}		
	}
	
	public void placeInHibernation(Order order) {
		orderBook.addToPending(order);
	}
	
	public void enterOrder(Order order) {
		
		int orderType = order.isBuyOrSell();
		
		if(orderType == OpCodes.BUY_ORDER) {
			orderBook.addToBuyHash(order);
		} else if(orderType == OpCodes.SELL_ORDER) {
			orderBook.addToSellHash(order);
		}

	}
	
}
