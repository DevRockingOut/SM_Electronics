package electronicsProject;

import java.io.PrintStream;
import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartA extends ScheduledAction {

	static ElectronicsProject model;
	static public TriangularVariate delayOfA;
	
	@Override
	protected double timeSequence()
	{
		return RVP.DuArrA();  // delay is added to every part arrival, it should be between 2% of times [TO_FIX]
	}

	@Override
	protected void actionEvent()
	{
		Part partA = RVP.uArrA();
		
		int BA = BuffConveyor.BufferType.BA.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BA].n < model.qBuffConveyor[BA].capacity) {
			model.qBuffConveyor[BA].spInsertQue(partA);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			model.qInputConveyor.spInsertQue(partA);
		}else {
			model.nLossA++;
		}
		
		//so.println("LossA: " + model.nLossA);
		//so.println("Part " + partA.uType.toString() + " created at time: " + model.getClock());
	}
	
	static void initRvps(Seeds sd)
	{
	    delayOfA = new TriangularVariate(5,15,60, new MersenneTwister(sd.uArrA));
	}

}
