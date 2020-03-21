package electronicsProject;

import java.util.ArrayList;

class BuffConveyor {
    int n;
    ArrayList<Part> list;
    int capacity;
    Part conveyorPartType;
    
    BuffConveyor(Part type){
        this.n = 0;
        this.list = new ArrayList<Part>();
        this.capacity = 10;
        this.conveyorPartType= type;
    }

}