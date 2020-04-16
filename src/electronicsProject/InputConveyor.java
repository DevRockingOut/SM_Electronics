package electronicsProject;

import java.util.ArrayList;


class InputConveyor {
	protected int n;
    protected Part[] list;  
    protected int capacity;
    
    InputConveyor(int capacity){
        this.n = 0;
        this.capacity = capacity;
        this.list = new Part[capacity];
    }
 
    // add a part to input conveyor
 	public void spInsertQue(Part part) {
 		list[n] = part;
 		n += 1;
 	}
 	
 	// remove the part at the head from the input conveyor
 	public Part spRemoveQue() {
 		if(n == 0) {
 			return Part.NO_PART;
 		}
 		
 		Part part = list[0];
 		//list[0] = null;
 		n -= 1;
 	
 		return part;
 	}
}