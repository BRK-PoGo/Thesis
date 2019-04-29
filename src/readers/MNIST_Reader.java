package readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;

public class MNIST_Reader {

	private String csvPathTrain = "fashion-mnist_train.csv"; //need to fix this
	private String csvPathTest = "fashion-mnist_test.csv";	//need to fix this
	private String line = "";
	private String splitBy = ",";
	private BufferedReader br = null;
	private LineNumberReader nr = null;
	private int numLines = 0;
	private double[][] data;
	private double[][] labels;
	private Double maxVal = 255.0;
	double[][][] dataset = null;
		
	public double[][][] Run(String type) {
		try {
			System.out.println("Loading data " + type);
			if (type.equals("train")) {
				FileReader fr = new FileReader(csvPathTrain);
				nr = new LineNumberReader(fr);
				while (nr.skip(Long.MAX_VALUE) > 0) {
				      // Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
				}
				numLines = nr.getLineNumber();
				fr = new FileReader(csvPathTrain);
				br = new BufferedReader(fr);
			} else if (type.equals("test")) {
				FileReader fr = new FileReader(csvPathTest);
				nr = new LineNumberReader(fr);
				while (nr.skip(Long.MAX_VALUE) > 0) {
				      // Loop just in case the file is > Long.MAX_VALUE or skip() decides to not read the entire file
				}
				numLines = nr.getLineNumber();
				fr = new FileReader(csvPathTest);
				br = new BufferedReader(fr);
			}
			data = new double[numLines][0];
			labels = new double[numLines][0];
			int ctr = 0;
            while ((line = br.readLine()) != null) {
                String[] pixels = line.split(splitBy);
                double[] newData = new double[pixels.length - 1];
                for (int i = 0; i < pixels.length; i++) {               	
                	if (i == 0) {
                		double[] newLabels = new double[10];
                		for (int j = 0; j < 10; j++) {
                			newLabels[j] = 0.0;
                		}
                		newLabels[Integer.parseInt(pixels[i])] = 1.0;
                		labels[ctr] = newLabels;
                	} else {
                		newData[i-1] = Integer.parseInt(pixels[i])/maxVal;
                	}
                }
                data[ctr] = newData;
                ctr++;
            }
            double[][][] dataset_tmp = {data, labels};
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
