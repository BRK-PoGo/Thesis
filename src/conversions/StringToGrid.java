package conversions;

public class StringToGrid {

	public int[][] getGrid(int num_ins, int num_outs, int num_neurons, int[] string) {
		int[][] connections = new int[num_neurons][num_neurons];
		for (int i = 0; i < num_neurons; i++) {
			for (int j = 0; j < num_neurons; j++) {
				connections[i][j] = 0;
			}
		}
		int ctr = 0;
		for (int i = 0; i < num_neurons - num_outs; i++) {
			for (int j = num_ins; j < num_neurons; j++) {
				connections[i][j] = string[ctr];
				ctr++;
			}
		}
		return connections;
	}
}
