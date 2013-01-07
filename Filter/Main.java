import java.io.*;



public class Main {

	/**
	 * @param args
	 */
	
	public  static int counttabs(String line){
		
		int count=0;
		
		for(int i=1;i<=line.length();i++){
	  		
	  		String searchtab = line.substring(i-1, i);
	  		
	  		if(searchtab.equals("\t"))
	  			count =count +1;
	  		
		}
		
		return count;
		
	}
	
	public  static int tab_index(String line){
		
		int count=0;
		int index=0;
		
		for(int i=1;i<=line.length();i++){
	  		
	  		String searchtab = line.substring(i-1, i);
	  		
	  		if(searchtab.equals("\t"))
	  			count =count +1;
	  		
	  		if (count==8){
	  			index =count-1;
	  			break;
	  		}
	  		
		}
		
		return index;
		
	}
	
	public static String filtertabs(String line){
		
		if (counttabs(line) == 8) 
				return line;
		
		String text="";
		
		
	
		text =line.substring((tab_index(line)),(line.length()));
		
		text = text.replace("\t"," ");
		
		text = line.substring(0, (tab_index(line))) + text;
		
		return text;
	}
	
	public static String concatstr(String l1,String l2,String l3){
		
		return l1 +l2 +l3;
	}
	public static void main(String[] args) {
		
			  try{
				  // Open the file that is the first 
				  // command line parameter
				  
				
				  FileInputStream fstream = new FileInputStream("18dec_tweets_not_complete.txt");
			  
				  //Get the object of DataInputStream
				  DataInputStream in = new DataInputStream(fstream);
				  BufferedReader br = new BufferedReader(new InputStreamReader(in));
				  String strLine;
				  String strLine1;
				  
				  String printedtext="";
				  
				  //write to a file
				  FileWriter fw=new FileWriter("18dec_tweets_not_complete.txt");
			      PrintWriter pw;
			  
				  ////Read File Line By Line
			      int count = 0;
			      
				  while ((strLine = br.readLine()) != null )   {
			  // Print the content on the console
					  //System.out.println (strLine + "countabs = " + counttabs(strLine));
					  //System.out.println (strLine1+ "countabs = " + counttabs(strLine1));
					 
					  
					  fw = new FileWriter("18dec_tweets_filtered_not_complete.txt",true);
					  pw = new PrintWriter(fw,true);
					  
					  System.out.println("interation" + (count +287) +" count1= " + counttabs(strLine) );
					  count = count +1;
					  
					if (counttabs(strLine)>=8){
						
						
						
						pw.print("\n"+ filtertabs(strLine));
					}
					
					else
					{
						pw.print(strLine);
					}
					  	
						// System.out.println(printedtext);
					  fw.close();
					 pw.close();
				  }
			  //Close the input stream
				  in.close();
			    }catch (Exception e){//Catch exception if any
			  
			    	System.err.println("Error: " + e.getMessage());
			  
			    }
		}
	
}
			
