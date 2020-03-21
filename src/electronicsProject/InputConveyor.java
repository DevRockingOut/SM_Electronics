package electronicsProject;

import java.util.ArrayList;

class InputConveyor {
	
	protected enum IPConveyorType { A, B, C };
    protected int n;
    protected ArrayList<Part> list;
    protected int capacity;
    
    InputConveyor(){
        this.n = 0;
        this.list = new ArrayList<Part>();
        this.capacity = 40;
    }

}