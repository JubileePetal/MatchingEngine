package model;

import java.util.HashMap;

import models.LimitOrder;
import models.OpCodes;
import models.Order;

public class OrderBook  {

	private HashMap<Double,LimitOrder> buyOrders;
	private HashMap<Double,LimitOrder> sellOrders;
	
	public OrderBook() {
		buyOrders = new HashMap<Double,LimitOrder>();
		sellOrders = new HashMap<Double,LimitOrder>();
	}
	
	public void enterOrder(Order order) {
		
		if(order instanceof LimitOrder) {
			
			LimitOrder limitOrder = (LimitOrder) order;
			
			int orderType = order.getOrderType();
			
			if(orderType == OpCodes.BUY_ORDER) {
				addToHash(buyOrders, limitOrder);
			} else if(orderType == OpCodes.SELL_ORDER) {
				addToHash(sellOrders, limitOrder);
			}
			System.out.println("This is a Limit Order");
			
		} else {
			System.out.println("This is a Market Order");
			//TODO handle market order			
		}
	}
	
	private void addToHash(HashMap<Double, LimitOrder> map , LimitOrder order) {

		Double price = order.getPrice();
		
		synchronized(map) {
			map.put(price, order);
		}
	}
	

}
