package algos;

import java.util.ArrayList;
import java.util.stream.IntStream;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

import conversions.GridToString;
import conversions.RandomString;
import conversions.StringToGrid;
import neuralNetwork.Network;
import problems.Problem;
import readers.HORSE_Reader;
import readers.MNIST_Reader;

public class EP {

	private int pop_size;
	private int max_neurons;
	private int num_ins;
	private int num_outs;
	
	private final int NUM_GENS = 10;
	
	private Member baseMember;
	
	private Member[] population;
	private Member[] newpop;
	
	private RandomString randStr = new RandomString();
	private GridToString G2S = new GridToString();
	private StringToGrid S2G = new StringToGrid();
	
	private Member[] bestMembers = new Member[NUM_GENS];
	
	private String problem;
	
	private DataSet trainingSet;
	private DataSet testSet;
	private double iterations = 0;
	private double error = 0;
	private double learningRate = 0;
	private double momentum = 0;
	
	public EP(int pop_size, Problem problem) {
		
		iterations = problem.getIterations();
		error = problem.getError();
		learningRate = problem.getLearningRate();
		momentum = problem.getMomentum();
		baseMember = new Member(problem.getBaseMember(),0.0,problem.getBaseMemberNeurons());
		this.problem = problem.getProblem();
		this.pop_size = pop_size;
		population =  new Member[this.pop_size];
		newpop = new Member[pop_size];
		num_ins = problem.getInputs();
		num_outs = problem.getOutputs();
		max_neurons = num_ins*2; //max hidden neurons = two times the number of inputs
		
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
				trainingSet.addRow(new DataSetRow(data[i], labels[i]));
			}
			dataset = reader.Run("test");
			data = dataset[0];
			labels = dataset[1];
			for (int i = 0; i < data.length; i++) {
				testSet.addRow(new DataSetRow(data[i], labels[i]));
			}
		}
		
		for (int i = 0; i < pop_size; i++) {
			double randomDouble = Math.random();
			randomDouble = randomDouble * (max_neurons + 1);
			//int hidden_neurons = (int) randomDouble;
			//int num_neurons = num_ins + num_outs + hidden_neurons;
			int num_neurons = num_ins + num_outs;
			population[i] = new Member(randStr.genRanString(num_ins, num_outs, num_neurons),0.0,num_neurons);
			//population[i] = new Member(new int[]{1,1,1,1,0,1,1,1,1,0,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1},0.0,7);
		}
	}
	
	public void testNetworks() {
		for (int x = 0; x < NUM_GENS; x++) {
			long start = System.nanoTime();
			System.out.println("Gen " + x);
			System.out.println("Training members");
			IntStream.range(0,population.length).parallel().forEach((int i) -> train(population[i],i));
			population = sortCollection(population);
			bestMembers[x] = population[0];
			System.out.println("Breeding new members");
			IntStream.range(0,population.length).parallel().forEach((int i) -> breed(i));
			population = newpop;
			System.out.println("gen " + x + " took " + (System.nanoTime() - start)/1000000000 + "s");
		}
		System.out.println();
		//train(baseMember, -1);
		bestMembers = sortCollection(bestMembers);
		Member bestMember = bestMembers[0];
		//System.out.println("Ideal had fitness " + baseMember.getFitness());
		//for (int gene : baseMember.getGene()) {
		//	System.out.print(gene + " ");
		//}
		//System.out.println();
		for (int i = 0; i < NUM_GENS; i++) {
			if (bestMembers[i].getFitness() < bestMember.getFitness()) bestMember = bestMembers[i];
			System.out.println("Gen " + i + " best had fitness " + bestMembers[i].getFitness());
			for (int gene : bestMembers[i].getGene()) {
				System.out.print(gene + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Input neurons:");
		for (int i = 0; i < num_ins; i++) {
			System.out.println(i);
		}
		System.out.println("Output neurons:");
		for (int i = bestMember.getNeurons() - num_outs; i < bestMember.getNeurons(); i++) {
			System.out.println(i);
		}
		System.out.println();
		System.out.println("Best config");
		System.out.println();
		int[][] bestGrid = S2G.getGrid(num_ins, num_outs, bestMember.getNeurons(), bestMember.getGene());
		for (int i = 0; i < bestMember.getNeurons(); i++) {
			for (int j = 0; j < bestMember.getNeurons(); j++) {
				if (bestGrid[i][j] == 1) {
					System.out.println("Neuron " + i + " connects to neuron " + j);
				}
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void train(Member member, int i) {
		Network network = new Network(num_ins, num_outs, S2G.getGrid(num_ins, num_outs, member.getNeurons(), member.getGene()),iterations,learningRate,error,problem,momentum);
		double mean = network.trainNetwork(trainingSet);
		double accuracy = network.testNetwork(testSet);
		member.setFitness(mean + accuracy);
	}
	
	public void breed(int i) {
		Member parent = population[i];
		Member child = crossover(parent);
		newpop[i] = child;
	}
	
	public double getMutationRate(Member parent) { //CHANGE TO DO DIFFERENT FUNCTION IF FITNESS IS LESS THAN 1/2 OF TOTAL
		double mutationRate = 0;
		double fitness = parent.getFitness();
		double exp = fitness / (iterations*2);
		mutationRate = (Math.exp(exp)-1)/(Math.exp(1.05)-1);
		return mutationRate;
	}
	
	public Member crossover(Member parent) {
		double mutationRate = getMutationRate(parent);
		int[] gene = parent.getGene();
		int[] child = new int[gene.length];
		int child_neurons = parent.getNeurons();
		for (int i = 0; i < gene.length; i++) {
			child[i] = gene[i];
		}
		
		//Mutation of for adding or removing connections
		for (int i = 0; i < gene.length; i++) {
			if (Math.random() < mutationRate) {
				if (child[i] == 0) {
					child[i] = 1;
				} else if (child[i] == 1){
					child[i] = 0;
				}
			}
		}
		
		//Mutation of for adding neurons
		for (int i = 0; i < child.length; i++) {
			if (Math.random() < mutationRate && child[i] == 1) {
				if (child_neurons < num_ins + num_outs + max_neurons) {
					int[][] grid = S2G.getGrid(num_ins, num_outs, child_neurons, child);
					int from = (int) i / (child_neurons-num_ins);
					int to = i % (child_neurons-num_ins) + num_ins;
					int[][] grid_new = new int[child_neurons + 1][child_neurons + 1];
					for (int x = 0; x < child_neurons; x++) {
						for (int y = 0; y < child_neurons - num_outs; y++) {
							grid_new[x][y] = grid[x][y];
						}
					}
					grid_new[from][to] = 0;
					for (int x = 0; x < child_neurons; x++) {
						grid_new[x][child_neurons] = grid[x][child_neurons - 1];
					}
					grid_new[from][child_neurons - num_outs] = 1;
					grid_new[child_neurons - num_outs][to] = 1;
					child_neurons++;
					child = G2S.getString(num_ins, num_outs, child_neurons, grid_new);
				}
			}
		}
		
		//Mutation for removing neurons
		int a = num_ins;
		while(a < child_neurons - num_outs) {
			if (Math.random() < mutationRate) {
				int[][] grid = S2G.getGrid(num_ins, num_outs, child_neurons, child);
				int[][] grid_new = new int[child_neurons - 1][child_neurons - 1];
				ArrayList<Integer> from = new ArrayList<Integer>();
				ArrayList<Integer> to = new ArrayList<Integer>();
				for (int x = 0; x < child_neurons; x++) {
					if (grid[x][a] == 1) {
						from.add(x);
					}
					if (grid[a][x] == 1) {
						to.add(x);
					}
				}
				for (int fromNeuron : from) {
					for (int toNeuron : to) {
						grid[fromNeuron][toNeuron] = 1;
					}
				}
				for (int x = 0; x < a; x++) {
					for (int y = 0; y < a; y++) {
						grid_new[x][y] = grid[x][y];
					}
					for (int y = a + 1; y < child_neurons; y++) {
						grid_new[x][y - 1] = grid[x][y];
					}
				}
				for (int x = a + 1; x < child_neurons; x++) {
					for (int y = 0; y < a; y++) {
						grid_new[x - 1][y] = grid[x][y];
					}
					for (int y = a + 1; y < child_neurons; y++) {
						grid_new[x - 1][y - 1] = grid[x][y];
					}
				}
				child_neurons--;
				child = G2S.getString(num_ins, num_outs, child_neurons, grid_new);
			} else {
				a++;
			}
		}
		Member child_new = new Member(child, 0.0, child_neurons);
		return child_new;
	}
	
	public void printPop() {
		for (Member member : population) {
			System.out.print(member.getNeurons() + " ");
			for (int gene : member.getGene()) {
				System.out.print(gene + " ");
			}
			System.out.println();
		}
	}
	
	public Member[] sortCollection(Member[] collection) {
		for (int i = 1; i < collection.length; i++) {
			for (int j = 0; j < collection.length - i; j++) {
				if (collection[j].getFitness() > collection[j+1].getFitness()) {
					Member tmp = collection[j];
					collection[j] = collection[j+1];
					collection[j+1] = tmp;
				}
			}
		}
		return collection;
	}
}
