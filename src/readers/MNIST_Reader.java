package readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MNIST_Reader {

	private String csvPathTrain = "fashion-mnist_test_mini.csv"; //need to fix this
	private String csvPathTest = "fashion-mnist_test_mini.csv";	//need to fix this
	private String line = "";
	private String splitBy = ",";
	private BufferedReader br = null;
	private ArrayList<Double[]> data = new ArrayList<Double[]>();
	private ArrayList<Double[]> labels = new ArrayList<Double[]>();
	private Double maxVal = 255.0;
	double[][][] dataset = null;
		
	public double[][][] Run(String type) {
		try {
			if (type.equals("train")) {
				br = new BufferedReader(new FileReader(csvPathTrain));
			} else if (type.equals("test")) {
				br = new BufferedReader(new FileReader(csvPathTest));
			}
            while ((line = br.readLine()) != null) {
                String[] pixels = line.split(splitBy);
                Double[] newData = new Double[pixels.length - 1];
                for (int i = 0; i < pixels.length; i++) {               	
                	if (i == 0) {
                		Double[] newLabels = new Double[10];
                		for (int j = 0; j < 10; j++) {
                			newLabels[j] = 0.0;
                		}
                		newLabels[Integer.parseInt(pixels[i])] = 1.0;
                		labels.add(newLabels);
                	} else {
                		newData[i-1] = Integer.parseInt(pixels[i])/maxVal;
                	}
                }
                data.add(newData);
            }
            double[][] dataArray = (double[][]) data.toArray();
            double[][] labelArray = (double[][]) labels.toArray();
            double[][][] dataset_tmp = {dataArray, labelArray};
            dataset = dataset_tmp;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
		return dataset;
	}
}
