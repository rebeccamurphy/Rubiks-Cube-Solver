package rubik;

import java.util.HashMap;

public class CubeNode {

	/**
	 * The state of the cube
	 */
	public  String state;

	/**
	 * The heuristic value
	 */
	public int heuristic;

	/**
	 *
	 * @param state the state of the cube
	 * @param heuristic the heuristic value
	 */
	public long encodedState;
	
	public CubeNode( String state, int heuristic) {
		this.state = state;
		this.heuristic = heuristic;
	}
	public CubeNode( String  state, int heuristic, long encodedState) {
		this.encodedState = encodedState;
		this.state = state;
		this.heuristic = heuristic;
	}



	
}
