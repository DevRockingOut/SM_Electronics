package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartB extends ScheduledAction {

	static ElectronicsProject model;

	
	@Override
	protected double timeSequence()
	{
		return duCArr();
	}

	@Override
	protected void actionEvent()
	{
		Part partB = new Part();
		partB.uType = Part.PartType.B;
		
		/*if(model.addBufferConveyor = false && model.inputConveyor.n < model.inputConveyor.capacity) {
			model.inputConveyor.spInsertQue(partB);
		}else {
			Output.nLossB++;
		}
		
		if (model.addBufferConveyor = false && model.qBuffConveyor[BufferConveyor.BufferType.BB].n < model.qBuffConveyor[BufferConveyor.BufferType.BB].capacity) {
			model.qBuffConveyor[BufferConveyor.BufferType.BB].spInsertQue(partB);
		}else {
			Output.nLossB++;
		}*/
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfB = new TriangularVariate(5,20,55, new MersenneTwister(sd.cArr));
        typeDM = new MersenneTwister(sd.type);	
	}
	
	// RVP for interarrival times.
	static public TriangularVariate delayOfB;
	static public MersenneTwister typeDM;
	
	static protected double duCArr()
	{
	   double nxtTime = 0.0;	   
	   nxtTime = model.getClock() + 1.4*60 + delayOfB.next();
	   return(nxtTime);
	}	

}
