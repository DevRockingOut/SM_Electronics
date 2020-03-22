package electronicsProject;

import cern.jet.random.engine.RandomSeedGenerator;

public class Seeds
{
	int uArrA;  // part A arrivals
	int uArrB;  // part B arrivals
	int uArrC;  // part C arrivals
	
	int type;   // for types
	int ptM1A;   // process times M1, A
	int ptM1B;   // process times M1, B
	int ptM2;   // process times M2
	int ptM3;   // process times M3

	public Seeds(RandomSeedGenerator rsg)
	{
		uArrA = rsg.nextSeed();
		uArrB = rsg.nextSeed();
		uArrC = rsg.nextSeed();
//		cArr=rsg.nextSeed();
//		type=rsg.nextSeed();
//		ptM1A=rsg.nextSeed();
//		ptM1B=rsg.nextSeed();
//		ptM2=rsg.nextSeed();
//		ptM3=rsg.nextSeed();
	}
}
