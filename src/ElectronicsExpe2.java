import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;
import cern.jet.random.engine.RandomSeedGenerator;
import cern.jet.random.engine.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import outputAnalysis.ConfidenceInterval;

public class ElectronicsExpe2 {


	    // Some experimental constants
		public static final int NUMRUNS = 200;   // for exploring number of runs
		public static final int [] NUM_WEEKS_ARR = { 2, 4, 8 };
	    public static double HOUR = 60*60;
	    public static final double WEEK = 5.0 * 16.0 * HOUR;  // 7 day week, 24 hours/day
	    public static final double WARM_UP_PERIOD = 1 * WEEK;  // 5 week warm up period for tanker waiting times - see PortV1Warm    
     
	    // Some arrays to collect experimental data
		public static double [][] lostCost1 = new double[NUM_WEEKS_ARR.length][NUMRUNS];

		// For output analysis
		static final double CONF_LEVEL = 0.95;
	    static final int [] NUM_RUNS_ARRAY = {20, 30, 40, 60, 80, 100, 200};
	    
	  /** main method **/
	   public static void main(String[] args)
	   {
			int i, ixNWeeks;
			double startTime=0.0;        // Observation interval starts at t = 0
		    double endTime;  
		    double ObservationTimeInWeek;
		    Seeds [] sds = new Seeds[NUMRUNS];
		  //double[]lostCost = new double [NUMRUNS];
		    
		    // Lets get a set of uncorrelated seeds
		    RandomSeedGenerator rsg = new RandomSeedGenerator();
		    
		    for(i=0 ; i<NUMRUNS ; i++) sds[i] = new Seeds(rsg);
		    System.out.println("Base Case");
		    for(ixNWeeks = 0; ixNWeeks < NUM_WEEKS_ARR.length; ixNWeeks++)
	 	    {
	 	      endTime=NUM_WEEKS_ARR[ixNWeeks] * WEEK;
	 	      ObservationTimeInWeek = NUM_WEEKS_ARR[ixNWeeks]- WARM_UP_PERIOD/WEEK;
	 	       
	 	       
	 	       System.out.println("End Time = "+NUM_WEEKS_ARR[ixNWeeks]+" weeks ("+endTime+" seconds), TimeStamp: "+
	 	                          new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
	 	       
	 	       
	 	      for(i=0 ; i < NUMRUNS ; i++)
		       { 	    	 
	 	    	ElectronicsProject model = new ElectronicsProject(endTime, 0 , 0, sds[i], false, 0);

	 	    	model.setTimef(WARM_UP_PERIOD);
	 	    	model.runSimulation();
	 	    	model.clearLostCost();
	 	    	model.setTimef(endTime);
	 	    	model.runSimulation();
                // Save the results in the arrays
		        lostCost1 [ixNWeeks][i] = model.getLostCost()/ObservationTimeInWeek;
		       }
	 	    }
			   displayTable (lostCost1);
  
	 	   }
	
	   
	   /*------------ Display the confidence intervals for various number of simulations --------------*/
		private static void displayTable(double [][] lostCost1)
		{

			   printALine(1); //-----------------------------------------------------------------------------------------
		       System.out.print("|                                                    Base Case                                                 \n");
			   printALine(1); //-----------------------------------------------------------------------------------------
		       System.out.printf("    tf:  |");
		       for(int ix2 = 0; ix2 < NUM_WEEKS_ARR.length; ix2++) System.out.printf("               %d weeks              |", NUM_WEEKS_ARR[ix2]);
		       System.out.printf("\n");
			   printALine(1); //-----------------------------------------------------------------------------------------
		       System.out.printf("    n    |");
		       for(int ix2 = 0; ix2 < NUM_WEEKS_ARR.length; ix2++) System.out.printf(" yb(n)      s(n)    z(n)  z(n)/yb(n)|");
		       System.out.printf("\n");
			   printALine(1); //-----------------------------------------------------------------------------------------
			   printCFIntervals(lostCost1);
			   }
		
		private static void printALine(int numLines) 
		{ 
			for(int i=0; i<numLines; i++) 
				System.out.printf("+--------+------------------------------------+------------------------------------+------------------------------------+\n"); 
		}
		
		private static void printCFIntervals(double [][] output)
		{
			int numRuns;
			
			for(int ix1 = 0; ix1 <  NUM_RUNS_ARRAY.length; ix1++)
			{
		       numRuns = NUM_RUNS_ARRAY[ix1];
		       System.out.printf("%8d |", numRuns);
		       for(int ix2 = 0; ix2 < NUM_WEEKS_ARR.length; ix2++)
		       {         			
		    	   double [] values = new double[numRuns];
		    	   for(int ix3 = 0 ; ix3 < numRuns; ix3++) values[ix3] = output[ix2][ix3];
		    	   ConfidenceInterval ci = new ConfidenceInterval(values, CONF_LEVEL);
		    	   System.out.printf("%8.3f %8.3f %8.3f %8.4f |",
		    			               ci.getPointEstimate(), ci.getStdDev(), ci.getZeta(),
		    			               ci.getZeta()/ci.getPointEstimate());
		       }
		       System.out.printf("\n");
			}
			printALine(1);
		}
		
	}
