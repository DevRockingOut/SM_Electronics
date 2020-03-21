package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartC extends ScheduledAction {

	static ElectronicsProject model;

	
	@Override
	protected double timeSequence()
	{
		return duCArr();
	}

	@Override
	protected void actionEvent()
	{
		Part partC = new Part();
		partC.uType = Part.PartType.C;
		
		/*if(model.addBufferConveyor=false && model.inputConveyor.n < model.inputConveyor.capacity) {
			model.inputConveyor.spInsertQue(partC);
		}else { 
			Output.nLossC++;
		}
		
		if (model.addBufferConveyor = false && BufferConveyor[BC].n < model.qBuffConveyor[BC].capacity) {
			model.qBuffConveyor[BC].spInsertQue(partC);
		}else {
			Output.nLossC++;
		}*/
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfC = new TriangularVariate(5,20,65, new MersenneTwister(sd.cArr));
        typeDM = new MersenneTwister(sd.type);	
	}
	
	// RVP for interarrival times.
	static public TriangularVariate delayOfC;
	static public MersenneTwister typeDM;
	
	static protected double duCArr( )
	{
	   double nxtTime=0.0;	   
	   nxtTime = model.getClock()+2.0*60 + delayOfC.next();
	   return(nxtTime);
	}	

}
