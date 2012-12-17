package masti4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.pig.backend.executionengine.ExecException;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;
import org.apache.pig.EvalFunc;

// Decided to do with an helper class instead of a loop because its more elegant and easier to add features later ;)

// input -> (userID:int, {(count:int, userID:int, gps_lat:double, gps_long:double, gpsBlock:chararray), (...)})
// output (one output tuple for each input tuple inside de bag) -> (userID:int, sourceGPSBlock:chararray, destinationGPSBlock:chararray, sourceCount:int, destinationCount:int)

public class ConvertGPStoFlows extends EvalFunc<String> {
	public String exec(Tuple input) throws IOException {

		int userID;
		DataBag bag;
		Iterator it;
		long bagSize;
		Tuple source;
		Tuple destination;
		List<Tuple> tuplesList = new ArrayList<Tuple>();
		List<UserFlows> flowsList = new ArrayList<UserFlows>();

		try {
			userID = (int)input.get(0);
			bag = (DataBag)input.get(1);
			it = bag.iterator();
			bagSize = bag.size();
			source = (Tuple)it.next();
		} catch (ExecException ee) {
			throw ee;
		}
		

		for (int i = 1; i < bagSize; i++) {
			destination = (Tuple)it.next();
			UserFlows flow = new UserFlows(userID);
			
			flow.setSourceTuple(source);
			flow.setDestinationTuple(destination);
			tuplesList.add(flow.getOutputTuple());
			flowsList.add(flow);

			source = destination;
		}

		if (tuplesList.size() > 0) { // TODO change to output ordered bag with all appropriate tuples inside
			return tuplesList.get(0).toDelimitedString(",");
			//return flowsList.get(0).getOutputString();
		}
		else
			return "";
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
		
		outputTuple.append(userID);
		outputTuple.append(sourceGPSBlock);
		outputTuple.append(destinationGPSBlock);
		outputTuple.append(sourceCount);
		outputTuple.append(destinationCount);
		return outputTuple;
	}

	// FOR TESTING PURPOSES ONLY
	public String getOutputString() {
		String outputString = String.format("userID:%d, sourceGPSBlock:%s, destinationGPSBlock:%s, sourceCount:%d, destinationCount:%d%n",
			userID,sourceGPSBlock,destinationGPSBlock,sourceCount,destinationCount);
		
		return outputString;
	}
}