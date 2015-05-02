package controller;

import java.util.ArrayList;

import communications.ClientHandler;
import communications.Greeter;
import models.BookStatus;
import models.OpCodes;
import models.Order;
import models.Trade;

public class TradeProcessor {

	private Greeter greeter;
	
	public TradeProcessor() {
		// TODO Auto-generated constructor stub
	}
		
	public Trade createTrade(Order order1, Order order2){
		Trade trade = new Trade();
		if(order1.isBuyOrSell() == OpCodes.BUY_ORDER){
			
			setBuyerInfo(order1, trade);
			setSellerInfo(order2, trade);
			
		}else{
			
			setBuyerInfo(order2, trade);
			setSellerInfo(order1, trade);
			
		}
		
		trade.setQuantity(order1.getQuantity());
		trade.setPrice(order1.getPrice());
		trade.setInstrument(order1.getInstrument());

		
		
		return trade;
 	}
	
	public void orderPlacedInBook(Order order) {
		ClientHandler owner = greeter.getTrader(order.getMyOwner());
		owner.addOrder(order);
	}
	
	private void setSellerInfo(Order sellOrder, Trade trade){
		
		trade.setSeller(sellOrder.getMyOwner());
		trade.setSelOrderID(sellOrder.getId());
	}
	
	private void setBuyerInfo(Order buyOrder, Trade trade){
		
		trade.setBuyer(buyOrder.getMyOwner());
		trade.setBuyOrderID(buyOrder.getId());
	}
	
	public Greeter getGreeter() {
		return greeter;
	}
	
	public void setGreeter(Greeter greeter) {
		this.greeter = greeter;
	}
	
	public void sendTrade(Trade trade){
		
		ClientHandler seller = greeter.getTrader(trade.getSeller());
		ClientHandler buyer  = greeter.getTrader(trade.getBuyer()); 
		
		seller.sendTrade(trade);
		buyer.sendTrade(trade);
		
	}

	public void broadCastMarketData(BookStatus bookStatus) {
		ArrayList<ClientHandler> handlers = greeter.getAllHandlers();
		for(ClientHandler handler : handlers) {
			handler.sendMarketData(bookStatus);
		}
		
	}
	
}


