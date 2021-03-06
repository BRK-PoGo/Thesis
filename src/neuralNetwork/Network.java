package neuralNetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.neuroph.core.*;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.input.*;
import org.neuroph.core.transfer.*;
import org.neuroph.nnet.*;
import org.neuroph.nnet.comp.neuron.*;
import org.neuroph.nnet.learning.*;
import org.neuroph.util.*;

public class Network {

	private Integer num_ins;
	private Integer num_neurons;
	private Integer num_outs;
	
	private final double MAX_INIT_WEIGHT = 0.05;
	private final double MIN_INIT_WEIGHT = -0.05;
	
	private double iterations;
	
	private int paths = 0;
	
	private Random r = new Random();
	
	private NeuralNetwork<BackPropagation> network;
	
	public Network(Integer ins, Integer outs, int[][] connections, double numIterations, double learningRate, double maxError, String problem, double momentum) {
		
		//Define parameters
		num_ins = ins;
		num_outs = outs;
		num_neurons = connections.length;
		iterations = numIterations;
		
		//Build initial network, remove initial connections and add learning rule
		NeuronProperties props = new NeuronProperties(TransferFunctionType.SIGMOID, false);
		List<Integer> inits = new ArrayList<Integer>();inits.add(num_ins);inits.add(num_outs);
		network = new MultiLayerPerceptron(inits,props);
		network.getLayerAt(0).addNeuron(new BiasNeuron());
		removeConnections(network);
		BackPropagation learningRule = getLearningRule(numIterations, learningRate, maxError, momentum);
		network.setLearningRule(learningRule);
		learningRule.setNeuralNetwork(network);
		
		//Define main layer and add it
		Layer mainLayer = new Layer();
		network.addLayer(1, mainLayer);
		
		//Configure hidden neurons
		for (int i = 0; i < num_neurons - num_ins - num_outs; i++) {
			Neuron neuron = new Neuron();
			neuron.setInputFunction(new WeightedSum());
			neuron.setTransferFunction(new Sigmoid());
			mainLayer.addNeuron(neuron);
		}
		
		//Get all neurons (and store the bias one for later)
		List<Neuron> neurons = new ArrayList<Neuron>();
		Neuron bias = null;
		int ctr = 1;
		for (int i = 0; i < network.getLayersCount(); i++) {
			for (Neuron neuron : network.getLayerAt(i).getNeurons()) {
				if (neuron.getClass().getName().equals("org.neuroph.nnet.comp.neuron.BiasNeuron")) {
					neuron.setLabel("bias");
					bias = neuron;
				} else {
					neuron.setLabel("neuron " + ctr);
					ctr++;
					neurons.add(neuron);
				}
			}
		}
		
		//Configure connections
		for (int i = 0; i < connections.length; i++) {
			for (int j = 0; j < connections[i].length; j++) {
				if (connections[i][j] == 1) {
					network.createConnection(neurons.get(i), neurons.get(j), 0);
					paths++;
				}
			}
		}
		
		//Configure all non-input nodes to the bias node
		if (bias != null ) {
			for (Neuron neuron : neurons) {
				if (neurons.indexOf(neuron) > num_ins - 1) {
					network.createConnection(bias, neuron, 0);
					paths++;
				}
			}
		}
		
		//Add initial weights
		network.setWeights(getWeights());
		
		//Configure output neurons
		if (problem.equals("XOR")) { //If XOR problem, use step output
			Step step = new Step();
			step.setYHigh(1.0);
			step.setYLow(0.0);
			network.getLayerAt(network.getLayersCount()-1).getNeuronAt(0).setTransferFunction(step);
			network.getLayerAt(network.getLayersCount()-1).getNeuronAt(0).setInputFunction(new WeightedSum());
		} else { //If MNIST or horse, use sigmoid outputs
			for (Neuron neuron : network.getLayerAt(network.getLayersCount()-1).getNeurons()) {
				neuron.setTransferFunction(new Sigmoid());
				neuron.setInputFunction(new WeightedSum());
			}
		}
	}
	
