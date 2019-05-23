package algos;

import java.util.stream.IntStream;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import neuralNetwork.Network;
import problems.Problem;
import readers.HORSE_Reader;
import readers.MNIST_Reader;

public class Base {
	
	private int num_ins;
	private int num_outs;
	
	private String problem;
	
	private DataSet trainingSet;
	private DataSet testSet;
	private double iterations = 0;
	private double error = 0;
	private double learningRate = 0;
	private double momentum = 0;
	private int[] base;
	
	private final int TIMES = 1;
	private double[] accuracies = new double[TIMES];
	private double[] means = new double[TIMES];
	private double[] fitnesses = new double[TIMES];
	
	public Base(Problem problem) {
		
		iterations = problem.getIterations();
		error = problem.getError();
		learningRate = problem.getLearningRate();
		momentum = problem.getMomentum();
		//baseMember = new Member(problem.getBaseMember(),0.0,problem.getBaseMemberNeurons());
		this.problem = problem.getProblem();
		base = problem.getBaseMember();
		num_ins = problem.getInputs();
		num_outs = problem.getOutputs();
		
		trainingSet = new DataSet(num_ins, num_outs);
		testSet = new DataSet(num_ins, num_outs);
		
		if (this.problem.equals("XOR")) {
			System.out.println("xor");
			trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
			trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
			trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
			trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));
			testSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0}));
			testSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
			testSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
			testSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));
		} else if (this.problem.equals("HORSE")) {
			System.out.println("horse");
			HORSE_Reader reader = new HORSE_Reader();
			double[][][] dataset = reader.Run();
			double[][] data = dataset[0];
			double[][] labels = dataset[1];
			for (int i = 0; i < data.length; i++) {
				if (i%3 == 0) {
					testSet.addRow(new DataSetRow(data[i], labels[i]));
				} else {
					trainingSet.addRow(new DataSetRow(data[i], labels[i]));
				}
			}
		} else if (this.problem.equals("MNIST")) {
			System.out.println("MNIST");
			MNIST_Reader reader = new MNIST_Reader();
			double[][][] dataset = reader.Run("train");
			double[][] data = dataset[0];
			double[][] labels = dataset[1];
			for (int i = 0; i < data.length; i++) {
				if (i%600 == 0) {
					trainingSet.addRow(new DataSetRow(data[i], labels[i]));
				}
			}
			dataset = reader.Run("test");
			data = dataset[0];
			labels = dataset[1];
			for (int i = 0; i < data.length; i++) {
				if (i%600 == 0) {
					testSet.addRow(new DataSetRow(data[i], labels[i]));
				}
			}
		}
	}
	
	public void testNetworks() {
		System.out.println("Training members");
		IntStream.range(0,TIMES).parallel().forEach((int i) -> train(i));
		for (int i = 0; i < TIMES; i++) {
			System.out.println("Iterations for run " + i + ": " + means[i]);
			System.out.println("Accuracy for run " + i + ": " + (100-(accuracies[i]/iterations)*100) + "%");
			System.out.println("Fitness for run " + i + ": " + fitnesses[i]);
		}
		
	}

	public void train(int i) {
		Network network = new Network(num_ins, num_outs, base,iterations,learningRate,error,problem,momentum);
		System.out.println("training");
		long start = System.nanoTime();
		double mean = network.trainNetwork(trainingSet);
		System.out.println("took " + (System.nanoTime() - start)/1000000000 + "s");
		System.out.println("testing");
		start = System.nanoTime();
		double accuracy = network.testNetwork(testSet);
		System.out.println("took " + (System.nanoTime() - start)/1000000000 + "s");
		double fitness = mean + accuracy;
		means[i] = mean;
		accuracies[i] = accuracy;
		fitnesses[i] = fitness;
	}
}
