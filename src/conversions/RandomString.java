package conversions;

import java.util.Random;

public class RandomString {

	public int[] genRanString(int num_ins, int num_outs, int num_neurons) {
		Random r = new Random();
		int[] ranString = new int[(num_neurons - num_outs) * (num_neurons - num_ins)];
		for (int i = 0; i < ranString.length; i++) {
			if (r.nextDouble() > 0.5) ranString[i] = 0; else ranString[i] = 1; 
		}
		return ranString;
	}
}
