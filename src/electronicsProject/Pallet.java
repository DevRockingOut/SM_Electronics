package electronicsProject;

class Pallet {
    protected int id;
    protected boolean isMoving;
    protected boolean isProcessed;
    protected Part part;
    protected static final int NO_PALLET_ID = -1;
    protected static final Pallet NO_PALLET = null;
}