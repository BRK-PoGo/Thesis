package problems;

public class XOR implements Problem {
	
	private final int NUM_INPUTS = 2;
	private final int NUM_OUTPUTS = 1;
	private final String PROBLEM_TYPE = "XOR";

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
