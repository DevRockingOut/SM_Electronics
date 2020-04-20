package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartA extends ScheduledAction {

	static ElectronicsProject model;  // reference to model object
	static public TriangularVariate delayOfA;
	static MersenneTwister delayPercentageA;
	
	@Override
	protected double timeSequence()
	{
		return RVP.DuArrA();
	}

	@Override
	protected void actionEvent()
	{
		// ArrivingOfPartA of part A Action Sequence SCS
		Part partA = RVP.uArrA();		
		int BA = BuffConveyor.BufferType.BA.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BA].n < model.qBuffConveyor[BA].capacity) {
			model.qBuffConveyor[BA].spInsertQue(partA);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			model.qInputConveyor.spInsertQue(partA);
		}else {
			model.nLossA++;
		}
		
		String s = "--------------- Part " + partA.uType.toString() + " arrived ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Buffer Conveyor " + BuffConveyor.BufferType.BA.getString() + " details: \n";
		s += "batchSize: " + model.batchSize + "  ";
//		s += "n: " + model.qBuffConveyor[BA].n + "  capacity: " + model.qBuffConveyor[BA].capacity + "\n";
		s += "Part " + partA.uType.toString() + " Loss: " + model.nLossA + "\n";
		
		Trace.write(s, "tracePartsArrival.txt", "PartsArrival");
	}
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfA = new TriangularVariate(5,15,60, new MersenneTwister(sd.uArrA));
	    delayPercentageA = new MersenneTwister(sd.delayA);

	}

}
