//having question on how to set 20% of A will be delay

import cern.jet.random.engine.MersenneTwister;
import simulationModelling.ScheduledAction;

class ArrivingOfPartC extends ScheduledAction {

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
			Part partC = new Part();
			partA.uType = Part.PartType.C;
			if(model.addBufferConveyor=false && InputConveyor.n<InputConveyor.capacity)
				model.InputConveyor.spInsertQue(partC);
			else 
				Output.nLossC++;
			if (model.addBufferConveyor=false && BufferConveyor[BC].n<BufferConveyor[BC].capacity)
				model.BufferConveyor[BC].spInsertQue(partC);
			else
				Output.nLossC++;
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
		   nxtTime = model.getClock()+2.0*60+delayOfC.nextDouble();
		   return(nxtTime);
		}	

}
