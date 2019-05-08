package main;

import algos.*;
import problems.*;

public class Run {

	public static void main(String[] args) {
		int pop = 1000;
		Problem problem = new XOR();
		//Problem problem = new MNIST();
		//Problem problem = new HORSE();
		//GA algo = new GA(pop, problem);
		EP algo = new EP(pop, problem);
		long start = System.nanoTime();
		algo.testNetworks();
		System.out.println("took " + (System.nanoTime() - start)/1000000000 + "s");
	}
}
