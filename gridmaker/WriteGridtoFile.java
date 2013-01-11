import java.io.*;
public class WriteGridtoFile{

	static double x_min = 0; //long_min
	static double x_max = 0; //long_max
	static double y_min = 0; //lat_min
	static double y_max = 0; //lat_max

	public static void main(String args[]){
		if (args.length != 2){
			System.err.println("Error! Please use format \"java WriteGridtoFile <code length> <outputfile>\"");
			return;
		}

		double lat_min,lat_max,long_min,long_max;
		lat_min = 50.0;
		lat_max = 55.0;
		long_min = 3.0;
		long_max = 8.0;
		writetofile(args[1],computeCodes(Integer.valueOf(args[0]),lat_min,lat_max,long_min,long_max));
	}

	static void writetofile(String filename, String towrite){
		try{
			// Create file 
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(towrite);
			
			//Close the output stream
			out.close();
		}catch (Exception e){ //Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	static String computeCodes(int depth, double lat_min, double lat_max, double long_min, double long_max){
		double[] y_min_parent, y_max_parent, x_min_parent, x_max_parent;
		y_min_parent = new double[20];
		y_max_parent = new double[20];
		x_min_parent = new double[20];
		x_max_parent = new double[20];

		y_min_parent[0] = lat_min;
		y_max_parent[0] = lat_max;
		x_min_parent[0] = long_min;
		x_max_parent[0] = long_max;
		
		// recursive(depth,j,out,code,y_min_parent,y_max_parent,x_min_parent,x_max_parent);
		return recursive(depth,0,"",y_min_parent,y_max_parent,x_min_parent,x_max_parent);
	}
	
	static String recursive(int depth, int j, String parentCode, double[] y_min_parent, double[] y_max_parent, double[] x_min_parent, double[] x_max_parent){
		String code = "";
		String out = "";

		for(char i='A'; i <='D'; i++){
			code = parentCode + String.valueOf(i); //append new code to the parent's
			out += code+'\t'+computeCoordinates(i,y_min_parent[j],y_max_parent[j],x_min_parent[j],x_max_parent[j])+'\n'; //add the limit coordinates
			y_min_parent[j+1] = y_min;
			y_max_parent[j+1] = y_max;
			x_min_parent[j+1] = x_min;
			x_max_parent[j+1] = x_max;
			
			if(j < (depth - 1)){ //block code still 'needs to grow'
				out += recursive(depth,j+1,code,y_min_parent,y_max_parent,x_min_parent,x_max_parent);
			}
		}

		j = 0;
		return out;
	}

	static String computeCoordinates(char code, double lat_min, double lat_max, double long_min, double long_max){
		String out = "";
		double delta_lat = lat_max - lat_min;
		double delta_long = long_max - long_min;
		
		if (code == 'A'){
			y_min = lat_min + delta_lat/2;
			y_max = lat_max; 
			x_min = long_min;
			x_max = long_max - delta_long/2;
		} else if (code == 'B'){
			y_min = lat_min + delta_lat/2;
			y_max = lat_max;
			x_min = long_min + delta_long/2;
			x_max = long_max;
		} else if (code == 'C'){
			y_min = lat_min;
			y_max = lat_max - delta_lat/2;
			x_min = long_min;
			x_max = long_max - delta_long/2;
		} else if (code == 'D'){
			y_min = lat_min;
			y_max = lat_max - delta_lat/2;
			x_min = long_min + delta_long/2;
			x_max = long_max;
		} else{
			out = "ERROR! code:" + code; 
			return out;
		}
		return out.format("%f\t%f\t%f\t%f",y_min,y_max,x_min,x_max);
	}
}
