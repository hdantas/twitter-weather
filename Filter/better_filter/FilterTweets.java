import java.io.*;
class FilterTweets 
{
   public static void main(String args[])
	{

	}

// valid tweet:
//count \t userID \t userName \t messageID \ Date \t latitude \t longitude \t source \t tweet \n
	private void writeToFile(String filename, String linetowrite) {
      try{
		// Create file 
		FileWriter fstream = new FileWriter(filename);
        BufferedWriter out = new BufferedWriter(fstream);
		out.write(linetowrite);
		//Close the output stream
		out.close();
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void readFromFile(String filename) {
		try{
		// Open the file that is the first 
		// command line parameter
		FileInputStream fstream = new FileInputStream(filename);
		// Get the object of DataInputStream
		DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null) 	{
			// Print the content on the console
			System.out.println (strLine);
		}
		//Close the input stream
		in.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
//remove unpropper tabs and newlines
//exclude tweets withou geoTag (with -1000,-1000)
//exclude tweets outside netherlands (with coordinates outside 50 <lat< 55; 3 <long< 8)
	private void cleanTweet(String tweet){


	}
	
	private void cleanTweet(String tweet){
		
	}
}

