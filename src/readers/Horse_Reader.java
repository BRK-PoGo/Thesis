package readers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Horse_Reader {
	
	private String csvPath = "horse.csv";	
	private String line = "";
	private String splitBy = ",";
	private BufferedReader br = null;
	private ArrayList<Double[]> data = new ArrayList<Double[]>();
	private ArrayList<Double[]> labels = new ArrayList<Double[]>();

	public void Run() {
		try {
            br = new BufferedReader(new FileReader(csvPath));
            while ((line = br.readLine()) != null) {
                String[] info = line.split(splitBy);
                Double[] newData = new Double[info.length - 1];
                for (int i = 0; i < info.length; i++) { //EDA pre-processing           	
                	if (i == 0) { //age
                		if (info[i] == "adult") {
                			newData[i] = 1.0;
                		} else {
                			newData[i] = 0.0;
                		}
                	} else if (i == 1) { //rectal_temp
                		if (info[i] == "NA") {
                			newData[i] = (37.8 - 35.4)/5.4;
                		} else {
                			newData[i] = (Double.valueOf(info[i]) - 35.4)/5.4;
                		}
                	} else if (i == 2) { //pulse
                		if (info[i] == "NA") {
                			newData[i] = (double) ((35 - 30)/154);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 30)/154;
                		}
                	} else if (i == 3) { //respiratory_rate
                		if (info[i] == "NA") {
                			newData[i] = (double) ((9 - 8)/88);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 8)/88;
                		}
                	} else if (i == 4) { //temp_of_extremities
                		if (info[i] == "NA" || info[i] == "normal") {
                			newData[i] = 0.0;
                		} else if (info[i] == "warm") {
                			newData[i] = 0.33;
                		} else if (info[i] == "cool") {
                			newData[i] = 0.66;
                		} else if (info[i] == "cold") {
                			newData[i] = 1.0;
                		}
                	} else if (i == 5) { //peripheral_pulse
                		if (info[i] == "NA" || info[i] == "normal") {
                			newData[i] = 0.0;
                		} else if (info[i] == "increased") {
                			newData[i] = 0.33;
                		} else if (info[i] == "decreased") {
                			newData[i] = 0.66;
                		} else if (info[i] == "absent") {
                			newData[i] = 1.0;
                		}
                	} else if (i == 6) { //mucous_membrane
                		if (info[i] == "NA" || info[i] == "normal_pink") {
                			newData[i] = 0.0;
                		} else if (info[i] == "bright_pink") {
                			newData[i] = 0.2;
                		} else if (info[i] == "pale_pink") {
                			newData[i] = 0.4;
                		} else if (info[i] == "pale_cyanotic") {
                			newData[i] = 0.6;
                		} else if (info[i] == "bright_red") {
                			newData[i] = 0.8;
                		} else if (info[i] == "dark_cyanotic") {
                			newData[i] = 1.0;
                		}
                	} else if (i == 7) { //capillary_refill_time
                		if (info[i] == "NA" || info[i] == "less_3_sec") {
                			newData[i] = 0.0;
                		} else if (info[i] == "more_3_sec") {
                			newData[i] = 1.0;
                		}
                	} else if (i == 8) { //pain
                		if (info[i] == "NA" || info[i] == "alert") {
                			newData[i] = 0.0;
                		} else if (info[i] == "depressed") {
                			newData[i] = 0.25;
                		} else if (info[i] == "mild_pain") {
                			newData[i] = 0.50;
                		} else if (info[i] == "severe_pain") {
                			newData[i] = 0.75;
                		} else if (info[i] == "extreme_pain") {
                			newData[i] = 1.0;
                		}
                	} else if (i == 9) { //peristalsis
                		if (info[i] == "NA" || info[i] == "normal") {
                			newData[i] = 0.0;
                		} else if (info[i] == "hypermotile") {
                			newData[i] = 0.33;
                		} else if (info[i] == "hypomotile") {
                			newData[i] = 0.66;
                		} else if (info[i] == "absent") {
                			newData[i] = 1.0;
                		}
                	} else if (i == 10) { //abdominal_distention
                		if (info[i] == "NA" || info[i] == "none") {
                			newData[i] = 0.0;
                		} else if (info[i] == "slight") {
                			newData[i] = 0.33;
                		} else if (info[i] == "moderate") {
                			newData[i] = 0.66;
                		} else if (info[i] == "severe") {
                			newData[i] = 1.0;
                		}
                	} else if (i == 11) { //nasogastric_tube
                		if (info[i] == "NA" || info[i] == "none") {
                			newData[i] = 0.0;
                		} else if (info[i] == "slight") {
                			newData[i] = 0.5;
                		} else if (info[i] == "significant") {
                			newData[i] = 1.0;
                		} 
                	} else if (i == 12) { //nasogastric_reflux
                		if (info[i] == "NA" || info[i] == "none") {
                			newData[i] = 0.0;
                		} else if (info[i] == "less_1_liter") {
                			newData[i] = 0.5;
                		} else if (info[i] == "more_1_liter") {
                			newData[i] = 1.0;
                		} 
                	} else if (i == 13) { //nasogastric_reflux_ph
                		if (info[i] == "NA" || info[i] == "none") {
                			newData[i] = 0.0;
                		} else if (info[i] == "less_1_liter") {
                			newData[i] = 0.5;
                		} else if (info[i] == "more_1_liter") {
                			newData[i] = 1.0;
                		} 
                	}
                }
                data.add(newData);
            }
            System.out.println("data");
            for (Double[] pixels : data) {
            	for (Double pixel : pixels) {
            		System.out.print(pixel + " ");
            	}
            	System.out.println();
            }
            System.out.println("labels");
            for (Double[] labels : labels) {
            	for (Double label : labels) {
            		System.out.print(label + " ");
            	}
            	System.out.println();
            }
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
	}
}
