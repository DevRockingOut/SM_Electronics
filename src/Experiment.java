import cern.jet.random.engine.RandomSeedGenerator;
import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;

public class Experiment {
	public static void main(String[] args)
	{
		int i, NUMRUNS = 40;
		//double endTime = 30 * 24 * 60; // run for 30 days
		double endTime = 1000.00;
		Seeds[] sds = new Seeds[NUMRUNS];
		ElectronicsProject mnf; //Simulation object
		int numPallets = 40;
		int batchSize = 0;
		
		batchSize = 5; 
		// Lets get a set of uncorrelated seeds, different seeds for each run
		RandomSeedGenerator rsg = new RandomSeedGenerator();
		for (i = 0; i < NUMRUNS; i++) {
			sds[i] = new Seeds(rsg);
		}
		
		mnf = new ElectronicsProject(endTime, numPallets, batchSize, sds[0], true);
		mnf.runSimulation();
	}
}
