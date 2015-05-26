package tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import models.Option;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import analytics.Analyzer;

public class AnalyzerTest {
	
	// 9 decimals, to account for lack of calculator precision
	private static final double DELTA = 0.000000001;
	
	@Test
	public void testCNDF() {
		assertEquals(0.779142452, Analyzer.N(0.7693), DELTA);
	}
	
	@Test
	public void testd1() {
		assertEquals(0.7692626281, Analyzer.d1(42, 40, 0.1, 0.2, 0.5), DELTA);
	}
	
	@Test
	public void testd2() {
		assertEquals(0.6278412719, Analyzer.d2(0.7692626281, 0.2, 0.5), DELTA);
	}
	
	@Test
	public void testGamma() {
		Double S = 49d;
		Double K = 50d;
		Double r = 0.05;
		Double sigma = 0.2;
		Double T = 0.3846;
		System.out.println(Analyzer.gamma(S, K, r, sigma, T));
		assertEquals(0.0655453772, Analyzer.gamma(S, K, r, sigma, T), DELTA);
	}
	
	@Test
	public void testNPrime() {
		Double d1 = 0.054173753;
		assertEquals(0.3983573027, Analyzer.NPrime(d1), DELTA);
	}
	
	@Test
	public void testPutDelta() {
		Double S = 49d;
		Double K = 50d;
		Double r = 0.05;
		Double sigma = 0.2;
		Double T = 0.3846;
		assertEquals(-0.478398296, Analyzer.putDelta(S, K, r, sigma, T), DELTA);
	}
	
	@Test
	public void testCallDelta() {
		Double S = 49d;
		Double K = 50d;
		Double r = 0.05;
		Double sigma = 0.2;
		Double T = 0.3846;
		assertEquals(0.521601704, Analyzer.callDelta(S, K, r, sigma, T), DELTA);
	}
	
	@Test
	public void testPutOptionPrice() {
		Double C = 4.759422997;
		Double S = 42d;
		Double K = 40d;
		Double r = 0.1;
		Double T = 0.5;
		assertEquals(0.808599977, Analyzer.putOptionPrice(C, S, K, r, T), DELTA);
	}
	
	@Test
	public void testCallOptionPrice() {
		Option option = new Option();
		option.setStrikePrice(40);
		option.setTimeToMaturity(0.5);
		Double price = Analyzer.callOptionPrice(42.0, 0.2, 0.1, option);
		assertEquals(4.759422997, price, DELTA);
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
