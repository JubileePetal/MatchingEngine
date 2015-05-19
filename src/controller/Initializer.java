package controller;

import java.util.ArrayList;

import model.Librarian;
import models.Instrument;
import models.Option;
import communications.Greeter;

public class Initializer {
	
	private Greeter 		greeter;
	private TradeProcessor 	tradeProcessor;
	private Archiver 		archiver;
	private Librarian 		librarian;
	private WorkPool 		wp;
	
	
	private ArrayList<Instrument> 	instruments;
	private ArrayList<Option>		options;
	
	
	
	public Initializer() {
		
		archiver 		= new Archiver();
		librarian 		= new Librarian();
		greeter 		= new Greeter();
		tradeProcessor 	= new TradeProcessor();
		wp 				= new WorkPool();
	}
		
	public void getInstrumentsFromArchives(){
		
		instruments = archiver.retrieveInstruments();
		
	}
	
	public void getOptionsFromArchives(){
		
		options		= archiver.retrieveOptions();
	}
	
	public void startGreeter(){
		(new Thread(greeter)).start();
	}
	
	public void setupWorkPool(int nrOfWorkers){

		wp.createWorkers(nrOfWorkers);
		
	}
	
	public void startWorkers(){
		
		wp.startWorkers();
		
	}
	
	public void establishDependencies(){
		
		getInstrumentsFromArchives();
		getOptionsFromArchives();
		greeter.setInstruments(instruments);
		greeter.setOptions(options);
		greeter.setLibrarian(librarian);
		wp.setLibrarian(librarian);
		tradeProcessor.setGreeter(greeter);
		wp.setTradeProcessor(tradeProcessor);
		
		
		for (Instrument i : instruments){
			
			librarian.addOrderBook(i);
			
		}
		
		
		
	}
	
	private ArrayList<Option> getOptions(String instrumentName){
		
		return null;
	}


}
