package controller;
import communications.Greeter;


public class MainServer {

	public MainServer() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
	
		if(args[0] != null){
			
			int nrOfWorkers  = Integer.parseInt(args[0]);
			
			Initializer initializer = new Initializer();
			initializer.establishDependencies();
			initializer.setupWorkPool(nrOfWorkers);
			initializer.startWorkers();
			initializer.startGreeter();

			
		}
		

	
		

	}

}
