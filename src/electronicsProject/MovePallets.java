package electronicsProject;

import simulationModelling.ConditionalActivity;
import java.util.ArrayList;
import java.util.List;

class MovePallets extends ConditionalActivity {
	
	static ElectronicsProject model; // For referencing the model
	List<int[]> palletsMove;
	static boolean wait = false; 
	
    public static boolean precondition() {
    	List<int[]> pallets = PalletsReadyToMove();
    	
    	return pallets.size() > 0 && wait == false;
    }
    
   	@Override
	protected double duration() {
   		// determine the duration of the movement of a pallet
		return Constants.MOVE_TIME;
	}
   
	
	@Override
	public void startingEvent() {
		palletsMove = PalletsReadyToMove();
    	
		wait = true;
		
		// set isMoving to true for all pallets that can move
    	for(int i = 0; i < palletsMove.size(); i++) {
    		int cellid = palletsMove.get(i)[0];
    		int pos = palletsMove.get(i)[1];
    		int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
    		
    		model.rcPallet[pid].isMoving = true;
    	}
    	
    	/*String s = "--------------- Move Pallets (start) ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		
		for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
			for(int j = 0; j < model.rqPowerAndFreeConveyor[i].position.length; j++) {
				s += "Power-and-free conveyor " + i + "  " + Cell.CellID.values()[i].toString();
				s += "  pid: " + model.rqPowerAndFreeConveyor[i].position[j] + "\n"; 
			}
		}
    	
    	Trace.write(s, "traceMovePallets.txt", this.getClass().getName());*/
	}
	
	
	@Override
	protected void terminatingEvent() {
		// MovePallets Activity Terminating Event SCS 
		
		for(int i = 0; i < palletsMove.size(); i++) {
			int cellid = palletsMove.get(i)[0];
			int pos = palletsMove.get(i)[1];
			int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
			
			if(pid != Pallet.NO_PALLET_ID) {
			
				// move the pallets
				if(pos < model.rqPowerAndFreeConveyor[cellid].position.length -1) {
					model.rqPowerAndFreeConveyor[cellid].position[pos+1] = pid;
					
				}else {
					if(cellid < model.rqPowerAndFreeConveyor.length -1) {
						model.rqPowerAndFreeConveyor[cellid+1].position[0] = pid;
					}else {
						model.rqPowerAndFreeConveyor[0].position[0] = pid;
					}
				}
				
				model.rqPowerAndFreeConveyor[cellid].position[pos] = Pallet.NO_PALLET_ID;
				// clear old pallet pos and set isMoving to false
				model.rcPallet[pid].isMoving = false;
				
				// reset isProcessed to false for pallets moving out of cells (C1 to C7)
				model.rcPallet[pid].isProcessed = false;
			}
		}
		
		if(model.logFlag) {
			String s = "Clock: " + model.getClock() + "\n";
			
			for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
				for(int j = 0; j < model.rqPowerAndFreeConveyor[i].position.length; j++) {
					s += "Power-and-free conveyor " + i + "  " + Cell.CellID.values()[i].toString();
					s += "  pid: " + model.rqPowerAndFreeConveyor[i].position[j] + "\n"; 
				}
			}
	    	
	    	Trace.write(s, "traceMovePallets.txt", this.getClass().getName()); 
		}
		
