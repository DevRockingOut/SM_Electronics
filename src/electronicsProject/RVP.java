package electronicsProject;

import cern.jet.random.engine.MersenneTwister;
import dataModelling.TriangularVariate;

class RVP 
{
	static ElectronicsProject model;  // reference to the complete model
		
	// Constructor
	RVP(Seeds sd) 
	{ 
	}

	// returns a Part A which arrived
	public static Part uArrA(){ 
		Part partA = new Part();
		partA.uType = Part.PartType.A;
		return partA;
	}

	// returns a Part B which arrived
	public static Part uArrB(){ 
		Part partB = new Part();
		partB.uType = Part.PartType.B;
		return partB;
	}

	// returns a Part C which arrived
	public static Part uArrC(){ 
		Part partC = new Part();
		partC.uType = Part.PartType.C;
		return partC;
	}
	
	// RVP for interarrival times.
	public static double DuArrA()
	{
	   double nxtTime = 0.0;	   
	   nxtTime = model.getClock() + (2.8*60) + ArrivingOfPartA.delayOfA.next();
	   return (nxtTime);
	}
	
	// RVP for interarrival times.
	public static double DuArrB()
	{
	   double nxtTime = 0.0;	   
	   nxtTime = model.getClock() + (1.4*60) + ArrivingOfPartB.delayOfB.next();
	   return (nxtTime);
	}
	
	// RVP for interarrival times.
	public static double DuArrC()
	{
	   double nxtTime = 0.0;	   
	   nxtTime = model.getClock() + (2.0*60) + ArrivingOfPartC.delayOfC.next();
	   return (nxtTime);
	}

	public static long uUnloadLoadTime(){ return 0; }
}
