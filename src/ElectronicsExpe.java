import cern.jet.random.engine.RandomSeedGenerator;
import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;
import electronicsProject.Trace;

public class ElectronicsExpe {
	
	public static void main(String[] args)
	{
		int NUMRUNS = 4;
		double endTime = 1000.00;//8000.00;//5 * 16 * 60 * 60; // run for 5 days, 16h per day, time unit is seconds
		Seeds[] sds = new Seeds[NUMRUNS];
		ElectronicsProject mnf; //Simulation object
		int numPallets = 40;
		int batchSize = 0;
		  
		
		// get a set of uncorrelated seeds, different seeds for each run
		RandomSeedGenerator rsg = new RandomSeedGenerator();
		String s = "";
		
		 for(int i=0 ; i < NUMRUNS ; i++) {
			sds[i] = new Seeds(rsg);	
				
			if(i == 0) {
				s = "Base Case (no additional pallets and no new buffer conveyors)";
				batchSize = 0; 
				numPallets = 40; 
			}else if(i == 1) {
				s = "Case 2 (10 additional pallets and no new buffer conveyors)";
				batchSize = 0; 
				numPallets = 50; 
			}else if(i == 2) {
				s = "Case 3 (no additional pallets and three buffer conveyors)";
				batchSize = 3; 
				numPallets = 40; 
			}else {
				s = "Case 4 (7 additional pallets and three buffer conveyors)";
				batchSize = 2; 
				numPallets = 47; 
			}
			
			Trace.write(s, "log.txt", "Experiment");
			
			mnf = new ElectronicsProject(endTime, numPallets, batchSize, sds[0], true);
			mnf.runSimulation();
		 }
		 
	}
	
}