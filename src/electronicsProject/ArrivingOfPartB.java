package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartB extends ScheduledAction {

	static ElectronicsProject model; // For referencing the model
	
	@Override
	protected double timeSequence()
	{
		return rvpDuArrB();
	}

	@Override
	protected void actionEvent()
	{
		// ArrivingOfPartB Action Sequence SCS
		Part partB = new Part();
		partB.uType = Part.PartType.B;
		
		int BB = BuffConveyor.BufferType.BB.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BB].n < model.qBuffConveyor[BB].capacity) {
			model.qBuffConveyor[BB].spInsertQue(partB);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			model.qInputConveyor.spInsertQue(partB);
		}else {
			model.output.ssov_nLossB++; // Part is considered lost
		}
		
		if(model.logFlag) {
			String s = "--------------- Part " + partB.uType.toString() + " arrived ---------------\n";
			s += "Clock: " + model.getClock() + "\n";
			s += "Buffer Conveyor " + BuffConveyor.BufferType.BB.getString() + " details: \n";
			s += "batchSize: " + model.batchSize + "  ";
			
			if(model.batchSize == 0) {
				s += "n: 0" + "  capacity: 0\n";
			}else {
				s += "n: " + model.qBuffConveyor[BB].n + "  capacity: " + model.qBuffConveyor[BB].capacity + "\n";
			}
			
			s += "Part " + partB.uType.toString() + " Loss: " + model.output.ssov_nLossB + "\n";
			
			Trace.write(s, "tracePartsArrival.txt", "PartsArrival"); 
		}
	}
	
	static public TriangularVariate delayOfB;
	static MersenneTwister delayPercentageB;
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfB = new TriangularVariate(5,20,55, new MersenneTwister(sd.uArrB));
	    delayPercentageB = new MersenneTwister(sd.delayB);
	}

	// RVP for interarrival times
	static private final double B_DelayPercentage = 0.0175;
	static private final double B_ArrivalTime = 1.4 * 60;
	
	public static double rvpDuArrB() // for getting the next arrival time of Part B
	{
		double nxtTime = 0.0;
		double delayTime = 0.0;	
		if (ArrivingOfPartB.delayPercentageB.nextDouble() < B_DelayPercentage) 
		{
			delayTime = ArrivingOfPartB.delayOfB.next();
		}
		nxtTime = model.getClock() + B_ArrivalTime + delayTime;
		return (nxtTime);
	}
	
}
