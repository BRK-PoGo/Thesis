package main;

import algos.GA;
import problems.*;

public class Run {

	public static void main(String[] args) {
		//Problem problem = new XOR();
		//Problem problem = new MNIST();
		Problem problem = new HORSE();
		GA ga = new GA(100, problem);
		long start = System.nanoTime();
		ga.testNetworks();
		System.out.println("took " + (System.nanoTime() - start)/1000000000 + "s");
	}
}
