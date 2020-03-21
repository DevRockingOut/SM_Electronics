import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;

public class Experiment {
	public static void main(String[] args)
	{
		int i, NUMRUNS = 40;
		double endTime = 30 * 24 * 60; // run for 30 days
		Seeds[] sds = new Seeds[NUMRUNS];
		ElectronicsProject mnf; //Simulation object
		int lc2 = 3;
		int lc3 = 3;
		
		mnf = new ElectronicsProject(endTime, lc2, lc3, sds[0], false);
		System.out.print(2);
	}
}
