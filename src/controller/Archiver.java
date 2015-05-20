package controller;

import java.util.ArrayList;

import tests.OptionsCollections;
import models.Instrument;
import models.OpCodes;
import models.OpNames;
import models.Option;

public class Archiver {

	public Archiver() {
		
	}
	
	
	public ArrayList<Instrument> retrieveInstruments(){
		
		/**this should be read from file, but quickfix for now.*/
		/**TODO Fix this. */
		ArrayList<Instrument> instruments = new ArrayList<Instrument>();
		
		Instrument inst1 = new Instrument();
		inst1.setAbbreviation(OpNames.INSTRUMENT1_ABB);
		inst1.setName(OpNames.INSTRUMENT1);
		inst1.setType(OpCodes.STOCK);
		
//		Instrument inst2 = new Instrument();
//		inst2.setAbbreviation(OpNames.INSTRUMENT2_ABB);
//		inst2.setName(OpNames.INSTRUMENT2);
//		inst2.setType(OpCodes.STOCK);
		

		instruments.add(inst1);
//		instruments.add(inst2);
	
		/**********************************************************/
		return instruments;
	
	}
	
	public ArrayList<Option> retrieveOptions(){
		
		return OptionsCollections.smallOptionsSet();
	}
}
