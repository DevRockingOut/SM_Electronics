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
    	if(model.batchSize > 0 && model.qInputConveyor.n <= model.qInputConveyor.capacity - model.batchSize) {
    		
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
    
    static boolean CellReadyForUnloadLoad() {
    	int C8 = Cell.CellID.C8.getInt();
		int last = model.rqPowerAndFreeConveyor[C8].position.length -1;
		int pid = model.rqPowerAndFreeConveyor[C8].position[last];
		
		Pallet pallet = model.rcPallet[pid];
		
		// A pallet is available at work cell 8
		if ( pallet != Pallet.NO_PALLET 
		     // A part is available in the input conveyor
			 && model.qInputConveyor.n != 0 
		     // Work cell 8 not busy
		   	 && model.rCell[C8].busy == false 
		     // Processing not done on pallet
             && pallet.isProcessed == false) {
			System.out.println("Cell Ready To Unload/Load: " + true);
			return true;
		}

		return false;
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
    			
    			Pallet pallet =  model.rcPallet[pid];
    			
    			// check if next position is empty
    			if(pallet != Pallet.NO_PALLET && nextPid == Pallet.NO_PALLET_ID) {
    				int[] p = new int[] {pid, i, j};
    				palletsMove.add(p);
    			}
    		}
    	}
    	
    	return palletsMove;
	}
    
}
