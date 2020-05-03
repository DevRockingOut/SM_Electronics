package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import electronicsProject.Seeds;
import dataModelling.TriangularVariate;
import electronicsProject.Cell.CellID;
import electronicsProject.Part.PartType;
import simulationModelling.ConditionalActivity;

class Processing extends ConditionalActivity {
	
	static ElectronicsProject model; // For referencing the model
	int cellID; // Identifier for Cell and PowerAndFreeConveyors
	PartType uType;
    int pid;
    static int NONE = -1;
    static final int[][] SETUP_TIME = {{0, 37, 0, 39, 41, 33, 31, 0}, 
							           {0, 46, 0, 27, 38, 41, 24, 0},
							           {0, 39, 0, 23, 47, 35, 51, 0}};

	public static boolean precondition()
	{
		int[] result = udpCellReadyForProcessing();
		int cellID = result[0];
		
		return cellID != NONE;
	}
	
	  
	@Override
	public void startingEvent() {
		
		int[] result = udpCellReadyForProcessing();
		
		cellID = result[0];
		pid = result[1];
		
		if (result[2] == 0) {
			uType = Part.PartType.A;
		}else if(result[2] == 1) {
			uType = Part.PartType.B;
		}else {
			uType = Part.PartType.C;
		}  
		
        // Update Cell busy status
        model.rCell[cellID].busy = true;
        
        if(model.logFlag) {
	        String s = "--- Processing Starts --- \n";
	        s += "Clock: " + model.getClock() + "\n";
	        s += "C" + Cell.CellID.values()[cellID].getInt();
	        s += "; busy: " + model.rCell[cellID].busy;
	        s += "; isProcessed: " + model.rcPallet[pid].isProcessed;
	        s += "; previousPartType: " + uType + "\n\n";
			
	        Trace.write(s, "traceProcessing.txt", this.getClass().getName());
        }
	}

	@Override
	public double duration() {
		// determine the processing duration
		return uServiceTime(pid, cellID);
	}


	@Override
	public void terminatingEvent() {
		// Processing Activity Terminating Event SCS 
		
		// Update Cell busy status
        model.rCell[cellID].busy = false; 
        
        // Update Pallet isProcessed status
        model.rcPallet[pid].isProcessed = true;
   
		if(model.logFlag) {
	        String s = "--- Processing Ends --- \n";
	        s += "Clock: " + model.getClock() + "\n";
	        s += "C" + Cell.CellID.values()[cellID].getInt();
	        s += "; busy: " + model.rCell[cellID].busy;
	        s += "; isProcessed: " + model.rcPallet[pid].isProcessed;
	        s += "; new previousPartType: " + uType;
	        s += "; old previousPartType: " + model.rCell[cellID].previousPartType + "\n\n";
	        
	        Trace.write(s, "traceProcessing.txt", this.getClass().getName());
		}
        
        // Update Cell previousPartType status
        model.rCell[cellID].previousPartType = uType;
	}

	static TriangularVariate PROC_TIME_C2_A;
    static TriangularVariate PROC_TIME_C2_B;
    static TriangularVariate PROC_TIME_C2_C;
    static TriangularVariate PROC_TIME_C7_A;
    static TriangularVariate PROC_TIME_C7_B;
    static TriangularVariate PROC_TIME_C7_C;
	
	// Initialise the RVP
	static void initRvp(Seeds sd)
	{
		PROC_TIME_C2_A = new TriangularVariate(36,45,52, new MersenneTwister(sd.ptC2A));
		PROC_TIME_C2_B = new TriangularVariate(21,32,39, new MersenneTwister(sd.ptC2B));
		PROC_TIME_C2_C = new TriangularVariate(32,36,42, new MersenneTwister(sd.ptC2C));
		PROC_TIME_C7_A = new TriangularVariate(27,35,41, new MersenneTwister(sd.ptC7A));
		PROC_TIME_C7_B = new TriangularVariate(31,39,43, new MersenneTwister(sd.ptC7B));
		PROC_TIME_C7_C = new TriangularVariate(22,27,38, new MersenneTwister(sd.ptC7C));
	}	

	// UDP
	// returns the operation time at the work cell
	public double uServiceTime(int pid, int cellID) {
		
		double[][] proc_time = {{0, 25, PROC_TIME_C2_A.next(), 52, 35, 29, 11, PROC_TIME_C2_A.next()},
							    {0, 20, PROC_TIME_C2_B.next(), 21, 22, 14, 19, PROC_TIME_C2_B.next()},
					      	    {0, 17, PROC_TIME_C2_C.next(), 34, 24, 37, 17, PROC_TIME_C2_C.next()}};
	    
	    uType = model.rcPallet[pid].part.uType;
	    
		double serviceTime = 0.0; // an arbitrary default value
		int partType;
		
		if (uType == Part.PartType.A) {
			partType = 0;
		}else if(uType == Part.PartType.B) {
			partType = 1;
		}else {
			partType = 2;
		}  
		  
		// calculate service time (processing time)
		serviceTime = proc_time[partType][cellID];
		  
		if (model.rCell[cellID].previousPartType != Part.NO_PART_TYPE && uType != model.rCell[cellID].previousPartType) {
			serviceTime =  proc_time[partType][cellID] + SETUP_TIME[partType][cellID]; // calculate service time (processing time + setup time)
		}
		  
		return serviceTime;
	}
	
	
	// UDP
	// Returns a vector containing the cell id (C1 to C7) that is ready for processing
	static protected int[] udpCellReadyForProcessing() {
		int[] output = {NONE, NONE, NONE};
		
		CellID[] cID = Cell.CellID.values();
		
		// Check all cells from cell 1 to 7
		for(int i = 1; i < cID.length ; i++) {
			int cid = cID[i].getInt();
			int last = model.rqPowerAndFreeConveyor[cid].position.length - 1; 
			int pid = model.rqPowerAndFreeConveyor[cid].position[last];
			
			
			if (pid != Pallet.NO_PALLET_ID  &&
				model.rcPallet[pid].part != Part.NO_PART &&
				model.rCell[cid].busy == false  &&
		        model.rcPallet[pid].isProcessed == false)
			{
				output[0] = cid;
				output[1] = pid;
				
				PartType partType = model.rcPallet[pid].part.uType;
				if (partType == Part.PartType.A) {
					output[2] = 0;
				}else if(partType == Part.PartType.B) {
					output[2] = 1;
				}else {
					output[2] = 2;
				}  

				return output;
			}
					
		}
		
		return output;			      
	}
		
}