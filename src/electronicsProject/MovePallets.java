package electronicsProject;

import java.util.List;
import java.util.Map;
import simulationModelling.ConditionalActivity;

class MovePallets extends ConditionalActivity {
	
	static ElectronicsProject model;
	static int conveyorID = 0;
	//private static Map<Integer,Integer> palletsMove; // list of pallets <palletsPos, power-and-free conveyorPos>
	private static List<int[]> palletsMove;
	
    public static boolean precondition() {
    	Object pallets = UDP.PalletReadyToMove();
    	
    	if(pallets instanceof Integer && (Integer) pallets == Constants.NONE) {
    		return (Integer) pallets != Constants.NONE; 
    	}else {
    		//palletsMove = (Map<Integer,Integer>) pallets;// store the array of pallets to move
    		palletsMove = (List<int[]>) pallets;
    		return true;
    	}
    }
    
   	@Override
	protected double duration() {
		return DVP.uPalletMoving(); // duration of the activity to simulate time to move a pallet   
	}
   
	
	@Override
	public void startingEvent() {
		/*for (Map.Entry<Integer, Integer> entry : palletsMove.entrySet()) {
			int palletPos = entry.getKey();
			model.crPallet[palletPos].isMoving = true;
		}*/
		for(int i = 0; i < palletsMove.size(); i++) {
			int conveyorID = palletsMove.get(i)[0];
			int pos = palletsMove.get(i)[1];
			int crPalletPos = palletsMove.get(i)[2];
			
			int lastPos = model.rqPowerAndFreeConveyor[conveyorID].position.length - 1;
			
			if(pos == lastPos) {
				if(conveyorID < model.rqPowerAndFreeConveyor.length -1) {
					model.rqPowerAndFreeConveyor[conveyorID +1].position[0] = model.crPallet[crPalletPos].id;
				}else {
					model.rqPowerAndFreeConveyor[0].position[0] = model.crPallet[crPalletPos].id;
				}
			}else {
				model.rqPowerAndFreeConveyor[conveyorID].position[pos+1] = model.rqPowerAndFreeConveyor[conveyorID].position[pos];
			}
			
			model.rqPowerAndFreeConveyor[conveyorID].position[pos] = Pallet.NO_PALLET_ID;
	    	model.crPallet[crPalletPos].isMoving = false;
		}
		
		for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
			for(int j = 0; j < model.rqPowerAndFreeConveyor[i].position.length; j++) {
				int pid = model.rqPowerAndFreeConveyor[i].position[j];
				System.out.println("power-and-free conveyor: " + i + ", pid: " + pid);
			}
		}
	}
	
	
	@Override
	protected void terminatingEvent() {
		/*for (Map.Entry<Integer, Integer> entry : palletsMove.entrySet()) {
			// move each pallet to next position
			int palletPos = entry.getKey();
			int pos = entry.getValue();
			int lastPos = model.rqPowerAndFreeConveyor[conveyorID].position.length - 1;
			
			if(pos == lastPos) {
				model.crPallet[palletPos].isProcessed = false; // [WTF_QUESTION] shouldn't this be done in the processing terminating event???
				model.rqPowerAndFreeConveyor[conveyorID +1].position[0] = model.crPallet[palletPos].id;
			}else {
				model.rqPowerAndFreeConveyor[conveyorID].position[pos+1] = model.rqPowerAndFreeConveyor[conveyorID].position[pos];
			}
			
			model.rqPowerAndFreeConveyor[conveyorID].position[pos] = Pallet.NO_PALLET_ID;
	    	model.crPallet[palletPos].isMoving = false;
	     }*/
		
		
	}
	        
}