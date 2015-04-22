package controller;

import model.Librarian;

public class Worker implements Runnable {
	
	private Librarian librarian;
	
	public Worker(Librarian librarian) {
		this.librarian = librarian;
	}

	@Override
	public void run() {
	
		
		String key = librarian.getFirstInQueue();
		System.out.println("Got this from the queue:" + key);
		
	}

}
