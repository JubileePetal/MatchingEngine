package controller;

import java.util.ArrayList;

import communications.ClientHandler;
import communications.Greeter;
import models.BookStatus;
import models.OpCodes;
import models.Order;
import models.PartialTrade;
import models.Trade;

public class TradeProcessor {

	private Greeter greeter;
	
	public Trade createTrade(Order orderA, Order orderB) {

		Order buyOrder = null;
		Order sellOrder = null;
		
		if(orderA.isBuyOrSell() == OpCodes.BUY_ORDER) {
			buyOrder = orderA;
			sellOrder = orderB;
		} else {
			buyOrder = orderB;
			sellOrder = orderA;
		}
		
		long id = greeter.getUniqueTradeID();	
		
		PartialTrade buyPartial = createPartialTrade(buyOrder, id);
		
		PartialTrade sellPartial = createPartialTrade(sellOrder, id);
		
		Trade trade = new Trade();
		trade.setTradeID(id);
		trade.setBuyPartial(buyPartial);
		trade.setSellPartial(sellPartial);
		
		return trade;
	}
	
	public PartialTrade createPartialTrade(Order order, long id) {
		PartialTrade partialTrade = new PartialTrade();
		partialTrade.setOrder(order);
		partialTrade.setTradeID(id);
		return partialTrade;
	}
	
	public void orderPlacedInBook(Order order) {
		ClientHandler owner = greeter.getTrader(order.getMyOwner());
		owner.addOrder(order);
	}

	public Greeter getGreeter() {
		return greeter;
	}
	
	public void setGreeter(Greeter greeter) {
		this.greeter = greeter;
	}
	
	public void sendTrade(Trade trade){
	
		sendPartialTrade(trade.getBuyPartial());
		sendPartialTrade(trade.getSellPartial());
		
	}
	
	public void sendPartialTrade(PartialTrade partialTrade) {
		String clientName = partialTrade.getOrder().getMyOwner();
		ClientHandler clientHandler = greeter.getTrader(clientName);
		if(clientHandler != null) {
			clientHandler.sendPartialTrade(partialTrade);
		}
	}

	public void broadCastMarketData(BookStatus bookStatus) {
		greeter.addBookStatus(bookStatus);
		ArrayList<ClientHandler> handlers = greeter.getAllHandlers();
		for(ClientHandler handler : handlers) {
			handler.sendMarketData(bookStatus);
		}
		
	}
	
}


