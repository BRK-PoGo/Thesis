package problems;

public class MNIST implements Problem {

	private final int NUM_INPUTS = 784;
	private final int NUM_OUTPUTS = 10;
	private final String PROBLEM_TYPE = "MNIST";

	@Override
	public int getInputs() {
		return NUM_INPUTS;
	}

	@Override
	public int getOutputs() {
		return NUM_OUTPUTS;
	}

	@Override
	public String getProblem() {
		return PROBLEM_TYPE;
	}

}
