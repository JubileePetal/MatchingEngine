package controller;

import java.util.ArrayList;

import model.Librarian;

public class WorkPool {
	
	private Librarian librarian;
	private ArrayList<Matcher> workers;

	
	/** PrincessOfTheUniverse **/
	
	public WorkPool() {
		workers  = new ArrayList<Matcher>();
		
	}
	
	public void createWorkers(int nrOfWorkers){
		
		for(int i = 0; i < nrOfWorkers; i++){
			
			Matcher worker = new Matcher(librarian);
			workers.add(worker);
		}
		
	}


	public void startWorkers(){

		
		for(Matcher w : workers){
			
			(new Thread(w)).start();
			
		}
		
	}

	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}
	
	
}
