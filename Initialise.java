//import simulationModelling.ScheduledAction;

class Initialise //extends ScheduledAction
{
	static ElectronicsProject model;
	
	double [] ts = { 0.0, -1.0 }; // -1.0 ends scheduling
	int tsix = 0;  // set index to first entry.
	public double timeSequence() 
	{
		return ts[tsix++];  // only invoked at t=0
	}

	public void actionEvent() 
	{
		
	}
}
