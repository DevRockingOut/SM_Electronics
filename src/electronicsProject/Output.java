package electronicsProject;

public class Output {
	
	static ElectronicsProject model; // For referencing the model
	protected int ssov_nLossA = 0;
	protected int ssov_nLossB = 0;
	protected int ssov_nLossC = 0;
	protected double ssov_lostCost = 0.0;
	
	// SSOV
	public double lostCost()
	{
		ssov_lostCost = (0.89 * ssov_nLossA) + (0.63 * ssov_nLossB) + (0.72 * ssov_nLossC);
		return ssov_lostCost;
	}
	
	public void clearLostCost() {
		ssov_nLossA = 0;
		ssov_nLossB = 0;
		ssov_nLossC = 0;
		ssov_lostCost = 0.0;
	}
}