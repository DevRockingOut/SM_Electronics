package electronicsProject;

class InputConveyor {
	// Attributes
	protected int n;
    protected Part[] list;  
    protected int capacity;
    
    InputConveyor(int capacity){
        this.n = 0;
        this.capacity = capacity;
        this.list = new Part[capacity];
    }
 
    // Standard procedure: add a part to input conveyor
 	public void spInsertQue(Part part) {
 		list[n] = part;
 		n += 1;
 	}
 	
 	// Standard procedure: remove the part at the head from the input conveyor
 	public Part spRemoveQue() {
 		/*if(n == 0) {
 			return Part.NO_PART;
 		}*/
 		
 		Part part = list[0];
 		for(int i = 0; i < n-1; i++) {
 			list[i] = list[i+1];
 		}
 		
 		n -= 1;
 	
 		return part;
 	}
}