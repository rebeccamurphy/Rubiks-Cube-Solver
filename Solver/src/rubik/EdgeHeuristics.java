package rubik;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class EdgeHeuristics {

	public static void generateEdgeHeuristics(int set) throws IOException {
		// Make a cube and initialize it with a solved cube state
		Cube cube = new Cube(Cube.GOAL_STRING);
		String fileNum;
		if (set==0)
			fileNum ="One";
		else 
			fileNum="Two";
		// Make a new Queue to perform BFS
		Queue<CubeNode> q = new LinkedList<CubeNode>();

		
		HashMap<Integer,char[]> edges = new HashMap<Integer,char[]> ();
		
		int base = 6 * set;
		int limit = 6 + base;

		// Select the proper set of edges to work with
		if (set == 0 || set == 1) {
			for (int i = base; i < limit; i++) {
				edges.put(i, cube.cubeEdgesMap.get(i));
			}
		} else {
			System.err.println("put in 0 or 1");
		}
		
		// Put the solved/initial state of the cube on the queue
		q.add(new CubeNode(Cube.GOAL_STRING, 0));
		// Iterate till the cows come home
		while (!q.isEmpty()) {
			CubeNode current = q.poll();
			// Turning for each possible node
			for (int i=0; i<Cube.FACES.length; i++){
				//Make a new cube
				 cube = new Cube(current.state);
				//Do a clockwise turn
				 cube.rotate(Cube.FACES[i], 'E');
				 String newState = cube.toString();
				 int encodedEdge = cube.encodeEdges(set);
				 //System.out.println(Cube.FACES[i]);
				 if (!checkInFile(encodedEdge, fileNum)){
					 //new combo, so we can add it to the queue
					 //System.out.println(encodedCorner+" " +newState);
					 q.add(new CubeNode(newState, current.heuristic+1));
				 }
			}

			// Handle the current node. We'll encode the corners, and check to
			// see if we've seen this permutation before.
			int encodedEdge = cube.encodeEdges(set);
			if (!checkInFile(encodedEdge, fileNum)) {
				FileWriter pw = new FileWriter("edges"+fileNum+"Duplicate.csv",true);
				pw.append(encodedEdge + "," + current.heuristic);
				pw.append("\n");
				pw.flush();
		        pw.close();
				System.out.println(encodedEdge + "," + current.heuristic);
			}
		}
		}
		
public static boolean checkInFile(int encoded, String fileNum){
	String csvFile = "edges"+fileNum+"Duplicate.csv";
	BufferedReader br = null;
	String line = "";
	
 
	try {
		br = new BufferedReader(new FileReader(csvFile));
		while ((line = br.readLine()) != null) {

		        // use comma as separator	
			String encodedCube = line.split(",")[0];
			if (encodedCube.isEmpty())
				return false;
			//System.out.println(Integer.parseInt(encodedCube)+" "+encoded);
			else if (encoded == Integer.parseInt(encodedCube)){
				return true;
			}
		}
 
	} catch (FileNotFoundException e1) {
		e1.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	return false;
}
}
