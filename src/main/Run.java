package main;

import algos.GA;
import problems.*;

public class Run {

	public static void main(String[] args) {
		Problem problem = new XOR();
		//Problem problem = new MNIST();
		GA ga = new GA(1000, problem.getInputs(), problem.getOutputs(), problem.getProblem());
		//ga.printPop();
		long start = System.nanoTime();
		ga.testNetworks();
		System.out.println("took " + (System.nanoTime() - start)/1000000000 + "s");
	}
}
