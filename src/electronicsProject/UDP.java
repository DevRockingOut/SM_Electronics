package electronicsProject;

import electronicsProject.BuffConveyor.BufferType;

public class UDP 
{
	static ElectronicsProject model; // For referencing the model
	static BufferType lastBuffConveyor = BuffConveyor.BUFF_TYPE_NONE;
	
    // returns the member identifier of the buffer conveyor that is ready to release the batch of parts
    static int BatchReadyForRelease() {
    	int NONE = -1;
    	
    	// check if there is available space in the input conveyor
    	if(model.batchSize > 0 && model.qInputConveyor.n <= model.qInputConveyor.capacity - model.batchSize) {
    		
    		// try releasing the batch from a different buffer conveyor each time
    		if(lastBuffConveyor != BufferType.BA && model.qBuffConveyor[BufferType.BA.getInt()].n >= model.batchSize) {
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
