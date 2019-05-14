package algos;

public class Member {

	private int[] gene;
	private double fitness;
	private int neurons;
	private double iterations;
	private double accuracy;
	
	public Member(int[] gene, double fitness, int neurons) {
		this.gene = gene;
		this.fitness = fitness;
		this.neurons = neurons;
	}
	
	public void setGene(int[] gene) {
		this.gene = gene;
	}
	
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	
	public void setNeurons(int neurons) {
		this.neurons = neurons;
	}
	
	public void setIterations(double iterations) {
		this.iterations = iterations;
	}
	
	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}
	
	public int[] getGene() {
		return gene;
	}
	
	public double getFitness() {
		return fitness;
	}
	
	public int getNeurons() {
		return neurons;
	}
	
	public double getIterations() {
		return iterations;
	}
	
	public double getAccuracy() {
		return accuracy;
	}
}
