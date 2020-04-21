package electronicsProject;

class BuffConveyor {
	// Attributes
	protected int n;
    protected Part[] list;
    protected int capacity;
    protected BufferType type;
    protected static final BuffConveyor NO_BUFF_CONVEYOR = null;  // Value for BuffConveyor
    protected static final BufferType BUFF_TYPE_NONE = null;     // Value for BufferType
    
    // Buffer Conveyor identifier
    protected enum BufferType { 
    	// assigning integer and string values to each enum member
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
	}
    
    BuffConveyor(int capacity){
        this.capacity = capacity;
        this.list = new Part[capacity];
    }
    
    // Standard procedure: add a part to buffer conveyor
 	public void spInsertQue(Part part) {
 		list[n] = part;
 		n += 1;
 	}
    
    // Standard procedure: remove the part at the head from the buffer conveyor
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