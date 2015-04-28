package controller;

import java.util.ArrayList;

import communications.Greeter;

import model.Librarian;

public class WorkPool {
	
	private TradeProcessor tradeProcessor;
	private Librarian librarian;
	private ArrayList<Matcher> workers;
	

	
	/** PrincessOfTheUniverse **/
	
	public WorkPool() {
		workers  = new ArrayList<Matcher>();
		
	}
	
	public void createWorkers(int nrOfWorkers){
		
		for(int i = 0; i < nrOfWorkers; i++){
			
			Matcher matcher = new Matcher();
			matcher.setLibrarian(librarian);
			matcher.setTradeProcessor(tradeProcessor);
			workers.add(matcher);
		}
		
	}

	public void startWorkers(){

		
		for(Matcher m : workers){
			
			(new Thread(m)).start();
			
		}
		
	}

	public void setLibrarian(Librarian librarian) {
		this.librarian = librarian;
	}
	

	public void setTradeProcessor(TradeProcessor tradeProcessor) {
		this.tradeProcessor = tradeProcessor;
	}

	
	

	
	
	
	
}
