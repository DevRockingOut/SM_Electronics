package electronicsProject;

import electronicsProject.Part.PartType;

class Cell {
    protected boolean busy;
    protected PartType previousPartType;
    
    // assigning integer values to each enum member
    protected enum CellID { 
    	C1(0), C2(1), C3(2), C4(3), C5(4), C6(5), C7(6), C8(7);
    	
		private final int value;

	    private CellID(int value) {
	        this.value = value;
	    }

	    public int getInt() {
	        return value;
	    }
	};
}