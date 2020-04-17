package electronicsProject;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import cern.jet.random.engine.MersenneTwister;
import electronicsProject.Seeds;
import dataModelling.TriangularVariate;
import electronicsProject.Constants;
import electronicsProject.Cell.CellID;
import electronicsProject.Part.PartType;
import simulationModelling.ConditionalActivity;

class Processing extends ConditionalActivity {
	
	static ElectronicsProject model;
	int CellID; // identifiers for Cell and PowerAndFreeConveyors
	PartType uType;
    int pid;
    static TriangularVariate PROC_TIME_C2_A;
    static TriangularVariate PROC_TIME_C2_B;
    static TriangularVariate PROC_TIME_C2_C;
    static TriangularVariate PROC_TIME_C7_A;
    static TriangularVariate PROC_TIME_C7_B;
    static TriangularVariate PROC_TIME_C7_C;
    static int NONE = -1;

	public static boolean precondition()
	{
		int[] result = CellReadyForProcessing();
		int CellID = result[0];
		//pid = result[1];

		return CellID != NONE;
	}
	
	  
	@Override
	public void startingEvent() {
		//int last = model.rqPowerAndFreeConveyor[CellID].position.length -1;
		
		//pid = model.rqPowerAndFreeConveyor[CellID].position[last];	
		int[] result = CellReadyForProcessing();
		CellID = result[0];
		pid = result[1];
        // Set machine to busy
        model.rCell[CellID].busy = true;
        
        System.out.println("--- Processing Starts ---");
		System.out.print("C" + Cell.CellID.values()[CellID].getInt());
		System.out.print(" busy: " + model.rCell[CellID].busy);
		System.out.print("; isProcessed: " + model.rcPallet[pid].isProcessed);
		System.out.print("; previousPartType: " + uType);
		System.out.println("");
	}

	@Override
	public double duration() {
		// determine the duration
		return uServiceTime(pid, CellID); //uServiceTime(CellID, uType); // uServiceTime(pid, CellID)
	}


	@Override
	public void terminatingEvent() {
		Pallet pallet = model.rcPallet[pid];
        model.rCell[CellID].busy = false; 
		pallet.isProcessed = true;
        model.rCell[CellID].previousPartType = uType;
        
        System.out.println("");
        System.out.println("--- Processing Ends ---");
		System.out.print("C" + Cell.CellID.values()[CellID].getInt());
		System.out.print("; busy: " + model.rCell[CellID].busy);
		System.out.print("; isProcessed: " + pallet.isProcessed);
		System.out.print("; previousPartType: " + uType);
		System.out.println("");
		System.out.println("");
        //model.rCell[2].previousPartType = Part.NO_PART_TYPE; // no need for this, since we don't use it anyways
        //model.rCell[7].previousPartType = Part.NO_PART_TYPE; // no need for this, since we don't use it anyways
	}

	static void initRvp(Seeds sd)
	{
		PROC_TIME_C2_A = new TriangularVariate(36,45,52, new MersenneTwister(sd.ptC2A));
		PROC_TIME_C2_B = new TriangularVariate(21,32,39, new MersenneTwister(sd.ptC2B));
		PROC_TIME_C2_C = new TriangularVariate(32,36,42, new MersenneTwister(sd.ptC2C));
		PROC_TIME_C7_A = new TriangularVariate(27,35,41, new MersenneTwister(sd.ptC7A));
		PROC_TIME_C7_B = new TriangularVariate(31,39,43, new MersenneTwister(sd.ptC7B));
		PROC_TIME_C7_C = new TriangularVariate(22,27,38, new MersenneTwister(sd.ptC7C));
	}	

	// returns the operation time at the work cell                            uServiceTime(int cellID, Part.PartType uType)
	public double uServiceTime(int pid, int cellID) {
		System.out.println("pid " + pid + "; cellID " + Cell.CellID.values()[cellID].getInt());
		
		double[][] PROC_TIME = {{0, 25, PROC_TIME_C2_A.next(), 52, 35, 29, 11, PROC_TIME_C2_A.next()},
							    {0, 20, PROC_TIME_C2_B.next(), 21, 22, 14, 19, PROC_TIME_C2_B.next()},
					      	    {0, 17, PROC_TIME_C2_C.next(), 34, 24, 37, 17, PROC_TIME_C2_C.next()}};
		
	    int[][] SETUP_TIME = {{0, 37, 0, 39, 41, 33, 31, 0}, 
	      		              {0, 46, 0, 27, 38, 41, 24, 0},
	                          {0, 39, 0, 23, 47, 35, 51, 0}};
	    
	    uType = model.rcPallet[pid].part.uType;
	    System.out.println("Part type " + uType);
	    
	        // [UPDATE_CM] change the name of this: TypeToArrLocation
		double serviceTime = 0.0; // an arbitrary default value
		int partType;
		
		if (uType == Part.PartType.A) {
			partType = 0;
		}else if(uType == Part.PartType.B) {
			partType = 1;
		}else {
			partType = 2;
		}  
		  
		serviceTime = PROC_TIME[partType][cellID];
		  
		if (uType != model.rCell[cellID].previousPartType) {
			serviceTime =  PROC_TIME[partType][cellID] + SETUP_TIME[partType][cellID];
		}
		  
		return serviceTime;
	}
	
	
	static protected int[] CellReadyForProcessing() {
		int[] output = {NONE, NONE};
		
		CellID[] cID = Cell.CellID.values();
		
		// Check all cells from cell 1 to 7
		for(int i = 1; i < cID.length ; i++) {
			int cid = cID[i].getInt();
			int last = model.rqPowerAndFreeConveyor[cid].position.length - 1; 
			int pid = model.rqPowerAndFreeConveyor[cid].position[last];
			
				// A pallet exists on the conveyor at the work cell
			if (pid != Pallet.NO_PALLET_ID  &&
				// Work cell is not busy
				model.rCell[cid].busy == false  &&
		        // Processing on the part is not complete
		        model.rcPallet[pid].isProcessed == false)
			{
				output[0] = cid;
				output[1] = pid;
				
				return output;
			}
					
		}
	
		return output;			      
	}
		
	
	
	
/*	private void trace() {

		PrintWriter writer = null;
		try {
			//writer = new PrintWriter("trace.txt", "UTF-8");
			FileWriter fileWriter = new FileWriter("traceProcessing.txt", true); //Set true for append mode
		    writer = new PrintWriter(fileWriter);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(model.getClock());
		writer.println();
		
		
		Pallet pallet = UDP.getPallet(pid);
		double serviceTime = uServiceTime(CellID, uType);
		if (pallet != Pallet.NO_PALLET ) {
		writer.println("cell " + CellID + "  Is ready for processing or not?  " + model.rCell[CellID].busy // 0 to 7
				+ ",   part on the pallet " + pid + "  is processed or not?  " +  pallet.isProcessed);
		writer.println("serviceTime" + "at " + CellID + "is " + serviceTime);
		}
		
		writer.println("---------------------------------------------------------------------");
		writer.close();
	} */
}