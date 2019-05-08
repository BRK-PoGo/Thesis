package problems;

public class XOR implements Problem {
	
	private final int NUM_INPUTS = 2;
	private final int NUM_OUTPUTS = 1;
	private final String PROBLEM_TYPE = "XOR";
	private final int ITERATIONS = 2000;
	private final double ERROR = 0.1;
	private final double LEARNING_RATE = 0.5;
	private final double MOMENTUM = 0.1;


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

}
