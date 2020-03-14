
class Pallet {
    private int index2; 
    private boolean isMoving;
    private boolean isProcessed;
    private Part part;
    protected static final Pallet NO_PALLET = null;

    Pallet(){}

    public int getIndex(){ return index; }
    public void setIndex(int index){ this.index = index; }


    public boolean getIsMoving(){ return isMoving; }
    public void setIsMoving(boolean isMoving){ this.isMoving = isMoving; }

    public boolean getIsProcessed(){ return isProcessed; }
    public void setIsProcessed(boolean isProcessed){ this.isProcessed = isProcessed; }

    public Part getPart(){ return part; }
    public void setPart(Part part){ this.part = part; }
}