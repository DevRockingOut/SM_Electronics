package electronicsProject;


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
    int pid;
	static final int[][] PROC_TIME = new int[][] {{25, 20, 17}, {0, 0, 0}, {52, 21, 34}, 
        {35, 22, 24}, {29, 14, 37}, {11, 19, 17}, {0, 0 , 0}};

    static final int[][] SETUP_TIME = new int[][] {{37, 46, 39}, {0, 0, 0}, {39, 27, 23}, 
        {41, 38, 47}, {33, 41, 35}, {31, 24, 51}, {0, 0, 0}};

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
		return(retVal);
	}
	
	@Override
	public void startingEvent() {
		CellID = CellReadyForProcessing(); // identify the cell that processes the part
		
        // Place Identifier in name of behaviour object for logging, used by showSbl()
        this.name = "C" + (CellID + 1);
        
        pid = model.rqPowerAndFreeConveyor[CellID].position[model.rqPowerAndFreeConveyor[CellID].position.length -1];
        		
        // Set machine to busy
        model.rCell[CellID].busy = true;       
	}

	@Override
	public double duration() {
		
		// determine the duration
		return uServiceTime(CellID, pid);
	}


	@Override
	public void terminatingEvent() {

        model.rCell[CellID].busy = false; 
        model.crPallet[pid].isProcessed = true; 	
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

	static public double uServiceTime(int cellID, int pid) {
		
		double serviceTime = 1.0; // an arbitrary default value
		

		
		
		if(cellID == 2) {
			
			if(model.crPallet[pid].part.uType == Part.PartType.A) {
				serviceTime = PROC_TIME_C2_A.next();
			}else if(model.crPallet[pid].part.uType == Part.PartType.B) {
				serviceTime = PROC_TIME_C2_B.next();
			}else if(model.crPallet[pid].part.uType == Part.PartType.C) {
				serviceTime = PROC_TIME_C2_C.next();			}
			
		else if(cellID == 7) {
			
			if(model.crPallet[pid].part.uType == Part.PartType.A) {
				serviceTime = PROC_TIME[cellID -1][0];
			}else if(model.crPallet[pid].part.uType == Part.PartType.B) {
				serviceTime = PROC_TIME[cellID -1][1];
			}else if(model.crPallet[pid].part.uType == Part.PartType.C) {
				serviceTime = PROC_TIME[cellID -1][2];
		}
		}
			
		else {
		
			if(model.crPallet[pid].part.uType ==  model.rCell[cellID].previousPartType) {
				
				if(model.crPallet[pid].part.uType == Part.PartType.A) {
					serviceTime = PROC_TIME[cellID -1][0];
				}else if(model.crPallet[pid].part.uType == Part.PartType.B) {
					serviceTime = PROC_TIME[cellID -1][1];
				}else if(model.crPallet[pid].part.uType == Part.PartType.C) {
					serviceTime = PROC_TIME[cellID -1][2];
				}
				
			}else {
				
				if(model.crPallet[pid].part.uType == Part.PartType.A) {
					serviceTime = PROC_TIME[cellID -1][0] + SETUP_TIME[cellID -1][0];
				}else if(model.crPallet[pid].part.uType == Part.PartType.B) {
					serviceTime = PROC_TIME[cellID -1][1] + SETUP_TIME[cellID -1][1];
				}else if(model.crPallet[pid].part.uType == Part.PartType.C) {
				serviceTime = PROC_TIME[cellID -1][2] + SETUP_TIME[cellID -1][2];			
				}
				
			}
		
		
		}
		}
		
		return (serviceTime);

	}

	
	static protected int CellReadyForProcessing()
	{

		CellID[] cID = Cell.CellID.values();
		
		// Check all cells
		for(int i = 1; i < cID.length; i++) {
			int id = cID[i].getInt();
			int pidHead = model.rqPowerAndFreeConveyor[id].position[0];
			
			if (
				//Work cell is not busy
				model.rCell[id].busy == false 
				&& model.crPallet[pidHead].isProcessed == false
				
				// A pallet with a part exists on the conveyor at the work cell
				&& model.rqPowerAndFreeConveyor[id].position[0] == Pallet.NO_PALLET_ID 
				&& model.crPallet[pidHead].part == Part.NO_PART
				
				//There exists a part in the conveyor feeding the work cell
				//&& model.rqPowerAndFreeConveyor[id].position[model.rqPowerAndFreeConveyor[id].position.length -1].part != 0
				//[WTF_QUESTION] don't think we need that anymore I mean its the same as the previous one!
				
			    && model.rCell[id].previousPartType == Part.NO_PART_TYPE)

				CellID = id;

		}
		return(CellID);


	}
	
	
}