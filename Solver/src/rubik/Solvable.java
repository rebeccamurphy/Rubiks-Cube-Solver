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
	        	System.out.println(IDAStar.doIDAStar(strFile,true));

	        	//System.out.println("RRRRRRRRRGGGYYYBBBGGGYYYBBBYYYBBBWWWOOOOOOOOOGGGWWWWWW");
	            
	        }
	        else{
	        	//Cube cube = new Cube("RRRRRRRRRGGGYYYBBBGGGYYYBBBYYYBBBWWWOOOOOOOOOGGGWWWWWW");
	        	//System.out.print(cube.encodeCorners());
	        			
	        	//System.out.println(Cube.GOAL_STRING);
	        	//CornerHeuristics.generateCornerHeuristicsBreathFirst();
	        	EdgeHeuristics.generateEdgeHeuristics(1);
	        	//System.out.println(IDAStar.doIDAStar("GGGRRRRRRRBBWWWGGOGGOYYYRBBGGOYYYRBBBBBOOOOOOWWWWWWYYY",true));
	        }
	        	
	        
	    }
	 public static String readFile(String path) 
			  throws IOException 
			{ 
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded);
			}

}
