package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartC extends ScheduledAction {

	static ElectronicsProject model;
	static public TriangularVariate delayOfC;
	
	
	@Override
	protected double timeSequence()
	{
		return RVP.DuArrC();
	}

	@Override
	protected void actionEvent()
	{
		Part partC = RVP.uArrC();
		
		int BC = BuffConveyor.BufferType.BC.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BC].n < model.qBuffConveyor[BC].capacity) {
			SP.spInsertQue(model.qBuffConveyor[BC], partC);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			SP.spInsertQue(model.qInputConveyor, partC);
		}else {
			model.nLossC++;
		}
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfC = new TriangularVariate(5,20,65, new MersenneTwister(sd.uArrC));
	}

}
