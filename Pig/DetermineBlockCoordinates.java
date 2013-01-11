package masti4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.DataType;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.EvalFunc;
import org.apache.pig.impl.logicalLayer.FrontendException;
import org.apache.pig.impl.logicalLayer.schema.Schema;


// Decided to do with an helper class instead of a loop because its more elegant and easier to add features later ;)

// input -> (source:chararray, destination:chararray)

public class DetermineBlockCoordinates extends EvalFunc<Tuple> {

	public Tuple exec(Tuple input) throws IOException {
		FlowPair pair;
		String sourceCode, destinationCode;

		try {
			sourceCode = (String)input.get(0);
			destinationCode = (String)input.get(1);
		} catch (ExecException ee) {
			throw ee;
		}
		
		pair = new FlowPair(sourceCode,destinationCode);
		return pair.getOutputTuple();
	}

	// output -> (sourceMinLat:double,sourceMaxLat:double,sourceMinLong:double,sourceMaxLong:double,
	//			destinationMinLat:double,destinationMaxLat:double,destinationMinLong:double,destinationMaxLong:double)
	public Schema outputSchema(Schema input) throws RuntimeException{

		List<Schema.FieldSchema> listTupleFields = new ArrayList<Schema.FieldSchema>();

		Schema.FieldSchema sourceMinLat = new Schema.FieldSchema("sourceMinLat",DataType.DOUBLE);
		listTupleFields.add(sourceMinLat);
		Schema.FieldSchema sourceMaxLat = new Schema.FieldSchema("sourceMaxLat",DataType.DOUBLE);
		listTupleFields.add(sourceMaxLat);
		Schema.FieldSchema sourceMinLong = new Schema.FieldSchema("sourceMinLong",DataType.DOUBLE);
		listTupleFields.add(sourceMinLong);
		Schema.FieldSchema sourceMaxLong = new Schema.FieldSchema("sourceMaxLong",DataType.DOUBLE);
		listTupleFields.add(sourceMaxLong);

		Schema.FieldSchema destinationMinLat = new Schema.FieldSchema("destinationMinLat",DataType.DOUBLE);
		listTupleFields.add(destinationMinLat);
		Schema.FieldSchema destinationMaxLat = new Schema.FieldSchema("destinationMaxLat",DataType.DOUBLE);
		listTupleFields.add(destinationMaxLat);
		Schema.FieldSchema destinationMinLong = new Schema.FieldSchema("destinationMinLong",DataType.DOUBLE);
		listTupleFields.add(destinationMinLong);
		Schema.FieldSchema destinationMaxLong = new Schema.FieldSchema("destinationMaxLong",DataType.DOUBLE);
		listTupleFields.add(destinationMaxLong);

		Schema.FieldSchema tupleFs;
		Schema.FieldSchema bagFs;

		try {
			Schema tupleSchema = new Schema(listTupleFields);
			tupleFs = new Schema.FieldSchema("tuple_of_flowCoordinates",tupleSchema,DataType.TUPLE);
		} catch (FrontendException e) {
			throw new RuntimeException("Unable to compute output schema.");
		}

		return new Schema(tupleFs);
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
	public Tuple getOutputTuple() {
		
		TupleFactory mTupleFactory = TupleFactory.getInstance();
		Tuple outputTuple = mTupleFactory.newTuple();
		outputTuple.append(sourceMinLat);
		outputTuple.append(sourceMaxLat);
		outputTuple.append(sourceMinLong);
		outputTuple.append(sourceMaxLong);

		outputTuple.append(destinationMinLat);
		outputTuple.append(destinationMaxLat);
		outputTuple.append(destinationMinLong);
		outputTuple.append(destinationMaxLong);

		return outputTuple;
	}
}