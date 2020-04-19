package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ConditionalActivity;

class UnLoadLoad extends ConditionalActivity
{
	static ElectronicsProject model;
    static TriangularVariate TIME_RESPOND_TO_JAM;
	static MersenneTwister JamOccur_CELL8;
    Part icPart;
    double tmpUnloadLoadTime;
    
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
		model.rCell[C8].busy = true;
		
		System.out.println("--------------- Unload/Load (start) ---------------");
		System.out.println("Clock: " + model.getClock());
		
		String s = "--------------- Unload/Load (start) ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Loaded Part " + icPart.uType.toString() + "\n";
		s += "Pallet " + pallet.id + "  pid: " + pid + "  part: " + pallet.part.uType.toString() + "\n";
		s += "Cell [" + Cell.CellID.C8.toString() + "]  busy: " + model.rCell[C8].busy + "\n";
		
		Trace.write(s, "traceUnloadLoad.txt", this.getClass().getName());
	}

	static void initRvp(Seeds sd)
	{
		JamOccur_CELL8 = new MersenneTwister(sd.jamC8);
		TIME_RESPOND_TO_JAM = new TriangularVariate(5, 15, 75, new MersenneTwister(sd.ultC8));
	}	
	
	@Override
	public double duration() {
		tmpUnloadLoadTime = uUnloadLoadTime();
		System.out.println("Unload/load time: " + tmpUnloadLoadTime);
		return tmpUnloadLoadTime;
	}
	
	private static double uUnloadLoadTime()
	{	
		double nxtTime = 0;
		double ResponseTime = 0.0;
		
		if (JamOccur_CELL8.nextInt() < 0.01) 
		{
			ResponseTime = TIME_RESPOND_TO_JAM.next();
		}
		nxtTime = Constants.UNLOAD_LOAD_TIME + ResponseTime;
		return nxtTime;
	}
	

	@Override
	protected void terminatingEvent() {
		int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		Pallet pallet = model.rcPallet[pid];
		
		model.rCell[C8].busy = false;
		pallet.isProcessed = true;
		
		String s = "--------------- Unload/Load (end) ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Pallet " + pallet.id + "  pid: " + pid + "  part: " + pallet.part.uType.toString() + "\n";
		s += "Cell [" + Cell.CellID.C8.toString() + "]  busy: " + model.rCell[C8].busy + "\n";
		
		Trace.write(s, "traceUnloadLoad.txt", this.getClass().getName());
	}
	
	static boolean CellReadyForUnloadLoad() {
    	int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		Pallet pallet = Pallet.NO_PALLET;
		if(pid != Pallet.NO_PALLET_ID) {
			pallet = model.rcPallet[pid];
		}
		
		// A pallet is available at work cell 8
		if ( pallet != Pallet.NO_PALLET 
		     // A part is available in the input conveyor
			 && model.qInputConveyor.n != 0 
		     // Work cell 8 not busy
		   	 && model.rCell[C8].busy == false 
		     // Processing not done on pallet
             && pallet.isProcessed == false) {
			//System.out.println("Cell Ready To Unload/Load: " + true);
			return true;
		}

		return false;
	}
	
}
