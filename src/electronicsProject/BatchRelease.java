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
		
		for(int i = 0; i < model.batchSize; i++) {
			Part icPart = model.qBuffConveyor[id].spRemoveQue();
			
			if(icPart != Part.NO_PART) {
				model.qInputConveyor.spInsertQue(icPart);
			}
		}

	}
	
}
