package electronicsProject;


import simulationModelling.ConditionalAction;

public class BatchRelease extends ConditionalAction {
	
	private static int id;
	static ElectronicsProject model;
	
	
	public static boolean precondition(ElectronicsProject md) {
		id = UDP.BatchReadyForRelease();
		//System.out.println("Batch ready for release ID: " + id);
		return id != Constants.NONE;
	}
	
	@Override
	protected void actionEvent() {
		BuffConveyor.BufferType[] bfID = BuffConveyor.BufferType.values();
		for(int i = 0; i < model.qBuffConveyor.length; i++) {
			System.out.println("BufferConveyor " + model.qBuffConveyor[bfID[i].getInt()].type + " :" + model.qBuffConveyor[bfID[i].getInt()].n);
		}
		for(int i = 0; i < model.batchSize; i++) {
			Part icPart = model.qBuffConveyor[id].spRemoveQue();
			
			if(icPart != Part.NO_PART) {
				model.qInputConveyor.spInsertQue(icPart);
			}
		}
		
		System.out.print("Input conveyor: ");
		for(int i = 0; i < model.qInputConveyor.list.length; i++) {
			System.out.print(model.qInputConveyor.list[i].uType.toString() + " ");
		}
		System.out.println("");
	}
	
}
