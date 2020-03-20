//having question on how to set 20% of A will be delay

import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;

class ArrivingOfPartA extends ScheduledAction {

	class CompArrivals extends ScheduledAction
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
			Part partA = new Part();
			partA.uType = Part.PartType.A;
			if(model.addBufferConveyor=false && InputConveyor.n<InputConveyor.capacity)
				model.InputConveyor.spInsertQue(partA);
			else 
				Output.nLossA++;
			if (model.addBufferConveyor=false && BufferConveyor[BA].n<BufferConveyor[BA].capacity)
				model.BufferConveyor[BA].spInsertQue(partA);
			else
				Output.nLossA++;
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
		
		static protected double duCArr( )
		{
		   double nxtTime=0.0;	   
		   nxtTime = model.getClock()+2.8*60+delayOfA.nextDouble();
		   return(nxtTime);
		}	

}
