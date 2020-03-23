package electronicsProject;

import java.util.ArrayList;
import electronicsProject.Part.PartType;

class BuffConveyor {
	protected int n;
    protected ArrayList<Part> list;
    protected int capacity;
    protected PartType conveyorPartType; // ?? [WTF_QUESTION]   I think we should store bufferType instead
    protected BufferType type;
    protected static final BuffConveyor NO_BUFF_CONVEYOR = null;
    protected static final BufferType BUFF_TYPE_NONE = null;
    
    // assigning integer values to each enum member
    protected enum BufferType { 
    	BA(0, "BA"), BB(1, "BB"), BC(2, "BC");
    	
		private final int value;
		private final String svalue;

	    private BufferType(int value, String svalue) {
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
    
    BuffConveyor(){
        this.n = 0;
        this.list = new ArrayList<Part>();
        //this.capacity = 10;            [Initialise class]
        //this.conveyorPartType = type;  [Initialise class]
    }

}