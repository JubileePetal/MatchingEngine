package analytics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;

public final class Analyzer {
	
	public static final Double simpleVolatility(LinkedList<Double> spotPrices, int testSize) {
		
		int size = spotPrices.size();
		
		int sampleSize = size <= testSize ? size -1 : testSize;

		ArrayList<Double> returns = new ArrayList<Double>();
		
		for(int i = 0; i < sampleSize; i++) {
			Double current = spotPrices.pollLast();
			Double previous = spotPrices.peekLast();
			//Double division = current / previous;
			//Double thisReturn = Math.log(division);
			Double thisReturn = (current - previous) / previous;
			returns.add(thisReturn);
			//System.out.println("first " + current + " Second: " + previous + " return: " + thisReturn + " division: " + division + " power: " + (thisReturn*thisReturn));
		}

		Double sumOfSquared = 0.0;
		for(Double aReturn : returns) {
			sumOfSquared += Math.pow(aReturn, 2);
		}
		//System.out.println("sum: " + sumOfSquared);
		Double simpleVariance = sampleSize > 0 ? sumOfSquared / sampleSize : 0.0;
		
		return Math.sqrt(simpleVariance);	
	}
	
	public static final Double simpleMovingAverage(LinkedList<Double> spotPrices, int testSize) {
		
		int size = spotPrices.size();
		
		int sampleSize = size < testSize ? size : testSize;

		Double result = 0.0;
		
		for(int i = 0; i < sampleSize; i++) {
			result += spotPrices.poll();
		}
		
		result = sampleSize > 0 ? result / sampleSize : result;
		
		return result;	
	}

	public static Double EWMA(Double prevVol, Double prevRate, double landa) {
		
		return Math.sqrt((landa * Math.pow(prevVol, 2)) + ((1 - landa) * Math.pow(prevRate, 2)));
	}
	
	public static Double callOptionPrice(Double spotPrice, Double volatility, Double riskFreeRate) {
		
		return null;
	}

}
