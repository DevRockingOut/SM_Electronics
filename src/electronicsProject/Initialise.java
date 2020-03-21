package electronicsProject;

import electronicsProject.BuffConveyor.BufferType;
import electronicsProject.Cell.CellID;
import simulationModelling.ScheduledAction;

class Initialise extends ScheduledAction
{
	static ElectronicsProject model;
	
	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	@Override
	public void actionEvent() 
	{
		// [UPDATE_CM]  update initialise action on CM
		CellID[] cID = Cell.CellID.values();
		
		// loop C1 to C8
		for(int i = 0; i < cID.length; i++) {
			int id = cID[i].getInt();
			
			model.rCell[id].busy = false;
			model.rCell[id].previousPartType = Part.NO_PART.uType;
		}
		
		model.qInputConveyor.n = 0;
		model.qInputConveyor.capacity = 40;
		
		BufferType[] bID = BuffConveyor.BufferType.values();
		
		// loop BA to BC
		for(int i = 0; i < bID.length; i++) {
			int id = bID[i].getInt();
			
			model.qBuffConveyor[id].n = 0;
			model.qBuffConveyor[id].capacity = 0;
			
			switch(bID[i]) {
				case BA :
					model.qBuffConveyor[id].conveyorPartType = Part.PartType.A;
					break;
				case BB :
					model.qBuffConveyor[id].conveyorPartType = Part.PartType.B;
					break;
				case BC :
					model.qBuffConveyor[id].conveyorPartType = Part.PartType.C;
					break;
			}
		}
		
		int pid = 0;
		int currentID = 0; // C8
		int pos = 0; // start at the head of the power-and-free conveyor
		
		for(int i = 0; i < (model.numPallets -1); i++) {  // [WTF_QUESTION]  if we have 40 pallets then we have 32 free positions, so power-and-free conveyors with no pallets at initialization
			Pallet crPallet = new Pallet();
			crPallet.id = pid;
			crPallet.index = pos;
			crPallet.isMoving = false;
			crPallet.isProcessed = false;
			crPallet.part = Part.NO_PART;
			
			// put the pallets in the power-and-free conveyors
			int cellid = cID[currentID].getInt();
			model.rqPowerAndFreeConveyor[cellid].position[pos] = pid;
			
			if(pos == 8) {
				currentID += 1; // go to next power-and-free conveyor
				pos = 0;
			}
			
		}
	}
}
