package electronicsProject;

import simulationModelling.ConditionalAction;

public class BatchRelease extends ConditionalAction {
	
	static ElectronicsProject model; // For referencing the model
	private static int id; // Buffer Conveyor member identifier
	
	public static boolean precondition(ElectronicsProject md) {
		int NONE = -1;
		id = UDP.BatchReadyForRelease();
		return id != NONE;
	}
	
	@Override
	protected void actionEvent() {
		// BatchRelease Action Sequence SCS
		BuffConveyor.BufferType[] bID = BuffConveyor.BufferType.values();
		
	/*	String s = "--------------- Batch Release [" + bID[id].getString() + "] ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Before Release\n";
		s += "   BuffConveyor n: " + model.qBuffConveyor[id].n + "\n";
		s += "   Input conveyor: ";
		
		for(int i = 0; i < model.qInputConveyor.n; i++) {
			s += model.qInputConveyor.list[i].uType.toString() + " ";
		}
		
		s += "\n"; */
		
		for(int i = 0; i < model.batchSize; i++) {
			// Removing parts from buffer conveyors
			Part icPart = model.qBuffConveyor[id].spRemoveQue();
			
			if(icPart != Part.NO_PART) {
				//  Inserting parts into input conveyor
				model.qInputConveyor.spInsertQue(icPart);
			}
		}

	/*	s += "After Release\n";
		s += "   BuffConveyor n: " + model.qBuffConveyor[id].n + "\n";
		s += "   Input conveyor: ";
		
		for(int i = 0; i < model.qInputConveyor.n; i++) {
			s += model.qInputConveyor.list[i].uType.toString() + " ";
		}
		
		s += "\n";
		
		Trace.write(s, "traceBatchRelease.txt", this.getClass().getName()); */
	}
	
}
