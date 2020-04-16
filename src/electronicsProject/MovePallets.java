package electronicsProject;

import simulationModelling.ConditionalActivity;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.io.IOException;

class MovePallets extends ConditionalActivity {
	
	static ElectronicsProject model;
	private static List<int[]> palletsMove; //pid, pos
	
    public static boolean precondition() {
    	palletsMove = UDP.PalletReadyToMove();
 //   	System.out.println();
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