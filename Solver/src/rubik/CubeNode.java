package rubik;

import java.util.ArrayList;
import java.util.HashMap;

public class CubeNode implements Comparable<CubeNode>{

	/**
	 * The state of the cube
	 */
	public  String state;

	/**
	 * The heuristic value
	 */
	public int hval;

	/**
	 * g the cost to reach current node
	 */
	public int g;

	/**
	 * The path from the goal state to this node
	 */
	public String path;
	/**
	 *
	 * @param state the state of the cube
	 * @param heuristic the heuristic value
	 */
	
	public CubeNode( String state, int heuristic) {
		this.state = state;
		this.hval= heuristic;
		this.g =0;
		this.path="";
	}
	public CubeNode( String state, int heuristic, String path) {
		this.state = state;
		this.hval= heuristic;
		this.g =0;
		this.path=path;
	}
	public CubeNode( String state, int heuristic, int g, String path) {
		this.state = state;
		this.hval= heuristic;
		this.g =g;
		this.path=path;
	}
	

	/**
	 * Make all successors of the given node.
	 * @param node to find the successor of
	 * @return an ArrayList<CubeNode> for all the successors
	 * 
	 */
	public static ArrayList<CubeNode> getSuccessors(CubeNode node) {
		ArrayList<CubeNode> successors = new ArrayList<CubeNode>();
		for (int i=0; i<Cube.FACES.length; i++) {
			
			Cube current = new Cube(node.state);
			//Make a new Cube of the node 
			
			// First make a Make a clockwise turn
			current.rotate(Cube.FACES[i]);
			
			//Covert the successor to a string
			String newState = current.toString();
			/*if (Cube.FACES[i] =='O'){
				System.out.println(newState);
				System.out.println(Cube.GOAL_STRING);
				System.out.println(newState.equals(Cube.GOAL_STRING));
				
			}*/
			//Encode the corners of the successor 
			int encodedCorner = current.encodeCorners();
			

			// Encode the edges of the successor
			int encodedEdgeSetOne = current.encodeEdges(0);
			int encodedEdgeSetTwo = current.encodeEdges(6);
			
			//Next we must find the heuristic values for the 3 sets for the cube
			int[] Hvals = new int[3];
			
			//if encoded state is not in table default to heuristic value of 0
			/*
			Hvals[0] =(encodedCorner >=IDAStar.corners.length) ? 0: IDAStar.corners[encodedCorner];
			Hvals[1] = (encodedEdgeSetOne >=IDAStar.edgesSetOne.length)? 0: IDAStar.edgesSetOne[encodedEdgeSetOne];
			Hvals[2] = (encodedEdgeSetTwo >=IDAStar.edgesSetTwo.length)? 0:IDAStar.edgesSetTwo[encodedEdgeSetTwo];
			*/
			
			Hvals[0] =0;
			Hvals[1] = 0;
			Hvals[2] = 0;
			
			
			// Find the maximum of the 3 heuristics
			int max = Hvals[0];
			for (int j = 1; j < Hvals.length; j++) {
				if (Hvals[j] > max) {
					max = Hvals[j];
				}
			}
			// Add current state and it's heuristic value to the successors list
			successors.add(new CubeNode(newState, Hvals[0], node.path + Cube.FACES[i] + "1")) ;
		}
		return successors;
	}

	@Override
	public int compareTo(CubeNode b) {
		if (this.hval< b.hval) {
			return -1;
		} else if (this.hval> b.hval) {
			return 1;
		}
		return 0;
	}



	
}
