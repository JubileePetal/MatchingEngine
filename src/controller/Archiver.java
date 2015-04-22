package controller;

import java.util.ArrayList;

import models.Instrument;

public class Archiver {

	public Archiver() {
		// TODO Auto-generated constructor stub
	}
	
	
	public ArrayList<Instrument> retrieveInstruments(){
		
		/**this should be read from file, but quickfix for now.*/
		/**TODO Fix this. */
		ArrayList<Instrument> instruments = new ArrayList<Instrument>();
		
		Instrument inst1 = new Instrument();
		inst1.setAbbreviation("ERB");
		inst1.setName("Ericsson B");
		
		Instrument inst2 = new Instrument();
		inst2.setAbbreviation("ICAA");
		inst2.setName("ICA A");
	
		instruments.add(inst1);
		instruments.add(inst2);
		
		return instruments;
	
	}

}
