package electronicsProject;

import java.util.ArrayList;

class PowerAndFreeConveyor {
    protected enum PFConveyorType { C1, C2, C3, C4, C5, C6, C7, C8 };
    protected PFConveyorType type;  // [Update_CM]   I think we should add this attribute to CM
    protected ArrayList<Pallet> position; // [WTF_QUESTION]    This should store pallet id not the pallets themselves
    
    /* * 
    int[] Pos=new int[8];
    int pid;
     * */

    PowerAndFreeConveyor(){
        this.position = new ArrayList<Pallet>();
    }
}