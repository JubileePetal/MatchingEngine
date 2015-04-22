package controller;



import java.util.ArrayList;

import model.OrderBooks;
import models.Instrument;
import communications.Greeter;

public class Initializer {
	
	private Greeter greeter;
	private TradeProcessor tradeProcessor;
	private Archiver archiver;
	private OrderBooks orderbooks;
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
		
		for (Instrument i : instruments){
			
			
		}
		
	}

}
