package electronicsProject;

class Pallet {
    protected int index; // [WTF_QUESTION]  Isn't this the id of the Pallet OR the position in the conveyor ???
    protected boolean isMoving;
    protected boolean isProcessed;
    protected Part part;
    protected static final Pallet NO_PALLET = null;
}