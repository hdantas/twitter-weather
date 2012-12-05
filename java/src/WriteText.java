import java.io.*;

public class WriteText{
  public static void main(String[] args)throws IOException{
  Writer output = null;
  File file = new File("write.txt");
  
  output = new BufferedWriter(new FileWriter(file));
  

FileWriter fw = new FileWriter("1.txt");
PrintWriter pw = new PrintWriter(fw);
  
  for (int i=0;i<10;i++){
	  
 // output.write(text);
 // output.write("\n");
  pw.println("test");
  }
  pw.close();
  output.close();
  System.out.println("Your file has been written");  
  }
}