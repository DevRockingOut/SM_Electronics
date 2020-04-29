package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ConditionalActivity;

class UnloadLoad extends ConditionalActivity
{
	static ElectronicsProject model; // For referencing the model
    Part icPart; // instance of Part involved in the Activity
    
	public static boolean precondition() {
		return udpCellReadyForUnloadLoad();
	}
	
	@Override
	public void startingEvent() {
		int C8 = Cell.CellID.C8.getInt();
		int LAST_CONV_POS = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[LAST_CONV_POS];
		
		// Remove part from the input conveyor
		icPart = model.qInputConveyor.spRemoveQue();
		
		// Load a part to a pallet
		model.rcPallet[pid].part = icPart;
		
		// Update work cell 8 busy status
		model.rCell[C8].busy = true;
		
		String s = "--------------- Unload/Load (start) ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Loaded Part " + icPart.uType.toString() + "\n";
		s += "Pallet " + "pid: " + pid + "  part: " + model.rcPallet[pid].part.uType.toString() + "\n";
		s += "Cell [" + Cell.CellID.C8.toString() + "]  busy: " + model.rCell[C8].busy + "\n";
		
		Trace.write(s, "traceUnloadLoad.txt", "Unloadload"); 
	}
	
	@Override
	public double duration() {
		// determine the unload/load duration
		return uUnloadLoadTime();
	}

	@Override
	protected void terminatingEvent() {
		// Unload/Load Activity Terminating Event SCS 
		int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		if(pid != Pallet.NO_PALLET_ID) {
			// Update pallet status
			model.rcPallet[pid].isProcessed = true;
		}
		
		// Update work cell 8 status
		model.rCell[C8].busy = false;
		
		
		String s = "--------------- Unload/Load (end) ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Pallet " + "pid: " + pid + "  part: " + model.rcPallet[pid].part.uType.toString() + "\n";
		s += "Cell [" + Cell.CellID.C8.toString() + "]  busy: " + model.rCell[C8].busy + "\n";
		
		Trace.write(s, "traceUnloadLoad.txt", this.getClass().getName()); 
	}
	
	static TriangularVariate time_respond_to_jam;
	static MersenneTwister jamoccur_cell8;
	
	// Initialise the RVP
	static void initRvp(Seeds sd)
	{
		jamoccur_cell8 = new MersenneTwister(sd.jamC8);
		time_respond_to_jam = new TriangularVariate(5, 15, 75, new MersenneTwister(sd.ultC8));
	}	
	
	// UDP
	// Returns a boolean describing whether work cell 8 is ready for the unload/load process or not
	static boolean udpCellReadyForUnloadLoad() {
    	int C8 = Cell.CellID.C8.getInt();
		int LAST_CONV_POS = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[LAST_CONV_POS];
		
		if ( pid != Pallet.NO_PALLET_ID
			 && model.rcPallet[pid] != Pallet.NO_PALLET 
			 && model.qInputConveyor.n != 0 
		   	 && model.rCell[C8].busy == false 
             && model.rcPallet[pid].isProcessed == false) {
			return true;
		}

		return false;
	}
	
	// RVP
	// Provides the time required to load and unload parts in work cell 8
	private static double uUnloadLoadTime()
	{	
		double nxtTime = 0;
		double ResponseTime = 0.0;
		
		if (jamoccur_cell8.nextDouble() < 0.01) 
		{
			ResponseTime = time_respond_to_jam.next();
		}
		nxtTime = Constants.UNLOAD_LOAD_TIME + ResponseTime;
	//	String s = "Loading Time= " + nxtTime ;
	//	Trace.write(s, "traceUnloadLoad.txt", "Unloadload");
		return nxtTime;
	}
	
}
