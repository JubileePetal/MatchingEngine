package controller;

import java.util.ArrayList;

import models.Instrument;
import models.OpCodes;

public class Archiver {

	public Archiver() {
		
	}
	
	
	public ArrayList<Instrument> retrieveInstruments(){
		
		/**this should be read from file, but quickfix for now.*/
		/**TODO Fix this. */
		ArrayList<Instrument> instruments = new ArrayList<Instrument>();
		
		Instrument inst1 = new Instrument();
		inst1.setAbbreviation("ERB");
		inst1.setName("Ericsson B");
		inst1.setType(OpCodes.STOCK);
		
		Instrument inst2 = new Instrument();
		inst2.setAbbreviation("ICAA");
		inst2.setName("ICA A");
		inst2.setType(OpCodes.STOCK);
	
		instruments.add(inst1);
		instruments.add(inst2);
		/**********************************************************/
		return instruments;
	
	}

}
