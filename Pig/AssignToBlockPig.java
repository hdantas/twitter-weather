package masti4;

import java.io.*;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class AssignToBlockPig extends EvalFunc<String>{

	public String exec(Tuple input) throws IOException {

		String y_str = (String)input.get(0); //latitude -> y
		String x_str = (String)input.get(1); //longitude -> x

		String level_str = null;
		String parentCode = null;
		
		if (input.size() == 4) { 
			level_str = (String)input.get(2);
			parentCode = (String)input.get(3);
		}

		Double x_double = 0.0;
		Double y_double = 0.0;
		int level = 0;

		
		System.err.format("%n%s%n%n",input);
		
		try {	
			x_double = Double.parseDouble(x_str);
			y_double = Double.parseDouble(y_str);
			if (level_str != null)
				level = Integer.parseInt(level_str);
		} catch (NumberFormatException e) {
				System.err.format("%nError! Failed to convert parsed coordinates to doubles%ninput tuple:%s%n%n",input);
		}  catch (NullPointerException e) {
			System.err.format("%nError! At least one coordinate is empty.%nInput tuple:%s%n%n",input);
		}

		String result = "";

		// GPSPoint point0 = new GPSPoint(x, y, <level>, <parentCode>);
		if (parentCode == null) {
			GPSPoint point = new GPSPoint(x_double, y_double);
			result = "x:" + point.getXcoordinate() + "\ty:" + point.getYcoordinate() + "\tnew block:" + point.findMyNewBlock() + '\n';			
		} else {
			GPSPoint point = new GPSPoint(x_double, y_double,level,parentCode);
			result = "x:" + point.getXcoordinate() + "\ty:" + point.getYcoordinate() + "\tparentCode:" + point.getParentCode() + "\tnew block:" + point.findMyNewBlock() + '\n';
			Grid parent = point.getParent();
			String blockCode = "";		
			if (parent.getChildren('A').belongsToGrid(x_double,y_double))
				blockCode += "A";
			if (parent.getChildren('B').belongsToGrid(x_double,y_double))
				blockCode += "B";
			if (parent.getChildren('C').belongsToGrid(x_double,y_double))
				blockCode += "C";
			if (parent.getChildren('D').belongsToGrid(x_double,y_double))
				blockCode += "D";
			System.err.format("%nblockCode: %s%n%n", blockCode);
		}

		return result;
	}
}

class GPSPoint {

	private double x_coordinate;
	private double y_coordinate;
	private int level;
	private String parentCode;
	private Grid parent;
	
	private static final double Y_MIN = 50.0; //lat_min
	private static final double Y_MAX = 55.0; //lat_max
	private static final double X_MIN = 3.0; //long_min
	private static final double X_MAX = 8.0; //long_max
	
	// FOR TESTING PURPOSES
	// private static final double X_MIN = 10.0; //long_min
	// private static final double X_MAX = 70.0; //long_max
	// private static final double Y_MIN = 10.0; //lat_min
	// private static final double Y_MAX = 30.0; //lat_max

	public GPSPoint (double x_coordinate, double y_coordinate) {
		constructorHelper(x_coordinate, y_coordinate, 0, "");
	}

	public GPSPoint (double x_coordinate, double y_coordinate, int level) {
		if (level == 0)
			constructorHelper(x_coordinate, y_coordinate, level, "");
		else
			System.err.format("%nError! Level is 0 so parentCode is ignored.%nx_coordinate:%f, y_coordinate:%f, level:%d%n%n",
				x_coordinate, y_coordinate, level);
	}

	public GPSPoint (double x_coordinate, double y_coordinate, int level, String parentCode) {
		constructorHelper(x_coordinate, y_coordinate, level, parentCode);
	}

	private void constructorHelper(double x_coordinate, double y_coordinate, int level, String parentCode) {
		if ((level == 0) && (parentCode.length() != 0))
			System.err.format("%nError! Level is 0 so parentCode is ignored.%nx_coordinate:%f, y_coordinate:%f, level:%d, parentCode:%s%n%n",
				x_coordinate, y_coordinate, level, parentCode);
		
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.level = level;
		this.parentCode = parentCode;

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