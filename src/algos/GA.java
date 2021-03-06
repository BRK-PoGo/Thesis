package algos;

import conversions.GridToString;
import conversions.RandomString;
import conversions.StringToGrid;
import neuralNetwork.Network;
import problems.Problem;
import readers.*;

import java.util.ArrayList;
import java.util.stream.IntStream;

import org.neuroph.core.data.*;

public class GA {
	
	private int pop_size;
	private final double BREEDING_SIZE = 0.2;
	private final double PASS_OVER = 0.1;
	private final int NUMBER_OF_TOURNEMENTS = 10;
	private final double MUTATION_RATE = 0.1;
	
	private int max_neurons;
	private int num_ins;
	private int num_outs;
	
	private final int NUM_GENS = 10;
	
	private Member baseMember;
	
	private Member[] population;
	private Member[] breeding;
	private Member[] newpop;
	private RandomString randStr = new RandomString();
	private GridToString G2S = new GridToString();
	private StringToGrid S2G = new StringToGrid();
	
	private Member[] bestMembers = new Member[NUM_GENS];
	
	private String problem;
	
	private DataSet trainingSet;
	private DataSet testSet;
	private int iterations = 0;
	private double error = 0;
	private double learningRate = 0;
	private double momentum = 0;
	
	public GA(int pop_size, Problem problem) {
		iterations = problem.getIterations();
		error = problem.getError();
		learningRate = problem.getLearningRate();
		momentum = problem.getMomentum();
		//baseMember = new Member(problem.getBaseMember(),0.0,problem.getBaseMemberNeurons());
		this.problem = problem.getProblem();
		this.pop_size = pop_size;
		population =  new Member[pop_size];
		newpop = new Member[pop_size];
		breeding = new Member[(int) (pop_size * BREEDING_SIZE)];
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
			//double randomDouble = Math.random();
			//randomDouble = randomDouble * (max_neurons + 1);
			//int hidden_neurons = (int) randomDouble;
			//int num_neurons = num_ins + num_outs + hidden_neurons;
			int num_neurons = num_ins + num_outs;
			population[i] = new Member(randStr.genRanString(num_ins, num_outs, num_neurons),0.0,num_neurons);
		}
	}
	
	public void testNetworks() {
		double total = 0.0;
		for (int x = 0; x < NUM_GENS; x++) {
			double tmp_total = 0.0;
			long start = System.nanoTime();
			System.out.println("Gen " + x);
			System.out.println("Training members");
			IntStream.range(0,population.length).parallel().forEach((int i) -> train(population[i],i));
			for (int i = 0; i < pop_size; i++) {
				tmp_total += population[i].getFitness();
			}
			total += tmp_total/pop_size;
			population = sortCollection(population);
			bestMembers[x] = population[0];
			getBreedingSet();
			for (int i = 0; i < (int) (pop_size * PASS_OVER); i++) {
				newpop[i] = population[i];
			}
			System.out.println("Breeding new members");
			IntStream.rangeClosed((int) ((pop_size*PASS_OVER)/2),(pop_size/2)-1).parallel().map((int i)->i*2).forEach(this::breed);
			population = newpop;
			System.out.println("gen " + x + " took " + (System.nanoTime() - start)/1000000000 + "s");
		}
		System.out.println();
		
		System.out.println("Algorithm had average fitness of " + total/NUM_GENS);
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
			System.out.println("Gen " + i + " best had " + bestMembers[i].getNeurons() + " neurons");
			System.out.println("Gen " + i + " best had iterations " + bestMembers[i].getIterations());
			System.out.println("Gen " + i + " best had accuracy " + (100-(bestMembers[i].getAccuracy()/iterations)*100) + "%");
			/*
			for (int gene : bestMembers[i].getGene()) {
				System.out.print(gene + " ");
			}
			*/
			System.out.println();
		}
		System.out.println();
		/*
		System.out.println("Input neurons:");
		for (int i = 0; i < num_ins; i++) {
			System.out.println(i);
		}
		System.out.println("Output neurons:");
		for (int i = bestMember.getNeurons() - num_outs; i < bestMember.getNeurons(); i++) {
			System.out.println(i);
		}
		System.out.println();
		*/
		System.out.println("Best member had fitness " + bestMember.getFitness());
		System.out.println("Best member had iterations " + bestMember.getIterations());
		System.out.println("Best member had accuracy " + (100-(bestMember.getAccuracy()/iterations)*100) + "%");
		/*
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
		*/
	}
	
	public void train(Member member, int i) {
		//System.out.println("member " + i);
		Network network = new Network(num_ins, num_outs, S2G.getGrid(num_ins, num_outs, member.getNeurons(), member.getGene()),iterations,learningRate,error,problem,momentum);
		double mean = network.trainNetwork(trainingSet);
		member.setIterations(mean);
		double accuracy = network.testNetwork(testSet);
		member.setAccuracy(accuracy);
		member.setFitness(mean + accuracy);
	}
	
	public void breed(int i) {
		Member parent1 = breeding[(int) (Math.random() * breeding.length)];
		Member parent2 = breeding[(int) (Math.random() * breeding.length)];
		Member[] parents = {parent1,parent2};
		Member[] children = crossover(parents);
		newpop[i] = children[0];
		newpop[i+1] = children[1];
	}
	
	public void getBreedingSet() {
		Member[][] tournaments = new Member[NUMBER_OF_TOURNEMENTS][pop_size/NUMBER_OF_TOURNEMENTS];
		ArrayList<Integer> members = new ArrayList<Integer>();
		for (int i = 0; i < pop_size; i++) {
			members.add(i);
		}
		for (int i = 0; i < pop_size/NUMBER_OF_TOURNEMENTS; i++) {
			for (int j = 0; j < NUMBER_OF_TOURNEMENTS; j++) {
				int memberPos = (int) Math.random() * members.size();
				Member member = population[members.get(memberPos)];
				members.remove(memberPos);
				tournaments[j][i] = member;
			}
		}
		int ptr = 0;
		for (Member[] tournament : tournaments) {
			tournament = sortCollection(tournament);
			for (int i = 0; i < tournament.length * BREEDING_SIZE; i++) {
				breeding[ptr] = tournament[i];
				ptr++;
			}
		}
	}
	
	public Member[] crossover(Member[] parents) {
		Member[] children = new Member[2];
		int[] gene0 = parents[0].getGene();
		int[] gene1 = parents[1].getGene();
		int[] child0 = new int[gene0.length];
		int[] child1 = new int[gene1.length];
		int child0_neurons = parents[0].getNeurons();
		int child1_neurons = parents[1].getNeurons();
		int minLength = Math.min(gene0.length, gene1.length);
		double crossover_tmp = Math.random() * minLength;
		int crossover = (int) crossover_tmp;
		
		for (int i = 0; i < crossover; i++) {
			child0[i] = gene1[i];
			child1[i] = gene0[i];
		}
		for (int i = crossover; i < gene0.length; i++) {
			child0[i] = gene0[i];
		}
		for (int i = crossover; i < gene1.length; i++) {
			child1[i] = gene1[i];
		}
		
		//Mutation of for adding or removing connections
		for (int i = 0; i < child0.length; i++) {
			if (Math.random() < MUTATION_RATE) {
				if (child0[i] == 0) {
					child0[i] = 1;
				} else if (child0[i] == 1){
						child0[i] = 0;
				}
			}
		}
		for (int i = 0; i < child1.length; i++) {
			if (Math.random() < MUTATION_RATE) {
				if (child1[i] == 0) {
					child1[i] = 1;
				} else if (child1[i] == 1){
					child1[i] = 0;
				}
			}
		}
		
		//Mutation of for adding neurons
		for (int i = 0; i < child0.length; i++) {
			if (Math.random() < MUTATION_RATE) {
				if (child0_neurons < num_ins + num_outs + max_neurons && child0[i] == 1) {
					int[][] grid0 = S2G.getGrid(num_ins, num_outs, child0_neurons, child0);
					int from = (int) i / (child0_neurons-num_ins);
					int to = i % (child0_neurons-num_ins) + num_ins;
					int[][] grid0_new = new int[child0_neurons + 1][child0_neurons + 1];
					for (int x = 0; x < child0_neurons; x++) {
						for (int y = 0; y < child0_neurons - num_outs; y++) {
							grid0_new[x][y] = grid0[x][y];
						}
					}
					grid0_new[from][to] = 0;
					for (int x = 0; x < child0_neurons; x++) {
						grid0_new[x][child0_neurons] = grid0[x][child0_neurons - 1];
					}
					grid0_new[from][child0_neurons - num_outs] = 1;
					grid0_new[child0_neurons - num_outs][to] = 1;
					child0_neurons++;
					child0 = G2S.getString(num_ins, num_outs, child0_neurons, grid0_new);
				}
			}
		}
		for (int i = 0; i < child1.length; i++) {
			if (Math.random() < MUTATION_RATE) {
				if (child1_neurons < num_ins + num_outs + max_neurons && child1[i] == 1) {
					int[][] grid1 = S2G.getGrid(num_ins, num_outs, child1_neurons, child1);
					int from = (int) i / (child1_neurons-num_ins);
					int to = i % (child1_neurons-num_ins) + num_ins;
					int[][] grid1_new = new int[child1_neurons + 1][child1_neurons + 1];
					for (int x = 0; x < child1_neurons; x++) {
						for (int y = 0; y < child1_neurons - num_outs; y++) {
							grid1_new[x][y] = grid1[x][y];
						}
					}
					grid1_new[from][to] = 0;
					for (int x = 0; x < child1_neurons; x++) {
						grid1_new[x][child1_neurons] = grid1[x][child1_neurons - 1];
					}
					grid1_new[from][child1_neurons - num_outs] = 1;
					grid1_new[child1_neurons - num_outs][to] = 1;
					child1_neurons++;
					child1 = G2S.getString(num_ins, num_outs, child1_neurons, grid1_new);
				}
			}
		}
		
		//Mutation for removing neurons
		int a = num_ins;
		while(a < child0_neurons - num_outs) {
			if (Math.random() < MUTATION_RATE) {
				int[][] grid0 = S2G.getGrid(num_ins, num_outs, child0_neurons, child0);
				int[][] grid0_new = new int[child0_neurons - 1][child0_neurons - 1];
				ArrayList<Integer> from = new ArrayList<Integer>();
				ArrayList<Integer> to = new ArrayList<Integer>();
				for (int x = 0; x < child0_neurons; x++) {
					if (grid0[x][a] == 1) {
						from.add(x);
					}
					if (grid0[a][x] == 1) {
						to.add(x);
					}
				}
				for (int fromNeuron : from) {
					for (int toNeuron : to) {
						grid0[fromNeuron][toNeuron] = 1;
					}
				}
				for (int x = 0; x < a; x++) {
					for (int y = 0; y < a; y++) {
						grid0_new[x][y] = grid0[x][y];
					}
					for (int y = a + 1; y < child0_neurons; y++) {
						grid0_new[x][y - 1] = grid0[x][y];
					}
				}
				for (int x = a + 1; x < child0_neurons; x++) {
					for (int y = 0; y < a; y++) {
						grid0_new[x - 1][y] = grid0[x][y];
					}
					for (int y = a + 1; y < child0_neurons; y++) {
						grid0_new[x - 1][y - 1] = grid0[x][y];
					}
				}
				child0_neurons--;
				child0 = G2S.getString(num_ins, num_outs, child0_neurons, grid0_new);
			} else {
				a++;
			}
		}
		a = num_ins;
		while(a < child1_neurons - num_outs) {
			if (Math.random() < MUTATION_RATE) {
				int[][] grid1 = S2G.getGrid(num_ins, num_outs, child1_neurons, child1);
				int[][] grid1_new = new int[child1_neurons - 1][child1_neurons - 1];
				ArrayList<Integer> from = new ArrayList<Integer>();
				ArrayList<Integer> to = new ArrayList<Integer>();
				for (int x = 0; x < child1_neurons; x++) {
					if (grid1[x][a] == 1) {
						from.add(x);
					}
					if (grid1[a][x] == 1) {
						to.add(x);
					}
				}
				for (int fromNeuron : from) {
					for (int toNeuron : to) {
						grid1[fromNeuron][toNeuron] = 1;
					}
				}
				for (int x = 0; x < a; x++) {
					for (int y = 0; y < a; y++) {
						grid1_new[x][y] = grid1[x][y];
					}
					for (int y = a + 1; y < child1_neurons; y++) {
						grid1_new[x][y - 1] = grid1[x][y];
					}
				}
				for (int x = a + 1; x < child1_neurons; x++) {
					for (int y = 0; y < a; y++) {
						grid1_new[x - 1][y] = grid1[x][y];
					}
					for (int y = a + 1; y < child1_neurons; y++) {
						grid1_new[x - 1][y - 1] = grid1[x][y];
					}
				}
				child1_neurons--;
				child1 = G2S.getString(num_ins, num_outs, child1_neurons, grid1_new);
			} else {
				a++;
			}
		}
		
		children[0] = new Member(child0, 0.0, child0_neurons);
		children[1] = new Member(child1, 0.0, child1_neurons);
		return children;
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
