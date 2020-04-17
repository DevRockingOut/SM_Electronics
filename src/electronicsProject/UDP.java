package electronicsProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import electronicsProject.BuffConveyor.BufferType;


public class UDP 
{
	static ElectronicsProject model; 
	static BufferType lastBuffConveyor = BuffConveyor.BUFF_TYPE_NONE;
	
    
    // returns the buffer conveyor id that is ready to release the batch of parts
    static BufferType BatchReadyForRelease() {
    	BufferType NONE = null;
    	
    	// check if there is available space in the input conveyor [UPDATE_CM]
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
    	
    		return lastBuffConveyor;
    	}
    	
    	return NONE;
    }
    
}
