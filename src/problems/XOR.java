package problems;

public class XOR implements Problem {
	
	private final int NUM_INPUTS = 2;
	private final int NUM_OUTPUTS = 1;
	private final String PROBLEM_TYPE = "XOR";
	private final int ITERATIONS = 1000;
	private final double ERROR = 0.1;
	private final double LEARNING_RATE = 0.2;
	private final double MOMENTUM = 0.5;
	private final int[] BASE_MEMBER = {1,1,1,1,0,1};
	private final int BASE_MEMBER_NEURON = 4;
	private final int POP = 100;

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
	public int getBaseMemberNeurons() {
		return BASE_MEMBER_NEURON;
	}
	
	@Override
	public int getPop() {
		return POP;
	}

}
