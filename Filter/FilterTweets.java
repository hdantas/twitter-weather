import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class FilterTweets 
{

//count \t userID \t userName \t messageID \t Date \t latitude \t longitude \t source \t tweet \n
	public static void main(String args[])
	{
		if (args.length !=2){
			System.err.println("Error! Use syntax \"java FilterTweets <inputfilename> <outputfilename>");
			return;
		}

		List<String> filteredTweets = new ArrayList<String>(); //each item is a correctly formed tweet
		List<String> allTweets = readFromFile(args[0]); //each item is a line of text

		ListIterator<String> allTweetsIterator = allTweets.listIterator();

		String tweet;
		String newTweet;

		Pattern rightPattern = Pattern.compile("[0-9]+\t[0-9]+\t[a-zA-Z_0-9]+\t[0-9]+\t[a-zA-Z0-9+: ]+\t5[0-5][.0-9]*\t[3-8][.0-9]*\t[^\t]+\t");
		Pattern wrongCoordinatesPattern = Pattern.compile("[0-9]+\t[0-9]+\t[a-zA-Z_0-9]+\t[0-9]+\t[a-zA-Z0-9+: ]+\t[-]*[0-9]+[.0-9]*\t[-]*[0-9]+[.0-9]*\t[^\t]+\t");
		Matcher rightMatcher;
		Matcher wrongCoordinatesMatcher;

		boolean disregardTweet = false;
		
		while (allTweetsIterator.hasNext()) {//while there are tweets
			tweet = allTweetsIterator.next();
			rightMatcher = rightPattern.matcher(tweet);

			if (rightMatcher.find()) { //find beginning of a tweet
			    disregardTweet = false;
			    newTweet = rightMatcher.group() + tweet.substring(rightMatcher.end(),tweet.length()).replace('\t',' '); //beginning of tweet + tweet content without tabs
			    filteredTweets.add(newTweet);
			}
			else {
				wrongCoordinatesMatcher = wrongCoordinatesPattern.matcher(tweet);
				
				if (disregardTweet) //still waiting for wrong tweet to finish
					continue;
				else if(wrongCoordinatesMatcher.find()){ //this tweet has the wrong coordinates
					disregardTweet = true;
					continue;
				} else //this line still belongs to the previous tweet's content
					newTweet = tweet.replace('\t',' '); //remove possibly existing \t
					newTweet = filteredTweets.remove(filteredTweets.size() - 1) + newTweet; //append the text to the last tweet of the list
					filteredTweets.add(newTweet);
			}
		}

		writeToFile(args[1],filteredTweets);
	}

	private static void writeToFile(String filename, List<String> listToWrite) {
		ListIterator<String> listToWriteIterator = listToWrite.listIterator();

		try{
			// Create file 
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			while (listToWriteIterator.hasNext())
				out.write(listToWriteIterator.next()+'\n');

			out.close();
		} catch (Exception e){//Catch exception if any
			System.err.println("Error writing to file: " + e.getMessage());
		}
	}

	private static List<String> readFromFile(String filename) {
		List<String> allTweets = new ArrayList<String>(); //each item is a line of text

		try{
			FileInputStream fstream = new FileInputStream(filename);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null)
				allTweets.add(strLine);
			in.close();
		} catch (Exception e){//Catch exception if any
			System.err.println("Error reading file: " + e.getMessage());
		}
		return allTweets;
	}	
}