import java.io.*;
public class AssignToBlock {

	public static void main(String args[]) {
		String result = "";

	// boolean isDouble(String str) {
	// 	try {	
	// 		Double.parseDouble(str);
	// 		return true;
	// 	} catch (NumberFormatException e) {
	// 		return false;
	// 	}
	// }

		GPSPoint point0 = new GPSPoint(43.0, 17.0, 0);
		GPSPoint point1 = new GPSPoint(43.0, 17.0, 1, "D");
		GPSPoint point2 = new GPSPoint(43.0, 17.0, 2, "DA");
		GPSPoint point3 = new GPSPoint(43.0, 17.0, 3, "DD");
		GPSPoint point4 = new GPSPoint(43.0, 17.0, 1, "DA");

		printPoint("point0",point0);
		printPoint("point1",point1);
		printPoint("point2",point2);
		printPoint("point3",point3);
		printPoint("point4",point4);

		//writetofile(result,"out.txt");
	}
	
	static void printPoint (String point_name, GPSPoint point) {
		System.out.format("%s(%f, %f) from Block: %s, becomes Block: %s%n",
			point_name,point.getXcoordinate(),point.getYcoordinate(),point.getParentCode(),point.findMyNewBlock());

	}
	
	static void writetofile(String towrite,String filename) {
		try {
			// Create file 
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(towrite);
			//Close the output stream
			out.close();
		} catch (Exception e) {//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
}

class GPSPoint {

	private double x_coordinate;
	private double y_coordinate;
	private int level;
	private String parentCode;
	private Grid parent;
	
	// private static final Y_MIN = 50.0; //lat_min
	// private static final Y_MAX = 55.0; //lat_max
	// private static final X_MIN = 3.0; //long_min
	// private static final X_MAX = 8.0; //long_max
	private static final double X_MIN = 10.0; //long_min
	private static final double X_MAX = 70.0; //long_max
	private static final double Y_MIN = 10.0; //lat_min
	private static final double Y_MAX = 30.0; //lat_max
	
	public GPSPoint (double x_coordinate, double y_coordinate, int level, String parentCode) {
		constructorHelper(x_coordinate, y_coordinate, level, parentCode);
	}

	public GPSPoint (double x_coordinate, double y_coordinate, int level) {
		if (level == 0)
			constructorHelper(x_coordinate, y_coordinate, level, "");
		else
			System.err.format("%nError! Level is 0 so parentCode is ignored.%nx_coordinate:%f, y_coordinate:%f, level:%d%n%n",
				x_coordinate, y_coordinate, level);
	}

	private void constructorHelper(double x_coordinate, double y_coordinate, int level, String parentCode) {
		if ((level == 0) && (parentCode.length() != 0))
			System.err.format("%nError! Level is 0 so parentCode is ignored.%nx_coordinate:%f, y_coordinate:%f, level:%d, parentCode:%s%n%n",
				x_coordinate, y_coordinate, level, parentCode);
		
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;

		if (level > parentCode.length()) {
			this.level = parentCode.length();
			this.parentCode = parentCode;
			System.err.format("%nWarning! Level and parentCode mismatch. Advertised level is HIGHER than the length of the actual code." +
				"%nx_coordinate:%f, y_coordinate:%f, level:%d, parentCode:%s%n%n",
				x_coordinate, y_coordinate, level, parentCode);
		}
		else if (level < parentCode.length()) {
			this.level = level;
			this.parentCode = parentCode.substring(0,level);
			System.err.format("%nWarning! Level and parentCode mismatch. Advertised level is LOWER than the length of the actual code." +
				"%nx_coordinate:%f, y_coordinate:%f, level:%d, parentCode:%s%n%n",
				x_coordinate, y_coordinate, level, parentCode);
		}
		
		this.parent = new Grid(X_MIN,X_MAX,Y_MIN,Y_MAX);	
		for (int i=0; i<this.level; i++) {
			this.parent = this.parent.getChildren(this.parentCode.charAt(i));
		}
	}

	public double getXcoordinate() {
		return x_coordinate;
	}

	public double getYcoordinate() {
		return y_coordinate;
	}

	public int getLevel() {
		return level;
	}

	public String getParentCode() {
		return parentCode;
	}

	public Grid getParent() {
		return parent;
	}

	public String findMyNewBlock () {
		String blockCode = "";
		if (parent.getChildren('A').belongsToGrid(x_coordinate,y_coordinate))
			blockCode = "A";
		else if (parent.getChildren('B').belongsToGrid(x_coordinate,y_coordinate))
			blockCode = "B";
		else if (parent.getChildren('C').belongsToGrid(x_coordinate,y_coordinate))
			blockCode = "C";
		else if (parent.getChildren('D').belongsToGrid(x_coordinate,y_coordinate))
			blockCode = "D";
		else {
			blockCode = "X";
			System.err.format("%nError! This point did not find a valid children! Check if parent is correct." +
				"%nx_coordinate:%f, y_coordinate:%f, level:%d, parentCode:%s%n%n",
				x_coordinate, y_coordinate, level, parentCode);
		}

		if (parentCode != null)
			blockCode = parentCode + blockCode;

		return blockCode;
	}
}

class Grid {
	private double x_min;
	private double y_min;
	private double x_max;
	private double y_max;
	
	public Grid (double x_min, double x_max, double y_min, double y_max) {
		this.x_min = x_min;
		this.x_max = x_max;
		this.y_min = y_min;
		this.y_max = y_max;
	}
	
	public double getXmin() {
		return x_min;
	}

	public double getXmax() {
		return x_max;
	}

	public double getYmin() {
		return y_min;
	}

	public double getYmax() {
		return y_max;
	}

	public boolean belongsToGrid(double x_coordinate, double y_coordinate) {
		return (x_coordinate>=x_min) && (x_coordinate<=x_max) && (y_coordinate>=y_min) && (y_coordinate<=y_max);
		/*
		* There is a bit of redundancy in the return expression since all grids include the borders
		* for example a point that lies exactly in the middle of the grid will belong to A,B,C and D.
		* but since there is not way to define what borders belong to who without knowing what grid we are talking about
		* and this will be very rare its easier to do it this way, IMHO.
		*/
	}

	public Grid getChildren(char code) {
		double new_x_min = 0;
		double new_y_min = 0;
		double new_x_max = 0;
		double new_y_max = 0;
		if (code == 'A' || code == 'a'){
			new_x_min = x_min;
			new_x_max = x_min + (x_max-x_min)/2;
			new_y_min = y_min + (y_max-y_min)/2;
			new_y_max = y_max;
		} else if (code == 'B' || code == 'b'){
			new_x_min = x_min + (x_max-x_min)/2;
			new_x_max = x_max;
			new_y_min = y_min + (y_max-y_min)/2;;
			new_y_max = y_max;
		} else if (code == 'C' || code == 'c'){
			new_x_min = x_min;
			new_x_max = x_min + (x_max-x_min)/2;;
			new_y_min = y_min;
			new_y_max = y_min + (y_max-y_min)/2;
		} else if (code == 'D' || code == 'd'){
			new_x_min = x_min + (x_max-x_min)/2;
			new_x_max = x_max;
			new_y_min = y_min;
			new_y_max = y_min + (y_max-y_min)/2;
		} else
			System.err.format("%nError! Not a valid code! Must be A, B, C or D." + 
				"%nx_min:%f, x_max:%f, y_min:%f, y_max:%f, code:%c%n%n",
				x_min, x_max, y_min, y_max, code);
		
		return new Grid(new_x_min,new_x_max,new_y_min,new_y_max);
	}
}