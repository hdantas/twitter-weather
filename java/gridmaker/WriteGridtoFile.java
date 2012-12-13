import java.io.*;
class mapToGrid
{
	static double x_min = 0; //long_min
	static double x_max = 0; //long_max
	static double y_min = 0; //lat_min
	static double y_max = 0; //lat_max
   
   public static void main(String args[])
	{
		double lat_min,lat_max,long_min,long_max;
		lat_min = 50.0;
		lat_max = 55.0;
		long_min = 3.0;
		long_max = 8.0;
		String result = computeCodes(lat_min,lat_max,long_min,long_max);
		writetofile(result);
		return;
	}
	static void writetofile(String towrite)
	{
		try{
		// Create file 
		FileWriter fstream = new FileWriter("out.txt");
		BufferedWriter out = new BufferedWriter(fstream);
		out.write(towrite);
		
		//Close the output stream
		out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	static String computeCodes(double lat_min, double lat_max, double long_min, double long_max){
		int code = 65; // 65 = 'A'
		String out = "";
		double[] y_min_parent, y_max_parent, x_min_parent, x_max_parent;
		y_min_parent = new double[3];
		y_max_parent = new double[3];
		x_min_parent = new double[3];
		x_max_parent = new double[3];
		
		for(char a='A';a<='D';a++){
			out += Character.toString(a)+'\t'+computeCoordinates(a,lat_min,lat_max,long_min,long_max)+'\n';
			y_min_parent[0] = y_min;
			y_max_parent[0] = y_max;
			x_min_parent[0] = x_min;
			x_max_parent[0] = x_max;			
			for(char b='A';b<='D';b++){
				out += Character.toString(a)+Character.toString(b)+'\t'+computeCoordinates(b,y_min_parent[0],y_max_parent[0],x_min_parent[0],x_max_parent[0])+'\n';
				y_min_parent[1] = y_min;
				y_max_parent[1] = y_max;
				x_min_parent[1] = x_min;
				x_max_parent[1] = x_max;
				for(char c='A';c<='D';c++){
					out += Character.toString(a)+Character.toString(b)+Character.toString(c)+'\t'+computeCoordinates(c,y_min_parent[1],y_max_parent[1],x_min_parent[1],x_max_parent[1])+'\n';
					y_min_parent[2] = y_min;
					y_max_parent[2] = y_max;
					x_min_parent[2] = x_min;
					x_max_parent[2] = x_max;
					for(char d='A';d<='D';d++){
						out += Character.toString(a)+Character.toString(b)+Character.toString(c)+Character.toString(d)+'\t'+computeCoordinates(d,y_min_parent[2],y_max_parent[2],x_min_parent[2],x_max_parent[2])+'\n';
					}
				}
			}
		}
		return out;
	}
	
	static String computeCoordinates(char code, double lat_min, double lat_max, double long_min, double long_max) {
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
		
		return out.format("%f %f\t%f %f",y_min,y_max,x_min,x_max);
	}
}

