package rubik;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CornerHeuristics {

	/**
	 * As per Korf's paper, we should generate all of the permutations
	 * by starting with a solved cube and then performing a breadth-
	 * first search.
	 * @throws IOException 
	 */
	public static void generateCornerHeuristics() throws IOException {
		// Make a cube and initialize it with a solved cube state
		Cube cube = new Cube(Cube.GOAL_STRING);

		// Make a new Queue to perform BFS
		Queue<CubeNode> q = new LinkedList<CubeNode>();

		// Put the solved/initial state of the corners on the queue
		q.add(new CubeNode(cube.cubeCornersMap , 0));
		int[] corners = new int[10000];
		//Set<Map.Entry<Character, int[]>> faces = Cube.FACES.entrySet();

		// Iterate till the cows come home
		while (!q.isEmpty()) {
			CubeNode current = q.poll();
			// For each cube state we're given, we need to try all of
			// possible turns of each other other faces
			
			for (int i=0; i<Cube.FACES.length; i++){
				//Do a clockwise turn
				 HashMap<Integer,char[]> newState = Cube.rotate(Cube.FACES[i], current.state, 'C');
				 int encodedCorner = Cube.encodeCorners(newState);
				 if (corners[encodedCorner]!=0){
					 //new combo, so we can add it to the queue
					 q.add(new CubeNode(newState, current.heuristic+1));
				 }
			}

			// Handle the current node. We'll encode the corners, and check to
			// see if we've seen this permutation before.
			int encodedCorner = Cube.encodeCorners(current.state);
			if (corners[encodedCorner]!=0) {
				corners[encodedCorner]=current.heuristic;
				FileWriter pw = new FileWriter("corner.csv",true);
				pw.append(encodedCorner + "," + current.heuristic);
				pw.append("\n");
				pw.flush();
		        pw.close();
				System.out.println(encodedCorner + "," + current.heuristic);
			}
		}
	}

}
