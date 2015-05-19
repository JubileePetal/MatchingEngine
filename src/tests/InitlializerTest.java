package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import models.OpNames;
import models.Option;

import org.junit.Before;
import org.junit.Test;

import controller.Initializer;

public class InitlializerTest {

	private ArrayList<Option> options;
	private Initializer init;
	@Before
	public void setUp () {
		
		init 	= new Initializer(); 
		init.getOptionsFromArchives();
		options = OptionsCollections.smallOptionsSet();
	}
	
	@Test
	public void testGetOptions(){
		
		ArrayList<Option> ops;
		
		String instrumentName = OpNames.INSTRUMENT1;
		ops 				  = init.getOptions(instrumentName);
		assertTrue(ops.size()==3);
		
		String instrumentName2 = OpNames.INSTRUMENT2;
		ops 				  = init.getOptions(instrumentName2);
		assertTrue(ops.size()==1);
		
	}

}
