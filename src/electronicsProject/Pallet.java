package electronicsProject;

class Pallet {
    protected int index; // [WTF_QUESTION]  why store the position in the conveyor ???
    protected int id; // [WTF_QUESTION] we should store the id otherwise how to use pallets id lol
    protected boolean isMoving;
    protected boolean isProcessed;
    protected Part part;
    protected static final int NO_PALLET_ID = -1; //[UPDATE_CM]
    protected static final Pallet NO_PALLET = null;
}