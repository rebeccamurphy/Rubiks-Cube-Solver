package rubik;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Solvable {
	 public static void main(String[] args) throws IOException {
	        if(args.length > 0) {
	        	char[][] left=new char[9][3], middle =new char[9][3], right = new char[9][3];
	            String file = args[0];
	            String strFile = readFile(file, StandardCharsets.UTF_8);
	            strFile = strFile.replaceAll("\\s+","");
	            //Cubies.checkValid(strFile, left, middle, right);
	            System.out.println("the cube is valid, " +Cubies.checkValid(strFile));
	            //System.out.println(left[4][0]=='G');
	            //System.out.println(Cubies.checkValid(left, middle, right));
	           // Cubies.checkValid(left, middle, right);
	        }
	        else{
	        	System.out.println("WXY".contains("YXW"));
	        	System.out.println("Invalid File Name");
	        }
	    }
	 public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
}
