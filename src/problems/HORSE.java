package problems;

public class HORSE implements Problem {
	
	private final int NUM_INPUTS = 20;
	private final int NUM_OUTPUTS = 3;
	private final String PROBLEM_TYPE = "HORSE";


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
