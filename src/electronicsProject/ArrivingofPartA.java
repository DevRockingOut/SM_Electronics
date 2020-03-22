package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartA extends ScheduledAction {

	static ElectronicsProject model;
	static public TriangularVariate delayOfA;
	
	
	@Override
	protected double timeSequence()
	{
		return RVP.DuArrA();
	}

	@Override
	protected void actionEvent()
	{
		Part partA = RVP.uArrA();
		
		int BA = BuffConveyor.BufferType.BA.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BA].n < model.qBuffConveyor[BA].capacity) {
			SP.spInsertQue(model.qBuffConveyor[BA], partA);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			SP.spInsertQue(model.qInputConveyor, partA);
		}else {
			model.nLossA++;
		}
	}
	
	static void initRvps(Seeds sd)
	{
	    delayOfA = new TriangularVariate(5,15,60, new MersenneTwister(sd.uArrA));
	}

}
