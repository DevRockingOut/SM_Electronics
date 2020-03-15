import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;

public class ElectronicsProject extends AOSimulationModel
{
	// Entities
	BuffConveyor [] qBuffConveyor = new BuffConveyor[3];
	//InputConveyor [] qInputConveyor = new InputConveyor[3];
	PowerAndFreeConveyor [] rqPowerAndFreeConveyor= new PowerAndFreeConveyor[8];
	Pallet [] crPallet = new Pallet[8];
	Cell [] rCell = new Cell[8];
	
	
	// Parameters
	// Implemented as attributes of qConveyors[M2].length and qConveyors[M3].length
	
	// Random variate procedures
	RVPs rvp;
	
	// User Defined Procedures
	UDPs udp = new UDPs();
	
	// Outputs
    Output output = new Output();
    
    // DSOVs
    public double getPercentTimeDown() { return output.percentTimeDown(); }
    public double getTimeC2Full() { return output.timeC2Full(); }
    public double getTimeC3Full() { return output.timeC3Full(); }
   
    // Constructor
	public ConveyorProject(double tftime, int lc2, int lc3, Seeds sd, boolean log)
	{
		// Adding references to model object to classes
		initialiseClasses(sd);
		
		// For turning on logging
		logFlag = log;
		
		// Set up RVPs
		rvp = new RVPs(sd);
		
		// Initialise parameters
		// Need to create the entities/objects here instead of the intialise action
		for(int id = Constants.M1; id <= Constants.M3; id++) qConveyors[id] = new Conveyors();
		qConveyors[Constants.M2].length = lc2;
		qConveyors[Constants.M3].length = lc3;
		
		this.initAOSimulModel(0, tftime);
		
		// Schedule Initialise action
		Initialise init = new Initialise();
		scheduleAction(init);  // Should always be first one scheduled.
		// Start arrivals
		CompArrivals aArr = new CompArrivals();
		scheduleAction(aArr);
		
		//printDebug("At Start");
	}
	
	// Initialize static components of model classes
	void initialiseClasses(Seeds sd)
	{
		// Add reference to standard classes
		Initialise.model = this;
		Output.model = this;
		RVPs.model = this;
		UDPs.model = this;
		// Add reference to activity/action classes
		CompArrivals.model = this;
		CompProcessing.model = this;
		MoveCOutOfM1.model = this;
		// Initialize RVPs in the classes
		CompProcessing.initRvp(sd);
		CompArrivals.initRvps(sd);
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
		if (MoveCOutOfM1.precondition(this) == true)
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
		}
		return (statusChanged);
	}
		
	public void eventOccured()
	{		
		output.updateSequences(); // for updating trajectory sets	
		if(logFlag) printDebug();
	}
	
	// for Debugging
	boolean logFlag = false;
	protected void printDebug()
	{
		// Debugging
		System.out.printf("Clock = %10.4f\n", getClock());
		// Machine M1
		System.out.print("   M1: qConveyor[].n= "+qConveyors[Constants.M1].getN()+
			             ", R.Machines[].busy="+rMachines[Constants.M1].busy +
			             ", R.Machines[].component=");
		if(rMachines[Constants.M1].component == Machines.NO_COMP) System.out.println("NO_COMP");
		else System.out.println(rMachines[Constants.M1].component.uType);
		// Machine M2
		System.out.println("   M2: qConveyor[].n= "+qConveyors[Constants.M2].getN()+
	             ", R.Machines[].busy="+rMachines[Constants.M2].busy);
		//Machine M3
		System.out.println("   M3: qConveyor[].n= "+qConveyors[Constants.M3].getN()+
	             ", R.Machines[].busy="+rMachines[Constants.M3].busy);		
		showSBL();
		System.out.println(">-----------------------------------------------<");
	}

}
