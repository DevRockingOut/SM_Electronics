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
	static boolean wait = false;
	
    public static boolean precondition() {
    	List<int[]> pallets = PalletsReadyToMove();
    	
    	/*for(int i = 0; i < pallets.size(); i++) {
    		int cellid = pallets.get(i)[0];
    		int pos = pallets.get(i)[1];
    		int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
    		
    		System.out.println("cellid: " + cellid + "  pos: " + pos + "  pid: " + pid);
    	}
    	
    	System.out.println("----------- 1 ------------");*/
    	
    	return pallets.size() > 0 && wait == false;
    }
    
   	@Override
	protected double duration() {
		return Constants.MOVE_TIME; // duration of the activity to simulate time to move a pallet   
	}
   
	
   	private void test() {
   		List<int[]> pallets = new ArrayList<int[]>();
   		boolean emptyFound = false;
		List<int[]> scannedPallets = new ArrayList<int[]>();
		int pidStop = -2;
		
		// scan forward till we find an empty position (or pallet stuck at cell i.e busy = true -- not coded yet)
		for(int k = 0; k < model.rqPowerAndFreeConveyor.length; k++) {
			for(int l = 0; l < model.rqPowerAndFreeConveyor[k].position.length; l++) {
				int currentCellid = k;
				int currentPos = l;
				int currentPid = model.rqPowerAndFreeConveyor[k].position[l];
				
				if(currentPid == Pallet.NO_PALLET_ID) { 
					emptyFound = true;
				}else {
					// store pallets scanned
					int[] p = {currentCellid, currentPos};
					scannedPallets.add(p);
					pidStop = currentPid;
				}
			}
		}
		
		if(emptyFound == true) {
			for(int m = scannedPallets.size() -1; m > -1; m--) {
				pallets.add(scannedPallets.get(m));
				int pid = model.rqPowerAndFreeConveyor[scannedPallets.get(m)[0]].position[scannedPallets.get(m)[1]];
				System.out.println("pid scanned : " + pid);
			}
		}
		
		System.out.println("------------------------------------------------");
   	}
   	
	@Override
	public void startingEvent() {
		palletsMove = PalletsReadyToMove();
    	
		//test();
		wait = true;
		// set isMoving to true for all pallets that can move
    	for(int i = 0; i < palletsMove.size(); i++) {
    		int cellid = palletsMove.get(i)[0];
    		int pos = palletsMove.get(i)[1];
    		int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
    		
    		model.rcPallet[pid].isMoving = true;
    		//System.out.println("cellid: " + cellid + "  pos: " + pos + "  pid: " + pid);
    	}
    	
    	//System.out.println("----------- 2 ------------");
	}
	
	
	@Override
	protected void terminatingEvent() {
		/*for(int i = 0; i < palletsMove.size(); i++) {
    		int cellid = palletsMove.get(i)[0];
    		int pos = palletsMove.get(i)[1];
    		int pid = model.rqPowerAndFreeConveyor[cellid].position[pos];
    	
    		System.out.println("cellid: " + cellid + "  pos: " + pos + "  pid: " + pid);
    	}*/
		
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
		
		//System.out.println("----------- 3 ------------");
		
		trace();
		wait = false;
	}
	
	
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
				
				
				if(pid != Pallet.NO_PALLET_ID) {
					
					if(cellid == model.rqPowerAndFreeConveyor.length -1
						&& pos == model.rqPowerAndFreeConveyor[i].position.length -1
						&& model.rqPowerAndFreeConveyor[0].position[0] != Pallet.NO_PALLET_ID) {
						// we are at position in C7 and first position of C8 contains a pallet
						// first store scannedPallets the keep filling pallets array as usual
						
						boolean emptyFound = false;
						List<int[]> scannedPallets = new ArrayList<int[]>();
						
						// scan forward till we find an empty position (or pallet stuck at cell i.e busy = true -- not coded yet)
						for(int a = 0; a < model.rqPowerAndFreeConveyor.length; a++) {
							for(int b = 0; b < model.rqPowerAndFreeConveyor[a].position.length; b++) {
								int currentCellid = a;
								int currentPos = b;
								int currentPid = model.rqPowerAndFreeConveyor[a].position[b];
								
								if(currentPid == Pallet.NO_PALLET_ID) { 
									//System.out.println("previousPid: " + model.rqPowerAndFreeConveyor[a].position[b-1]);
									emptyFound = true;
									break;
								}else {
									pidStop = currentPid;
									System.out.println("pidStop " + pidStop);
									// store pallets scanned
									int[] p = {currentCellid, currentPos};
									scannedPallets.add(p);
								}
							}
							
							if(emptyFound == true) {
								break;
							}
						}
						
						if(emptyFound == true) {
							for(int m = scannedPallets.size() -1; m > -1; m--) {
								int mpid = model.rqPowerAndFreeConveyor[scannedPallets.get(m)[0]].position[scannedPallets.get(m)[1]];
								System.out.println(mpid);
								pallets.add(scannedPallets.get(m));
							}
							//int[] pallet = {cellid, pos};
							//pallets.add(pallet);
							
							for(int m = 0; m < pallets.size(); m++) {
								int mpid = model.rqPowerAndFreeConveyor[pallets.get(m)[0]].position[pallets.get(m)[1]];
								System.out.print(mpid + " ");
							}
							System.out.println("");
							System.out.println("--------");
						}
					}
					
					if(pid == pidStop) {
						System.out.println(pallets.size());
						System.out.println("print this pid " + pid);
						System.out.println("and this pidStop " + pidStop);
						i = -1;
						j = -1;
						pidStop = -2;
					}else {
						int[] pallet = {cellid, pos};
						pallets.add(pallet);
					}
				}
			}
		}
		
		return pallets;
	}
	
	
	// returns a list of pallets ready to move
	static List<int[]> PalletsReadyToMove_backup(){
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
	        
}