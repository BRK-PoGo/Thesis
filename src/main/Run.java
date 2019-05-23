package main;

import algos.*;
import problems.*;

public class Run {

	public static void main(String[] args) {
		//Problem problem = new XOR();
		//Problem problem = new MNIST();
		//Problem problem = new HORSE();
		Problem problem = new MNIST();
		//Problem problem = new HORSE();
		int pop = problem.getPop();
		//GA algo = new GA(pop, problem);
		//EP algo = new EP(pop, problem);
		Base algo = new Base(problem);
		long start = System.nanoTime();
		algo.testNetworks();
		System.out.println("took " + (System.nanoTime() - start)/1000000000 + "s");
	}
}
