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
    
    // assigning integer values to each enum member
    protected enum BufferType { 
    	BA(0), BB(1), BC(2);
    	
		private final int value;

	    private BufferType(int value) {
	        this.value = value;
	    }

	    public int getInt() {
	        return value;
	    }
	};
    
    BuffConveyor(){
        this.n = 0;
        this.list = new ArrayList<Part>();
        //this.capacity = 10;            [Initialise class]
        //this.conveyorPartType = type;  [Initialise class]
    }

}