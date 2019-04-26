package main;

import algos.GA;

public class Run {

	public static void main(String[] args) {
		GA ga = new GA(100, 2, 1, "XOR");
		//ga.printPop();
		long start = System.nanoTime();
		ga.testNetworks();
		System.out.println("took " + (System.nanoTime() - start)/1000000000 + "s");
	}
}
