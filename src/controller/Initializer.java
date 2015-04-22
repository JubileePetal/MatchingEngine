package controller;



import java.util.ArrayList;

import model.Librarian;
import models.Instrument;
import communications.Greeter;

public class Initializer {
	
	private Greeter greeter;
	private TradeProcessor tradeProcessor;
	private Archiver archiver;
	private Librarian orderbooks;
	private ArrayList<Instrument> instruments;
	
	private Librarian librarian;
	private WorkPool wp;
	
	public Initializer() {
		
	}
	

	public void setUpArchive(){
		
		archiver = new Archiver();
		readFromArchives();
		
	}
	
	public void readFromArchives(){
		
		instruments = archiver.retrieveInstruments();
		
	}
	
	public void setUpLibrary(){
		
		librarian = new Librarian();
		
		for (Instrument i : instruments){
			
			librarian.addOrderBook(i);
			
		}
		
	}
	
	public void setupGreeter(){
		
		greeter = new Greeter(librarian);
		greeter.setInstruments(instruments);
	}
	
	public void startGreeter(){
		(new Thread(greeter)).start();
	}
	
	public void setupWorkPool(int nrOfWorkers){
		
		wp = new WorkPool(librarian);
		wp.createWorkers(nrOfWorkers);
		
	}
	
	public void startWorkers(){
		
		wp.startWorkers();
		
	}


}
