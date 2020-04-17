package electronicsProject;

import simulationModelling.ConditionalActivity;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

class MovePallets extends ConditionalActivity {
	
	static ElectronicsProject model;
	List<int[]> palletsMove; //cellid, pos
	
    public static boolean precondition() {
    	return PalletsReadyToMove().size() > 0;
    }
    
   	@Override
	protected double duration() {
		return Constants.MOVE_TIME; // duration of the activity to simulate time to move a pallet   
	}
   
	
	@Override
	public void startingEvent() {
		palletsMove = PalletsReadyToMove();
		
		// set isMoving to true for all pallets that can move
		for(int i = 0; i < palletsMove.size(); i++) {
			int cellid = palletsMove.get(i)[0];
			int pos = palletsMove.get(i)[1];
			
			int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
			System.out.println("here " + model.rqPowerAndFreeConveyor[cellid].position[pos]);
			model.rcPallet[pid].isMoving = true;
		}
	}
	
	
	@Override
	protected void terminatingEvent() {
		for(int i = 0; i < palletsMove.size(); i++) {
			int cellid = palletsMove.get(i)[0];
			int pos = palletsMove.get(i)[1];
			
			int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
			
		
			
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
			
			// clear old pallet pos and set isMoving to false
			model.rcPallet[pid].isMoving = false;
			
			// reset isProcessed to false for pallets moving out of cells (C1 to C7)
			model.rcPallet[pid].isProcessed = false;
			model.rqPowerAndFreeConveyor[cellid].position[pos] = Pallet.NO_PALLET_ID;
		}
		
		trace();
	}
	
	// returns a list of pallets ready to move
	static List<int[]> PalletsReadyToMove(){
		List<int[]> pallets = new ArrayList<int[]>();
		
		// scan power-and-free conveyors forward
		for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
			
			// reverse scan pallets
			for(int j = model.rqPowerAndFreeConveyor[i].position.length -1; j > -1; j--) {
				int cellid = i;
				int pos = j;
				int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
				int posLength = model.rqPowerAndFreeConveyor[cellid].position.length -1;
				
				// if cell is busy then current pallet cannot move
				if(pos == posLength && model.rCell[cellid].busy == true) {
					pid = Pallet.NO_PALLET_ID;
				}
				
				if(pid != Pallet.NO_PALLET_ID) {
					int nextPid = Pallet.NO_PALLET_ID;
					
					// get pallet in next position
					if(pos < posLength) {
						nextPid = model.rqPowerAndFreeConveyor[cellid].position[pos+1];
					}else {
						// get pallet in next conveyor
						if(cellid < model.rqPowerAndFreeConveyor.length -1) {
							nextPid = model.rqPowerAndFreeConveyor[cellid+1].position[0];
						}else {
							nextPid = model.rqPowerAndFreeConveyor[0].position[0];
						}
					}
					
					// next position is empty or next pallet is moving
					if(nextPid == Pallet.NO_PALLET_ID || (nextPid != Pallet.NO_PALLET_ID && model.rcPallet[nextPid].isMoving == true)) {
						int[] pallet = {cellid, pos};
						pallets.add(pallet);
						
					}else if(pos == posLength && nextPid != Pallet.NO_PALLET_ID && model.rcPallet[nextPid].isMoving == false) {
						// start searching at next conveyor
						int start = cellid;
						if(i < model.rqPowerAndFreeConveyor.length -1) {
							start += 1;
						}else {
							start = 0;
						}
						
						// scan on next conveyor till an empty position is found or a pallet stuck in a cell
						for(int k = start; k == i; k++) { // k == i  means we are back same conveyor so stop searching
							
							for(int l = model.rqPowerAndFreeConveyor[k].position.length -1; l > -1; l--) {
								int cellid2 = k;
								int pid2 = model.rqPowerAndFreeConveyor[cellid].position[k];
								
								// empty position found
								if(pid2 == Pallet.NO_PALLET_ID) {
									int[] pallet = {cellid, pos};
									pallets.add(pallet);
									
									k = model.rqPowerAndFreeConveyor.length; // used to exit loop
									l = model.rqPowerAndFreeConveyor[k].position.length -1; // used to exit loop
									
									// pallet is stuck in cell
								}else if(model.rCell[cellid2].busy == true) {
									k = model.rqPowerAndFreeConveyor.length; // used to exit loop
									l = model.rqPowerAndFreeConveyor[k].position.length -1; // used to exit loop
								}
							}
						}
					}
				}
			}
		}
		
		return pallets;
	}
	
	private void trace() {

		PrintWriter writer = null;
		try {
			//writer = new PrintWriter("trace.txt", "UTF-8");
			FileWriter fileWriter = new FileWriter("trace.txt", true); //Set true for append mode
		    writer = new PrintWriter(fileWriter);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writer.println(model.getClock());
		
		for(int i = 0; i < model.rqPowerAndFreeConveyor.length; i++) {
			for(int j = 0; j < model.rqPowerAndFreeConveyor[i].position.length; j++) {
				int pid = model.rqPowerAndFreeConveyor[i].position[j];
				writer.println("power-and-free conveyor: " + i + ", pid: " + pid);
			}
		}
		
		writer.println("---------------------------------------------------------------------");
		
		writer.close();
	}
	
    /*static List<int[]> PalletReadyToMove() {
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
	}*/
	        
}