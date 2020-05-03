package electronicsProject;

import electronicsProject.BuffConveyor.BufferType;
import electronicsProject.Cell.CellID;
import simulationModelling.ScheduledAction;

class Initialise extends ScheduledAction
{
	static ElectronicsProject model; // For referencing the model
	
	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	@Override
	public void actionEvent() 
	{
		// System Initialisation
		
		// cID contains all the cells identifiers and string representations used for logging/debugging purposes
		CellID[] cID = Cell.CellID.values();
		
		// loop C1 to C8
		// create and initialise cells
		for(int i = 0; i < cID.length; i++) {
			int id = cID[i].getInt();
			
			model.rCell[id] = new Cell();
			model.rCell[id].busy = false;
			model.rCell[id].previousPartType = Part.NO_PART_TYPE;
		}
		
		// initialise input conveyor
		model.qInputConveyor.n = 0;
		model.qInputConveyor.setCapacity(40); // need to use this setter to update capacity and create an array with max capacity of 40
		
		// create and initialise buffer conveyors
		if(model.batchSize > 0) {
			
			// bID contains all the buffer conveyor identifiers and string representations used for logging/debugging purposes
			BufferType[] bID = BuffConveyor.BufferType.values();
			
			// loop BA to BC
			for(int i = 0; i < bID.length; i++) {
				int id = bID[i].getInt();
				model.qBuffConveyor[id] = new BuffConveyor();
				model.qBuffConveyor[id].setCapacity(10); // need to use this setter to update capacity and create an array with max capacity of 10
				model.qBuffConveyor[id].n = 0;
				model.qBuffConveyor[id].type = bID[i];
			}
		}
		
		// create and initialise power-and-free conveyors
		for(int id = 0; id < cID.length; id++) {
			model.rqPowerAndFreeConveyor[id] = new PowerAndFreeConveyor();
			
			for(int j = 0; j <= 8; j++){
				model.rqPowerAndFreeConveyor[id].position[j] = Pallet.NO_PALLET_ID;
			}
		}
		
		int pid = 0;
		int cellid = Cell.CellID.C8.getInt();
		int pos = model.rqPowerAndFreeConveyor[cellid].position.length -1;
		int last = model.rqPowerAndFreeConveyor[cellid].position.length -1;
		
		// create and initialise pallets
		for(int id = 0; id < model.numPallets; id++) {
			model.rcPallet[id] = new Pallet();
			model.rcPallet[id].isMoving = false;
			model.rcPallet[id].isProcessed = false;
			model.rcPallet[id].part = Part.NO_PART;
			
			// put the pallets in the power-and-free conveyors
			model.rqPowerAndFreeConveyor[cellid].position[pos] = pid;
			
			if(pos == 0) {
				pos = last;
				if(cellid == Cell.CellID.C8.getInt()) {
					cellid = Cell.CellID.C7.getInt();
				}else {
					cellid -= 1;
				}
			}else {
				pos -= 1;
			}
			
			pid += 1;
		}
		
	}

}
