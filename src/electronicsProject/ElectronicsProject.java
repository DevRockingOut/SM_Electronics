package electronicsProject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;

import simulationModelling.AOSimulationModel;
import simulationModelling.Behaviour;
import simulationModelling.SBNotice;

public class ElectronicsProject extends AOSimulationModel
{
	// Entities
	BuffConveyor[] qBuffConveyor;
	InputConveyor qInputConveyor = new InputConveyor(40);
	PowerAndFreeConveyor[] rqPowerAndFreeConveyor = new PowerAndFreeConveyor[8];
	Pallet[] rcPallet;
	Cell[] rCell = new Cell[8];
	
	// Parameters
	int numPallets;
	int batchSize;
	
	// Random variate procedures
	RVP rvp;
	
	// User Defined Procedures
	UDP udp = new UDP();
	
	// Output object
    Output output = new Output();
    
    // SSOV
    int nLossA = 0;
	int nLossB = 0;
	int nLossC = 0;
	
    public double getLostCost()
    {
    	return output.lostCost(nLossA, nLossB, nLossC);
    }
   
    // Constructor
	public ElectronicsProject(double tftime, int _numPallets, int _batchSize, Seeds sd, boolean log)
	{
		// Adding references to model object to classes
		initialiseClasses(sd);
		
		// For turning on logging
		logFlag = log;
		
		// Set up RVPs
		rvp = new RVP();
		
		// Initialise parameters
		numPallets = _numPallets;
		batchSize = _batchSize;
		
		rcPallet = new Pallet[numPallets];
		
		if(batchSize > 0) {
			qBuffConveyor = new BuffConveyor[3];
		}
		
		this.initAOSimulModel(0, tftime);
		System.out.println("Simulation endtime: " + tftime + "\n");
		
		// Schedule Initialise action
		Initialise init = new Initialise();
		scheduleAction(init);  // Should always be first one scheduled.
		
		// Start arrivals
		ArrivingOfPartA arrPA = new ArrivingOfPartA();
		scheduleAction(arrPA);
		
		ArrivingOfPartB arrPB = new ArrivingOfPartB();
		scheduleAction(arrPB);
		
		ArrivingOfPartC arrPC = new ArrivingOfPartC();
		scheduleAction(arrPC);
	}
	
	// Initialize static components of model classes
	void initialiseClasses(Seeds sd)
	{
		// Add reference to standard classes
		Initialise.model = this;
		Output.model = this;
		RVP.model = this;
		UDP.model = this;
		
		// Add reference to activity/action classes
		ArrivingOfPartA.model = this;
		ArrivingOfPartB.model = this;
		ArrivingOfPartC.model = this;
		BatchRelease.model = this;
		MovePallets.model = this;
		UnloadLoad.model = this;
		Processing.model = this;
		
		// Initialize RVPs in the classes
		ArrivingOfPartA.initRvps(sd);
		ArrivingOfPartB.initRvps(sd);
		ArrivingOfPartC.initRvps(sd);
		Processing.initRvp(sd);
		UnloadLoad.initRvp(sd);
	}
	
	@Override
	public void testPreconditions(Behaviour behObj)
	{
		reschedule(behObj);
		//while (scanPreconditions() == true) /* repeat */;
		scanPreconditions();
	}

