class Cell {
    private boolean busy;
    private Pallet pallet;
    private boolean automated;
    private Part.PartType previousPartType;

    Cell(){}

    public boolean getBusy(){ return busy; }
    public void setBusy(boolean busy){ this.busy = busy; }

    public Pallet getPallet(){ return pallet; }
    public void setPallet(Pallet pallet){ this.pallet = pallet; }

    public boolean getAutomated(){ return automated; }
    public void setAutomated(boolean automated){ this.automated = automated; }

    public Part.PartType getPreviousPartType(){ return previousPartType; }
    public void setIndex(Part.PartType previousPartType){ this.previousPartType = previousPartType; }

}