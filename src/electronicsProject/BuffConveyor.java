package electronicsProject;

import java.util.ArrayList;
import electronicsProject.Part.PartType;

class BuffConveyor {
	protected enum BufferType { BA, BB, BC };
	protected int n;
    protected ArrayList<Part> list;
    protected int capacity;
    protected PartType conveyorPartType; // ?? [WTF_QUESTION]   I think we should store bufferType instead
    protected static final BuffConveyor NO_BUFF_CONVEYOR = null;
    
    BuffConveyor(){
        this.n = 0;
        this.list = new ArrayList<Part>();
        //this.capacity = 10;            [Initialise class]
        //this.conveyorPartType = type;  [Initialise class]
    }

}