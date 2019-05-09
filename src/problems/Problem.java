package problems;

public interface Problem {

	public int getInputs();
	public int getOutputs();
	public String getProblem();
	public int getIterations();
	public double getError();
	public double getLearningRate();
	public double getMomentum();
	public int[] getBaseMember();
	public int getBaseMemberNeurons();
	public int getPop();
}
