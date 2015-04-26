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
			
			trade.setBuyer("HAMMOCK");
			
		}else{
			
		}
		
		
		
	}
}


