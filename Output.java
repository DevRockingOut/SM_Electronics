
public class Output {

     private int nLossA;
     private int nLossB;
     private int nLossC;
     private double lostCost;
     
     public double getLostCost(int nLossA, int nLossB, int nLossC)
     {
    	 return lostCost=(0.89 * nLossA) + (0.63 * nLossB) + (0.72 * nLossC);
     }  
	}


