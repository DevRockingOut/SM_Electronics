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
    
	public static boolean precondition()
	{			
		boolean retVal = false;
		if(CellReadyForUnloadLoad() == true )
		{
			retVal = true;
			System.out.println(model.getClock());
	        System.out.println( " Is cell ready for unloadLoad?  " + retVal);

		}
	 	return(retVal);
	}

	
	@Override
	public void startingEvent() {

		icPart = SP.RemoveQue(model.qInputConveyor);
				
					model.rCell[0].busy = true;
					
		traceSTART();
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
		
		nxtTime =   25 + clearTime;
		return nxtTime;
		

	}
	

	@Override
	protected void terminatingEvent() {


		System.out.println( " Is cell busy when the ending event starts?  " + model.rCell[0].busy);

		int last = model.rqPowerAndFreeConveyor[0].position[0];

		Pallet pallet = UDP.getPallet(last);
		
		// Any existing part in the pallet is considered removed
		if (pallet != Pallet.NO_PALLET )
		{
		pallet.part = icPart;
		model.rCell[0].busy = false;
		pallet.isProcessed = true;
		System.out.println( " Is cell busy before the event ending?  " + model.rCell[0].busy);
		}
		System.out.println( " Is cell busy one the event ends?  " + model.rCell[0].busy);


	
		

		traceEND();


	}
	
	
	private static boolean CellReadyForUnloadLoad() {
		int last = model.rqPowerAndFreeConveyor[0].position.length -1;
		Pallet pallet = UDP.getPallet(last);
		
		// A pallet is available at work cell 8
		if ( last != Pallet.NO_PALLET_ID
			&& pallet != Pallet.NO_PALLET 
		     // A part is available in the input conveyor
			 && model.qInputConveyor.n != 0 
		     // Work cell 8 not busy
		   	 && model.rCell[0].busy == false 
		     // Processing not done on pallet
             && pallet.isProcessed == false)
			
		return true;
		else
		return false;

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
