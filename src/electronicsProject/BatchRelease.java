package electronicsProject;


import electronicsProject.BuffConveyor.BufferType;
import simulationModelling.ConditionalAction;

public class BatchRelease extends ConditionalAction {
	
	int id;
	static ElectronicsProject model;
	
	
	public static boolean precondition(ElectronicsProject md) {
		BufferType NONE = null;
		
		//System.out.println("Batch ready for release ID: " + id);
		return UDP.BatchReadyForRelease() != NONE;
	}
	
	@Override
	protected void actionEvent() {
		BufferType bType = UDP.BatchReadyForRelease();
		id = bType.getInt();
		
		BuffConveyor.BufferType[] bID = BuffConveyor.BufferType.values();
		for(int i = 0; i < model.qBuffConveyor.length; i++) {
			System.out.println("BufferConveyor " + model.qBuffConveyor[bID[i].getInt()].type + " :" + model.qBuffConveyor[bID[i].getInt()].n);
		}
		for(int i = 0; i < model.batchSize; i++) {
			Part icPart = model.qBuffConveyor[id].spRemoveQue();
			
			if(icPart != Part.NO_PART) {
				model.qInputConveyor.spInsertQue(icPart);
			}
		}
		
		System.out.print("Input conveyor: ");
		for(int i = 0; i < model.qInputConveyor.n; i++) {
			System.out.print(model.qInputConveyor.list[i].uType.toString() + " ");
		}
		System.out.println("");
	}
	
}
