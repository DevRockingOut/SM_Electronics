package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;
import simulationModelling.ScheduledAction;

class ArrivingOfPartA extends ScheduledAction {

	static ElectronicsProject model;  // For referencing the model
	
	@Override
	protected double timeSequence()
	{
		return rvpDuArrA();
	}

	@Override
	protected void actionEvent()
	{
		// ArrivingOfPartA Action Sequence SCS
		Part partA = new Part();
		partA.uType = Part.PartType.A;
		
		int BA = BuffConveyor.BufferType.BA.getInt();
		
		if(model.batchSize != 0 && model.qBuffConveyor[BA].n < model.qBuffConveyor[BA].capacity) {
			model.qBuffConveyor[BA].spInsertQue(partA);
		}else if(model.batchSize == 0 && model.qInputConveyor.n < model.qInputConveyor.capacity) {
			model.qInputConveyor.spInsertQue(partA);
		}else {
			model.output.ssov_nLossA++; // Part is considered lost
		}
		
		if(model.logFlag) {
			String s = "--------------- Part " + partA.uType.toString() + " arrived ---------------\n";
			s += "Clock: " + model.getClock() + "\n";
			s += "Buffer Conveyor " + BuffConveyor.BufferType.BA.getString() + " details: \n";
			s += "batchSize: " + model.batchSize + "  ";
			if(model.batchSize == 0) {
				s += "n: 0" + "  capacity: 0\n";
			}else {
				s += "n: " + model.qBuffConveyor[BA].n + "  capacity: " + model.qBuffConveyor[BA].capacity + "\n";
			}
			s += "Part " + partA.uType.toString() + " Loss: " + model.output.ssov_nLossA + "\n";
			
			Trace.write(s, "tracePartsArrival.txt", "PartsArrival"); 
		}

	}
	
	static public TriangularVariate delayOfA;
	static MersenneTwister delayPercentageA;
	
	static void initRvps(Seeds sd)
	{
		// Initialise Internal modules, user modules and input variables
	    delayOfA = new TriangularVariate(5,15,60, new MersenneTwister(sd.uArrA));
	    delayPercentageA = new MersenneTwister(sd.delayA);

	}
	
	// RVP for interarrival times
	static private final double A_DelayPercentage = 0.02;
	static private final double A_ArrivalTime = 2.8 * 60;
	
	public static double rvpDuArrA()  // for getting the next arrival time of Part A
	{
	   double nxtTime = 0.0;
	   double delayTime = 0.0; 
	   if (ArrivingOfPartA.delayPercentageA.nextDouble() < A_DelayPercentage)
	   {
		   delayTime = ArrivingOfPartA.delayOfA.next();
	   }
	   nxtTime = model.getClock() + A_ArrivalTime + delayTime;
	   return (nxtTime);
	}

}
