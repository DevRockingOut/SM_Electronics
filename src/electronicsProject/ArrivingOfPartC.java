package electronicsProject;

import java.io.PrintStream;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartC extends ScheduledAction {

	static ElectronicsProject model;
	static public TriangularVariate delayOfC;
	
	@Override
	protected double timeSequence()
	{
		return RVP.DuArrC(); // delay is added to every part arrival, it should be between 0.5% of times [TO_FIX]
	}

	@Override
	protected void actionEvent()
	{
		Part partC = RVP.uArrC();
		
		int BC = BuffConveyor.BufferType.BC.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BC].n < model.qBuffConveyor[BC].capacity) {
			model.qBuffConveyor[BC].spInsertQue(partC);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			model.qInputConveyor.spInsertQue(partC);
		}else {
			model.nLossC++;
		}
		
		//so.println("LossC: " + model.nLossC);
		//so.println("Part " + partC.uType.toString() + " created at time: " + model.getClock());
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfC = new TriangularVariate(5,20,65, new MersenneTwister(sd.uArrC));
	}

}
