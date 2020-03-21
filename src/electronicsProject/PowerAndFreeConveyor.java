package electronicsProject;

import java.util.ArrayList;

class PowerAndFreeConveyor {
    protected enum PFConveyorType { C1, C2, C3, C4, C5, C6, C7, C8 };
    protected PFConveyorType type;
    protected int n;
    protected ArrayList<Pallet> position;  //changed from list to position
    protected int capacity;
    
    /* * 
    
    int[] PowerndFreeConv=new int[8];
    int[] Pos=new int[8];
    int pid;
     
     * */

    PowerAndFreeConveyor(PFConveyorType type){
        this.type = type;
        this.n = 0;
        this.position = new ArrayList<Pallet>();
        this.capacity = 8;
    }
}