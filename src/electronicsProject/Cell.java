package electronicsProject;

import electronicsProject.Part.PartType;

class Cell {
	// Attributes
    protected boolean busy;
    protected PartType previousPartType;
    
    // Cell identifier
    protected enum CellID { 
    	// integer value assigned are used as identifier for the Cells
    	// and string values of each enum member are used for logging/debugging purposes
    	C8(0, "C8"), C1(1, "C1"), C2(2, "C2"), C3(3, "C3"), C4(4, "C4"), C5(5, "C5"), C6(6, "C6"), C7(7, "C7");
    	
		private final int value;
		private final String svalue;

	    private CellID(int value, String svalue) {
	        this.value = value;
	        this.svalue = svalue;
	    }

	    public int getInt() {
	        return value;
	    }
	    
	    public String getString() {
	    	return svalue;
	    }
	};
}