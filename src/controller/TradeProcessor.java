package controller;

import models.OpCodes;
import models.Order;

public class TradeProcessor {

	public TradeProcessor() {
		// TODO Auto-generated constructor stub
	}
	
	
	public void createTrade(Order order1, Order order2){
		
		Trade trade = new Trade();
		if(order1.isBuyOrSell() == OpCodes.BUY_ORDER){
			
			setBuyerInfo(order1, trade);
			setSellerInfo(order2, trade);
			
		}else{
			
			setBuyerInfo(order2, trade);
			setSellerInfo(order1, trade);
			
		}
		
		//trade.setQuantity();
		

		
		
		
 	}
	
	private void setSellerInfo(Order sellOrder, Trade trade){
		
		trade.setSeller(sellOrder.getMyOwner());
		trade.setSelOrderID(sellOrder.getId());
	}
	
	private void setBuyerInfo(Order buyOrder, Trade trade){
		
		trade.setBuyer(buyOrder.getMyOwner());
		trade.setBuyOrderID(buyOrder.getId());
	}
	
	
}


