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
    static public TriangularVariate UNLOAD_LOAD_TIME;
	static MersenneTwister JamOccur_CELL8;
    Part icPart;
    
	public static boolean precondition() { 
		return UDP.CellReadyForUnloadLoad();
	}

	
	@Override
	public void startingEvent() {
		int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		Pallet pallet = UDP.getPallet(pid);
		
		// first do unload
		if(pallet.part != Part.NO_PART) {
			SP.Leave(pallet.part);  // parts at cell 8 leaves the system
		}
		
		// now do load, why not put part in pallet here instead of terminating event ???
		icPart = SP.RemoveQue(model.qInputConveyor);
		model.rCell[C8].busy = true;
					
		//traceSTART();
	}

	static void initRvp(Seeds sd)
	{
		JamOccur_CELL8 = new MersenneTwister(sd.jamC8);
		UNLOAD_LOAD_TIME = new TriangularVariate(5, 15, 75, new MersenneTwister(sd.ultC8));
	}	
	
	@Override
	public double duration() {
		double UnloadLoadTime = uUnloadLoadTime();
		System.out.println("##########loading time      " + UnloadLoadTime);
		return uUnloadLoadTime();
	}
	
	private static double uUnloadLoadTime()
	{
		
		double nxtTime = 0;
		double clearTime = 0.0;
		
		if (JamOccur_CELL8.nextInt() < 0.01)
		{
			clearTime = UNLOAD_LOAD_TIME.next();
		//	System.out.println("JamOccur_CELL8=    " + JamOccur_CELL8.nextInt());
		}
		
		nxtTime = 25 + clearTime;
		return nxtTime;
	}
	

	@Override
	protected void terminatingEvent() {
		int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		Pallet pallet = UDP.getPallet(pid);
		
		// Any existing part in the pallet is considered removed
		if (pallet != Pallet.NO_PALLET && pallet.part != Part.NO_PART) 
		{
			pallet.part = icPart;
			model.rCell[C8].busy = false;
			pallet.isProcessed = true;
		}

		//traceEND();
	}

	private void traceSTART() {

		PrintWriter writer = null;
		try {
			//writer = new PrintWriter("trace.txt", "UTF-8");
			FileWriter fileWriter = new FileWriter("traceUnloadLoadSTART.txt", true); //Set true for append mode
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
		
		writer.println(" Is cell busy? " + model.rCell[0].busy);
		
		
		writer.println("---------------------------------------------------------------------");
		writer.close();
	}

	private void traceEND() {

		PrintWriter writer = null;
		try {
			//writer = new PrintWriter("trace.txt", "UTF-8");
			FileWriter fileWriter = new FileWriter("traceUnloadLoadEND.txt", true); //Set true for append mode
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
		
		writer.println("  Is cell busy? " + model.rCell[0].busy);
		
		
		writer.println("---------------------------------------------------------------------");
		writer.close();
	}

	
}
