package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartA extends ScheduledAction {

	static ElectronicsProject model;

	
	@Override
	protected double timeSequence()
	{
		return duCArr();
	}

	@Override
	protected void actionEvent()
	{
		Part partA = new Part();
		partA.uType = Part.PartType.A;
		
		/*if(model.addBufferConveyor == false && model.inputConveyor.n < model.inputConveyor.capacity) {
			//model.inputConveyor.spInsertQue(partA);
		}else { 
			Output.nLossA++;
		}
		
		if (model.addBufferConveyor == false && model.qBuffConveyor[BufferConveyor.BufferType.BA].n < model.qBuffConveyor[BufferConveyor.BufferType.BA].capacity) {
			model.qBuffConveyor[BufferConveyor.BufferType.BA].spInsertQue(partA);
		}else {
			Output.nLossA++;
		}*/
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfA = new TriangularVariate(5,15,60, new MersenneTwister(sd.cArr));
        typeDM = new MersenneTwister(sd.type);	
	}
	
	// RVP for interarrival times.
	static public TriangularVariate delayOfA;
	static public MersenneTwister typeDM;
	
	static protected double duCArr()
	{
	   double nxtTime=0.0;	   
	   nxtTime = model.getClock() + 2.8*60 + delayOfA.next();
	   return(nxtTime);
	}	

}
