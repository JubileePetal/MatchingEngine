package model;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;

import models.Instrument;
import models.Order;

public class Librarian implements Observer {
	
	private HashMap<String,OrderBook> library;
	private PriorityQueue<String> queue;
	
	public Librarian() {
		
		library = new HashMap<String, OrderBook>();
		queue 	= new PriorityQueue<String>();
	}
	
	public void putInQueue(String abbreviation){
		
		
		System.out.println("Putting in queue...");
		synchronized(queue){
			
			queue.add(abbreviation);
		};
	}
	
	public String getFirstInQueue(){
		
		System.out.println("Getting from queue...");
		String abbrev;
		synchronized(queue){
			
			abbrev =  queue.poll();
			
		}
		
		return abbrev;
	}
	
	public void addOrderBook(Instrument inst){
		
		OrderBook newOrderBook = new OrderBook();
		//TODO Setup orderbook stuff here
		library.put(inst.getAbbreviation(), newOrderBook);
		putInQueue(inst.getAbbreviation());
	
		
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {

			Order order = (Order)arg1;
			System.out.println("Order ID in Librarian: " + order.getId());
			System.out.println("Order Abbrev: " + order.getInstrument().getAbbreviation());
			String key = order.getInstrument().getAbbreviation();
			
			OrderBook ob = library.get(key);
			ob.handOffOrder(order);
			order.setTimeEnteredOrderBook(System.currentTimeMillis());
			
		
	}
	
	
}
