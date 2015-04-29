package model;

import java.util.ArrayList;
import java.util.TreeSet;

import models.Order;

public class BookStatus {
	
	ArrayList<Level> buyLevels;
	ArrayList<Level> sellLevels;
	String instrumentName;
	
	public BookStatus(String instrumentName) {
		this.instrumentName = instrumentName;
	}
	
	public void generateBuyLevels(TreeSet<Order> buySet) {
		buyLevels = generateLevels(buySet);
	}
	
	public void generateSellLevels(TreeSet<Order> sellSet) {
		sellLevels = generateLevels(sellSet);
	}
	
	public ArrayList<Level> generateLevels(TreeSet<Order> set) {
		
		ArrayList<Level> levels = new ArrayList<Level>();

		while(!set.isEmpty()) {
			
			Order first = set.pollFirst();	
			double price = first.getPrice();
			int quantity = first.getQuantity();
			
			boolean lookingForMore = true;
			
			while(lookingForMore) {
				
				if(!set.isEmpty()) {
					
					first = set.first();
					if(first.getPrice() == price) {
						quantity = quantity + set.pollFirst().getQuantity();
					} else {
						lookingForMore = false;
					}
				} else {
					lookingForMore = false;
				}
			}
			
			Level level = new Level();
			level.setPrice(price);
			level.setQuantity(quantity);
			levels.add(level);
		}
		
		return levels;
	}
	
}
