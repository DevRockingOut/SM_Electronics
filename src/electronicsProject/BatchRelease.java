package electronicsProject;

import simulationModelling.ConditionalAction;

public class BatchRelease extends ConditionalAction {
	
	private static int id;
	static ElectronicsProject model;
	
	
	protected static boolean precondition() {
		id = UDP.BatchReadyForRelease();
		return id != Constants.NONE;
	}
	
	@Override
	protected void actionEvent() {
		for(int i = 0; i < model.batchSize; i++) {
			Part icPart = SP.RemoveQue(model.qBuffConveyor[id]);
			
			if(icPart != Part.NO_PART) {
				SP.InsertQue(model.qInputConveyor, icPart);
			}
		}
	}
	
}
