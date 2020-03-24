package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ConditionalActivity;

class UnLoadLoad extends ConditionalActivity
{
	static ElectronicsProject model;
    static public TriangularVariate UNLOAD_LOAD_TIME;


	public static boolean precondition()
	{
		
		boolean retVal = false;
		if(CellReadyForUnloadLoad() == true )
			retVal = true;
		return(retVal);
	}

	
	@Override
	public void startingEvent() {
		Part icPart = SP.RemoveQue(model.qInputConveyor);
		
		if(icPart != Part.NO_PART) {
			int last = model.rqPowerAndFreeConveyor[0].position.length -1;
			int pid = model.rqPowerAndFreeConveyor[0].position[last];
			
			for(int i = 0; i < model.crPallet.length; i++) {
				if(model.crPallet[i].id == pid) {
					model.crPallet[i].part = icPart;
					model.rCell[0].busy = true;
				}
			}
		}
		
	}


	@Override
	protected void terminatingEvent() {

		if (model.crPallet[model.rqPowerAndFreeConveyor[1].position[0]].part != Part.NO_PART
		&& model.rqPowerAndFreeConveyor[1].position[0] == Pallet.NO_PALLET_ID )

			{
			model.crPallet[model.rqPowerAndFreeConveyor[0].position[0]].part = SP.RemoveQue(model.qInputConveyor);
		    model.rCell[0].busy = false;
		    model.crPallet[model.rqPowerAndFreeConveyor[0].position[0]].isProcessed = false;
			}

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
		int last = model.rqPowerAndFreeConveyor[0].position.length -1;
		
		if (model.rqPowerAndFreeConveyor[0].position[last] != Pallet.NO_PALLET_ID
		// A part is available in the input conveyor
			&& model.qInputConveyor.n != 0 
		// Work cell 8 not busy
			&& model.rCell[0].busy == false 
		// Processing not done on pallet
           && model.crPallet[model.rqPowerAndFreeConveyor[0].position[last]].isProcessed == false)

		return true;
		else
		return false;
	}




}
