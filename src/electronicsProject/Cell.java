package electronicsProject;

import electronicsProject.Part.PartType;

class Cell {
    protected boolean busy;
    protected PartType previousPartType;
    
    // assigning integer values to each enum member
    protected enum CellID { 
    	C8(0), C1(1), C2(2), C3(3), C4(4), C5(5), C6(6), C7(7);
    	
		private final int value;

	    private CellID(int value) {
	        this.value = value;
	    }

	    public int getInt() {
	        return value;
	    }
	};
}