import java.util.ArrayList;

class PowerAndFreeConveyor {
    protected enum PFConveyorType { C1, C2, C3, C4, C5, C6, C7, C8 };
    protected PFConveyorType type;
    int n;
    ArrayList<Pallet> list;
    int capacity;


    PowerAndFreeConveyor(PFConveyorType type){
        this.type = type;
        this.n = 0;
        this.list = new ArrayList<Pallet>();
        this.capacity = 8;
    }
}