	// Single scan of all preconditions
	// Returns true if at least one precondition was true
	private boolean scanPreconditions()
	{
		boolean statusChanged = false;
		
		// Conditional Actions
		if (BatchRelease.precondition(this) == true)
		{
			BatchRelease act = new BatchRelease(); // Generate instance
			act.actionEvent();
			statusChanged = true;
		}
		
		// Conditional Activities
		if(UnloadLoad.precondition() == true)
		{
			UnloadLoad act = new UnloadLoad(); // Generate instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
		
	    if (Processing.precondition() == true)
		{
			Processing act = new Processing(); // Generate instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
		
		
		if (MovePallets.precondition() == true)
		{
			MovePallets act = new MovePallets(); // Generate instance
			act.startingEvent();
			scheduleActivity(act);
			statusChanged = true;
		}
	
		
		//System.out.println(getTime0());
		
		statusChanged = false;
		return (statusChanged);
		
	}
		
	public void eventOccured()
	{		
		//output.updateSequences(); // for updating trajectory sets	
		if(logFlag) printDebug();
	}
	
	// for Debugging
	boolean logFlag = false;
	protected void printDebug()
	{
		// Debugging
		
		String s = "";
		s += "\n";
		s += "Clock =  " + getClock();
		s += "\n\n";
		
		// Buffer Conveyors
		if(batchSize > 0) {
			s += "   Buffer Conveyors:\n";
			for(int i = 0; i < qBuffConveyor.length; i++) {
				if(qBuffConveyor[i] != null) {
					if(i > 0) {
						s += ", ";
					}
					s += "   Q.BuffConveyor[" + BuffConveyor.BufferType.values()[i] + "].n = " + qBuffConveyor[i].n;
				}
			}
		}
	
		if(batchSize == 0) {
			s += "   Buffer Conveyors:\n";
			s += "   Q.BuffConveyor[BA].n = " + "0";
			s += "   ,  Q.BuffConveyor[BB].n = " + "0";
			s += "   ,  Q.BuffConveyor[BC].n = " + "0";
		}
		
		// Input Conveyor
		s += "\n\n";
		s += "   Q.InputConveyor.n= " + qInputConveyor.n + "\n";
		s += "   Q.InputConveyor: ";
		
		if (qInputConveyor.n == 0) {
			s += "The Input Conveyor is empty.";
		}else {
			for(int i = 0; i < qInputConveyor.n; i++) {
			s += qInputConveyor.list[i].uType.toString() + " ";
			}
			}
		
		s += "\n\n";
		s += "   " + "RQ.PowerAndFreeConveyor:";
		s += "\n" + "   " +  "----------------------------------------------------------------------------------------------------------------------------------";
		s += "\n";
		
		// Power-and-free conveyors
		for(int i = 1; i < rqPowerAndFreeConveyor.length; i++) {
			s +=  "   " + Cell.CellID.values()[i].toString() + ": ";
			
			for(int j = 0; j < rqPowerAndFreeConveyor[i].position.length; j++) {
				int pid = rqPowerAndFreeConveyor[i].position[j];
				
				if(pid == Pallet.NO_PALLET_ID) {
					s += "(NP, N/A, N/A)";
				}else {
					if(rcPallet[pid].part != Part.NO_PART) {
						if(rcPallet[pid].isMoving) {
							s += "(" + pid + ", T, " + rcPallet[pid].part.uType + ")"; 
						}else {
							s += "(" + pid + ", F, " + rcPallet[pid].part.uType + ")"; 
						}
					}else {
						if(rcPallet[pid].isMoving) {
							s += "(" + pid + ", T, N/A)"; 
						}else {
							s += "(" + pid + ", F, N/A)"; 
						}
					}
				}
				}
			s += "\n" + "   " +  "----------------------------------------------------------------------------------------------------------------------------------";
			s += "\n";
		}
		
		s +=  "   " + Cell.CellID.values()[0].toString() + ": ";
		
		for(int j = 0; j < rqPowerAndFreeConveyor[0].position.length; j++) {
			int pid = rqPowerAndFreeConveyor[0].position[j];
			
			if(pid == Pallet.NO_PALLET_ID) {
				s += "(NP, N/A, N/A)";
			}else {
				if(rcPallet[pid].part != Part.NO_PART) {
					if(rcPallet[pid].isMoving) {
						s += "(" + pid + ", T, " + rcPallet[pid].part.uType + ")"; 
					}else {
						s += "(" + pid + ", F, " + rcPallet[pid].part.uType + ")"; 
					}
				}else {
					if(rcPallet[pid].isMoving) {
						s += "(" + pid + ", T, N/A)"; 
					}else {
						s += "(" + pid + ", F, N/A)"; 
					}
				}
			}

		}
		
		// Cells
		s += "\n" + "   " +  "----------------------------------------------------------------------------------------------------------------------------------";
		s += "\n\n";
		s += "   R.Cell:";
		s += "\n";
		for(int j = 1; j < rCell.length; j++) {
			s += "   " + Cell.CellID.values()[j].toString() + ":   ";
			
			if (rCell[j].busy) {
				s += "(" + "T" + ", ";
			}else {
				s += "(" + "F" + ", ";
			}
			
			if(rCell[j].previousPartType != null) {
				s += rCell[j].previousPartType + ")\n";
			}else {
				s += "N/A)\n";
			}
		}
		
		s +=  "   " + Cell.CellID.values()[0].toString() + ":   ";
		if (rCell[0].busy) {
			s += "(" + "T" + ", ";
		}else {
			s += "(" + "F" + ", ";
		}
		
		if(rCell[0].previousPartType != null) {
			s += rCell[0].previousPartType + ")\n";
		}else {
			s += "N/A)\n";
		}
		
		// Lost Cost
		s += "\n" + "   " +  "----------------------------------------------------------------------------------------------------------------------------------";
		s += "\n";
		s += "   " + "Lost Cost = " + String.format("%.2f", getLostCost());
		s += "\n" + "   " +  "----------------------------------------------------------------------------------------------------------------------------------";
		s += "\n\n";
		
		System.out.println(s);
		showSBL();
		Trace.write(s, "log.txt", "Experiment");
	}

}
