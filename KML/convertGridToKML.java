package masti4;

import java.io.*;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;

public class convertGridToKML extends EvalFunc<String>{

	private final String yellowStyle="#yellowPoly";
	private final String greenStyle="#greenPoly";
	private final String redStyle="#redPoly";
	private final String blueStyle="#bluePoly";
	private final String errorStyle="#errorPoly";
	private final String header="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
"<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"+
	"\t<Document>\n"+
		"\t\t<Style id=\"bluePoly\">\n"+
			"\t\t\t<LineStyle>\n"+
				"\t\t\t\t<color>50783C00</color>\n"+
				"\t\t\t\t<width>4</width>\n"+
			"\t\t\t</LineStyle>\n"+
			"\t\t\t<PolyStyle>\n"+
				"\t\t\t\t<color>50967800</color>\n"+
			"\t\t\t</PolyStyle>\n"+
		"\t\t</Style>\n"+
		"\t\t<Style id=\"redPoly\">\n"+
			"\t\t\t<LineStyle>\n"+
				"\t\t\t\t<color>501400C8</color>\n"+
				"\t\t\t\t<width>4</width>\n"+
			"\t\t\t</LineStyle>\n"+
			"\t\t\t<PolyStyle>\n"+
				"\t\t\t\t<color>501400F0</color>\n"+
			"\t\t\t</PolyStyle>\n"+
		"\t\t</Style>\n"+
		"\t\t<Style id=\"greenPoly\">\n"+
			"\t\t\t<LineStyle>\n"+
				"\t\t\t\t<color>5078DC00</color>\n"+
				"\t\t\t\t<width>4</width>\n"+
			"\t\t\t</LineStyle>\n"+
			"\t\t\t<PolyStyle>\n"+
				"\t\t\t\t<color>5000B414</color>\n"+
			"\t\t\t</PolyStyle>\n"+
		"\t\t</Style>\n"+
		"\t\t<Style id=\"yellowPoly\">\n"+
			"\t\t\t<LineStyle>\n"+
				"\t\t\t\t<color>5014F0FA</color>\n"+
				"\t\t\t\t<width>4</width>\n"+
			"\t\t\t</LineStyle>\n"+
			"\t\t\t<PolyStyle>\n"+
				"\t\t\t\t<color>5014F0FA</color>\n"+
			"\t\t\t</PolyStyle>\n"+
		"\t\t</Style>\n"+
		"\t\t<Style id=\"errorPoly\">\n"+
			"\t\t\t<LineStyle>\n"+
				"\t\t\t\t<color>50282828</color>\n"+
				"\t\t\t\t<width>4</width>\n"+
			"\t\t\t</LineStyle>\n"+
			"\t\t\t<PolyStyle>\n"+
				"\t\t\t\t<color>505A5A5A</color>\n"+
			"\t\t\t</PolyStyle>\n"+
		"\t\t</Style>";
	private final String footer = "</Document></kml>";

    public String exec(Tuple input) throws IOException{
		
		if (input.size() != 5) {
		    return "Error";
		}

		String code = "";
		Double minLat = 0.0;
		Double minLong = 0.0;
		Double maxLat = 0.0;
		Double maxLong = 0.0;
		String result = "";
		String colorStyle ="";

		try {	
			code = (String)input.get(0);
			minLat = (Double)input.get(1);
			maxLat = (Double)input.get(2);
			minLong = (Double)input.get(3);
			maxLong = (Double)input.get(4);
		} catch (NumberFormatException e) {
			System.err.format("%nError! Failed to convert parsed coordinates to doubles%ninput tuple:%s%n%n",input);
			return result;
		}  catch (NullPointerException e) {
			System.err.format("%nError! At least one coordinate is empty.%nInput tuple:%s%n%n",input);
			return result;
		}

		switch (code.charAt(code.length()-1)) { //get last char of Code to determine colorStyle
			case 'A':  colorStyle = yellowStyle;
				break;
			case 'B':  colorStyle = greenStyle;
				break;
			case 'C':  colorStyle = redStyle;
				break;
			case 'D':  colorStyle = blueStyle;
				break;
			default: colorStyle = errorStyle;
				break;
		}

		result = "<Placemark>\n"+
						"\t<styleUrl>"+colorStyle+"</styleUrl>\n"+
						"\t<name>"+code+"</name>\n"+
						"\t<Polygon>\n"+
							"\t\t<extrude>0</extrude>\n"+
							"\t\t<altitudeMode>absolute</altitudeMode>\n"+
							"\t\t<outerBoundaryIs>\n"+
								"\t\t\t<LinearRing>\n"+
									"\t\t\t\t<coordinates>"+
															maxLong+','+minLat+','+5000*code.length()+' '+
															maxLong+','+maxLat+','+5000*code.length()+' '+
															minLong+','+maxLat+','+5000*code.length()+' '+
															minLong+','+minLat+','+5000*code.length()+
									"</coordinates>\n"+
								"\t\t\t</LinearRing>\n"+
							"\t\t</outerBoundaryIs>\n"+
						"\t</Polygon>\n"+
					"</Placemark>";


		if (code.equals("A")) {
			result = header+"\n<Folder><name>Folder 1</name>\n"+result;
		} else if (code.equals("AA")) {
			result = "<Folder><name>Folder 2</name>\n"+result;
		} else if (code.equals("AAA")) {
			result = "<Folder><name>Folder 3</name>\n"+result;
		} else if (code.equals("AAAA")) {
			result = "<Folder><name>Folder 4</name>\n"+result;
		} else if (code.equals("D") || code.equals("DD") || code.equals("DDD")){
			result += "\n</Folder>";
		} else if (code.equals("DDDD")) {
			result += "\n</Folder>\n"+footer;
		}

		return result;
	}
}
