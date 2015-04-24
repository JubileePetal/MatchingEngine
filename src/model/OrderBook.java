package model;

import java.util.HashMap;

import models.OpCodes;
import models.Order;

public class OrderBook  {

	private HashMap<Double,Order> buyOrders;
	private HashMap<Double,Order> sellOrders;
	
	public OrderBook() {
		buyOrders = new HashMap<Double,Order>();
		sellOrders = new HashMap<Double,Order>();
	}
	
	public void enterOrder(Order order) {
		
		int typeOfOrder = order.getTypeOfOrder();
		
		if(typeOfOrder == OpCodes.LIMIT_ORDER) {
			
			int orderType = order.isBuyOrSell();
			
			if(orderType == OpCodes.BUY_ORDER) {
				addToHash(buyOrders, order);
			} else if(orderType == OpCodes.SELL_ORDER) {
				addToHash(sellOrders, order);
			}
			
			System.out.println("This is a Limit Order");
			
		} else if(typeOfOrder == OpCodes.MARKET_ORDER) {
			
			System.out.println("This is a Market Order");
			//TODO handle market order			
		}
	}
	
	private void addToHash(HashMap<Double, Order> map , Order order) {

		Double price = order.getPrice();
		
		synchronized(map) {
			map.put(price, order);
		}
	}
	

}
