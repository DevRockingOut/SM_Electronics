package electronicsProject;

import simulationModelling.Activity;

class MovePallets extends Activity {
	static ElectronicsProject model;
	//model.totalPallets
	static int[] canMovePosi = new int[40];
	static int[] canMovePosj = new int[40];
	static int m = 0;
	//check if there is available space ahead of the pallets
	/*for(int i=Initialise.C1; i<=Initialise.C8; i++) {
	   for(int j=0; j<=8; j++) {
		   
		  if (model.rqPowerAndFreeConveyor.position[i].pos[j]==null)
		  {
			  canMovePosi[m]=i;
		      canMovePosj[m]=j;
		      m++;
		  }
		}
	}*/
   public static boolean precondition() {
	   //as long as there is available space, logically pallets can move
	   return (canMovePosi!=null && canMovePosj!=null);
   }
	
	int prei=0;
	int prej=0;
	@Override
	public void startingEvent() {
	     for(int i=0; i<=m; i++) {
	         /*if (canMovePosi[i]==Initialise.C1)
	        	 prei=Initialise.C8;
	         else 
	        	 prei=canMovePosi[i]-1;*/
	         
	         if (canMovePosj[i]==0) 
	        	 prej=8;
	         else 
	        	 prej=canMovePosj[i]-1;
	         //all pallets could move one step forward as long as its processing is finished.
	         for(int j=0;j<=prei;j++) {
	        	for(int k=0; k<=prej; k++) {
	                //if (PowerAndFreeConveyor.PowerndFreeConv[j].pos[k].Pallet[pid].processing=false)
	    	        //PowerAndFreeConveyor.PowerndFreeConv[j+1].pos[k+1]
	    	        //=PowerAndFreeConveyor.PowerndFreeConv[i].pos[j].Pallet[pid];
	        	}
	         }
	     }
	}
	
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		
	}
	        
}