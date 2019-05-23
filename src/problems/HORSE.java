package problems;

public class HORSE implements Problem {
	
	private final int NUM_INPUTS = 20;
	private final int NUM_OUTPUTS = 3;
	private final String PROBLEM_TYPE = "HORSE";
	private final int ITERATIONS = 1000;
	private final double ERROR = 0.25;
	private final double LEARNING_RATE = 0.1;
	private final double MOMENTUM = 0.3;
	private final int[] BASE_MEMBER = {15,6};
	private final int POP = 1000;


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

	@Override
	public int getIterations() {
		return ITERATIONS;
	}

	@Override
	public double getError() {
		return ERROR;
	}

	@Override
	public double getLearningRate() {
		return LEARNING_RATE;
	}
	
	@Override
	public double getMomentum() {
		return MOMENTUM;
	}

	@Override
	public int[] getBaseMember() {
		return BASE_MEMBER;
	}
	
	@Override
	public int getPop() {
		return POP;
	}

}
