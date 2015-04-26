package controller;

public class Trade {

	private long tradeID;
	private long buyOrderID;
	private long selOrderID;
	
	private String buyer;
	private String seller;

	private int quantity;
	private double price; 

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Trade() {
		// TODO Auto-generated constructor stub
	}

	public long getTradeID() {
		return tradeID;
	}

	public void setTradeID(long tradeID) {
		this.tradeID = tradeID;
	}

	public long getBuyOrderID() {
		return buyOrderID;
	}

	public void setBuyOrderID(long buyOrderID) {
		this.buyOrderID = buyOrderID;
	}

	public long getSelOrderID() {
		return selOrderID;
	}

	public void setSelOrderID(long selOrderID) {
		this.selOrderID = selOrderID;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	

}
