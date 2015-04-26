package model;

import models.OpCodes;
import models.Order;

public class OrderGuide {
	
	public void handOffOrder(Order order, OrderBook orderBook) {
		
		if(order.isActive()) {
			takeActionDependentOnType(order, orderBook);
		} else {
		}
	}
	
	public void takeActionDependentOnType(Order order, OrderBook orderBook) {
		
		int typeOfOrder = order.getTypeOfOrder();
		
		if(typeOfOrder == OpCodes.LIMIT_ORDER) {
			
			enterOrder(order, orderBook);
			
			System.out.println("This is a Limit Order");
			
		} else if(typeOfOrder == OpCodes.MARKET_ORDER) {
			
			System.out.println("This is a Market Order");
			//TODO handle market order			
		}		
	}
	
	public void enterOrder(Order order, OrderBook orderBook) {
		
		int orderType = order.isBuyOrSell();
		
		if(orderType == OpCodes.BUY_ORDER) {
			orderBook.addToBuyOrders(order);
		} else if(orderType == OpCodes.SELL_ORDER) {
			orderBook.addToSellOrders(order);
		}

	}
	
}
