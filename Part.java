 class Part {
   protected enum PartType { A, B, C };
   protected PartType uType;
   protected static final Part NO_PART = null;

   Part(PartType type){
      this.uType = type;
   }
}
