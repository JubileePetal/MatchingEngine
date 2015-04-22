package controller;
import communications.Greeter;


public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		//Greeter greeter = new Greeter();
		//(new Thread(greeter)).start();
		Initializer initializer = new Initializer();
		initializer.setUpArchive();
		initializer.setUpLibrary();
		initializer.setupGreeter();
		initializer.startGreeter();
	
		

	}

}
