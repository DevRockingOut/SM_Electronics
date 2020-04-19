package electronicsProject;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ConditionalActivity;

class UnLoadLoad extends ConditionalActivity
{
	static ElectronicsProject model;
    static TriangularVariate TIME_RESPOND_TO_JAM;
	static MersenneTwister JamOccur_CELL8;
    Part icPart;
    
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
	}

	static void initRvp(Seeds sd)
	{
		JamOccur_CELL8 = new MersenneTwister(sd.jamC8);
		TIME_RESPOND_TO_JAM = new TriangularVariate(5, 15, 75, new MersenneTwister(sd.ultC8));
	}	
	
	@Override
	public double duration() {
		return uUnloadLoadTime();
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

	private void trace(String s) {

		PrintWriter writer = null;
		try {
			FileWriter fileWriter = new FileWriter("traceUnloadLoad.txt", true); //Set true for append mode
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
		
		writer.println(s);
		writer.close();
	}

	
}
