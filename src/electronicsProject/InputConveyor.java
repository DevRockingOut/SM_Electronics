package electronicsProject;

import java.util.ArrayList;

class InputConveyor {
	protected int n;
    protected ArrayList<Part> list;
    protected int capacity;
    
    InputConveyor(){
        this.n = 0;
        this.list = new ArrayList<Part>();
        //this.capacity = 40;  // [Initialise class]
    }

    protected int getN() { return this.n; }
}