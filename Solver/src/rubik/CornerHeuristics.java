package rubik;

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
	 */
	public static void generateCornerHeuristics() {
		// Make a cube and initialize it with a solved cube state
		Cube cube = new Cube(Cube.GOAL_STRING);

		// Make a new Queue to perform BFS
		Queue<CubeNode> q = new LinkedList<CubeNode>();

		// Put the solved/initial state of the corners on the queue
		q.add(new CubeNode(cube.cubeCornersMap , 0));
		ArrayList corners = new ArrayList();
		//Set<Map.Entry<Character, int[]>> faces = Cube.FACES.entrySet();

		// Iterate until we can't anymore
		while (!q.isEmpty()) {
			CubeNode current = q.poll();
			// For each cube state we're given, we need to try all of
			// possible turns of each other other faces
			
			for (int i=0; i<Cube.FACES.length; i++){
				//Do a clockwise turn
				 HashMap<Integer,char[]> newState = Cube.rotate(Cube.FACES[i], current.state, 'C');
				 int encodedCorner = Cube.encodeCorners(newState);
				 if (!corners.contains(encodedCorner)){
					 //new combo, so we can add it to the queue
					 q.add(new CubeNode(newState, current.heuristic+1));
				 }
			}

			// Handle the current node. We'll encode the corners, and check to
			// see if we've seen this permutation before.
			int encodedCorner = Cube.encodeCorners(current.state);
			if (!corners.contains(encodedCorner)) {
				corners.add(encodedCorner, current.heuristic);
				// Print this out
				System.out.println(encodedCorner + "," + current.heuristic);
			}
		}
	}

}
