import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.Writer;

import java.io.InputStreamReader;
import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import com.google.gson.Gson;
import java.io.*;

import java.text.SimpleDateFormat;
import java.util.*;


public class get_stream {
	
	
	public static void method(){
		
		String twitterusername = "mastitwit";
		String twitterpassword = "ema0188";
	
		//String wordstosearch = "3.21,51.36,4.21,52.36,4.21,51.36,5.21,53.36,5.21,51.36,6.21,52.36,4.51,52.36,5.51,53.36,5.51,52.36,6.51,53.36";
		String wordstosearch ="";
		
		//array to store grid
		String[] grid;;
		grid =new String[22];
		
		//grid covering NL
		grid[0]="3.36573,51.19957";
		grid[1]="4.36573,52.19957";
		
		grid[2]="4.36573,51.19957";
		grid[3]="5.36573,52.19957";
		
		grid[4]="5.36573,51.19957";
		grid[5]="6.36573,52.19957";
		
		grid[6]="5.638527,50.758179";
		grid[7]="6.179707,51.19957";
		
		grid[8]="4.33,52.19957";
		grid[9]="5.33,53.19957";
		
		grid[10]="5.33,52.19957";
		grid[11]="6.33,53.19957";
		
		grid[12]="6.33,52.19957";
		grid[13]="7.33,53.19957";
		
		grid[14]="4.7,53.19957";
		grid[15]="5.7,54.19957";
		
		grid[16]="5.7,53.19957";
		grid[17]="6.7,54.19957";
		
		grid[18]="6.7,53.19957";
		grid[19]="7.23,54.19957";
		
		grid[20]="6.36,51.822941";
		grid[21]="7.08,52.19957";
		
		//amount of grids 22/2 = 11
		int n=22;
		
		//concat all the strings for the search
		for(int i=0; i<n;i++){
			if(i ==n-1 ){
				wordstosearch = wordstosearch + grid[i];
			}
			else 
				wordstosearch = wordstosearch + grid[i] + ",";
			}
	
		
		try {
			
			Gson gson = new Gson();
			
			final HttpParams params = new BasicHttpParams(); 

			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setUseExpectContinue(params, false);
			DefaultHttpClient client = new DefaultHttpClient();
			client.getParams().setParameter("http.useragent", "Mozilla/4.0 (compatible; MSIE 5.5; Windows NT 4.0)");
	        client.getCredentialsProvider().setCredentials(  new AuthScope("stream.twitter.com", 443),new UsernamePasswordCredentials(twitterusername, twitterpassword));
	        client.setParams(params);
	        HttpGet httpget = new HttpGet();

	        String url = "https://stream.twitter.com/1/statuses/filter.json?locations="+wordstosearch;
	        
	        httpget.setURI(new URI(url));
	        HttpResponse response = client.execute(httpget);
	        BufferedReader bw = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	       
	        FileWriter fw;
	        PrintWriter pw;
	    
        	Writer output = null;
        	int linecount = 0;
        	 
        	//get count from file
        	RandomAccessFile read_count =  new RandomAccessFile("count.txt", "rw");
        
      
        	try{
        		linecount =Integer.parseInt(read_count.readLine());
        	
        	}catch (Exception e){e.printStackTrace();}
        	
        	//datatypes
	        String line="";
	        long count =linecount;
	        //int count = Integer.parseInt(line_count1);
	        String user_screen_name = "";
	        String user_id="";
	        String time_created="";
	        String source="";
	        
	        String msg_id="";
	        String text="";
	        double[] coordinates= new double[2];
	        coordinates[0]=-1000;
	        coordinates[1]=-1000;
	       
	       //setup filename
	        Calendar currentDate = Calendar.getInstance(); //Get the current date
	        SimpleDateFormat formatter= new SimpleDateFormat("dd-MMM-yyyy_HH_mm_ss"); //format it as per your requirement
	        SimpleDateFormat formatter_date= new SimpleDateFormat("dd"); //format it as per your requirement
	  	  	String dateNow = formatter.format(currentDate.getTime());
	  	  	String dateNow_date = formatter_date.format(currentDate.getTime());
	        String filename ="tweets" +dateNow +".txt";
	        
	        //setup day of month for date
	        String date =dateNow_date;
	       
	      
	        while((line = bw.readLine())!= null){
	        	tweet t  = gson.fromJson(line, tweet.class);
	        	if(t.user!=null && t.text!=null)
	        		
	        		coordinates[0]=-1000;
		        	coordinates[1]=-1000;
	        		//store user information
	        		
		        	try{
	        			
		        		user_id = t.user.id_str;
	        		}catch(NullPointerException e)  {}
	        		
	        		try{
	        			
	        			user_screen_name = t.user.screen_name;
	        		}catch(NullPointerException e)  {}
	        		
	        	
	        		//store message information
	        		try{
	        			
	        			msg_id =t.id_str;
	        		}catch(NullPointerException e)  {}
	        		
	        		//store message information
	        		try{
	        			
	        			time_created = t.created_at;
	        		}catch(NullPointerException e)  {}
	        		
	        	
	        		//store coordinates of geo
	        		try{
	        			coordinates[0]=t.geo.coordinates[0];
	        			coordinates[1]=t.geo.coordinates[1];
	      		
	        		}catch(NullPointerException e)  {}
	        		
	        		//store source
	        		try{
	        			source =t.source;
	        			        			
	        		}catch(NullPointerException e)  {}
	        		
	        		//store text
	        		try{
	        			text =t.text;
	        			
	        		}catch(NullPointerException e)  {}
	        		
	        		//get current date
	        		currentDate = Calendar.getInstance();
	        		
	        		//check if date equals and create a new file
	        		if (!(formatter_date.format(currentDate.getTime()).equals(date))){
	        			
	        				
	        				//create a new file each day 
	        				currentDate = Calendar.getInstance(); //Get the current date
	        				formatter= new SimpleDateFormat("dd-MMM-yyyy_HH_mm_ss"); //format it as per your requirement
	        	  	  	 	dateNow = formatter.format(currentDate.getTime());
	        	  	  	 	filename ="tweets" +dateNow +".txt";
	        	         
	        	  	  	 	//update date
	        	  	  	 	date = (formatter_date.format(currentDate.getTime()));
	        		}
	        		
	        		//write to file and set flag to true to not overwrite contents
	        		fw = new FileWriter(filename,true);
	        		pw = new PrintWriter(fw,true);
	        		
	        		//write update count
	        		read_count =  new RandomAccessFile("count.txt", "rw");
	        		
	        		//print to console and to file
	        		System.out.println(count+ "\t"+ user_id +"\t" + user_screen_name +"\t" + msg_id +"\t" + time_created +"\t" + coordinates[0]+"\t" + coordinates[1]+ "\t" +source+ "\t" +text);
	        		pw.println(count+ "\t"+ user_id +"\t" + user_screen_name +"\t" + msg_id +"\t" + time_created +"\t" + coordinates[0]+"\t" + coordinates[1]+ "\t" +source+ "\t" +text);
	        		
	        		count =count +1;
	        	
	        		//write count to count file
	        		String count_str = ""+count;
	        		read_count.writeBytes(count_str);
	        		
	        		pw.close();
	        		read_count.close();
	        }
	        bw.close();
	        output.close();
	        
		} catch (Exception e) {e.printStackTrace();}
		
	}

	public static void main(String[] args) {
		
		while(true){
			
			method();
		}
	}

}
