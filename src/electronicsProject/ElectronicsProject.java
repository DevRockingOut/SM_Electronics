package electronicsProject;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;

public class ElectronicsProject extends AOSimulationModel
{
	// Entities
	BuffConveyor[] qBuffConveyor;
	InputConveyor qInputConveyor = new InputConveyor();
	PowerAndFreeConveyor[] rqPowerAndFreeConveyor= new PowerAndFreeConveyor[8];
	Pallet[] crPallet = new Pallet[8];
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
		
		if(batchSize > 0) {
			qBuffConveyor = new BuffConveyor[3];
		}
		
		this.initAOSimulModel(0, tftime);
		
		// Schedule Initialise action
		Initialise init = new Initialise();
		scheduleAction(init);  // Should always be first one scheduled.
		// Start arrivals
		//CompArrivals aArr = new CompArrivals();
		//scheduleAction(aArr);
		
		printDebug();
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
		MovePallets.model = this;
		UnLoadLoad.model = this;
		Processing.model = this;
		// Initialize RVPs in the classes
		//CompProcessing.initRvp(sd);
		//CompArrivals.initRvps(sd);
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
		/*if (MoveCOutOfM1.precondition(this) == true)
		{
			MoveCOutOfM1 act = new MoveCOutOfM1(); // Generate instance																// instance
			act.actionEvent();
			statusChanged = true;
		}
		// Conditional Activities
		if (CompProcessing.precondition() == true)
		{
			CompProcessing act = new CompProcessing(); // Generate instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}*/
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
		
	}

}
