package electronicsProject;

import java.util.ArrayList;
import electronicsProject.Part.PartType;

class BuffConveyor {
	protected int n;
    protected Part[] list;
    protected int capacity;
    protected PartType conveyorPartType;
    protected static final BuffConveyor NO_BUFF_CONVEYOR = null;
   
    
    BuffConveyor(int capacity){
        this.capacity = capacity;
        this.list = new Part[capacity];
    }
    
    // add a part to buffer conveyor
 	public void spInsertQue(Part part) {
 		list[n] = part;
 		n += 1;
 	}
    
    // remove the part at the head from the buffer conveyor
 	public Part spRemoveQue() {
 		if(n == 0) {
 			return Part.NO_PART;
 		}
 	
 		Part part = list[0];
 		for(int i = 0; i < n-1; i++) {
 			list[i] = list[i+1];
 		}

 		n -= 1;
 	
 		return part;
 	}

}