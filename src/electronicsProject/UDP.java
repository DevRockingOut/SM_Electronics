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
    
    // returns a list of pallets ready to move [UPDATE_CM]
    static List<int[]> PalletReadyToMove() {
    	List<int[]> palletsMove = new ArrayList<int[]>();
    	
    	for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
    		for(int j = 0; j < model.rqPowerAndFreeConveyor[i].position.length; j++) {
    			int pid = model.rqPowerAndFreeConveyor[i].position[j];
    			int nextPid = Pallet.NO_PALLET_ID;
    			
    			if(j < model.rqPowerAndFreeConveyor[i].position.length -1) {
    				nextPid = model.rqPowerAndFreeConveyor[i].position[j+1];
    			}else {
    				if(i < model.rqPowerAndFreeConveyor.length -1) {
    					nextPid = model.rqPowerAndFreeConveyor[i+1].position[0];
    				}else {
    					nextPid = model.rqPowerAndFreeConveyor[0].position[0];
    				}
    			}
    			
    			Pallet pallet = getPallet(pid);
    			
    			// check if next position is empty
    			if(pallet != Pallet.NO_PALLET && nextPid == Pallet.NO_PALLET_ID) {
    				int[] p = new int[] {pid, i, j};
    				palletsMove.add(p);
    			}
    		}
    	}
    	
    	return palletsMove;
	}
    
    private static Pallet getPallet(int pid) {
    	for(int i = 0; i < model.crPallet.length; i++) {
    		if(model.crPallet[i].id == pid) {
    			return model.crPallet[i];
    		}
    	}
    	
    	return Pallet.NO_PALLET;
    }
}
