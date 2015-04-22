package model;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.PriorityQueue;

import models.Instrument;

public class Librarian implements Observer {
	
	private HashMap<String,OrderBook> library;
	private PriorityQueue<String> queue;
	
	public Librarian() {
		
		library = new HashMap<String, OrderBook>();
		queue 	= new PriorityQueue<String>();
	}
	
	public void putInQueue(String abbreviation){
		
		synchronized(queue){
			
			queue.add(abbreviation);
		};
	}
	
	public String getFirstInQueue(){
		
		synchronized(queue){
			
			String abbrev =  queue.poll();
			
		}
		
		return null;
	}
	
	public void addOrderBook(Instrument inst){
		
		OrderBook newOrderBook = new OrderBook();
		//TODO Setup orderbook stuff here
		library.put(inst.getAbbreviation(), newOrderBook);
		putInQueue(inst.getAbbreviation());
		System.out.println("Size: " + library.size());
		
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {

			System.out.println("SIR, YES SIR!");

		
	}
	
	
}
