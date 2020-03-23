package electronicsProject;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

public class UDP 
{
	static ElectronicsProject model;   
	

    // returns the operation time at the work cell
    public long uServiceTime(int cellId, Part.PartType currentparttype, Part.PartType previousparttype){
        return 0;
    }
    
    // returns a list of pallets ready to move <palletsPos, power-and-free conveyorPos>
    static Object PalletReadyToMove() {
    	List<Pair<Integer,Integer>> pallets = new ArrayList<Pair<Integer, Integer>>();
    	
    	for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
    		int last = model.rqPowerAndFreeConveyor[i].position.length -1;
    		
    		for(int j = 0; j < model.rqPowerAndFreeConveyor.length; j++) {
    			int pid = model.rqPowerAndFreeConveyor[i].position[j];
    			
    			for(int k = 0; k < model.crPallet.length; k++) {
    				if(model.crPallet[k].id == pid) {
    					int pos = j;
    					pallets.add(new Pair<Integer,Integer>(k, pos));
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
    	}else {
			return Constants.NONE;
		}
	}
}
