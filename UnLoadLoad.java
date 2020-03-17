package conveyorProject;

import simulationModelling.ConditionalAction;

class UnLoadLoad extends ConditionalAction
{
	static ElectronicsProject model;

	public static boolean precondition(ElectronicsProject md)
	{
		boolean retVal = false;
		if(conveyorReadyForComp() != Constants.NONE) retVal = true;
		return(retVal);
	}
	@Override
	public void actionEvent()
	{
		int qid = conveyorReadyForComp();
	    model.qConveyors[qid].spInsertQue(model.rMachines[Constants.M1].component);
	    model.rMachines[Constants.M1].component = Machines.NO_COMP;
	}
	
	// UDP
	// Conveyor M2 or M3 is ready to receive the component in Machine M1
	 static protected int conveyorReadyForComp()
	 {
		 int convId = Constants.NONE;
		 // Check all conveyors
		 if(!model.rMachines[Constants.M1].busy && model.rMachines[Constants.M1].component != Machines.NO_COMP)
		 {
			 // Check conveyor to Machine M2
			 if(model.rMachines[Constants.M1].component.uType == Component.CompType.A &&
			    model.qConveyors[Constants.M2].getN() < model.qConveyors[Constants.M2].length)  convId = Constants.M2;
			 // Check Conveyor to Machine M3
			 if(model.rMachines[Constants.M1].component.uType == Component.CompType.B &&
			    model.qConveyors[Constants.M3].getN() < model.qConveyors[Constants.M3].length)  convId = Constants.M3;			 
		 }
		 return(convId);
	 }
	 

}
