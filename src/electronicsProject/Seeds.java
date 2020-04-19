package electronicsProject;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds
{
	int uArrA;  // part A arrivals
	int uArrB;  // part B arrivals
	int uArrC;  // part C arrivals
	
	int type;   // for types
	int ptC2A;  // process times C2, A
	int ptC2B;  // process times C2, B
	int ptC2C;  // process times C2, C
	int ptC7A;  // process times C7, A
	int ptC7B;  // process times C7, B
	int ptC7C;  // process times C7, C
	int ultC8;  // unload/Load times C8
	int jamC8;  // Jam at cell 8

	public Seeds(RandomSeedGenerator rsg)
	{
		uArrA = rsg.nextSeed();
		uArrB = rsg.nextSeed();
		uArrC = rsg.nextSeed();
		ptC2A=rsg.nextSeed();
		ptC2B=rsg.nextSeed();
		ptC2C=rsg.nextSeed();
		ptC7A=rsg.nextSeed();
		ptC7B=rsg.nextSeed();
		ptC7C=rsg.nextSeed();
		ultC8=rsg.nextSeed();
		jamC8=rsg.nextSeed();
	}
}
