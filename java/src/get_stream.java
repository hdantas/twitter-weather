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


public class get_stream {

	public static void main(String[] args) {
		
		String twitterusername = "mastitwit";
		String twitterpassword = "ema0188";
	
		String wordstosearch = "3.21,51.36,4.21,52.36,4.21,51.36,5.21,53.36,5.21,51.36,6.21,52.36,4.51,52.36,5.51,53.36,5.51,52.36,6.51,53.36";
		//String wordstosearch = "weather";
		
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
	        FileWriter fw = new FileWriter("tweets1.txt");
	        PrintWriter pw = new PrintWriter(fw,true);
        	
        	File file = new File("write.txt");
        	 Writer output = null;
	        String line="";
	        int count = 0;
	       
	        
	        output = new BufferedWriter(new FileWriter(file));
	        
	        while((line = bw.readLine())!= null){
	        	tweet t  = gson.fromJson(line, tweet.class);
	        	if(t.user!=null && t.text!=null)
	        		//if(t.user.location.equals("Netherlands")){
	        	
	        	System.out.println(count+ "," +t.user.screen_name + "," + t.text + "," +t.user.location + "," + t.geo + "," +t.coordinates );
	        		//System.out.println(line);
	        		//}
	        	
	        	
	        fw = new FileWriter("tweets1.txt",true);
		       pw = new PrintWriter(fw,true);
	        	pw.println(count+ ","+ t.user.screen_name + "," + t.text + "," +t.user.location + "," + t.geo + "," +t.coordinates);
	        	pw.close();
	        		//pw.append("test");
	        		//output.write(count+ ","+ t.user.screen_name + "," + t.text + "," +t.user.location + "," + t.geo + "," +t.coordinates);
	        		count =count +1;
	        }
	        bw.close();
	        
	      //  pw.close();
	        output.close();
	        
		} catch (Exception e) {e.printStackTrace();}
	}

}
