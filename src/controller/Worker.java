package controller;

import model.Librarian;
import model.OrderBook;

public class Worker implements Runnable {
	
	private Librarian librarian;
	
	public Worker(Librarian librarian) {
		this.librarian = librarian;
	}

	@Override
	public void run() {
		
		//TODO Should be while true loop
		String key = librarian.getFirstInQueue(); //Synchronized
		System.out.println("Got this from the queue:" + key);
		OrderBook orderBook = librarian.getOrderBook(key);
	}

}
