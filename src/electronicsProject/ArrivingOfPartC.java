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
	
		String s = "--------------- Part " + partC.uType.toString() + " arrived ---------------\n";
		s += "Clock: " + model.getClock() + "\n";
		s += "Buffer Conveyor " + BuffConveyor.BufferType.BC.getString() + " details: \n";
		s += "batchSize: " + model.batchSize + "  ";
		s += "n: " + model.qBuffConveyor[BC].n + "  capacity: " + model.qBuffConveyor[BC].capacity + "\n";
		s += "Part " + partC.uType.toString() + " Loss: " + model.nLossC + "\n";
		
		Trace.write(s, "tracePartsArrival.txt", "PartsArrival");
	}
	
	static void initRvps(Seeds sd)
	{
	    delayOfC = new TriangularVariate(5,20,65, new MersenneTwister(sd.uArrC));
	}

}
