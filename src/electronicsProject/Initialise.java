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
		// System Initialization
		CellID[] cID = Cell.CellID.values();
		
		// loop C1 to C8
		// create and initialise cells
		for(int i = 0; i < cID.length; i++) {
			int id = cID[i].getInt();
			
			model.rCell[id] = new Cell();
			model.rCell[id].busy = false;
			model.rCell[id].previousPartType = Part.NO_PART_TYPE;
		}
		
		// initialize input conveyor
		model.qInputConveyor.n = 0;
		
		// create and initialise buffer conveyors
		if(model.batchSize > 0) {
			BufferType[] bID = BuffConveyor.BufferType.values();
			
			// loop BA to BC
			for(int i = 0; i < bID.length; i++) {
				int id = bID[i].getInt();
				int n = 10;
				model.qBuffConveyor[id] = new BuffConveyor(n);
				model.qBuffConveyor[id].n = 0;
				model.qBuffConveyor[id].type = bID[i];
			}
		}
		
		// create and initialize power-and-free conveyorss
		for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
			model.rqPowerAndFreeConveyor[i] = new PowerAndFreeConveyor();
			model.rqPowerAndFreeConveyor[i].type = cID[i];
			
			for(int j = 0; j < model.rqPowerAndFreeConveyor[i].position.length; j++){
				model.rqPowerAndFreeConveyor[i].position[j] = Pallet.NO_PALLET_ID;
			}
		}
		
		int pid = 0;
		int cellid = Cell.CellID.C8.getInt();
		int pos = model.rqPowerAndFreeConveyor[cellid].position.length -1;
		
		// create and initialize pallets
		for(int i = 0; i < model.numPallets; i++) {
			Pallet pallet = new Pallet();
			pallet.id = pid;
			pallet.isMoving = false;
			pallet.isProcessed = false;
			pallet.part = Part.NO_PART;
			
			model.rcPallet[i] = pallet;
			
			// put the pallets in the power-and-free conveyors
			int last = model.rqPowerAndFreeConveyor[cellid].position.length -1;
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
