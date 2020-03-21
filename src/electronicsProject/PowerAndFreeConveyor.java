package electronicsProject;

import java.util.ArrayList;

class PowerAndFreeConveyor {
    protected PFConveyorType type;  // [Update_CM]   I think we should add this attribute to CM
    protected int[] position = new int[9];
    //protected ArrayList<Integer> position; // [WTF_QUESTION]    This should store pallet id not the pallets themselves
    
    /** [WTF_QUESTION]   I think no capacity attribute here because power-and-free conveyors contain always 9 pallets **/
    
    // assigning integer values to each enum member
    protected enum PFConveyorType { 
    	C8(0), C1(1), C2(2), C3(3), C4(4), C5(5), C6(6), C7(7);
    	
		private final int value;

	    private PFConveyorType(int value) {
	        this.value = value;
	    }

	    public int getInt() {
	        return value;
	    }
	};
}