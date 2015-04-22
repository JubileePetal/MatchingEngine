package controller;

import java.util.ArrayList;

import model.Librarian;

public class WorkPool {
	
	private Librarian librarian;
	private ArrayList<Worker> workers;

	
	/** PrincessOfTheUniverse **/
	
	public WorkPool() {
		workers  = new ArrayList<Worker>();
		
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

	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}
	
	
}
