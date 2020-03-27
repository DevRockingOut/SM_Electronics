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
	

    // returns the operation time at the work cell
    public long uServiceTime(int cellId, Part.PartType currentparttype, Part.PartType previousparttype){
        return 0;
    }
    
    // returns the buffer conveyor id that is ready to release the batch of parts
    static int BatchReadyForRelease() {
    	
    	// check if there is available space in the input conveyor [UPDATE_CM]
    	if(model.batchSize > 0 && model.qInputConveyor.capacity > (model.qInputConveyor.n - model.batchSize)) {
    		
    		// try releasing the batch from a different buffer conveyor each time
    		if(lastBuffConveyor != BufferType.BA && model.qBuffConveyor[BufferType.BA.getInt()].n >= model.batchSize) {
    			lastBuffConveyor = BufferType.BA;
    		}else if(lastBuffConveyor != BufferType.BB && model.qBuffConveyor[BufferType.BB.getInt()].n >= model.batchSize) {
    			lastBuffConveyor = BufferType.BB;
    		}else if(lastBuffConveyor != BufferType.BC && model.qBuffConveyor[BufferType.BC.getInt()].n >= model.batchSize) {
    			lastBuffConveyor = BufferType.BC;
    		}else {
    			return Constants.NONE;
    		}
    		
    		return lastBuffConveyor.getInt();
    	}
    	
    	return Constants.NONE;
    }
    
    // returns a list of pallets ready to move <conveyorID, pallet pos in conveyor, crPallet index> [UPDATE_CM]
    static Object PalletReadyToMove() {
    	//Map<Integer,Integer> pallets = new HashMap<>();
    	List<int[]> pallets = new ArrayList<int[]>();
    	
    	// scan conveyors in reverse order
    	for(int i = model.rqPowerAndFreeConveyor.length -1; i >= 0; i--) {
    		// scan pallets in reverse order
    		for(int j = model.rqPowerAndFreeConveyor[i].position.length -1; j >= 0; j--) {
    			int pid = model.rqPowerAndFreeConveyor[i].position[j];
    			int nextPid = Pallet.NO_PALLET_ID;
    			Pallet nextPallet = Pallet.NO_PALLET;
    			
    			if(j < (model.rqPowerAndFreeConveyor[i].position.length -1)) {
    				nextPid = model.rqPowerAndFreeConveyor[i].position[j+1];
    			}else {
    				// get first pallet id in next power-and-free conveyor
    				if(i < (model.rqPowerAndFreeConveyor.length -1)) {
    					nextPid = model.rqPowerAndFreeConveyor[i+1].position[0];
    				}else {
    					nextPid = model.rqPowerAndFreeConveyor[0].position[0];
    				}
    			}
    			
    			nextPallet = getPallet(nextPid);
    			
    			// check if next position in conveyor is empty or if the pallet is moving
    			if(nextPid == Pallet.NO_PALLET_ID || (nextPallet != Pallet.NO_PALLET && nextPallet.isMoving == true)) {
    				int crPalletPos = getPalletPos(pid); // search for pallet in crPallet array
    				int pos = j; // pallet position in conveyor
    				int conveyorID = i;
    				
    				if(crPalletPos != Constants.NONE) {
    					int[] p = new int[] {conveyorID, pos, crPalletPos};
    					pallets.add(p);
    					
    					// have to set isMoving = true here instead of startingEvent in order to move multiple pallets at the same time
    					model.crPallet[crPalletPos].isMoving = true; 
    				}
    			}
    		}
    	}
    	
    	System.out.println("pallets size: " + pallets.size());
    	if(pallets.size() > 0) {
    		return pallets;
    	}
    	
		return Constants.NONE;	
	}
    
    /**
     * Helper functions to search for a pallet by id, note that pallet ids do not start at 0
     **/
    private static int getPalletPos(int pid) {
    	for(int k = 0; k < model.crPallet.length; k++) {
			if(model.crPallet[k].id == pid) {     
				return k;
			}
		}
    	
    	return Constants.NONE;
    }
    
    private static Pallet getPallet(int pid) {
    	for(int k = 0; k < model.crPallet.length; k++) {
			if(model.crPallet[k].id == pid) {     
				return model.crPallet[k];
			}
		}
    	
    	return Pallet.NO_PALLET;
    }
}
