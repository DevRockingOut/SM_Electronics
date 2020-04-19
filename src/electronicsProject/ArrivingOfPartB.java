package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartB extends ScheduledAction {

	static ElectronicsProject model;
	static public TriangularVariate delayOfB;
	
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
		
		String s = "--------------- Part " + partB.uType.toString() + " arrived ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Buffer Conveyor " + BuffConveyor.BufferType.BB.getString() + " details: \n";
		s += "batchSize: " + model.batchSize + "  ";
		s += "n: " + model.qBuffConveyor[BB].n + "  capacity: " + model.qBuffConveyor[BB].capacity + "\n";
		s += "Part " + partB.uType.toString() + " Loss: " + model.nLossB + "\n";
		
		Trace.write(s, "tracePartsArrival.txt", "PartsArrival");
	}
	
	static void initRvps(Seeds sd)
	{
	    delayOfB = new TriangularVariate(5,20,55, new MersenneTwister(sd.uArrB));
	}

}
