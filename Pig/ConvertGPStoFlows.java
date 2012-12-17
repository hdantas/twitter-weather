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

// input -> (userID:int, {(count:int, userID:int, gps_lat:double, gps_long:double, gpsBlock:chararray), (...)})
// output (one output tuple for each input tuple inside de bag) -> (userID:int, sourceGPSBlock:chararray, destinationGPSBlock:chararray, sourceCount:int, destinationCount:int)

public class ConvertGPStoFlows extends EvalFunc<DataBag> {
	public DataBag exec(Tuple input) throws IOException {

		BagFactory bagFactory = BagFactory.getInstance();
		DataBag outputBag = bagFactory.newDefaultBag();

		int userID;
		DataBag inputBag;
		Iterator it;
		long inputBagSize;
		
		Tuple source;
		Tuple destination;

		try {
			userID = (int)input.get(0);
			inputBag = (DataBag)input.get(1);
			it = inputBag.iterator();
			inputBagSize = inputBag.size();
			source = (Tuple)it.next();
		} catch (ExecException ee) {
			throw ee;
		}
		

		for (int i = 1; i < inputBagSize; i++) {
			destination = (Tuple)it.next();
			UserFlows flow = new UserFlows(userID);
			
			flow.setSourceTuple(source);
			flow.setDestinationTuple(destination);
			
			Tuple outputTuple = flow.getOutputTuple();
			if (outputTuple != null) 
				outputBag.add(outputTuple);

			source = destination;
		}

		// TODO change to output ordered bag with all appropriate tuples inside
		return outputBag;
	}

	// output (one output tuple for each input tuple inside de bag) -> 
	//(userID:int, sourceGPSBlock:chararray, destinationGPSBlock:chararray, sourceCount:int, destinationCount:int)

	public Schema outputSchema(Schema input) {

		List<Schema.FieldSchema> listTupleFields = new ArrayList<Schema.FieldSchema>();

		Schema.FieldSchema userID = new Schema.FieldSchema("userID",DataType.INTEGER);
		listTupleFields.add(userID);
		Schema.FieldSchema sourceGPSBlock = new Schema.FieldSchema("sourceGPSBlock",DataType.CHARARRAY);
		listTupleFields.add(sourceGPSBlock);
		Schema.FieldSchema destinationGPSBlock = new Schema.FieldSchema("destinationGPSBlock",DataType.CHARARRAY);
		listTupleFields.add(destinationGPSBlock);
		Schema.FieldSchema sourceCount = new Schema.FieldSchema("sourceCount",DataType.INTEGER);
		listTupleFields.add(sourceCount);
		Schema.FieldSchema destinationCount = new Schema.FieldSchema("destinationCount",DataType.INTEGER);
		listTupleFields.add(destinationCount);

		Schema.FieldSchema tupleFs;
		Schema.FieldSchema bagFs;

		try {
			Schema tupleSchema = new Schema(listTupleFields);
			tupleFs = new Schema.FieldSchema("tuple_of_flowFields",tupleSchema,DataType.TUPLE);

			Schema bagSchema = new Schema(tupleFs);
			bagFs = new Schema.FieldSchema("bag_of_flowTuples",bagSchema,DataType.BAG);
		} catch (FrontendException e) {
			throw new RuntimeException("Unable to compute output schema.");
		}

		return new Schema(bagFs);
	}
}

class UserFlows {
	private int userID;
	
	private int sourceCount;
	private int destinationCount;
	
	private double sourceLatitude;
	private double sourceLongitude;
	
	private double destinationLatitude;
	private double destinationLongitude;
	
	private String sourceGPSBlock;
	private String destinationGPSBlock;

	public UserFlows(int userID) {
		this.userID = userID;
	}
	
	//input -> (userID:int, {(count:int, userID:int, gps_lat:double, gps_long:double, gpsBlock:chararray), (...)})
	public void setSourceTuple(Tuple sourceTuple) throws IOException {
		try {
			sourceCount = (int)sourceTuple.get(0);
			sourceLatitude = (double)sourceTuple.get(2);
			sourceLongitude = (double)sourceTuple.get(3);
			sourceGPSBlock = (String)sourceTuple.get(4);
		} catch (ExecException ee) {
			throw ee;
		}
		
	}

	public void setDestinationTuple(Tuple destinationTuple) throws IOException {
		try {
			destinationCount = (int)destinationTuple.get(0);
			destinationLatitude = (double)destinationTuple.get(2);
			destinationLongitude = (double)destinationTuple.get(3);
			destinationGPSBlock = (String)destinationTuple.get(4);
		} catch (ExecException ee) {
			throw ee;
		}
	}


	// output (one output tuple for each input tuple inside de bag) -> (userID:int, sourceGPSBlock:chararray, destinationGPSBlock:chararray, sourceCount:int, destinationCount:int)
	public Tuple getOutputTuple() {
		TupleFactory mTupleFactory = TupleFactory.getInstance();
		Tuple outputTuple = mTupleFactory.newTuple();
		if(!sourceGPSBlock.equals(destinationGPSBlock)) {
			outputTuple.append(userID);
			outputTuple.append(sourceGPSBlock);
			outputTuple.append(destinationGPSBlock);
			outputTuple.append(sourceCount);
			outputTuple.append(destinationCount);
			return outputTuple;
		}
		else //to avoid counting flows inside the same block
			return null;
	}

	// FOR TESTING PURPOSES ONLY
	public String getOutputString() {
		String outputString = String.format("userID:%d, sourceGPSBlock:%s, destinationGPSBlock:%s, sourceCount:%d, destinationCount:%d%n",
			userID,sourceGPSBlock,destinationGPSBlock,sourceCount,destinationCount);
		
		return outputString;
	}
}