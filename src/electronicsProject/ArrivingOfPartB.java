package electronicsProject;

import java.io.PrintStream;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartB extends ScheduledAction {

	static ElectronicsProject model;
	static public TriangularVariate delayOfB;
	static private PrintStream so = System.out;
	
	@Override
	protected double timeSequence()
	{
		return RVP.DuArrB();  // delay is added to every part arrival, it should be between 1.75% of times [TO_FIX]
	}

	@Override
	protected void actionEvent()
	{
		Part partB = RVP.uArrB();
		
		int BB = BuffConveyor.BufferType.BB.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BB].n < model.qBuffConveyor[BB].capacity) {
			model.qBuffConveyor[BB].spInsertQue(partB);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			model.qInputConveyor.spInsertQue(partB);
		}else {
			model.nLossB++;
		}
		
		//so.println("LossB: " + model.nLossB);
		//so.println("Part " + partB.uType.toString() + " created at time: " + model.getClock());
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfB = new TriangularVariate(5,20,55, new MersenneTwister(sd.uArrB));
	}

}
