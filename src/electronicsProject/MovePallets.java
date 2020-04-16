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
	private static List<int[]> palletsMove; //pid, pos
	
    public static boolean precondition() {
    	palletsMove = PalletReadyToMove();
    	return palletsMove.size() > 0;
    }
    
   	@Override
	protected double duration() {
		return Constants.MOVE_TIME; // duration of the activity to simulate time to move a pallet   
	}
   
	
	@Override
	public void startingEvent() { //later set is moving to true
		for(int i = 0; i < palletsMove.size(); i++) {
			int[] p = palletsMove.get(i);
			int pid = p[0];
			int conveyorID = p[1];
			int pos = p[2];
			
			if(pos < model.rqPowerAndFreeConveyor[conveyorID].position.length -1) {
				model.rqPowerAndFreeConveyor[conveyorID].position[pos+1] = pid;
				
			}else {
				if(conveyorID < model.rqPowerAndFreeConveyor.length -1) {
					model.rqPowerAndFreeConveyor[conveyorID+1].position[0] = pid;
				}else {
					model.rqPowerAndFreeConveyor[0].position[0] = pid;
				}
			}
			model.rqPowerAndFreeConveyor[conveyorID].position[pos] = Pallet.NO_PALLET_ID;
			

		}
		
	//	model.crPallet[pid].isMoving = true;

		trace();
	}
	
	
	@Override
	protected void terminatingEvent() {
		

		//model.crPallet[pid].isMoving = false;

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