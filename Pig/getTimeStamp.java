package masti4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class getTimeStamp extends EvalFunc<String>{
	public String exec(Tuple input) throws IOException {
    	Calendar cal = Calendar.getInstance();
    	cal.getTime();
    	SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy_HH-mm-ss_");
    	return sdf.format(cal.getTime());
    }
}