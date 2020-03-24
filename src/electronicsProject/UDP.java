package electronicsProject;

import java.util.HashMap;
import java.util.Map;
import electronicsProject.BuffConveyor.BufferType;


public class UDP 
{
	static ElectronicsProject model; 
	static BufferType lastBuffConveyor = BuffConveyor.BUFF_TYPE_NONE;
	

    // returns the operation time at the work cell
    public long uServiceTime(int cellId, Part.PartType currentparttype, Part.PartType previousparttype){
        return 0;
    }
    
    // returns the buffer conveyor id that is ready to release the batch of parts
    static int BatchReadyForRelease() {
    	
    	// check if there is available space in the input conveyor [UPDATE_CM]
    	if(model.qInputConveyor.capacity > (model.qInputConveyor.n - model.batchSize)) {
    		
    		// try releasing the batch from a different buffer conveyor each time
    		if(lastBuffConveyor != BufferType.BA) {
    			lastBuffConveyor = BufferType.BA;
    		}else if(lastBuffConveyor != BufferType.BB) {
    			lastBuffConveyor = BufferType.BB;
    		}else if(lastBuffConveyor != BufferType.BC) {
    			lastBuffConveyor = BufferType.BC;
    		}
    		
    		// check if there are enough parts in the buffer conveyor to be released
    		if(model.qBuffConveyor[lastBuffConveyor.getInt()].n >= model.batchSize) {
    			return lastBuffConveyor.getInt();
    		}
    	}
    	
    	return Constants.NONE;
    }
    
    // returns a list of pallets ready to move <palletsPos, power-and-free conveyorPos> [UPDATE_CM]
    static Object PalletReadyToMove() {
    	Map<Integer,Integer> pallets = new HashMap<>();
    	
    	for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
    		int last = model.rqPowerAndFreeConveyor[i].position.length -1;
    		
    		for(int j = 0; j < model.rqPowerAndFreeConveyor.length; j++) {
    			int pid = model.rqPowerAndFreeConveyor[i].position[j];
    			
    			for(int k = 0; k < model.crPallet.length; k++) {
    				if(model.crPallet[k].id == pid) {
    					int pos = j;
    					pallets.put(k, pos);
    				}
    			}
    		}
    		
    		if(MovePallets.conveyorID == last) {
    			MovePallets.conveyorID = 0;
    		}else if(MovePallets.conveyorID == i) {
    			MovePallets.conveyorID += 1;
    		}	
    	}
    	
    	if(pallets.size() > 0) {
    		return pallets;
    	}
    	
		return Constants.NONE;	
	}
}
