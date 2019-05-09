package problems;

public class MNIST implements Problem {

	private final int NUM_INPUTS = 784;
	private final int NUM_OUTPUTS = 10;
	private final String PROBLEM_TYPE = "MNIST";
	private final int ITERATIONS = 1000;
	private final double ERROR = 0.01;
	private final double LEARNING_RATE = 0.1;
	private final double MOMENTUM = 0.1;
	private final int POP = 12;


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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getBaseMemberNeurons() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getPop() {
		return POP;
	}

}
