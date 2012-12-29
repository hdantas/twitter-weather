package masti4;

import java.io.*;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class convertTweetsToKML extends EvalFunc<String>{

	private final String yellowStyle="#yellowPoly";
	private final String greenStyle="#greenPoly";
	private final String redStyle="#redPoly";
	private final String blueStyle="#bluePoly";
	private final String errorStyle="#errorPoly";
	private final String header="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
"<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"+"\t<Document>\n";
	private final String footer = "</Document></kml>";

    public String exec(Tuple input) throws IOException{
		
		if (input.size() != 4) {
		    return "Error";
		}

		int count = 0;
		Double latitude = 0.0;
		Double longitude = 0.0;
		int total_count = 0;
		String result = "";
		
		try {	
			count = (int)input.get(0);
			latitude = (Double)input.get(1);
			longitude = (Double)input.get(2);
			total_count = (int)input.get(3);
		} catch (NumberFormatException e) {
			System.err.format("%nError! Failed to convert parsed coordinates to doubles%ninput tuple:%s%n%n",input);
			return result;
		}  catch (NullPointerException e) {
			System.err.format("%nError! At least one coordinate is empty.%nInput tuple:%s%n%n",input);
			return result;
		}

		result = "<Placemark>\n"+
					"\t<name>"+count+"</name>\n"+
					"\t<Point>\n"+
						"\t\t<coordinates>"+longitude+','+latitude+','+0+"</coordinates>\n"+
					"\t</Point>\n"+
				"</Placemark>";

		if (count == 0) {
			result = header+"\n<Folder><name>Tweets</name>\n"+result;
		}
		
		if (count == total_count) {
			result += "\n</Folder>\n"+footer;
		}

		return result;
	}
}
