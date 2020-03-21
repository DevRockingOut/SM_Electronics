package electronicsProject;

import electronicsProject.BuffConveyor.BufferType;
import electronicsProject.Cell.CellID;
import electronicsProject.PowerAndFreeConveyor.PFConveyorType;
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
			
			model.rCell[id] = new Cell();
			model.rCell[id].busy = false;
			model.rCell[id].previousPartType = null; //Part.NO_PART.uType;
		}
		
		model.qInputConveyor.n = 0;
		model.qInputConveyor.capacity = 40;
		
		BufferType[] bID = BuffConveyor.BufferType.values();
		
		// loop BA to BC
		for(int i = 0; i < bID.length; i++) {
			int id = bID[i].getInt();
			
			model.qBuffConveyor[id] = new BuffConveyor();
			model.qBuffConveyor[id].n = 0;
			model.qBuffConveyor[id].capacity = 10;
			model.qBuffConveyor[id].type = bID[i];
			
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
		int cellid = 0; // C8
		int pos = 0; // start at the head of the power-and-free conveyor
		PFConveyorType[] pfID = PowerAndFreeConveyor.PFConveyorType.values();
		
		for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
			model.rqPowerAndFreeConveyor[i] = new PowerAndFreeConveyor();
			model.rqPowerAndFreeConveyor[i].type = pfID[i];
		}
		
		for(int i = 0; i < (model.numPallets -1); i++) {  // [WTF_QUESTION]  if we have 40 pallets then we have 32 free positions, so power-and-free conveyors with no pallets at initialization
			Pallet crPallet = new Pallet();
			crPallet.id = pid;
			crPallet.index = pos;
			crPallet.isMoving = false;
			crPallet.isProcessed = false;
			crPallet.part = Part.NO_PART;
			
			// put the pallets in the power-and-free conveyors
			model.rqPowerAndFreeConveyor[cellid].position[pos] = pid;
			
			if(pos == (model.rqPowerAndFreeConveyor[cellid].position.length -1)) { // pos == 8
				cellid += 1; // go to next power-and-free conveyor
				pos = 0;
			}
			
			pid += 1;
		}
		
		printDebug();
	}
	
	private void printDebug()
	{
		// Debugging
		System.out.printf("Clock = %10.2f", model.getClock());
		System.out.print("\n\n");
		
		// Cells
		for(int i = 0; i < model.rCell.length; i++) {
			if(model.rCell[i].previousPartType == null) {
				System.out.print("C" + String.valueOf(i) + " (busy,previousPart): " + "(" + String.valueOf(model.rCell[i].busy) + ",null)");
			}else {
				System.out.print("C" + String.valueOf(i) + " (busy,previousPart): " + "(" + String.valueOf(model.rCell[i].busy) + ",not null)");
			}
			System.out.print("\n");
		}
		
		// Input conveyor
		System.out.print("\nInput conveyor capacity is " + model.qInputConveyor.capacity + "\n\n");
		
		// Buffer conveyors
		if(model.qBuffConveyor == null) {
			System.out.print("No buffer conveyors"); // should print if batchSize == 0
		}else {
			for(int i = 0; i < model.qBuffConveyor.length; i++){
				System.out.print(model.qBuffConveyor[i].type.getString() + " (capacity, partType): " + "(" + model.qBuffConveyor[i].capacity + ", " + model.qBuffConveyor[i].conveyorPartType + ")\n");
			}
		}
		
		System.out.print("\n");
		
		// Power-and-free conveyors
		for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
			String pids = "";
			//System.out.print("i == " + i + "\n");
			for(int j = 0; j < model.rqPowerAndFreeConveyor[i].position.length; j++) {
				pids += String.valueOf(model.rqPowerAndFreeConveyor[i].position[j]) + " ";
			}
			System.out.print("Power-and-free conveyor " + model.rqPowerAndFreeConveyor[i].type.getString() + ": ");
			System.out.print(pids + "\n");
		}
		
	}

}