		wait = false;
	}
	
	// UDP
	// returns all pallets that are ready to move
	static List<int[]> PalletsReadyToMove(){
		List<int[]> pallets = new ArrayList<int[]>();
		int pidStop = -2; // used to exit loop 
		
		// reverse scan power-and-free conveyors
		for(int i = model.rqPowerAndFreeConveyor.length -1; i > -1 ; i--) {
			
			// reverse scan pallets
			for(int j = model.rqPowerAndFreeConveyor[i].position.length -1; j > -1; j--) {
				int cellid = i;
				int pos = j;
				int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
				int last = model.rqPowerAndFreeConveyor[cellid].position.length -1;
				int lastCell = model.rqPowerAndFreeConveyor.length -1;
				boolean palletCanMove = false;
				
				/*if(pos == last && model.rCell[cellid].busy == true) {
					break;
				}*/
				
				if(pid != Pallet.NO_PALLET_ID) {
					
					int nextPid;
					int nextCellid;
					int nextPos;
					
					if(pos < last) {
						nextCellid = cellid;
						nextPos = pos+1;
					}else {
						if(cellid < lastCell) {
							nextCellid = cellid+1;
							nextPos = 0;
						}else {
							nextCellid = 0;
							nextPos = 0;
						}
					}
					
					nextPid = model.rqPowerAndFreeConveyor[nextCellid].position[nextPos];
					
					// we are at position in C7 and first position of C8 contains a pallet
					int pidCell8 = model.rqPowerAndFreeConveyor[0].position[0];
					
					if(cellid == lastCell
						&& pos == last
						&& pidCell8 != Pallet.NO_PALLET_ID
						&& model.rcPallet[pidCell8].part != Part.NO_PART
						&& model.rCell[cellid].busy == false) {
						
						// first store scannedPallets the keep filling pallets array as usual
						
						boolean emptyFound = false;
						List<int[]> scannedPallets = new ArrayList<int[]>();
						
						// scan forward till we find an empty position (or pallet stuck at cell i.e busy = true)
						for(int a = 0; a < model.rqPowerAndFreeConveyor.length; a++) {
							int currentCellid = a;
							
							for(int b = 0; b < model.rqPowerAndFreeConveyor[a].position.length; b++) {
								int currentPos = b;
								int currentPid = model.rqPowerAndFreeConveyor[a].position[b];
								
								if(currentPid == Pallet.NO_PALLET_ID) { 
									emptyFound = true;
									break;
								}else if(model.rCell[currentCellid].busy == true){
									break;
								}else {
									pidStop = currentPid;
									// store pallets scanned
									int[] p = {currentCellid, currentPos};
									scannedPallets.add(p);
								}
							}
							
							if(emptyFound == true) {
								break;
							}else if(model.rCell[currentCellid].busy == true){
								break;
							}
						}
						
						if(emptyFound == true) {
							for(int m = scannedPallets.size() -1; m > -1; m--) {
								int mpid = model.rqPowerAndFreeConveyor[scannedPallets.get(m)[0]].position[scannedPallets.get(m)[1]];
								pallets.add(scannedPallets.get(m));
							}
						}
						
					}
					
					// there must be a part in pallet at C8 before moving it out of C8
					if(cellid == Cell.CellID.C8.getInt() && pos == last
						&& pid != Pallet.NO_PALLET_ID && model.rcPallet[pid].part == Part.NO_PART) {
						palletCanMove = false;
					}else if(nextPid == Pallet.NO_PALLET_ID || model.rcPallet[nextPid].isMoving == true
						|| palletInList(pallets, nextCellid, nextPos) == true) {
						
						// cell position in power-and-free-conveyor is not busy
						if(pos == last && model.rCell[cellid].busy == true) {
							palletCanMove = false;
						}else if(pos == last && model.rCell[cellid].busy == false) {
							palletCanMove = true;
						}else if(pos != last) { // every other position in power-and-free conveyor
							palletCanMove = true;
						}
					}
					
					if(pid == pidStop) {
						i = -1;
						j = -1;
						pidStop = -2;
						
					}else if(palletCanMove == true) {
						// all conditions met, so pallet can move
						int[] pallet = {cellid, pos};
						pallets.add(pallet);
					}
				}
			}
		}
		
		return pallets;
	}
	
	// helper method which returns whether a pallet is contained in a list of pallets
	private static boolean palletInList(List<int[]> pallets, int cellid, int pos) {
		
		for(int i = 0; i < pallets.size(); i++) {
			if(pallets.get(i)[0] == cellid && pallets.get(i)[1] == pos) {
				return true;
			}
		}
		
		return false;
	}
	        
}