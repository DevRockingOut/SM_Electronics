import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;

public class Experimentation {
	
    static final int NUMRUNS = 20;
    final static int numPallets = 72;
	final static int [] BatchSize = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    final static double HOUR = 60 * 60;
    final static double WEEK = 5 * 16 * HOUR;  // 5 day week, 16 hours/day
    public static final int NUM_WEEKS = 4;
    final static double WARM_UP_PERIOD = 1 * WEEK; 
    static double[]lostCost_step2 = new double[numPallets + 1];
    static double [][] lostCost_step3AND4 = new double[numPallets + 1][BatchSize.length];
 
	public static void main(String[] args) {
		
		double endTime = NUM_WEEKS * WEEK;
		double ObservationTimeInWeek = NUM_WEEKS- (WARM_UP_PERIOD/WEEK);
	    Seeds [] sds = new Seeds[numPallets*BatchSize.length];
	    RandomSeedGenerator rsg = new RandomSeedGenerator();
	     
	    for(int i = 0; i < numPallets*BatchSize.length; i++) { sds[i] = new Seeds(rsg);}
		for(int addPallet = 40; addPallet <= numPallets; addPallet++)
		{
			ElectronicsProject model = new ElectronicsProject(endTime, addPallet, 0, sds[addPallet], false, 0);
		    model.setTimef(WARM_UP_PERIOD);
		    model.runSimulation();
		    model.clearLostCost(); 
		    model.setTimef(endTime);
		    model.runSimulation();             
		    lostCost_step2[addPallet] = model.getLostCost()/ObservationTimeInWeek;
		    model.clearLostCost(); 
		   // System.out.println("ADD_PALLET_NUMBER: " + addPallet);
		 //   System.out.println("NO_BATCHLEAST");
		   //     System.out.println(lostCost_step2[addPallet]);
		
		    
		    for(int batch = 0; batch < BatchSize.length; batch++)
		    {    
		    	int k = batch+addPallet;
		    	if(batch > 0) {
		    		k = batch+addPallet*batch;
		    	}
				 ElectronicsProject model1 = new ElectronicsProject(endTime, addPallet, BatchSize[batch], sds[k], false, 0);
				 model1.setTimef(WARM_UP_PERIOD);
				 model1.runSimulation();
			     model1.clearLostCost(); 
			     model1.setTimef(endTime);
			     model1.runSimulation();             
			     lostCost_step3AND4[addPallet][batch] = model1.getLostCost()/ObservationTimeInWeek;
			     model.clearLostCost(); 
		 //    System.out.println("ADD_PALLET_NUMBER: " + addPallet);
		   //  System.out.println("batchLeast: " + batch);
		   //      System.out.println(lostCost_step3AND4[addPallet][batch]);
		    }	
		}
		displayTable(lostCost_step3AND4);     
		}
	
	 private static void displayTable(double [][]lostCost)
	 {
	  
	  System.out.print("Batch Size        ");
	  for (int i = 0; i < BatchSize.length; i++)
	  {
		  System.out.print("|     " + BatchSize[i] + "    ");
	  }
	  System.out.print("\n");
	  for (int i = 40; i < numPallets + 1; i++)
	  {
		  if(i < 10) {
			  System.out.print("Pallet Number: " + i + "  |");
		  }else {
			  System.out.print("Pallet Number: " + i +" |");
	  }
	  for (int q = 0; q < BatchSize.length; q++) 
	  {
		  System.out.printf("%8.2f ", lostCost[i][q]);
		  System.out.print(" |");
	  }
	  System.out.printf("%8.2f ",lostCost_step2[i]);
	  System.out.println(" |");
	  }
	  }
	 }
