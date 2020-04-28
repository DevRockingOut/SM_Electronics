package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ConditionalActivity;

class UnloadLoad extends ConditionalActivity
{
	static ElectronicsProject model; // For referencing the model
    static TriangularVariate TIME_RESPOND_TO_JAM;
	static MersenneTwister JamOccur_CELL8;
    Part icPart; // instance of Part involved in the Activity
    
	public static boolean precondition() {
		return CellReadyForUnloadLoad();
	}

	@Override
	public void startingEvent() {
		int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		Pallet pallet =  model.rcPallet[pid];
		
		// Remove part from the input conveyor
		icPart = model.qInputConveyor.spRemoveQue();
		
		// Load a part to a pallet
		pallet.part = icPart;
		
		// Update work cell 8 busy status
		model.rCell[C8].busy = true;
		
	/*	String s = "--------------- Unload/Load (start) ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Loaded Part " + icPart.uType.toString() + "\n";
		s += "Pallet " + pallet.id + "  pid: " + pid + "  part: " + pallet.part.uType.toString() + "\n";
		s += "Cell [" + Cell.CellID.C8.toString() + "]  busy: " + model.rCell[C8].busy + "\n";
		
		Trace.write(s, "traceUnloadLoad.txt", "Unloadload"); */
	}

	// Initialise the RVP
	static void initRvp(Seeds sd)
	{
		JamOccur_CELL8 = new MersenneTwister(sd.jamC8);
		TIME_RESPOND_TO_JAM = new TriangularVariate(5, 15, 75, new MersenneTwister(sd.ultC8));
	}	
	
	@Override
	public double duration() {
		// determine the unload/load duration
		return uUnloadLoadTime();
	}
	
	// RVP
	// Provides the time required to load and unload parts in work cell 8
	private static double uUnloadLoadTime()
	{	
		double nxtTime = 0;
		double ResponseTime = 0.0;
		
		if (JamOccur_CELL8.nextInt() < 0.01) 
		{
			ResponseTime = TIME_RESPOND_TO_JAM.next();
		}
		nxtTime = Constants.UNLOAD_LOAD_TIME + ResponseTime;
	//	String s = "Loading Time= " + nxtTime ;
	//	Trace.write(s, "traceUnloadLoad.txt", "Unloadload");
		return nxtTime;
	}
	

	@Override
	protected void terminatingEvent() {
		// Unload/Load Activity Terminating Event SCS 
		int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		if(pid != Pallet.NO_PALLET_ID) {
			Pallet pallet = model.rcPallet[pid];
			// Update pallet status
			pallet.isProcessed = true;
		}
		
		// Update work cell 8 status
		model.rCell[C8].busy = false;
		
		
		
	/*	String s = "--------------- Unload/Load (end) ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Pallet " + pallet.id + "  pid: " + pid + "  part: " + pallet.part.uType.toString() + "\n";
		s += "Cell [" + Cell.CellID.C8.toString() + "]  busy: " + model.rCell[C8].busy + "\n";
		
		Trace.write(s, "traceUnloadLoad.txt", this.getClass().getName()); */
	}
	
	
	// UDP
	// Returns a boolean describing whether work cell 8 is ready for the unload/load process or not
	static boolean CellReadyForUnloadLoad() {
    	int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		Pallet pallet = Pallet.NO_PALLET;
		if(pid != Pallet.NO_PALLET_ID) {
			pallet = model.rcPallet[pid];
		}
		
		if ( pallet != Pallet.NO_PALLET 
			 && model.qInputConveyor.n != 0 
		   	 && model.rCell[C8].busy == false 
             && pallet.isProcessed == false) {
			return true;
		}

		return false;
	}
	
}
