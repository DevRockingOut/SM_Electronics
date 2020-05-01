package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartC extends ScheduledAction {

	static ElectronicsProject model; // For referencing the model
	
	@Override
	protected double timeSequence()
	{
		return rvpDuArrC();
	}

	@Override
	protected void actionEvent()
	{
		// ArrivingOfPartC Action Sequence SCS
		Part partC = new Part();
		partC.uType = Part.PartType.C;
		
		int BC = BuffConveyor.BufferType.BC.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BC].n < model.qBuffConveyor[BC].capacity) {
			model.qBuffConveyor[BC].spInsertQue(partC);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			model.qInputConveyor.spInsertQue(partC);
		}else {
			model.output.ssov_nLossC++; // Part is considered lost
		}
	
		if(model.logFlag) {
			String s = "--------------- Part " + partC.uType.toString() + " arrived ---------------\n";
			s += "Clock: " + model.getClock() + "\n";
			s += "Buffer Conveyor " + BuffConveyor.BufferType.BC.getString() + " details: \n";
			s += "batchSize: " + model.batchSize + "  ";
			if(model.batchSize == 0) {
				s += "n: 0" + "  capacity: 0\n";
			}else {
				s += "n: " + model.qBuffConveyor[BC].n + "  capacity: " + model.qBuffConveyor[BC].capacity + "\n";
			}
			s += "Part " + partC.uType.toString() + " Loss: " + model.output.ssov_nLossC + "\n";
			
			Trace.write(s, "tracePartsArrival.txt", "PartsArrival"); 
		}
	}
	
	static public TriangularVariate delayOfC;
	static MersenneTwister delayPercentageC;
	
	static void initRvps(Seeds sd)
	{	
		// Initialise Internal modules, user modules and input variables
	    delayOfC = new TriangularVariate(5,20,65, new MersenneTwister(sd.uArrC));
	    delayPercentageC = new MersenneTwister(sd.delayC);

	}

	// RVP for interarrival times
	static private final double C_DelayPercentage = 0.005;
	static private final double C_ArrivalTime = 2.00 * 60;
	
	public static double rvpDuArrC() // for getting the next arrival time of Part C
	{
		double nxtTime = 0.0;
		double delayTime = 0.0;	
		if (ArrivingOfPartC.delayPercentageC.nextDouble() < C_DelayPercentage) 
		{
			delayTime = ArrivingOfPartC.delayOfC.next();
		}
		nxtTime = model.getClock() + C_ArrivalTime + delayTime;
		return (nxtTime);
	}
}
