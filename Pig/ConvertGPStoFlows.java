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

// input -> (userID:long, {(count:long,userID:long,userName:chararray,messageID:long,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray,gpsblock:chararray), (...)})

// output (one output tuple for each input tuple inside the bag) ->
// (userID:long, sourceGPSBlock:chararray, destinationGPSBlock:chararray, sourceDate:chararray, destinationDate:chararray, sourceMesssageID:long, destinationMesssageID:long)

public class ConvertGPStoFlows extends EvalFunc<DataBag> {

	public DataBag exec(Tuple input) throws IOException {

		BagFactory bagFactory = BagFactory.getInstance();
		DataBag outputBag = bagFactory.newDefaultBag();

		long userID;
		DataBag inputBag;

		try {
			userID = (long)input.get(0);
			inputBag = (DataBag)input.get(1);
		} catch (ExecException ee) {
			throw ee;
		}
		
		UserFlows flows = new UserFlows(userID,inputBag);
		outputBag = flows.getOutputDataBag();

		return outputBag;
	}

// output (one output tuple for each input tuple inside the bag) ->
// (userID:long, sourceGPSBlock:chararray, destinationGPSBlock:chararray, sourceDate:chararray, destinationDate:chararray, sourceMesssageID:long, destinationMesssageID:long)

	public Schema outputSchema(Schema input) throws RuntimeException{

		List<Schema.FieldSchema> listTupleFields = new ArrayList<Schema.FieldSchema>();

		Schema.FieldSchema userID = new Schema.FieldSchema("userID",DataType.LONG);
		listTupleFields.add(userID);
		Schema.FieldSchema sourceGPSBlock = new Schema.FieldSchema("sourceGPSBlock",DataType.CHARARRAY);
		listTupleFields.add(sourceGPSBlock);
		Schema.FieldSchema destinationGPSBlock = new Schema.FieldSchema("destinationGPSBlock",DataType.CHARARRAY);
		listTupleFields.add(destinationGPSBlock);
		Schema.FieldSchema sourceDate = new Schema.FieldSchema("sourceDate",DataType.CHARARRAY);
		listTupleFields.add(sourceDate);
		Schema.FieldSchema destinationDate = new Schema.FieldSchema("destinationDate",DataType.CHARARRAY);
		listTupleFields.add(destinationDate);
		Schema.FieldSchema sourceMesssageID = new Schema.FieldSchema("sourceMesssageID",DataType.LONG);
		listTupleFields.add(sourceMesssageID);
		Schema.FieldSchema destinationMesssageID = new Schema.FieldSchema("destinationMesssageID",DataType.LONG);
		listTupleFields.add(destinationMesssageID);

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
	BagFactory bagFactory;
	DataBag outputBag;

	private long userID;
	DataBag inputBag;
	
	public UserFlows(long userID, DataBag inputBag) {
		bagFactory = BagFactory.getInstance();
		outputBag = bagFactory.newDefaultBag();

		this.userID = userID;
		this.inputBag = inputBag;
	}
	
//input -> (userID:long, {(count:long,userID:long,userName:chararray,messageID:long,date:chararray,gps_lat:double,gps_long:double,source:chararray,tweet:chararray,gpsblock:chararray), (...)})
	public boolean validateTuplePair(Tuple sourceTuple, Tuple destinationTuple) throws IOException {

		String sourceGPSBlock;
		String sourceDate;
		long sourceMesssageID;

		String destinationGPSBlock;
		String destinationDate;
		long destinationMesssageID;

		try {
			sourceGPSBlock = (String) sourceTuple.get(9);
			sourceDate = (String) sourceTuple.get(4);
			sourceMesssageID = (long) sourceTuple.get(3);
			destinationGPSBlock = (String) destinationTuple.get(9);
			destinationDate = (String) destinationTuple.get(4);
			destinationMesssageID = (long) destinationTuple.get(3);
		} catch (ExecException ee) {
			throw ee;
		}

		return !(sourceGPSBlock.equals(destinationGPSBlock));
	}

// output (one output tuple for each input tuple inside the bag) ->
// (userID:long, sourceGPSBlock:chararray, destinationGPSBlock:chararray, sourceDate:chararray, destinationDate:chararray, sourceMesssageID:long, destinationMesssageID:long)
	public void addTuplePairToOutputBag(Tuple sourceTuple, Tuple destinationTuple) throws IOException{
		TupleFactory mTupleFactory = TupleFactory.getInstance();
		Tuple outputTuple = mTupleFactory.newTuple();

		String sourceGPSBlock;
		String sourceDate;
		long sourceMesssageID;
		String destinationGPSBlock;
		String destinationDate;
		long destinationMesssageID;

		try {
			sourceGPSBlock = (String) sourceTuple.get(9);
			sourceDate = (String) sourceTuple.get(4);
			sourceMesssageID = (long) sourceTuple.get(3);
			destinationGPSBlock = (String) destinationTuple.get(9);
			destinationDate = (String) destinationTuple.get(4);
			destinationMesssageID = (long) destinationTuple.get(3);
		} catch (ExecException ee) {
			throw ee;
		}

		outputTuple.append(userID);
		outputTuple.append(sourceGPSBlock);
		outputTuple.append(destinationGPSBlock);
		outputTuple.append(sourceDate);
		outputTuple.append(destinationDate);
		outputTuple.append(sourceMesssageID);
		outputTuple.append(destinationMesssageID);
		
		outputBag.add(outputTuple);
	}

	public DataBag getOutputDataBag() throws IOException{
		Tuple source;
		Tuple destination;
			
		Iterator it;
		long inputBagSize;
		
		it = inputBag.iterator();
		inputBagSize = inputBag.size();
		source = (Tuple)it.next();

		for (long i = 1; i < inputBagSize; i++) {
			destination = (Tuple)it.next();
			try {
				if (validateTuplePair(source,destination)) //check if flow is valid 
					addTuplePairToOutputBag(source,destination); //if so add it to the bag
			} catch (ExecException ee) {
				throw ee;
			}
			
			source = destination;
		}

		return outputBag;
	}
}