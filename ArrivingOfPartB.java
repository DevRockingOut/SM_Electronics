//having question on how to set 20% of A will be delay

import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;

class ArrivingOfPartB extends ScheduledAction {

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
			Part partB = new Part();
			partA.uType = Part.PartType.B;
			if(model.addBufferConveyor=false && InputConveyor.n<InputConveyor.capacity)
				model.InputConveyor.spInsertQue(partB);
			else 
				Output.nLossB++;
			if (model.addBufferConveyor=false && BufferConveyor[BB].n<BufferConveyor[BB].capacity)
				model.BufferConveyor[BB].spInsertQue(partB);
			else
				Output.nLossB++;
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
		
		static protected double duCArr( )
		{
		   double nxtTime=0.0;	   
		   nxtTime = model.getClock()+1.4*60+delayOfB.nextDouble();
		   return(nxtTime);
		}	

}
