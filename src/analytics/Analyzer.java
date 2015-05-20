package analytics;

import java.util.ArrayList;
import java.util.LinkedList;

import models.Option;

public final class Analyzer {
	
	public static final Double simpleVolatility(LinkedList<Double> spotPrices, int testSize) {
		
		int size = spotPrices.size();
		
		int sampleSize = size <= testSize ? size -1 : testSize;

		ArrayList<Double> returns = new ArrayList<Double>();
		
		for(int i = 0; i < sampleSize; i++) {
			Double current = spotPrices.pollLast();
			Double previous = spotPrices.peekLast();
			Double thisReturn = (current - previous) / previous;
			returns.add(thisReturn);
		}

		Double sumOfSquared = 0.0;
		for(Double aReturn : returns) {
			sumOfSquared += Math.pow(aReturn, 2);
		}
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
	
	public static Double gamma(double S, double K, double r, double sigma, double T) {
		return NPrime(d1(S, K, r, sigma, T)) / (S*sigma*Math.sqrt(T));
	}
	
	public static Double NPrime(Double x) {
		
		return (1/Math.sqrt(2*Math.PI)) * Math.exp(Math.pow(x, 2)/2);
	}
	
	public static Double putDelta(double S, double K, double r, double sigma, double T) {
		return callDelta(S, K, r, sigma, T) - 1;
	}
	
	public static Double callDelta(double S, double K, double r, double sigma, double T) {
		
		return N(d1(S, K, r, sigma, T));
	}

	public static Double EWMA(Double prevVol, Double prevRate, double landa) {
		
		return Math.sqrt((landa * Math.pow(prevVol, 2)) + ((1 - landa) * Math.pow(prevRate, 2)));
	}
	
	public static Double putOptionPrice(Double C, Double S, Double K, Double r, Double T) {
		
		return C - S + (K * Math.exp(-r*T));
	}
	
	public static Double callOptionPrice(Double S, Double sigma, Double r, Option option) {
		
		Double K = option.getStrikePrice();
		Double T = option.getTimeToMaturity();
		
		Double d1 = d1(S, K, r, sigma, T);
		Double d2 = d2(d1, sigma, T);
		
		return S*N(d1) - K*Math.exp(-r*T)*N(d2);
	}
	
	public static double d1(double S, double K, double r, double sigma, double T) {
		
		Double sigmaPow2Div2 = (Math.pow(sigma, 2)/2);
		
		Double nominator = Math.log((S/K)) + ((r + sigmaPow2Div2)*T);
		Double deNominator = sigma * Math.sqrt(T);
		
		return nominator / deNominator;
	}
	
	public static double d2(double d1, double sigma, double T) {
		return d1 - (sigma * Math.sqrt(T));
	}
	
	public static double N(double val) {
		return comultativeNormalDistribution(val);
	}
	
	private static double comultativeNormalDistribution(double x) {
		
	    int neg = (x < 0d) ? 1 : 0;
	    if (neg == 1) { 
	        x *= -1d;
	    }
	    
	    double k = (1d / ( 1d + 0.2316419 * x));
	    double y = (((( 1.330274429 * k - 1.821255978) * k + 1.781477937) *
	                   k - 0.356563782) * k + 0.319381530) * k;
	    y = 1.0 - 0.398942280401 * Math.exp(-0.5 * x * x) * y;

	    return (1d - neg) * y + neg * (1d - y);
	}

}
