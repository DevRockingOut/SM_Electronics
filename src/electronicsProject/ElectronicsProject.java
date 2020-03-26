package electronicsProject;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;

public class ElectronicsProject extends AOSimulationModel
{
	// Entities
	BuffConveyor[] qBuffConveyor;
	InputConveyor qInputConveyor = new InputConveyor();
	PowerAndFreeConveyor[] rqPowerAndFreeConveyor= new PowerAndFreeConveyor[8];
	Pallet[] crPallet;
	Cell[] rCell = new Cell[8];
	
	// Parameters
	int numPallets;
	int batchSize;
	
	// Random variate procedures
	RVP rvp;
	
	// User Defined Procedures
	UDP udp = new UDP();
	
	// Outputs
    Output output = new Output();
    
    // SSOV
    int nLossA = 0;
	int nLossB = 0;
	int nLossC = 0;
	
    public double lostCost()
    {
    	return output.lostCost(nLossA, nLossB, nLossC);
    }
   
    // Constructor
	public ElectronicsProject(double tftime, int _numPallets, int _batchSize, Seeds sd, boolean log)
	{
		// Adding references to model object to classes
		initialiseClasses(sd);
		
		// For turning on logging
		logFlag = log;
		
		// Set up RVPs
		rvp = new RVP(sd);
		
		// Initialise parameters
		numPallets = _numPallets;
		batchSize = _batchSize;
		
		crPallet = new Pallet[numPallets];
		
		if(batchSize > 0) {
			qBuffConveyor = new BuffConveyor[3];
		}
		
		this.initAOSimulModel(0, tftime);
		System.out.println("Simulation endtime: " + tftime + "\n");
		
		// Schedule Initialise action
		Initialise init = new Initialise();
		scheduleAction(init);  // Should always be first one scheduled.
		
		//System.out.print("testing seed (random number) : " + sd.uArrA + "\n\n");
		// Start arrivals
		ArrivingOfPartA arrPA = new ArrivingOfPartA();
		scheduleAction(arrPA);
		
		ArrivingOfPartB arrPB = new ArrivingOfPartB();
		scheduleAction(arrPB);
		
		ArrivingOfPartC arrPC = new ArrivingOfPartC();
		scheduleAction(arrPC);
		
		//printDebug();
	}
	
	// Initialize static components of model classes
	void initialiseClasses(Seeds sd)
	{
		// Add reference to standard classes
		Initialise.model = this;
		Output.model = this;
		RVP.model = this;
		UDP.model = this;
		// Add reference to activity/action classes
		ArrivingOfPartA.model = this;
		ArrivingOfPartB.model = this;
		ArrivingOfPartC.model = this;
		BatchRelease.model = this;
		MovePallets.model = this;
		UnLoadLoad.model = this;
		Processing.model = this;
		
		// Initialize RVPs in the classes
		ArrivingOfPartA.initRvps(sd);
		ArrivingOfPartB.initRvps(sd);
		ArrivingOfPartC.initRvps(sd);
		Processing.initRvp(sd);
		UnLoadLoad.initRvp(sd);
	}
	
	@Override
	public void testPreconditions(Behaviour behObj)
	{
		reschedule(behObj);
		while (scanPreconditions() == true) /* repeat */;
	}

	// Single scan of all preconditions
	// Returns true if at least one precondition was true.
	private boolean scanPreconditions()
	{
		boolean statusChanged = false;
		
		// Conditional Actions
		if (BatchRelease.precondition(this) == true)
		{
			BatchRelease act = new BatchRelease(); // Generate instance																// instance
			act.actionEvent();
			statusChanged = true;
		}
		
		
		// Conditional Activities
		/*if (Processing.precondition() == true)
		{
			Processing act = new Processing(); // Generate instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
		
		
		if (UnLoadLoad.precondition() == true)
		{
			UnLoadLoad act = new UnLoadLoad(); // Generate instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
		
		
		if (MovePallets.precondition() == true)
		{
			MovePallets act = new MovePallets(); // Generate instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}*/
		//statusChanged = false;
		return (statusChanged);
		
	}
		
	public void eventOccured()
	{		
		//output.updateSequences(); // for updating trajectory sets	
		if(logFlag) printDebug();
	}
	
	// for Debugging
	boolean logFlag = false;
	protected void printDebug()
	{
		// Debugging
		System.out.printf("Clock = %10.4f\n", getClock());
		System.out.print("\n");
		
		if(batchSize > 0) {
			for(int i = 0; i < qBuffConveyor.length; i++) {
				if(qBuffConveyor[i] != null && qBuffConveyor[i].n > 0) {
					System.out.println("Number of parts in BuffConveyor " + qBuffConveyor[i].conveyorPartType + ": " + qBuffConveyor[i].n);
					for(int j = 0; j < qBuffConveyor[i].n; j++) {
						Part p = qBuffConveyor[i].list.get(j);
						System.out.print("Part " + p.uType + "  ");
					}
					System.out.print("\n\n");
				}
			}
		}else {
			System.out.println("No buffer conveyors created due to batch size of 0");
		}
		
		/*
		System.out.println("Number of parts in Input Conveyor is " + qInputConveyor.n + " and its capacity is " + qInputConveyor.capacity);
		
		for(int i = 0; i < qInputConveyor.n; i++) {
			Part p = qInputConveyor.list.get(i);
			System.out.print("Part " + p.uType + "  ");
		}
		
		System.out.print("\n\n");
		
		for(int i = 0; i < rCell.length; i++) {
			if(rCell[i] != null) {
				if(rCell[i].previousPartType == null) {
					System.out.print("C" + String.valueOf(i) + " (busy, previousPartType): " + "(" + String.valueOf(rCell[i].busy) + ", null)");
				}else {
					System.out.print("C" + String.valueOf(i) + " (busy, previousPartType): " + "(" + String.valueOf(rCell[i].busy) + ", not null)");
				}
			
				System.out.print("\n");
			}
		}
		
		System.out.print("\n\n");
		
		// Power-and-free conveyors
		for(int i = 0; i < rqPowerAndFreeConveyor.length; i++) {
			String pids = "";

			if(rqPowerAndFreeConveyor[i] != null) {
				for(int j = 0; j < rqPowerAndFreeConveyor[i].position.length; j++) {
					int id = rqPowerAndFreeConveyor[i].position[j];
					
					if(id < 10) {
						pids += String.valueOf(id) + "  ";
					}else {
						pids += String.valueOf(id) + " ";
					}
				}
				System.out.print("Power-and-free conveyor " + rqPowerAndFreeConveyor[i].type.getString() + ": ");
				System.out.print(pids + "\n");
			}
		}
		
		System.out.print("\n\n");*/
		
		//showSBL();
		System.out.println(">-----------------------------------------------<");
	}

}
