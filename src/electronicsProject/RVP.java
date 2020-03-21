package electronicsProject;

class RVP 
{
	static ElectronicsProject model;  // reference to the complete model
		
	// Constructor
	RVP(Seeds sd) 
	{ 
	}

	// returns a Part A which arrived
	public Part uArrA(){ 
		Part partA = new Part();
		partA.uType = Part.PartType.A;
		return partA;
	}

	// returns a Part B which arrived
	public Part uArrB(){ 
		Part partB = new Part();
		partB.uType = Part.PartType.B;
		return partB;
	}

	// returns a Part C which arrived
	public Part uArrC(){ 
		Part partC = new Part();
		partC.uType = Part.PartType.C;
		return partC;
	}

	public long uUnloadLoadTime(){ return 0; }
}
