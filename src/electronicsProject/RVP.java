package electronicsProject;

class RVP 
{
	static ElectronicsProject model;  // reference to the complete model

	
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
	
	// RVP for interarrival times
		static private final double A_DelayPercentage = 0.02;
		static private final double A_ArrivalTime = 2.8 * 60;
		public static double DuArrA()
		{
		   double nxtTime = 0.0;
		   double delayTime = 0.0; 
		   if (ArrivingOfPartA.delayPercentageA.nextInt() < A_DelayPercentage)
		   {
			   delayTime = ArrivingOfPartA.delayOfA.next();
		   }
		   nxtTime = model.getClock() + (A_ArrivalTime) + delayTime;
		   return (nxtTime);
		}
		
		// RVP for interarrival times
		static private final double B_DelayPercentage = 0.0175;
		static private final double B_ArrivalTime = 1.4 * 60;
		public static double DuArrB()
		{
			double nxtTime = 0.0;
			double delayTime = 0.0;	
			if (ArrivingOfPartB.delayPercentageB.nextInt() < B_DelayPercentage) 
			{
				delayTime = ArrivingOfPartB.delayOfB.next();
			}
			nxtTime = model.getClock() + (B_ArrivalTime) + delayTime;
			return (nxtTime);
		}

		// RVP for interarrival times
		static private final double C_DelayPercentage = 0.005;
		static private final double C_ArrivalTime = 2.00 * 60;
		public static double DuArrC()
		{
			double nxtTime = 0.0;
			double delayTime = 0.0;	
			if (ArrivingOfPartC.delayPercentageC.nextInt() < C_DelayPercentage) 
			{
				delayTime = ArrivingOfPartC.delayOfC.next();
			}
			nxtTime = model.getClock() + (C_ArrivalTime) + delayTime;
			return (nxtTime);
		}

}
