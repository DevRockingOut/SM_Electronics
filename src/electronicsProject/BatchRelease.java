package electronicsProject;

import electronicsProject.BuffConveyor.BufferType;
import simulationModelling.ConditionalAction;

public class BatchRelease extends ConditionalAction {
	
	static ElectronicsProject model; // For referencing the model
	private static int id; // Buffer Conveyor member identifier
	static BufferType lastBuffConveyor = BuffConveyor.BUFF_TYPE_NONE;  // Update_CM
	static int NONE = -1;
	
	public static boolean precondition(ElectronicsProject md) {
		id = udpBatchReadyForRelease(); // Update_CM
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
		
		s += "\n";  */
		
		for(int i = 0; i < model.batchSize; i++) {
			// Removing parts from buffer conveyors
			Part icPart = model.qBuffConveyor[id].spRemoveQue();
			
			//  Inserting parts into input conveyor
			model.qInputConveyor.spInsertQue(icPart);
		}

	/*	s += "After Release\n";
		s += "   BuffConveyor n: " + model.qBuffConveyor[id].n + "\n";
		s += "   Input conveyor: ";
		
		for(int i = 0; i < model.qInputConveyor.n; i++) {
			s += model.qInputConveyor.list[i].uType.toString() + " ";
		}
		
		s += "\n";
		
		Trace.write(s, "traceBatchRelease.txt", this.getClass().getName());  */
	}
	
    // returns the member identifier of the buffer conveyor that is ready to release the batch of parts
    static int udpBatchReadyForRelease() {
    	
    	// check if there is available space in the input conveyor
    	if(model.batchSize > 0 && model.qInputConveyor.n <= model.qInputConveyor.capacity - model.batchSize) {
    		
    		// try releasing the batch from a different buffer conveyor each time
    		if(lastBuffConveyor == BufferType.BB 
    				&& model.qBuffConveyor[BufferType.BA.getInt()].n >= model.batchSize
    				&& model.qBuffConveyor[BufferType.BB.getInt()].n >= model.batchSize
    				&& model.qBuffConveyor[BufferType.BC.getInt()].n >= model.batchSize) {
    			lastBuffConveyor = BufferType.BC;
    		}else if(lastBuffConveyor != BufferType.BA && model.qBuffConveyor[BufferType.BA.getInt()].n >= model.batchSize) {
    			lastBuffConveyor = BufferType.BA;
    		}else if(lastBuffConveyor != BufferType.BB && model.qBuffConveyor[BufferType.BB.getInt()].n >= model.batchSize) {
    			lastBuffConveyor = BufferType.BB;
    		}else if(lastBuffConveyor != BufferType.BC && model.qBuffConveyor[BufferType.BC.getInt()].n >= model.batchSize) {
    			lastBuffConveyor = BufferType.BC;
    		}else {
    			return NONE;
    		}
    	
    		return lastBuffConveyor.getInt();
    	}
    	
    	return NONE;
    }
	
}
