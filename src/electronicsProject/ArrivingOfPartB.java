package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartB extends ScheduledAction {

	static ElectronicsProject model; // reference to model object
	static public TriangularVariate delayOfB;
	static MersenneTwister delayPercentageB;
	
	@Override
	protected double timeSequence()
	{
		return RVP.DuArrB();
	}

	@Override
	protected void actionEvent()
	{
		// ArrivingOfPartB of part B Action Sequence SCS
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
	//	s += "n: " + model.qBuffConveyor[BB].n + "  capacity: " + model.qBuffConveyor[BB].capacity + "\n";
		s += "Part " + partB.uType.toString() + " Loss: " + model.nLossB + "\n";
		
		Trace.write(s, "tracePartsArrival.txt", "PartsArrival");
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfB = new TriangularVariate(5,20,55, new MersenneTwister(sd.uArrB));
	    delayPercentageB = new MersenneTwister(sd.delayB);
	}

}
