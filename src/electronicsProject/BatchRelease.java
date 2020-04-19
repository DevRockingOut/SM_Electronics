package electronicsProject;

import simulationModelling.ConditionalAction;

public class BatchRelease extends ConditionalAction {
	
	private static int id;
	static ElectronicsProject model;
	
	public static boolean precondition(ElectronicsProject md) {
		int NONE = -1;
		id = UDP.BatchReadyForRelease();
		return id != NONE;
	}
	
	@Override
	protected void actionEvent() {
		BuffConveyor.BufferType[] bID = BuffConveyor.BufferType.values();
		
		String s = "--------------- Batch Release [" + bID[id].getString() + "] ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Before Release\n";
		s += "   BuffConveyor n: " + model.qBuffConveyor[id].n + "\n";
		s += "   Input conveyor: ";
		
		for(int i = 0; i < model.qInputConveyor.n; i++) {
			s += model.qInputConveyor.list[i].uType.toString() + " ";
		}
		
		s += "\n";
		
		for(int i = 0; i < model.batchSize; i++) {
			Part icPart = model.qBuffConveyor[id].spRemoveQue();
			
			if(icPart != Part.NO_PART) {
				model.qInputConveyor.spInsertQue(icPart);
			}
		}

		s += "After Release\n";
		s += "   BuffConveyor n: " + model.qBuffConveyor[id].n + "\n";
		s += "   Input conveyor: ";
		
		for(int i = 0; i < model.qInputConveyor.n; i++) {
			s += model.qInputConveyor.list[i].uType.toString() + " ";
		}
		
		s += "\n";
		
		Trace.write(s, "traceBatchRelease.txt", this.getClass().getName());
	}
	
}
