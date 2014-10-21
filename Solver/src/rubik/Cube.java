package rubik;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Cube {
	public static String GOAL_STRING = "RRRRRRRRRGGGYYYBBBGGGYYYBBBGGGYYYBBBOOOOOOOOOWWWWWWWWW";
	static int[][] corners =new int[][]{
		//starting 0,0
		//X  Y  Z
		{12, 11, 6}, //0-00
		{30, 29, 36},//0-02
		
		{51, 9, 0 }, //0-20
		{45, 27, 42},//0-22
		
		{14, 15, 8}, //2-00
		{32, 33, 38},//2-02
		
		{53, 17, 2}, //2-20
		{47, 35, 44} //2-22
	};
	static char[][] cornersFaces = new char[][]{
		//X=Y,W; Y=G,B, Z =O,R
		//X    Y    Z
		{'Y', 'G', 'R'}, //0-00
		{'Y', 'G', 'O'}, //0-02
		{'W', 'G', 'R'}, //0-20
		{'W', 'G', 'O'}, //0-22
		
		{'Y', 'B', 'R'}, //2-00
		{'Y', 'B', 'O'}, //2-02
		{'W', 'B', 'R'}, //2-20
		{'W', 'B', 'O'} //
	};
	
	public static int [][]  edges = new int[][]{
		{21, 20}, //A-01
		
		{3, 10},  //A-10
		{39, 28}, //A-12
		
		{48, 18}, //A-21
		
		{13, 7},  //B-00
		{31, 37}, //B-02
		
		{52, 1},  //B-20
		{46, 43}, //B-22
		
		{23, 24}, //C-01
		
		{5, 16},  //C-10
		{34, 41}, //C-12
		
		{50, 26}, //C-21
	};
	public static char[][] edgesFace = new char[][]{
		//X, Y, Z
		{'Y','G','0'},	//A-01
		{'0','G','R'}, //A-10
		{'0','G','O'}, //A-12
		{'W','G','0'}, //A-21
		
		{'Y','0','R'}, //B-00
		{'Y','0','O'}, //B-02
		{'W','0','R'}, //B-20
		{'W','0','O'}, //B-22
		
		{'Y','B','0'}, //C-01
		{'0','B','R'}, //C-10
		{'0','B','O'}, //C-12
		{'W','B','0'}, //C-21
	};
	public static int[] centers = new int[] {22, 49, 4,25, 40, 19};
	public static char[] centersColors = new char[] {'Y', 'W', 'R','B', 'O', 'G'};
	
	public static char[][]cubeCornerFaces =new char[8][3];
	public static char[][]cubeEdgeFaces = new char[12][6];
	public static HashMap<Integer, int[]> ce= new HashMap<Integer, int[]>();
	
	public static HashMap<Integer,char[]> cubeCornersMap = new HashMap <Integer, char[] > ();
	public static HashMap<Integer, char[]> cubeEdgesMap = new HashMap <Integer, char[] > ();
	
	public static final char[] FACES = {'Y', 'W', 'R', 'B', 'O', 'G'}; 
	
	public static  int[] cubeCorners = new int[8];
	public static  int[] cubeEdges = new int[12];
	//indexes of cubes in theses faces, first char = color, second = corner or edge
	//order dependent upon view of face
	static int[] faceYC = {0, 4, 5, 1};
	static int[] faceYE = {0, 4, 8, 5};
	
	static int[] faceWC = {2, 6, 7, 3};
	static int[] faceWE = {3, 6, 11, 7};
	
	static int[] faceRC= {0, 4, 6, 2};
	static int[] faceRE= {1, 4, 9, 6};
	
	static int[] faceBC ={5, 4, 6, 7};
	static int[] faceBE ={8, 9, 11, 10};
	
	static int[] faceOC ={1, 5, 7, 3};
	static int[] faceOE ={2,5, 10, 7};
	
	static int[] faceGC={0, 2, 3, 1};
	static int[] faceGE={0,1, 3, 2};
	
	public String firstState;
	
	static List<char[]> cornerValues;
	static List<char[]> edgeValues; //0-5
	static List<char[]> edgeValues1; //6-11
	static HashMap<String, Integer> goalEdges;
	
	public static int limit =0;
	
	public Cube(String input){
		firstState =input;
		goalEdges=initGoalEdges();
		makeCube(input);
		
		//System.out.println(encodeCorners(cubeCornersMap));
	}
	
	
	public void makeCube(String cubeString){
		for (int i =0; i< corners.length; i++){
			//check corners for sticker swap
			cubeCornerFaces[i][0] = cubeString.charAt(corners[i][0]);
			cubeCornerFaces[i][1] = cubeString.charAt(corners[i][1]);
			cubeCornerFaces[i][2] = cubeString.charAt(corners[i][2]);
			cubeCornersMap.put(i, cubeCornerFaces[i]);
			
			for(int j=0; j< cornersFaces.length; j++){
				if (checkFaces(cubeCornerFaces[i], cornersFaces[j])){
					cubeCorners[i] = j;		
				}
					
			}
		}	
		
		//parse edges
		for(int i=0; i<edges.length;i++){
			int curColor =0;
			for (int j=0; j<3; j++){
				if (edgesFace[i][j]!='0'){
					cubeEdgeFaces[i][j] = cubeString.charAt(edges[i][curColor]);
					curColor++;
				}
				else 
					cubeEdgeFaces[i][j] = '0';
			}
			cubeEdgesMap.put(i, cubeEdgeFaces[i]);
		}
		
	}

	public static void initCornerValues(){
		//pattern is XYZ, ZXY, YZX
		cornerValues= new ArrayList<char[]>();
		cornerValues.add(new char[] {'Y','G','R'}); //0
		cornerValues.add(new char[] {'R', 'Y', 'G'});//1
		cornerValues.add(new char[] {'G', 'R', 'Y'});//2
		
		cornerValues.add(new char[]{'Y', 'G', 'O'});//3
		cornerValues.add(new char[]{'O', 'Y', 'G'});//4
		cornerValues.add(new char[]{'G', 'O', 'Y'});//5
		
		cornerValues.add(new char[]{'W', 'G', 'R'});//6
		cornerValues.add(new char[]{'R', 'W', 'G'});//7
		cornerValues.add(new char[]{'G', 'R', 'W'});//8
		
		cornerValues.add(new char[]{'W', 'G', 'O'});//9
		cornerValues.add(new char[]{'O', 'W', 'G'});//10
		cornerValues.add(new char[]{'G', 'O', 'W'});//11
		
		cornerValues.add(new char[]{'Y', 'B', 'R'});//12
		cornerValues.add(new char[]{'R', 'Y', 'B'});//13
		cornerValues.add(new char[]{'B', 'R', 'Y'});//14
		
		cornerValues.add(new char[]{'Y', 'B', 'O'});//15
		cornerValues.add(new char[]{'O', 'Y', 'B'});//16
		cornerValues.add(new char[]{'B', 'O', 'Y'});//17
		
		cornerValues.add(new char[]{'W','B', 'R'});//18
		cornerValues.add(new char[]{'R', 'W', 'B'});//19
		cornerValues.add(new char[]{'B', 'R', 'W'});//20
		
		cornerValues.add(new char[]{'W', 'B', 'O'});//21
		cornerValues.add(new char[]{'O', 'W', 'B'});//22
		cornerValues.add(new char[]{'B', 'O', 'W'});//23
	}
	
	public static void initEdgeValues(int whichedge){
/*
		//pattern is XYZ
		if (whichedge==0){
			//0-6
			edgeValues0= new ArrayList<char[]>();
			edgeValues0.add(new char[] {'Y', 'G'});//0
			edgeValues0.add(new char[] {'G', 'Y'});//1
			
			edgeValues0.add(new char[] {'G', 'R'});//2
			edgeValues0.add(new char[] {'R', 'G'});//3
			
			edgeValues0.add(new char[] {'G', 'O'});//4
			edgeValues0.add(new char[] {'O', 'G'});//5
			
			edgeValues0.add(new char[] {'W', 'G'});//6
			edgeValues0.add(new char[] {'G', 'W'});//7
			
			edgeValues0.add(new char[] {'Y', 'R'});//8
			edgeValues0.add(new char[] {'R', 'Y'});//9
			
			edgeValues0.add(new char[] {'Y', 'O'});//10
			edgeValues0.add(new char[] {'O', 'Y'});//11
			
		}
		else if (whichedge==1){
			edgeValues1= new ArrayList<char[]>();
			edgeValues1.add(new char[] {'W', 'R'});//0
			edgeValues1.add(new char[] {'R', 'W'});//1
			
			edgeValues1.add(new char[] {'W', 'O'});//2
			edgeValues1.add(new char[] {'O', 'W'});//3
			
			edgeValues1.add(new char[] {'Y', 'B'});//4
			edgeValues1.add(new char[] {'B', 'Y'});//5
			
			edgeValues1.add(new char[] {'B', 'R'});//6
			edgeValues1.add(new char[] {'R', 'B'});//7
			
			edgeValues1.add(new char[] {'B', 'O'});//8
			edgeValues1.add(new char[] {'O', 'B'});//9
			
			edgeValues1.add(new char[] {'W', 'B'});//10
			edgeValues1.add(new char[] {'B', 'W'});//11
			
			
		
		}
		else 
			System.err.println("put in 0 or 1");
	*/
		//pattern is XYZ
	
			//0-6
			edgeValues= new LinkedList<char[]>();
			edgeValues.add(new char[] {'Y', 'G'});//0
			edgeValues.add(new char[] {'G', 'Y'});//1
			
			edgeValues.add(new char[] {'G', 'R'});//2
			edgeValues.add(new char[] {'R', 'G'});//3
			
			edgeValues.add(new char[] {'G', 'O'});//4
			edgeValues.add(new char[] {'O', 'G'});//5
			
			edgeValues.add(new char[] {'W', 'G'});//6
			edgeValues.add(new char[] {'G', 'W'});//7
			
			edgeValues.add(new char[] {'Y', 'R'});//8
			edgeValues.add(new char[] {'R', 'Y'});//9
			
			edgeValues.add(new char[] {'Y', 'O'});//10
			edgeValues.add(new char[] {'O', 'Y'});//11
			
		
			edgeValues= new ArrayList<char[]>();
			edgeValues.add(new char[] {'W', 'R'});//0
			edgeValues.add(new char[] {'R', 'W'});//1
			
			edgeValues.add(new char[] {'W', 'O'});//2
			edgeValues.add(new char[] {'O', 'W'});//3
			
			edgeValues.add(new char[] {'Y', 'B'});//4
			edgeValues.add(new char[] {'B', 'Y'});//5
			
			edgeValues.add(new char[] {'B', 'R'});//6
			edgeValues.add(new char[] {'R', 'B'});//7
			
			edgeValues.add(new char[] {'B', 'O'});//8
			edgeValues.add(new char[] {'O', 'B'});//9
			
			edgeValues.add(new char[] {'W', 'B'});//10
			edgeValues.add(new char[] {'B', 'W'});//11
			
	}
	
	private static HashMap<String, Integer> initGoalEdges() {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		result.put("RW", 0);
		result.put("GR", 1);
		result.put("BR", 2);
		result.put("RY", 3);
		result.put("GW", 4);
		result.put("GY", 5);
		result.put("BY", 6);
		result.put("BW", 7);
		result.put("GO", 8);
		result.put("OY", 9);
		result.put("BO", 10);
		result.put("OW", 11);
		return result;
	}

	
	private static boolean checkFaces(char[] cubie, char[] defaultCubieFaces){
		for(int i =0; i< cubie.length; i++){
			if ((new String (defaultCubieFaces)).indexOf(cubie[i])==-1){
				return false;
			}
		}
		return true;
	}
	public static void rotate(char face,HashMap<Integer,char[]> state, int turns, char CorE){
		//turns shouldnt be greater than 3
		turns = turns%4;
		for(int i=0; i<turns;i++)
			rotate(face, state, CorE);
	}
	public static HashMap<Integer,char[]>rotate(char face, HashMap<Integer,char[]> state, char CorE){
		if (CorE=='C'){
			HashMap<Integer,char[]> cubeCornersMapCopy = new HashMap <Integer, char[] > (state);
		
			switch (face){
				case 'Y':{
					
					for (int i=0; i<4; i++){
						
						int currPos =faceYC[i];
						int newPos = (i==3)? faceYC[0] :faceYC[i+1];
						if (state.containsKey(currPos)){
							char[] tempCube = state.get(currPos);
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
						
					}
				
					break;
				}
				case 'W':{
					for (int i=0; i<4; i++){
						
						int currPos =faceWC[i];
						int newPos = (i==3)? faceWC[0] :faceWC[i+1];
						if (state.containsKey(currPos)){
						char[] tempCube = state.get(currPos);
						cubeCornersMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}

					}
					break;
				}
				case 'R':{
					for (int i=0; i<4; i++){
						
						int currPos =faceRC[i];
						int newPos = (i==3)? faceRC[0] :faceRC[i+1];
						if (state.containsKey(currPos)){
							char[] tempCube = state.get(currPos);
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
				case 'B':{
					for (int i=0; i<4; i++){
						
						int currPos =faceBC[i];
						int newPos = (i==3)? faceBC[0] :faceBC[i+1];
						if (state.containsKey(currPos)){
							char[] tempCube = state.get(currPos);
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
	
					}
					break;
				}
				case 'O':{
					for (int i=0; i<4; i++){
						
						int currPos =faceOC[i];
						int newPos = (i==3)? faceOC[0] :faceOC[i+1];
						if (state.containsKey(currPos)){
							char[] tempCube = state.get(currPos);
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
					
						}
					}
					break;
				}
				case 'G':{
					for (int i=0; i<4; i++){
						
						int currPos =faceGC[i];
						int newPos = (i==3)? faceGC[0] :faceGC[i+1];
						if (state.containsKey(currPos)){
							char[] tempCube = state.get(currPos);
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					
					}
					break;
				}
			}
			return cubeCornersMapCopy;
		}
		else { 
			HashMap<Integer, char[]> cubeEdgesMapCopy = new HashMap <Integer, char[] >(state);
			int currPos=0, newPos=0;
			char[] tempCube;

			System.out.println("before turn size "+state.size());
			System.out.println(cubeEdgesMapCopy.size());
			switch (face){
				case 'Y':{
					for (int i=0; i<12; i++)
						if (cubeEdgesMapCopy.containsKey(i))
						System.out.println(new String (cubeEdgesMapCopy.get(i)));
					
					
					for (int i=0; i<4; i++){
						
						currPos =faceYE[i];
						newPos = (i==3)? faceYE[0] :faceYE[i+1];
						if (state.containsKey(newPos)){
							cubeEdgesMapCopy.remove(newPos);
						}
						if (state.containsKey(currPos)){
						tempCube = state.get(currPos);
						cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					System.out.println("after turn size "+cubeEdgesMapCopy.size());
					for (int i=0; i<12; i++)
						if (cubeEdgesMapCopy.containsKey(i))
						
					break;
				}
				case 'W':{
					for (int i=0; i<4; i++){
						
						currPos =faceWE[i];
						newPos = (i==3)? faceWE[0] :faceWE[i+1];
						if (state.containsKey(newPos)){
							cubeEdgesMapCopy.remove(newPos);
						}
						if (state.containsKey(currPos)){
							tempCube = state.get(currPos);
							cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
				case 'R':{
					for (int i=0; i<4; i++){
						
	
						currPos =faceRE[i];
						newPos = (i==3)? faceRE[0] :faceRE[i+1];
						if (state.containsKey(newPos)){
							cubeEdgesMapCopy.remove(newPos);
						}
						if (state.containsKey(currPos)){
							tempCube = state.get(currPos);
							//cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
				case 'B':{
					for (int i=0; i<4; i++){
		
						currPos =faceBE[i];
						newPos = (i==3)? faceBE[0] :faceBE[i+1];
						if (state.containsKey(newPos)){
							cubeEdgesMapCopy.remove(newPos);
						}
						if (state.containsKey(currPos)){
							tempCube = state.get(currPos);
							cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
				case 'O':{
					for (int i=0; i<4; i++){
												
						currPos =faceOE[i];
						newPos = (i==3)? faceOE[0] :faceOE[i+1];
						if (state.containsKey(newPos)){
							cubeEdgesMapCopy.remove(newPos);
						}
						if (state.containsKey(currPos)){
							tempCube = state.get(currPos);
						//	cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
				case 'G':{
					for (int i=0; i<4; i++){
						
						currPos =faceGE[i];
						newPos = (i==3)? faceGE[0] :faceGE[i+1];
						if (state.containsKey(newPos)){
							cubeEdgesMapCopy.remove(newPos);
						}
						if (state.containsKey(currPos)){
							tempCube = state.get(currPos);
							//cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
			}

			System.out.println("turn size after: " + face +" "+cubeEdgesMapCopy.size());
			System.out.println();
			for (int i=0; i<12; i++)
				if (cubeEdgesMapCopy.containsKey(i))
				System.out.println(new String (cubeEdgesMapCopy.get(i)));
			System.out.println();
			return cubeEdgesMapCopy;
		}

}
		
	
	/*
	 * Converts rotated Cube back to string state
	 * */
	public String convertToState(HashMap<Integer, char[]>cubeCornersMap,HashMap<Integer, char[]>cubeEdgesMap){
		String result ="";
		/*
		result+=cubeCornersMap.get(2)[2] + cubeEdgesMap.get(6)[1] + cubeCorners.get(6)[2];//012
		result+=cubeEdgesMap.get(1)[0] + "R" +  cubeEdgesMap.get(9)[0]; //345
		result+=cubeCornersMap.get(0)[2]+ cubeEdgesMap.get(4)[1] + cubeCornersMap.get(8)[2]; //678
		result+=cubeCorners
		 */
		
		return result;
		
	}
	
	/*
	 * creates a map of where the cubie current is to its goal position
	 * */
	private HashMap<Integer, Integer> mappedCorners(){
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
		for (int i=0; i<8; i++){
			char[] cubeOutofPos = cubeCornersMap.get(i);
			Arrays.sort(cubeOutofPos);
			//System.out.print(new String (cubeOutofPos));
			for(int j=0; j<8; j++){
				char[] cubeGoalPos = cornersFaces[j];
				Arrays.sort(cubeGoalPos);
				//System.out.print(new String(cubeGoalPos));
				if (Arrays.equals(cubeOutofPos, cubeGoalPos)){
					result.put(i, j);
					//System.out.println(i+ " " + j);
					break;
				}
			}
		}
		return result;
	}
	
	private static int encode (HashMap<Integer, Integer> mappedSides, int encodedLength){
		//need to fix, minus i?
		int result =0;
		for (int i =0; i<encodedLength; i++){
			int diff =0;
			int currPos=i;
			int goalPos =mappedSides.get(currPos);
			// Find the shift amount for this current position
			
			//result += (diff) * factorial(encodedLength-i);
		}
	
		return result;
	}
	
	public static long encodeCorners (HashMap<Integer, char[]> state){
		long result =0;
		initCornerValues();
		//ArrayList<char[]> cornerValuesCopy = new ArrayList<char[]>(cornerValues);
		for (int i=0; i< state.size(); i++){
			int ortVal =0; //orientation value
			char[] cubie = state.get(i);
			int indexOfCubies = findCubie(cubie, cornerValues);
			if (indexOfCubies<0){
				System.out.println(indexOfCubies);
			}
			//System.out.println(indexOfCubies);
			for(int j=0; j<3; j++){
				//get find the value of the cubie's orientation, by comparing the first char in the orientation
				if ((cornerValues.get(indexOfCubies+j))[0] == cubie[0]){
					ortVal = indexOfCubies+j;
					break;
				}
			}
			//remove cube orientation values from cornerValues 
			for (int j=0; j<3; j++){
				//remove i (first instance of cubie) 3 times
				cornerValues.remove(indexOfCubies);
			}

			//multiple ortVal by the size of cornerValues
			System.out.println(i+" " + ortVal +" "+ cornerValues.size());
			result += ortVal * factorial(cornerValues.size());
			
		}
		if (result<0){
			for (int i=0; i<state.size(); i++)
				System.out.println(result +" "+ new String (state.get(i)));
			System.exit(0);
		}
		System.out.println(result);
		return result;
	}
	
	public static String encodeEdges(HashMap<Integer, char[]> state){
		/*HashMap<Integer, Integer> mappedEdges = mapEdges(state);
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < mappedEdges.size(); i++) {
			
			builder.append(mappedEdges.get(i));
		}
		return builder.toString();
		*/
		if (limit<10000){
			limit+=1;
			System.out.println(limit);
			return ""+limit;
			
		}
		else{
			System.out.println(limit);
			return "0";
		}
	}
	private static HashMap<Integer, Integer> mapEdges(HashMap<Integer, char[]> state) {
		for (int i=0; i<12; i++)
			if (state.containsKey(i))
			System.out.println(new String (state.get(i)));
		HashMap<Integer, Integer> result = new HashMap<Integer, Integer>();
			for (int i = 0; i <12; i++) {
				if (state.containsKey(i)){
					char[] temp = new char[3];
					temp = state.get(i);
					System.out.println("temp " + new String(temp));
					String tempStr ="";
					for (int j=0; j< temp.length;j++){
						//remove 0s from string
						if (temp[j] != '0')
							tempStr+=temp[j];
					}
					if (tempStr.charAt(0)>tempStr.charAt(1))
						tempStr="" +tempStr.charAt(1) +tempStr.charAt(0);
					result.put(Cube.goalEdges.get(tempStr), i);
					System.out.println(Cube.goalEdges.get(tempStr) +" " +i +" tempstr "+tempStr );
			}	
		}
			
		return result;
	}
	
	public static int findCubie(char[] cubie,List<char[]> cornerValuesCopy ){

		Arrays.sort(cubie);
		//System.out.println(new String (cubie));
		for (int i=0; i<cornerValuesCopy.size(); i+=3){
			char[] temp1 =cornerValuesCopy.get(i);
			Arrays.sort(temp1);
			if (Arrays.equals(temp1, cubie)){
				
				return i;
			}
		}
		return -1;
	}
	
    public static int factorial(int n) {
        int fact = 1; // this  will be the result
        for (int i = 1; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }
	
}