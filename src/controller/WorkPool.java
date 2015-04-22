package controller;

import java.util.ArrayList;

import model.Librarian;

public class WorkPool {
	
	private Librarian librarian;
	private ArrayList<Worker> workers;

	
	/** PrincessOfTheUniverse **/
	
	public WorkPool(Librarian librarian) {
		workers  = new ArrayList<Worker>();
		this.librarian = librarian;
	}
	
	public void createWorkers(int nrOfWorkers){
		
		for(int i = 0; i < nrOfWorkers; i++){
			
			Worker worker = new Worker(librarian);
			workers.add(worker);
		}
		
	}


	public void startWorkers(){
		
		for(Worker w : workers){
			
			(new Thread(w)).start();
			
		}
		
	}
}
