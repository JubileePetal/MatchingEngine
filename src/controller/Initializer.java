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
	
	public Initializer() {
		// TODO Auto-generated constructor stub
	}
	

	public void setUpArchive(){
		
		archiver = new Archiver();
		readFromArchives();
		
	}
	
	public void readFromArchives(){
		
		instruments = archiver.retrieveInstruments();
		
	}
	
	public void setUpOrderBooks(){
		
		Librarian librarian = new Librarian();
		
		for (Instrument i : instruments){
			
			librarian.addOrderBook(i);
			
		}
		
	}
	
	public void setupGreeter(){
		
		greeter = new Greeter();
		greeter.setInstruments(instruments);
	}
	
	public void startGreeter(){
		(new Thread(greeter)).start();
	}



}
