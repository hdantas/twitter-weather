import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


// Decided to do with an helper class instead of a loop because its more elegant and easier to add features later ;)

// input -> (source:chararray, destination:chararray)

public class DetermineBlockCoordinates{

	public static void main (String[] args) {
		FlowPair pair;
		
		pair = new FlowPair("AACD","DCAB");
		System.out.println(pair.getOutputTuple());
	}
}

class FlowPair {

	String sourceCode;
	String destinationCode;

	double sourceMinLat;
	double sourceMaxLat;
	double sourceMinLong;
	double sourceMaxLong;

	double destinationMinLat;
	double destinationMaxLat;
	double destinationMinLong;
	double destinationMaxLong;

	double x_min = 0; //long_min
	double x_max = 0; //long_max
	double y_min = 0; //lat_min
	double y_max = 0; //lat_max

	public FlowPair(String sourceCode, String destinationCode) {
		
		this.sourceCode = sourceCode;
		this.destinationCode = destinationCode;
		// sourceMinLat = 0.0;
		// sourceMaxLat = 0.0;
		// sourceMinLong = 0.0;
		// sourceMinLong = 0.0;

		// destinationMinLat = 0.0;
		// destinationMaxLat = 0.0;
		// destinationMinLong = 0.0;
		// destinationMinLong = 0.0;
				
		computeCoordinateBoundaries();
	}
	
	private void computeCoordinateBoundaries(){

		y_min = 50.0;
		y_max = 55.0;
		x_min = 3.0;
		x_max = 8.0;

		for (int i=0; i<sourceCode.length(); i++){
			computeCoordinates(sourceCode.charAt(i),y_min,y_max,x_min,x_max);
		}

		sourceMinLat = y_min;
		sourceMaxLat = y_max;
		sourceMinLong = x_min;
		sourceMaxLong = x_max;

		//Reset boundaries
		y_min = 50.0;
		y_max = 55.0;
		x_min = 3.0;
		x_max = 8.0;

		for (int i=0; i<destinationCode.length(); i++){
			computeCoordinates(destinationCode.charAt(i),y_min,y_max,x_min,x_max);
		}

		destinationMinLat = y_min;
		destinationMaxLat = y_max;
		destinationMinLong = x_min;
		destinationMaxLong = x_max;

	}

	private String computeCoordinates(char code, double lat_min, double lat_max, double long_min, double long_max){
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

	// output -> (sourceMinLat:double,sourceMaxLat:double,sourceMinLong:double,sourceMaxLong:double,
	//			destinationMinLat:double,destinationMaxLat:double,destinationMinLong:double,destinationMaxLong:double)
	public String getOutputTuple() {
		String out = sourceMinLat + "\t" + sourceMaxLat + "\t" + sourceMinLong + "\t" + sourceMaxLong + "\t" +
					destinationMinLat + "\t" + destinationMaxLat + "\t" + destinationMinLong + "\t" + destinationMaxLong;
		return out;
	}
}