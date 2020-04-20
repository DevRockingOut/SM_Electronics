package electronicsProject;

public class Output {
	/* SSOVs */
	 static ElectronicsProject model;
	
     public double lostCost(int nLossA, int nLossB, int nLossC)
     {
    	 return (0.89 * nLossA) + (0.63 * nLossB) + (0.72 * nLossC);
     }  
}