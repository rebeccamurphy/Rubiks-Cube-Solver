package rubik;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Solvable {
	 public static void main(String[] args) throws IOException {
	        if(args.length > 0) {
	        	String file = args[0];
	            String strFile = readFile(file);
	            strFile = strFile.replaceAll("\\s+","");
	            System.out.println(Cubies.checkValid(strFile));
	            
	        }
	        else{
	        	Cube cube = new Cube("RRRRRRRRRGGGYYYBBBGGGYYYBBBGGGYYYBBBOOOOOOOOOWWWWWWWWW");
	        	//cube.encodeCorners(cube.cubeCornersMap);
	        	CornerHeuristics.generateCornerHeuristics();
	        }
	        	
	        
	    }
	 public static String readFile(String path) 
			  throws IOException 
			{ 
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded);
			}

}