	public Network(Integer ins, Integer outs, int[]layers, double numIterations, double learningRate, double maxError, String problem, double momentum) {
		
		//Define parameters
		num_ins = ins;
		num_outs = outs;
		iterations = numIterations;
		List<Integer> inits = new ArrayList<Integer>();
		inits.add(ins);
		for (int i : layers) inits.add(i);
		inits.add(outs);
		
		//Build initial network, remove initial connections and add learning rule
		NeuronProperties props = new NeuronProperties(TransferFunctionType.SIGMOID, true);
		network = new MultiLayerPerceptron(inits,props);
		BackPropagation learningRule = getLearningRule(numIterations, learningRate, maxError, momentum);
		network.setLearningRule(learningRule);
		learningRule.setNeuralNetwork(network);
		
		/*
		//Define main layer and add it
		Layer mainLayer = new Layer();
		network.addLayer(1, mainLayer);
		
		//Configure hidden neurons
		for (int i = 0; i < num_neurons - num_ins - num_outs; i++) {
			Neuron neuron = new Neuron();
			neuron.setInputFunction(new WeightedSum());
			neuron.setTransferFunction(new Sigmoid());
			mainLayer.addNeuron(neuron);
		}
		
		//Get all neurons (and store the bias one for later)
		List<Neuron> neurons = new ArrayList<Neuron>();
		Neuron bias = null;
		int ctr = 1;
		for (int i = 0; i < network.getLayersCount(); i++) {
			for (Neuron neuron : network.getLayerAt(i).getNeurons()) {
				if (neuron.getClass().getName().equals("org.neuroph.nnet.comp.neuron.BiasNeuron")) {
					neuron.setLabel("bias");
					bias = neuron;
				} else {
					neuron.setLabel("neuron " + ctr);
					ctr++;
					neurons.add(neuron);
				}
			}
		}
		
		//Configure connections
		for (int i = 0; i < connections.length; i++) {
			for (int j = 0; j < connections[i].length; j++) {
				if (connections[i][j] == 1) {
					network.createConnection(neurons.get(i), neurons.get(j), 0);
					paths++;
				}
			}
		}
		
		//Configure all non-input nodes to the bias node
		if (bias != null ) {
			for (Neuron neuron : neurons) {
				if (neurons.indexOf(neuron) > num_ins - 1) {
					network.createConnection(bias, neuron, 0);
					paths++;
				}
			}
		}
		*/
		//Add initial weights
		network.randomizeWeights(MIN_INIT_WEIGHT, MAX_INIT_WEIGHT);
		
		//Configure output neurons
		if (problem.equals("XOR")) { //If XOR problem, use step output
			Step step = new Step();
			step.setYHigh(1.0);
			step.setYLow(0.0);
			network.getLayerAt(network.getLayersCount()-1).getNeuronAt(0).setTransferFunction(step);
			network.getLayerAt(network.getLayersCount()-1).getNeuronAt(0).setInputFunction(new WeightedSum());
		} else { //If MNIST or horse, use sigmoid outputs
			for (Neuron neuron : network.getLayerAt(network.getLayersCount()-1).getNeurons()) {
				neuron.setTransferFunction(new Sigmoid());
				neuron.setInputFunction(new WeightedSum());
			}
		}
		
	}
	
	public double trainNetwork(DataSet trainingSet) {
		double times = 100;
		double total = 0;
		for (int i = 0; i < times; i++) {
			network.randomizeWeights(MIN_INIT_WEIGHT, MAX_INIT_WEIGHT);
			network.learn(trainingSet);
			total += network.getLearningRule().getCurrentIteration();
		}
		double mean = total/times;
		return mean;
	}
	
	public double testNetwork(DataSet testSet) {
		double accuracy = 0;
		double correct = 0;
		double total = 0;
		for(DataSetRow dataRow : testSet.getRows()) {
			total++;
			network.setInput(dataRow.getInput());
			network.calculate();
			double[] networkOutput = network.getOutput();
			double[] desiredOutput = dataRow.getDesiredOutput();
			if (networkOutput.length > 1) {
				int networkOutputIndex = 0;
				int desiredOutputIndex = 0;
				for (int i = 0; i < networkOutput.length; i++) {
					networkOutputIndex = networkOutput[i] > networkOutput[networkOutputIndex] ? i : networkOutputIndex;
				}
				for (int i = 0; i < desiredOutput.length; i++) {
					desiredOutputIndex = desiredOutput[i] > desiredOutput[desiredOutputIndex] ? i : desiredOutputIndex;
				}
				if (networkOutputIndex == desiredOutputIndex) correct++;
			} else {
				if (networkOutput[0] == desiredOutput[0]) correct++;
			}
			
		}
		accuracy = iterations * (1-(correct/total));
		return accuracy;
	}
	
	public BackPropagation getLearningRule(double iterations, double learningRate, double maxError, double momentum) { //Get the learning rule for the network
		MomentumBackpropagation learningRule = new MomentumBackpropagation();
		learningRule.setLearningRate(learningRate);
		learningRule.setMaxIterations((int)iterations);
		learningRule.setMaxError(maxError);
		learningRule.setMomentum(momentum);
		return learningRule;
	}
	
	public double getRand() { //Get random weight
		return MIN_INIT_WEIGHT + ((MAX_INIT_WEIGHT - MIN_INIT_WEIGHT) * r.nextDouble());
	}
	
	public void removeConnections(NeuralNetwork<BackPropagation> network) { //Remove the initial connections in the network
		for (Neuron neuron : network.getLayerAt(0).getNeurons()) {
			neuron.removeAllConnections();
		}
		for (Neuron neuron : network.getLayerAt(1).getNeurons()) {
			neuron.removeAllConnections();
		}
	}
	
	public double[] getWeights() {
		double[] weights = new double[paths];
		for (int i = 0; i < paths; i++) {
			weights[i] = getRand();
		}
		return weights;
	}
}
