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
    static public TriangularVariate PROC_TIME_C2_A;
    static public TriangularVariate PROC_TIME_C2_B;
    static public TriangularVariate PROC_TIME_C2_C;
    static public TriangularVariate PROC_TIME_C7_A;
    static public TriangularVariate PROC_TIME_C7_B;
    static public TriangularVariate PROC_TIME_C7_C;
    

	public static boolean precondition()
	{
		boolean retVal = false;
		if(CellReadyForProcessing() != Constants.NONE)
			retVal = true;
	//	System.out.println("Is cell ready for processing?  " + retVal);
		return(retVal);
	}
	
	  
	@Override
	public void startingEvent() {
		CellID = CellReadyForProcessing(); // identify the cell that processes the part
	//	System.out.println(CellID + "Is ready for processing  ");

		pid = model.rqPowerAndFreeConveyor[CellID].position[model.rqPowerAndFreeConveyor[CellID].position.length -1];	
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
        System.out.println("ended");

        model.rCell[CellID].busy = false; 
        if(pid != Pallet.NO_PALLET_ID) {
        	Pallet pallet = UDP.getPallet(pid);
        	pallet.isProcessed = true;
        }
        
        model.rCell[CellID].previousPartType = uType;
        model.rCell[2].previousPartType = null; 
        model.rCell[7].previousPartType = null;
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

	static public double uServiceTime(int cellID, Part.PartType uType ) {
		int [][]SETUP_TIME = {{0, 25, 0, 52, 35, 29, 11, 0},
				{0, 20, 0, 21, 22, 14, 19, 0},
	      		{0, 17, 0, 34, 24, 37, 17, 0}};
	    double [][]PROC_TIME = {{0, 37, PROC_TIME_C2_A.next(), 39, 41, 33, 31, PROC_TIME_C7_A.next()}, 
	      		                {0, 46, PROC_TIME_C2_B.next(), 27, 38, 41, 24, PROC_TIME_C7_B.next()},
	                            {0, 39, PROC_TIME_C2_C.next(), 23, 47, 35, 51, PROC_TIME_C7_C.next()}};
	        
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
		  
		  if (uType!= model.rCell[cellID].previousPartType)
			  serviceTime =  PROC_TIME[partType][cellID] + SETUP_TIME[partType][cellID];
		  
		 System.out.println("Service time for cell: " + cellID + "=  " + serviceTime );
		  return serviceTime;
		 }
	
	
	
	static protected int CellReadyForProcessing() {
		CellID[] cID = Cell.CellID.values();
		// Check all cells from cell 1 to 7
		for(int i = 1; i < cID.length ; i++) {
			int cid = cID[i].getInt();
			System.out.println("Cell ID is   " + cid);
			int pidHead = model.rqPowerAndFreeConveyor[cid].position.length - 1; 

	//	Pallet palletHead = UDP.getPallet(pidHead);
		//	System.out.println("Pallet position is     " + palletHead.NO_PALLET);

	
				if (model.rqPowerAndFreeConveyor[cid].position[pidHead] != model.rqPowerAndFreeConveyor[cid].NO_PALLET
					&& model.crPallet[pidHead] != Pallet.NO_PALLET 
						&&
						// Work cell is not busy
						model.rCell[cid].busy == false
						&&
						//A pallet with a part exists on the conveyor at the work cell
						pidHead != Pallet.NO_PALLET_ID 
				        &&  
				         // Processing on the part is not complete
				         model.crPallet[pidHead].isProcessed == false  
						)
				
					     {CellID = cid;

					//	System.out.println("Is cell ready for processing?  " + model.rCell[cid].busy);
						//System.out.println("Is there a pallet in the cell?  " + Pallet.NO_PALLET );
						System.out.println("cell:   " + CellID + "   is ready for processing!");
					     }
					
		}
		 System.out.println(CellID );
		  return CellID;		
				      
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