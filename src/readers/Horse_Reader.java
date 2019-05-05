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
                Double[] newLabel = new Double[3];
                for (int i = 0; i < info.length; i++) { //EDA pre-processing           	
                	if (i == 0) { //age
                		if (info[i].equals("adult")) {
                			newData[i] = 1.0;
                		} else {
                			newData[i] = 0.0;
                		}
                	} else if (i == 1) { //rectal_temp
                		if (info[i].equals("NA")) {
                			newData[i] = (37.8 - 35.4)/5.4;
                		} else {
                			newData[i] = (Double.valueOf(info[i]) - 35.4)/5.4;
                		}
                	} else if (i == 2) { //pulse
                		if (info[i].equals("NA")) {
                			newData[i] = (double) ((35 - 30)/154);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 30)/154;
                		}
                	} else if (i == 3) { //respiratory_rate
                		if (info[i].equals("NA")) {
                			newData[i] = (double) ((9 - 8)/88);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 8)/88;
                		}
                	} else if (i == 4) { //temp_of_extremities
                		if (info[i].equals("NA") || info[i].equals("normal")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("warm")) {
                			newData[i] = 0.33;
                		} else if (info[i].equals("cool")) {
                			newData[i] = 0.66;
                		} else if (info[i].equals("cold")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 5) { //peripheral_pulse
                		if (info[i].equals("NA") || info[i].equals("normal")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("increased")) {
                			newData[i] = 0.33;
                		} else if (info[i].equals("decreased")) {
                			newData[i] = 0.66;
                		} else if (info[i].equals("absent")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 6) { //mucous_membrane
                		if (info[i].equals("NA") || info[i].equals("normal_pink")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("bright_pink")) {
                			newData[i] = 0.2;
                		} else if (info[i].equals("pale_pink")) {
                			newData[i] = 0.4;
                		} else if (info[i].equals("pale_cyanotic")) {
                			newData[i] = 0.6;
                		} else if (info[i].equals("bright_red")) {
                			newData[i] = 0.8;
                		} else if (info[i].equals("dark_cyanotic")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 7) { //capillary_refill_time
                		if (info[i].equals("NA") || info[i].equals("less_3_sec")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("more_3_sec")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 8) { //pain
                		if (info[i].equals("NA") || info[i].equals("alert")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("depressed")) {
                			newData[i] = 0.25;
                		} else if (info[i].equals("mild_pain")) {
                			newData[i] = 0.50;
                		} else if (info[i].equals("severe_pain")) {
                			newData[i] = 0.75;
                		} else if (info[i].equals("extreme_pain")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 9) { //peristalsis
                		if (info[i].equals("NA") || info[i].equals("normal")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("hypermotile")) {
                			newData[i] = 0.33;
                		} else if (info[i].equals("hypomotile")) {
                			newData[i] = 0.66;
                		} else if (info[i].equals("absent")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 10) { //abdominal_distention
                		if (info[i].equals("NA") || info[i].equals("none")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("slight")) {
                			newData[i] = 0.33;
                		} else if (info[i].equals("moderate")) {
                			newData[i] = 0.66;
                		} else if (info[i].equals("severe")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 11) { //nasogastric_tube
                		if (info[i].equals("NA") || info[i].equals("none")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("slight")) {
                			newData[i] = 0.5;
                		} else if (info[i].equals("significant")) {
                			newData[i] = 1.0;
                		} 
                	} else if (i == 12) { //nasogastric_reflux
                		if (info[i].equals("NA") || info[i].equals("none")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("less_1_liter")) {
                			newData[i] = 0.5;
                		} else if (info[i].equals("more_1_liter")) {
                			newData[i] = 1.0;
                		} 
                	} else if (i == 13) { //nasogastric_reflux_ph
                		if (info[i].equals("NA")) {
                			newData[i] = (double) ((3 - 1)/6.5);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 1)/6.5;
                		}
                	} else if (i == 14) { //rectal_exam_feces
                		if (info[i].equals("NA") || info[i].equals("normal")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("increased")) {
                			newData[i] = 0.33;
                		} else if (info[i].equals("reduced")) {
                			newData[i] = 0.66;
                		} else if (info[i].equals("absent")) {
                			newData[i] = 1.0;
                		} 
                	} else if (i == 15) { //abdomen
                		if (info[i].equals("NA") || info[i].equals("normal")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("other")) {
                			newData[i] = 0.25;
                		} else if (info[i].equals("firm")) {
                			newData[i] = 0.5;
                		} else if (info[i].equals("distend_small")) {
                			newData[i] = 1.0;
                		} 
                	} else if (i == 16) { //packed_cell_volume
                		if (info[i].equals("NA")) {
                			newData[i] = (double) ((40 - 23)/52);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 23)/52;
                		}
                	} else if (i == 17) { //total_protein
                		if (info[i].equals("NA")) {
                			newData[i] = (double) ((6.75 - 3.3)/85.7);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 3.3)/85.7;
                		}
                	} else if (i == 18) { //abdomen
                		if (info[i].equals("NA") || info[i].equals("clear")) {
                			newData[i] = 0.0;
                		} else if (info[i].equals("cloudy")) {
                			newData[i] = 0.5;
                		} else if (info[i].equals("serosanguious")) {
                			newData[i] = 1.0;
                		}
                	} else if (i == 19) { //total_protein
                		if (info[i].equals("NA")) {
                			newData[i] = (double) ((2.3 - 0.1)/10);
                		} else {
                			newData[i] = (double) (Double.valueOf(info[i]) - 0.1)/10;
                		}
                	} else if (i == 19) { //outcome
                		if (info[i].equals("lived")) {
                			newLabel[0] = 1.0;
                			newLabel[1] = 0.0;
                			newLabel[2] = 0.0;
                		} else if (info[i].equals("euthanized")) {
                			newLabel[0] = 0.0;
                			newLabel[1] = 1.0;
                			newLabel[2] = 0.0;
                		} else if (info[i].equals("died")) {
                			newLabel[0] = 0.0;
                			newLabel[1] = 0.0;
                			newLabel[2] = 1.0;
                		}
                	}
                }
                data.add(newData);
                labels.add(newLabel);
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
