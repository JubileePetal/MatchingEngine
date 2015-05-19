package tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import analytics.Analyzer;

public class AnalyzerTest {
	
	// 9 decimals, to account for lack of calculator precision
	private static final double DELTA = 0.000000001;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testCallOptionPrice() {
		Double price = Analyzer.callOptionPrice(null, null, null);
	}
	

	@Test 
	public void testEWMAHullExample() {
		Double lambda = 0.9;
		Double prevVol = 0.01;
		Double prevRate = 0.02;
		Double volatility = Analyzer.EWMA(prevVol, prevRate, lambda);
		
		assertEquals(0.0114017543, volatility, DELTA);
	}
	
	@Test 
	public void testEWMAEmpty() {
		Double lambda = 0.95;
		Double prevVol = 0.0;
		Double prevRate = 0.0;
		Double volatility = Analyzer.EWMA(prevVol, prevRate, lambda);
		
		assertEquals(0.0, volatility, DELTA);
	}
	
	@Test
	public void testSimpleVolatilityTooLarge() {
		
		LinkedList<Double> spotPrices = new LinkedList<Double>();

		spotPrices.add(10.0);
		spotPrices.add(20.0);
		spotPrices.add(10.0);
		spotPrices.add(20.0);
		spotPrices.add(10.0);
		spotPrices.add(20.0);
		spotPrices.add(10.0);
		spotPrices.add(20.0);
		spotPrices.add(24.5);
		spotPrices.add(25.25);
		spotPrices.add(23.0);
		spotPrices.add(21.5);
		spotPrices.add(22.0);
		spotPrices.add(24.75);
		spotPrices.add(26.0);
		spotPrices.add(26.5);
		spotPrices.add(27.5);
		spotPrices.add(26.0);
		int testSize = 9;
		
		Double result = Analyzer.simpleVolatility(spotPrices, testSize);
		assertEquals(0.063784735, result, DELTA);
	}
	
	@Test
	public void testSimpleVolatility10() {
		
		LinkedList<Double> spotPrices = new LinkedList<Double>();

		spotPrices.add(24.5);
		spotPrices.add(25.25);
		spotPrices.add(23.0);
		spotPrices.add(21.5);
		spotPrices.add(22.0);
		spotPrices.add(24.75);
		spotPrices.add(26.0);
		spotPrices.add(26.5);
		spotPrices.add(27.5);
		spotPrices.add(26.0);
		int testSize = 10;
		
		Double result = Analyzer.simpleVolatility(spotPrices, testSize);
		assertEquals(0.063784735, result, DELTA);
	}

	@Test
	public void testSimpleVolatilityOneVal() {

		LinkedList<Double> spotPrices = new LinkedList<Double>();
		spotPrices.add(12.0);
		int testSize = 10;
		
		Double result = Analyzer.simpleVolatility(spotPrices, testSize);
		assertEquals(0.0, result, DELTA);
	}
	
	@Test
	public void testSimpleVolatilityEmpty() {

		LinkedList<Double> spotPrices = new LinkedList<Double>();
		int testSize = 10;
		
		Double result = Analyzer.simpleVolatility(spotPrices, testSize);
		assertEquals(0.0, result, DELTA);
	}
	
	@Test
	public void testSMAEmpty() {
		
		LinkedList<Double> spotPrices = new LinkedList<Double>();
		
		int testSize = 10;
		
		Double result = Analyzer.simpleMovingAverage(spotPrices, testSize);
		
		assertEquals(0.0, result, DELTA);
	}

	@Test
	public void testSMAOneVal() {
		
		LinkedList<Double> spotPrices = new LinkedList<Double>();
		spotPrices.add(10.0);
		
		int testSize = 10;
		
		Double result = Analyzer.simpleMovingAverage(spotPrices, testSize);
		
		assertEquals(10.0, result, DELTA);
	}
	
	@Test
	public void testSMASameSize() {
		
		LinkedList<Double> spotPrices = new LinkedList<Double>();
		spotPrices.add(24.5);
		spotPrices.add(25.25);
		spotPrices.add(23.0);
		spotPrices.add(21.5);
		spotPrices.add(22.0);
		spotPrices.add(24.75);
		spotPrices.add(26.0);
		spotPrices.add(26.5);
		spotPrices.add(27.5);
		spotPrices.add(26.0);
		
		int testSize = 10;
		
		Double result = Analyzer.simpleMovingAverage(spotPrices, testSize);
		
		assertEquals(24.7, result, DELTA);
	}
	
	@Test
	public void testSMALargeSize() {
		
		LinkedList<Double> spotPrices = new LinkedList<Double>();
		spotPrices.add(10.0);
		spotPrices.add(10.0);
		spotPrices.add(10.0);
		spotPrices.add(10.0);
		spotPrices.add(10.0);
		spotPrices.add(20.0);
		spotPrices.add(20.0);
		spotPrices.add(20.0);
		spotPrices.add(20.0);
		spotPrices.add(20.0);
		spotPrices.add(30.0);
		spotPrices.add(30.0);
		spotPrices.add(30.0);
		spotPrices.add(30.0);
		spotPrices.add(30.0);
		
		int testSize = 10;
		
		Double result = Analyzer.simpleMovingAverage(spotPrices, testSize);
		
		assertEquals(15.0, result, DELTA);
	}

}
