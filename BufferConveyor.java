import java.util.ArrayList;

class BufferConveyor {

    protected enum BufferType { BA, BB, BC };
    protected Part.PartType conveyorPartType;
    int n;
    ArrayList<Part> list;
    int capacity;

    BufferConveyor(Part.PartType conveyorPartType){
        this.list = new ArrayList<Part>();
        this.n = 0;
        this.capacity = 10;
        this.conveyorPartType = conveyorPartType;
    }
    
}