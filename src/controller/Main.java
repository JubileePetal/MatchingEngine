package controller;
import communications.Greeter;


public class Main {

	public Main() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
	
		if(args[0] != null){
			
			int nrOfWorkers  = Integer.parseInt(args[0]);
			
			Initializer initializer = new Initializer();
			initializer.setUpArchive();
			initializer.setUpLibrary();
			initializer.setupWorkPool(nrOfWorkers);
			initializer.setupGreeter();
			initializer.startWorkers();
			initializer.startGreeter();

			
		}
		

	
		

	}

}
