import java.io.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

class Patterns 
{
//count \t userID \t userName \t messageID \t Date \t latitude \t longitude \t source \t tweet \n
	public static void main(String args[])
	{
		String tweet1 = "123	242714967	mediazaken	276724472889884672	Thu Dec 06 16:27:10 +0000 2012	-1000.000	-100	<a href=\"http://twitter.com/download/android\" rel=\"nofollow\">Twitter for Android</a>	@";
		Pattern pattern = Pattern.compile("[0-9]+\t[0-9]+\t[a-zA-Z]+\t[0-9]+\t[a-zA-Z0-9+: ]+\t[-]*[0-9]+[.0-9]*\t[-]*[0-9]+[.0-9]*\t[^\t]+\t");
		//Pattern pattern = Pattern.compile("[0-9]+");
		Matcher matcher = pattern.matcher(tweet1);
		if (matcher.find()) {
		    System.out.println(matcher.group(0)+"\t"+matcher.end()); //prints /{item}/
		} else {
		    System.out.println("Match not found");
		}
	}
}