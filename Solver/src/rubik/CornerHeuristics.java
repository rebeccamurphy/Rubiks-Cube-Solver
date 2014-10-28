package rubik;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class CornerHeuristics {

	static int limit =12;
	public static void generateCornerHeuristics(){
		/*
		function DEPTH-LIMITED-SEARCH(problem, limit) returns a solution, or failure/cutoff

		return RECURSIVE-DLS(MAKE-NODE(problem.INITIAL-STATE), problem, limit)

		function RECURSIVE-DLS(node, problem, limit) returns a solution, or failure/cutoff

		if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)

		else if limit = 0 then return cutoff

		else

		cutoff_occurred? ← false

		for each action in problem.ACTIONS(node.STATE) do

		child ← CHILD-NODE(problem, node, action)

		result ← RECURSIVE-DLS(child, problem, limit − 1)

		if result = cutoff then cutoff_occurred? ← true

		else if result ≠ failure then return result

		if cutoff_occurred? then return cutoff else return failure
		*/
		
	}
	public static void recursive(){
		if (limit==0){
			
		}
		else{
			
		}
	}
	/**
	 * As per Korf's paper, generate all of the permutations
	 * by starting with a solved cube and then performing a breadth-
	 * first search.
	 * @throws IOException 
	 */
	public static void generateCornerHeuristicsBreathFirst() throws IOException {
		// Make a cube and initialize it with a solved cube state
		Cube cube = new Cube(Cube.GOAL_STRING);

		// Make a new Queue to perform BFS
		Queue<CubeNode> q = new LinkedList<CubeNode>();

		// Put the solved/initial state of the corners on the queue
		q.add(new CubeNode(cube.toString(), 0));
		//Set<Map.Entry<Character, int[]>> faces = Cube.FACES.entrySet();

		// Iterate till the cows come home
		while (!q.isEmpty()) {
			CubeNode current = q.poll();
			// For each cube state we're given, we need to try all of
			// possible turns of each other other faces
			
			for (int i=0; i<Cube.FACES.length; i++){
				//Make a new cube
				 cube = new Cube(current.state);
				//Do a clockwise turn
				 cube.rotate(Cube.FACES[i], 'C');
				 String newState = cube.toString();
				 int encodedCorner = cube.encodeCorners();
				 //System.out.println(Cube.FACES[i]);
				 if (!checkInFile(encodedCorner)){
					 //new combo, so we can add it to the queue
					 //System.out.println(encodedCorner+" " +newState);
					 q.add(new CubeNode(newState, current.heuristic+1));
				 }
			}

			// Handle the current node. We'll encode the corners, and check to
			// see if we've seen this permutation before.
			int encodedCorner = cube.encodeCorners();
			if (!checkInFile(encodedCorner)) {
				FileWriter pw = new FileWriter("corner.csv",true);
				pw.append(encodedCorner + "," + current.heuristic);
				pw.append("\n");
				pw.flush();
		        pw.close();
				System.out.println(encodedCorner + "," + current.heuristic);
			}
		}
	}
	
	public static boolean checkInFile(int encoded){
		String csvFile = "corner.csv";
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
