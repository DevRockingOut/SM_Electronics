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
	static int CellID; // identifiers for Cell and PowerAndFreeConveyors
	static PartType uType;
    int pid;
    static TriangularVariate PROC_TIME_C2_A;
    static TriangularVariate PROC_TIME_C2_B;
    static TriangularVariate PROC_TIME_C2_C;
    static TriangularVariate PROC_TIME_C7_A;
    static TriangularVariate PROC_TIME_C7_B;
    static TriangularVariate PROC_TIME_C7_C;
    

	public static boolean precondition()
	{
		if(CellReadyForProcessing() != Constants.NONE) {
			return true;
		}
		
		return false;
	}
	
	  
	@Override
	public void startingEvent() {
		int last = model.rqPowerAndFreeConveyor[CellID].position.length -1;
		
		CellID = CellReadyForProcessing(); // identify the cell that processes the part
		pid = model.rqPowerAndFreeConveyor[CellID].position[last];	
	//	System.out.println(pid);
	//	System.out.println(CellID);
        // Set machine to busy
        model.rCell[CellID].busy = true;  
    //    System.out.println("is cell busy?  " + model.rCell[CellID].busy);
	}

	@Override
	public double duration() {
		// determine the duration
		return uServiceTime(CellID, uType);
	}


	@Override
	public void terminatingEvent() {
		Pallet pallet = model.rcPallet[pid];
		
        model.rCell[CellID].busy = false; 
		pallet.isProcessed = true;
        
        model.rCell[CellID].previousPartType = uType;
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

	static public double uServiceTime(int cellID, Part.PartType uType) {
		double[][] PROC_TIME = {{0, 25, PROC_TIME_C2_A.next(), 52, 35, 29, 11, PROC_TIME_C2_A.next()},
							    {0, 20, PROC_TIME_C2_B.next(), 21, 22, 14, 19, PROC_TIME_C2_B.next()},
					      	    {0, 17, PROC_TIME_C2_C.next(), 34, 24, 37, 17, PROC_TIME_C2_C.next()}};
		
	    int[][] SETUP_TIME = {{0, 37, 0, 39, 41, 33, 31, 0}, 
	      		              {0, 46, 0, 27, 38, 41, 24, 0},
	                          {0, 39, 0, 23, 47, 35, 51, 0}};
	        
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
		  
		System.out.println("Service time for cell: " + cellID + "=  " + serviceTime);
		return serviceTime;
	}
	
	
	
	static protected int CellReadyForProcessing() {
		CellID[] cID = Cell.CellID.values();
		// Check all cells from cell 1 to 7
		for(int i = 1; i < cID.length ; i++) {
			int cid = cID[i].getInt();
			System.out.println("Cell ID is   " + cid);
			int pidHead = model.rqPowerAndFreeConveyor[cid].position.length - 1; 

			Pallet palletHead =  model.rcPallet[pidHead];
	
				// A pallet exists on the conveyor at the work cell
			if (palletHead != Pallet.NO_PALLET  &&
				// Work cell is not busy
				model.rCell[cid].busy == false  &&
				// A pallet with a part exists on the conveyor at the work cell
				palletHead.part != Part.NO_PART &&  
		        // Processing on the part is not complete
		        palletHead.isProcessed == false)
			{
				System.out.println("cell:   " + cid + "   is ready for processing!");
				return cid;
				//	System.out.println("Is cell ready for processing?  " + model.rCell[cid].busy);
				//System.out.println("Is there a pallet in the cell?  " + Pallet.NO_PALLET );
		    }
					
		}
	
		return Constants.NONE;			      
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