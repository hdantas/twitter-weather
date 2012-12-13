package myUDF;

import java.io.*;
import java.io.IOException;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class mapToGrid extends EvalFunc<String>
{


public String exec(Tuple input) throws IOException {
		if (input == null || input.size() == 0)
			return null;
		try{
			String word = (String)input.get(0);
			String result = word.toLowerCase().replaceAll("[^a-zA-Z0-9 ]+","");
			return result;
		} catch (Exception e) {
			// Throwing an exception will cause the task to fail.
			throw new IOException("Something bad happened!", e);
		}
	}

	