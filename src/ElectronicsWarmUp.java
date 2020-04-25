import electronicsProject.ElectronicsProject;
import electronicsProject.Seeds;
import cern.jet.random.engine.*;
import outputAnalysis.WelchAverage;

public class ElectronicsWarmUp {
	
	   private static ElectronicsProject model;
	   private static double[][] LostCostOutput;
	   int batchSize = 0;
	   
	  /** main method **/
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	       int NUMRUNS = 10;
	       Seeds [] sds = new Seeds[NUMRUNS];
	       double intervalStart, intervalEnd;     // start and end times of current interval
	       double intervalLength = 60* 60 ;       // 1 week intervals, 7 days
	       int numIntervals = 60;                 // Total 60 hours observation interval
	       LostCostOutput = new double[NUMRUNS][numIntervals];

	       // Lets get a set of uncorrelated seeds
	       RandomSeedGenerator rsg = new RandomSeedGenerator();
	       for(int i=0 ; i<NUMRUNS ; i++)
	           sds[i] = new Seeds(rsg);

	       int k = (int) (intervalLength*numIntervals);
	       // Run for NUMRUNS simulation runs
	    	  System.out.println("Base case: ");
	          for(int i= 0 ; i < NUMRUNS ; i++)
	          {
	             // For computing warm-up, compute average over intervalLength for numIntervals
	             // Setup the simulation object
	        	  model = new ElectronicsProject(0, 0 , 0, sds[i], false,intervalLength*numIntervals);

	             // Loop for the all intervals
	             for( int interval=0 ; interval<numIntervals ; interval++) 
	             {
	            	// Run the simulation for an interval
	            	intervalStart = interval*intervalLength;
	            	intervalEnd = intervalStart+intervalLength;
	            	model.setTimef(intervalEnd);
	            	model.runSimulation();
	                // compute scalar output
	            	LostCostOutput[i][interval]= model.getLostCost();
	                // Reset ESOutput Objects
	            	model.clearLostCost();         	
	             }
	          }
	          
	    	  int [] wSizeLostCost = {0,1,3,5};
	    	   WelchAverage welchLostCost  = new WelchAverage(LostCostOutput, wSizeLostCost);
	    	   System.out.println("Lost Cost");
	    	   printWelchOutputMatrix(welchLostCost.getWelchAvgOutput(), wSizeLostCost, 1);  
	       }
	   

	   private static void  printWelchOutputMatrix(double[][] m, int [] w, double intervalLength)
	   {
		  int ix, jx;
		  // Print out the window Sizes
		  System.out.print("t,");
		  for(ix=0; ix < w.length-1; ix++) System.out.print("w = "+w[ix]+",");
		  System.out.println("w = "+w[ix]); // Last one
		  // Let's output the data
		  for(jx = 0 ; jx < m[0].length ; jx++)  // print rows as columns
		  {
			  System.out.print( ((jx+1)*intervalLength)+", ");
		      for(ix = 0 ; ix < m.length ; ix++) // columns becomes rows
		      {
		    	  if(jx < m[ix].length)  System.out.print(m[ix][jx]); // rows have different lengths, assumes row 0 is longest		         
		    	  if(ix != m.length-1 && jx < m[ix+1].length) System.out.print(", "); // more to come
		    	  else if(jx<m[ix].length) System.out.println();   // Assumes that all rows decrease in length
		      }
		  }
		  }

}
