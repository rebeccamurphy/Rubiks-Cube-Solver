package rubik;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class IDAStar {

	public static int nextBound;
	public static int nodesVisited;
	public static boolean details;
	public static PriorityQueue<CubeNode> frontier = new PriorityQueue<CubeNode>();
	public static HashSet<CubeNode> explored = new HashSet<CubeNode>();
	public static int[] corners;
	public static int[] edgesOne;
	public static int[] edgesTwo;
	
	/***
	 * Sudo Code for IDA*
	 	 node              current node
		 g                 the cost to reach current node
		 f                 estimated cost of the cheapest path (root..node..goal)
		 h(node)           estimated cost of the cheapest path (node..goal)
		 cost(node, succ)  path cost function
		 is_goal(node)     goal test
		 successors(node)  node expanding function
		 
		 procedure ida_star(root, cost(), is_goal(), h())
		   bound := h(root)
		   loop
		     t := search(root, 0, bound)
		     if t = FOUND then return FOUND
		     if t = ∞ then return NOT_FOUND
		     bound := t
		   end loop
		 end procedure
		 
		 function search(node, g, bound)
		   f := g + h(node)
		   if f > bound then return f
		   if is_goal(node) then return FOUND
		   min := ∞
		   for succ in successors(node) do
		     t := search(succ, g + cost(node, succ), bound)
		     if t = FOUND then return FOUND
		     if t < min then min := t
		   end for
		   return min
		 end function
	 */
	
	/**
	 * Perform ida*
	 * @param String state
	 * @param boolean moreInfo
	 * @return path of solution
	 */
	public static String doIDAStar(String state, boolean moreInfo){
		 
		
		//extra variable for debugging purposes
		details = moreInfo;
		if (state.equals(Cube.GOAL_STRING)){
			return "The cube is already solved";
		}
		
		try {
			//read in heuristics
			corners = readHeuristicFile(88179840, "rubik/corners.csv");
			edgesOne = readHeuristicFile(42577920, "rubik/edgesOne.csv");
			edgesTwo = readHeuristicFile(42577920, "rubik/edgesTwo.csv");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//create start state of cube
		Cube cubeStart = new Cube(state);
		//encode the first set of edges 
		int encodedEdge0 = cubeStart.encodeEdges(0);
		
		//create the start heurisitc based on the first edgeset heuristic 
		int startHeuristic = (encodedEdge0 > IDAStar.edgesOne.length) ? 0: IDAStar.edgesOne[encodedEdge0];
		
		//create the first node  based on the first state and start heurisitc
		CubeNode startState = new CubeNode(state, startHeuristic);
		
		//set the nextbound to the original value
		//bound := h(root) 
		nextBound = startState.hval; 
		
		//set nodes visited
		nodesVisited =0;
		
		//initialize end as null
		
		CubeNode end = null;
		
		//keep going until the solution is found
		//loop
		while (end == null) {
			
			if (details) {
				System.out.println("Current bound is: " + nextBound);
				System.out.println("Number of Nodes visited: " + nodesVisited);
			}
			
			frontier.add(startState);
			//t:= search(root, 0, bound);
			end = search(nextBound);
			
			// The iterative-deepening portion of IDA*
			// Increment the bound if a solution isn't found
			
			//bound:=t
			nextBound++;
			
			// Clear the frontier and explored
			frontier.clear();
			explored.clear();
		}
		if (details) {
			System.out.println("Solved Yay");
			System.out.println("Total # of nodes visited: " + nodesVisited);
		}
		
		return formatSolution(end.path);
		
		
	}
	/**
	 * The recursive function for IDA*
	 * @param bound the current bound - used to tell if nodes need to be expanded
	 * @return the node version of the goal state (eventually) (hopefully)
	 */
	private static CubeNode search(int bound) {
		nodesVisited++;

		while (!frontier.isEmpty()) {
			nodesVisited++;
			//current node is node at the top of the frontier
			CubeNode current = frontier.poll();
			
			if (details){
				System.out.println("Current State: " + current.state);
			}

			//If the goal has been reached, return the goal node
			//RETURN FOUND
			if (current.state.equals(Cube.GOAL_STRING)) {
				return current;
			}
			
			// mark current node as explored
			explored.add(current);
			
			// Get all of the possible successors from the current cubenode
			ArrayList<CubeNode> successors = CubeNode.getSuccessors(current);
			
			// Go through each successor
			for (int i =0; i<successors.size(); i++) {
				//f := g + h(node)
				int f = current.g + successors.get(i).hval;
				//this extra check is because my heuristic tables are incomplete
				if (successors.get(i).state.equals(Cube.GOAL_STRING))
					return successors.get(i);
				
				successors.get(i).g = current.g + 1;
				
				if (f <= bound && !explored.contains(successors.get(i))) {
					// Add it to the frontier
					frontier.add(successors.get(i));
					
				}
				
			}
		}
		//If the solution wasn't found at the current bound return null
		return null;
	}
	
	/****
	 * reads the heuristics and returns an array containing the entries
	 * @param fileLength
	 * @param fileName
	 * @return returns an array containing the entries
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private static int[] readHeuristicFile (int fileLength, String fileName) throws NumberFormatException, IOException {
		int[] heuristics = new int[fileLength];
		FileReader file = null;
		String line; 
		try {
			file = new FileReader(fileName);
			BufferedReader reader = new BufferedReader(file);
			while ((line = reader.readLine()) != null) {
				// For each line, split by the comma
				String[] lineState = line.split(",");
				//As in the function to generate the heuristics, [0] is the encoded state,[1] is the hval
				if (!(lineState[0].equals("") || lineState[1].equals(""))) 
				{	if (Integer.parseInt(lineState[0]) <fileLength)
						heuristics[Integer.parseInt(lineState[0])] = Integer.parseInt(lineState[1]);
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found");
		}

		return heuristics;
	}
	/**
	 * Formats the path to the proper solution ie R1R1R1 = R3
	 * @param path
	 * @return String condensed path
	 */
	private static String formatSolution(String path){
		String result ="";
		int count =1;
		String lastTurn="";

		if(path.length() ==2)
			return path;
		for (int i=0; i< path.length()-2; i+=2){
			String turn = path.substring(i, i+1);
			String nextTurn = path.substring(i+2, i+3);
			if (turn.equals(nextTurn)){
				count++;
			} 
			else{
				if (count > 3)
					count = count%4;
				if (!(count==0))
					result += turn + count;
				count =1;
			}
			lastTurn = nextTurn;
		}
		return result + lastTurn +count ;
	}
}
