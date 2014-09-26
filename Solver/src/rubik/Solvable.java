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
	            String strFile = readFile(file);
	            strFile = strFile.replaceAll("\\s+","");
	            //Cubies.checkValid(strFile, left, middle, right);
	            System.out.println(Cubies.checkValid(strFile));
	            //System.out.println(left[4][0]=='G');
	            //System.out.println(Cubies.checkValid(left, middle, right));
	           // Cubies.checkValid(left, middle, right);
	        }
	        else{
	        	//System.out.println("WXY".contains("YXW"));
	        	//System.out.println("Invalid File Name");
	        	runAll(new File ("/home/rebecca/Documents/RubiksCubeStates-master/valid/"));
	        }
	    }
	 public static String readFile(String path) 
			  throws IOException 
			{ 
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded);
			}
	 public static void runAll(final File folder) throws IOException {
		    for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		             runAll(fileEntry);
		             
		        } else {
		        	String strFile = readFile(fileEntry.getAbsolutePath());
		        	strFile = strFile.replaceAll("\\s+","");
		        	if (!Cubies.checkValid(strFile)){
		        	System.out.println(fileEntry.getName());
		        	System.out.println("Something went wrong");
		        	break;
		        	}
		       }
		    }
		}
}
