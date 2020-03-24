package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.Activity;

class UnLoadLoad extends Activity
{
	static ElectronicsProject model;
    static public TriangularVariate UNLOAD_LOAD_TIME;


	public static boolean precondition(ElectronicsProject md)
	{
		boolean retVal = false;
		if(CellReadyForUnloadLoad() == true )
			retVal = true;
		return(retVal);
	}

	
	@Override
	public void startingEvent() {
		SP.spRemoveQue(model.qInputConveyor, model.rCell[0]);
		model.rCell[0].busy = true;
	}


	@Override
	protected void terminatingEvent() {

		if (model.crPallet[pid].part != Part.NO_PART
		&& model.rqPowerAndFreeConveyor[1].position[0] == Pallet.NO_PALLET_ID )

// It was originally like this:
//(RQ.PowerAndFreeConveyor[C1].n < RQ.PowerAndFreeConveyor[C1].capacity) 
// so I changed it because I'm not sure if we have an array or not??

			SP.spRemoveQue(model.qInputConveyor, model.crPallet[model.rqPowerAndFreeConveyor[0].position[0]].part);		
		    model.rCell[0].busy = false;
		    model.crPallet[model.rqPowerAndFreeConveyor[0].position[0]].isProcessed = false;
	}
	 


	
	static void initRvp(Seeds sd)
	{
		UNLOAD_LOAD_TIME = new TriangularVariate(5, 15, 75, new MersenneTwister(sd.ultC8));
	}	
	
	@Override
	public double duration() {
		
		return (model.rvp.uUnloadLoadTime());

	}

	private static boolean CellReadyForUnloadLoad() {
		
		// A pallet is available at work cell 8
		if (model.rqPowerAndFreeConveyor[0].position[0] != Pallet.NO_PALLET_ID
		// A part is available in the input conveyor
			&& model.qInputConveyor.n != 0 
		// Work cell 8 not busy
			&& model.rCell[0].busy == false 
		// Processing not done on pallet
           && model.crPallet[model.rqPowerAndFreeConveyor[0].position[0]].isProcessed == false)

		return true;
		else
		return false;
	}
	


}
