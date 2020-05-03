import cern.jet.random.engine.RandomSeedGenerator;
import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;
import electronicsProject.Trace;

public class ElectronicsExpe {
	
	public static void main(String[] args)
	{
		int NUMRUNS = 4;
		double endTime = 8000.00;
		Seeds[] sds = new Seeds[NUMRUNS];
		ElectronicsProject mnf; //Simulation object
		int numPallets = 40;
		int batchSize = 0;
		  
		
		RandomSeedGenerator rsg = new RandomSeedGenerator();
		String s = "";
		
		 for(int i=0 ; i < NUMRUNS ; i++) {
			// get a set of uncorrelated seeds, different seeds for each run
			sds[i] = new Seeds(rsg);	
				
			if(i == 0) {
				// Simulating base case
				s = "Base Case (no additional pallets and no new buffer conveyors)";
				batchSize = 0; 
				numPallets = 40; 
			}else if(i == 1) {
				// Simulating case 2
				s = "Case 2 (10 additional pallets and no new buffer conveyors)";
				batchSize = 0; 
				numPallets = 50; 
			}else if(i == 2) {
				// Simulating case 3
				s = "Case 3 (no additional pallets and three buffer conveyors)";
				batchSize = 3; 
				numPallets = 40; 
			}else {
				// Simulating case 4
				s = "Case 4 (7 additional pallets and three buffer conveyors)";
				batchSize = 2; 
				numPallets = 47; 
			}
			
			System.out.println(s);
			Trace.write(s, "log.txt", "Experiment");

			mnf = new ElectronicsProject(endTime, numPallets, batchSize, sds[i], true,0);
			mnf.runSimulation();
		 }
		 
	}
	
}