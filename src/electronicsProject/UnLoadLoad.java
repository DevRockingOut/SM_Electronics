package electronicsProject;

import simulationModelling.Activity;

class UnLoadLoad extends Activity
{
	static ElectronicsProject model;

	public static boolean precondition(ElectronicsProject md)
	{
		boolean retVal = false;
		//if(conveyorReadyForComp() != BuffConveyor.NO_BUFF_CONVEYOR) retVal = true;
		return(retVal);
	}

	
	// UDP
	// Conveyor M2 or M3 is ready to receive the component in Machine M1
	 static protected int conveyorReadyForComp()
	 {
		 //int convId = Constants.NONE;
		 // Check all conveyors
		 /*if(!model.rMachines[Constants.M1].busy && model.rMachines[Constants.M1].component != Machines.NO_COMP)
		 {
			 // Check conveyor to Machine M2
			 if(model.rMachines[Constants.M1].component.uType == Component.CompType.A &&
			    model.qConveyors[Constants.M2].getN() < model.qConveyors[Constants.M2].length)  convId = Constants.M2;
			 // Check Conveyor to Machine M3
			 if(model.rMachines[Constants.M1].component.uType == Component.CompType.B &&
			    model.qConveyors[Constants.M3].getN() < model.qConveyors[Constants.M3].length)  convId = Constants.M3;			 
		 }*/
		 return(0);//convId);
	 }
	@Override
	protected double duration() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void startingEvent() {
		// TODO Auto-generated method stub
		int qid = conveyorReadyForComp();
	    //model.qBuffConveyor[qid].spInsertQue(model.rMachines[Constants.M1].component);
	    //model.rMachines[Constants.M1].component = Machines.NO_COMP;
	}
	@Override
	protected void terminatingEvent() {
		// TODO Auto-generated method stub
		
	}
	 

}
