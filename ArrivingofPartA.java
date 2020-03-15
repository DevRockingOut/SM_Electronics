import cern.jet.random.Exponential;
import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;

class ArrivingofPartA extends ScheduledAction
{
	static ElectronicsProject model;
	
	@Override
	protected double timeSequence()
	{
		return duCArr();
	}

	@Override
	protected void actionEvent()
	{
		Component iCComponent = new Component();
		iCComponent.uType = uCompType();
		model.qConveyors[Constants.M1].spInsertQue(iCComponent);
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
		interArr = new Exponential(1/MEAN_INTER_ARR, new MersenneTwister(sd.cArr));
        typeDM = new MersenneTwister(sd.type);	
	}
	
	// RVP for interarrival times.
	static private final double MEAN_INTER_ARR= 7.0;
	static private Exponential interArr;
	static protected double duCArr( )
	{
	   double nxtTime=0.0;	   
	   nxtTime = model.getClock()+interArr.nextDouble();
	   return(nxtTime);
	}
	
	// For determining type of arriving component.
	static private final double PERCENT_A = 0.55;  // PERCENT_B not required
	static private MersenneTwister typeDM;
	static protected Component.CompType uCompType()
	{
		Component.CompType type = Component.CompType.B;
		if(typeDM.nextDouble() < PERCENT_A) type = Component.CompType.A;
		return(type);			
	}

}
