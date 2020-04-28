package electronicsProject;

public class Output {
	
	static ElectronicsProject model; // For referencing the model
	protected int nLossA = 0;
	protected int nLossB = 0;
	protected int nLossC = 0;
	protected double lostCost = 0.0;
	
	// SSOV
	public double lostCost()
	{
		lostCost = (0.89 * nLossA) + (0.63 * nLossB) + (0.72 * nLossC);
		return lostCost;
	}
	
	public void clearLostCost() {
		nLossA = 0;
		nLossB = 0;
		nLossC = 0;
		lostCost = 0.0;
	}
}