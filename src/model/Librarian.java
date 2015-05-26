package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import models.Instrument;
import models.Option;
import models.Order;

public class Librarian implements Observer {
	
	private HashMap<String,OrderBook> library;
	private LinkedList<String> queue;
		
	public Librarian() {
		
		library = new HashMap<String, OrderBook>();
		queue 	= new LinkedList<String>();
	}
	
	public void putInQueue(String abbreviation){
		
		synchronized(queue){
			
			queue.add(abbreviation);
		};
	}
	
	public String getFirstInQueue(){
		
		String abbrev;
		synchronized(queue){
			abbrev =  queue.poll();
		}
		
		return abbrev;
	}
	
	public void addOrderBook(Instrument inst, ArrayList<Option> options){
		
		OrderBook newOrderBook = new OrderBook();
		newOrderBook.setMyOptions(options);
		newOrderBook.setInstrumentName(inst.getName());
		library.put(inst.getAbbreviation(), newOrderBook);
		putInQueue(inst.getAbbreviation());

	}

	@Override
	public void update(Observable arg0, Object arg1) {

			Order order		 = (Order)arg1;
			String key 		 = order.getInstrument().getAbbreviation();
			OrderBook ob	 = library.get(key);
			
			ob.addToQueue(order);
	}
	
	public OrderBook getOrderBook(String key){
		
		return library.get(key);
		
	}
	
}
