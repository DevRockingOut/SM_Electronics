import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;

public class Experiment {
	public static void main(String[] args)
	{
		int i, NUMRUNS = 40;
		double endTime = 30 * 24 * 60; // run for 30 days
		Seeds[] sds = new Seeds[NUMRUNS];
		ElectronicsProject mnf; //Simulation object
		int numPallets = 40;
		int batchSize = 0;
		
		batchSize = 5; 
		
		mnf = new ElectronicsProject(endTime, numPallets, batchSize, sds[0], false);
		mnf.runSimulation();
	}
}
