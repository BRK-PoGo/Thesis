package conversions;

public class GridToString {
	
	public int[] getString(int num_ins, int num_outs, int num_neurons, int[][] connections) {
		int[] string = new int[(num_neurons - num_outs) * (num_neurons - num_ins)];
		int ctr = 0;
		for (int i = 0; i < num_neurons - num_outs; i++) {
			for (int j = num_ins; j < num_neurons; j++) {
				string[ctr] = connections[i][j];
				ctr++;
			}
		}
		return string;
	}
}
