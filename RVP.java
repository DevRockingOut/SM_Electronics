class RVP 
{
	static ElectronicsProject model;  // reference to the complete model
		
	// Constructor
	RVP(Seeds sd) 
	{ 
	}

	// returns a Part A which arrived
	public Part uArrA(){ return new Part(Part.PartType.A); }

	// returns a Part B which arrived
	public Part uArrB(){ return new Part(Part.PartType.B); }

	// returns a Part C which arrived
	public Part uArrC(){ return new Part(Part.PartType.C); }

	public long uUnloadLoadTime(){ return 0; }
}
