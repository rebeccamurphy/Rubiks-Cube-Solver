package rubik;

import java.util.HashMap;

public class CubeNode {

	/**
	 * The state of the cube
	 */
	public  HashMap<Integer,char[]>  state;

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
	public CubeNode( HashMap<Integer,char[]>  state, int heuristic) {
		this.state = state;
		this.heuristic = heuristic;
	}
	public CubeNode( HashMap<Integer,char[]>  state, int heuristic, long encodedState) {
		this.encodedState = encodedState;
		this.state = state;
		this.heuristic = heuristic;
	}



	
}
