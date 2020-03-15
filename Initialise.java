//import simulationModelling.ScheduledAction;

class Initialise //extends ScheduledAction
{
	static ElectronicsProject model;
	
	private static final int C1=0;
	private static final int C2=1;
	private static final int C3=2;
	private static final int C4=3;
	private static final int C5=4;
	private static final int C6=5;
	private static final int C7=6;
	private static final int C8=7;
	private static final int BA=0;
	private static final int BB=1;
	private static final int BC=2;
	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	public void actionEvent() 
	{
	  for(int id=C1; id<=C8; id++)
	  {
		  R.Cell[id].busy = false;
		  R.Cell[id].previousPartType = A;
		  if (int id = C2 || id = C7) 
		     R.Cell[id].automated = false;
		     else 
		     R.Cell[id].automated = true;
	      for (int pos=0; pos<=8; pos++)
	         RQ.PowerAndFreeConveyor[id].position[pos]=NOPALLET;  
	  }
	  Q.InputConveyor.n=0;
	  Q.InputConveyor.capacity=40;
	  for(int id=BA; id<=BC; id++)
	  {
		  Q.BuffConveyor[id].n=0;
		  Q.BuffConveyor[id].capacity=10;
		  if (id==BA)
			  Q.BuffConveyor[id].conveyorPartType=A;
		  else if (id==BB)
			  Q.BuffConveyor[id].conveyorPartType=B;
		  else
			  Q.BuffConveyor[id].conveyorPartType=C;
	  }
	 int cellid=C8;
	 int pos=0;
	  for(int id=0; id<numPallets; id++) {
		  PowerAndFreeConveyor[cellid].position[pos]=id;
		  pos++;
		  if(pos==9)
			 cellid++;
		     pos=0;
		  if (cellid==8)
			  cellid=C1;
	  }
}
