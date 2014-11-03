package rubik;

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
	
	public final static int [][]  edges = new int[][]{
		{21, 20}, //A-01
		
		{10, 3},  //A-10
		{28, 39}, //A-12
		
		{48, 18}, //A-21
		
		{13, 7},  //B-00
		{31, 37}, //B-02
		
		{52, 1},  //B-20
		{46, 43}, //B-22
		
		{23, 24}, //C-01
		
		{16, 5},  //C-10
		{34, 41}, //C-12
		
		{50, 26}, //C-21
	};
	public final static char[][] edgesFace = new char[][]{
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
	
	public char[][]cubeCornerFaces =new char[8][3];
	public char[][]cubeEdgeFaces = new char[12][6];
	public static HashMap<Integer, int[]> ce= new HashMap<Integer, int[]>();
	
	public HashMap<Integer,char[]> cubeCornersMap = new HashMap <Integer, char[] > ();
	public HashMap<Integer, char[]> cubeEdgesMap = new HashMap <Integer, char[] > ();
	
	public static final char[] FACES = {'Y', 'W', 'R', 'B', 'O', 'G'}; 
	
	public static  int[] cubeCorners = new int[8];
	public static  int[] cubeEdges = new int[12];
	//indexes of cubes in theses faces, first char = color, second = corner or edge
	//order dependent upon view of face
	private final static int[] faceYC = {0, 4, 5, 1};
	private final static int[] faceYE = {0, 4, 8, 5};
	
	private final static int[] faceWC = {2, 6, 7, 3};
	private final static int[] faceWE = {3, 6, 11, 7};
	
	private final static int[] faceRC= {0, 4, 6, 2};
	private final static int[] faceRE= {1, 4, 9, 6};
	
	private final static int[] faceBC ={5, 4, 6, 7};
	private final static int[] faceBE ={8, 9, 11, 10};
	
	private final static int[] faceOC ={1, 5, 7, 3};
	private final static int[] faceOE ={2,5, 10, 7};
	
	private final static int[] faceGC={0, 2, 3, 1};
	private final static int[] faceGE={0,1, 3, 2};
	
	public String firstState;
	
	static List<char[]> cornerValues;
	static List<char[]> edgeValues; //0-5
	static List<char[]> edgeValues1; //6-11
	static HashMap<String, Integer> goalEdges;
	
	public static int limit =0;
	
	public Cube(String input){
		firstState =input;
		
		
		makeCube(input);

		//System.out.println(rotateStr('O'));
		//System.out.println("encoded edges " +encodeEdges(0));
		
		/*
		rotate('G');
		encodeCorners();
		System.out.println(toString());
		for(int i=0; i< cubeCornersMap.size();i++)
			System.out.println(i +" "+ findCubie(cubeCornersMap.get(i), cornersFaces));
		encodeCorners(cubeCornersMap);
		*/
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
		cornerValues= new LinkedList<char[]>();
		cornerValues.add(new char[] {'Y','G','R'}); //0
		//cornerValues.add(new char[] {'R', 'Y', 'G'});//1
		//cornerValues.add(new char[] {'G', 'R', 'Y'});//2
		
		cornerValues.add(new char[]{'Y', 'G', 'O'});//3
		//cornerValues.add(new char[]{'O', 'Y', 'G'});//4
		//cornerValues.add(new char[]{'G', 'O', 'Y'});//5
		
		cornerValues.add(new char[]{'W', 'G', 'R'});//6
		//cornerValues.add(new char[]{'R', 'W', 'G'});//7
		//cornerValues.add(new char[]{'G', 'R', 'W'});//8
		
		cornerValues.add(new char[]{'W', 'G', 'O'});//9
		//cornerValues.add(new char[]{'O', 'W', 'G'});//10
		//cornerValues.add(new char[]{'G', 'O', 'W'});//11
		
		cornerValues.add(new char[]{'Y', 'B', 'R'});//12
		//cornerValues.add(new char[]{'R', 'Y', 'B'});//13
		//cornerValues.add(new char[]{'B', 'R', 'Y'});//14
		
		cornerValues.add(new char[]{'Y', 'B', 'O'});//15
		//cornerValues.add(new char[]{'O', 'Y', 'B'});//16
		//cornerValues.add(new char[]{'B', 'O', 'Y'});//17
		
		cornerValues.add(new char[]{'W','B', 'R'});//18
		//cornerValues.add(new char[]{'R', 'W', 'B'});//19
		//cornerValues.add(new char[]{'B', 'R', 'W'});//20
		
		cornerValues.add(new char[]{'W', 'B', 'O'});//21
		//cornerValues.add(new char[]{'O', 'W', 'B'});//22
		//cornerValues.add(new char[]{'B', 'O', 'W'});//23
	}
	
	public static void initEdgeValues(){
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
	
	public void rotate(char face){
		cubeCornersMap =rotate(face,cubeCornersMap, 'C');
		cubeEdgesMap=rotate(face,cubeEdgesMap, 'E');
		
	}
	
	public String rotateStr(char face){
		return this.toString(rotate(face,cubeCornersMap, 'C'), rotate(face,cubeEdgesMap, 'E'));
		
	}
	
	public void rotate(char face, char cubieType){
		cubeCornersMap =rotate(face,cubeCornersMap, cubieType);
		
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
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[1],tempCube[0],tempCube[2]});
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
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[2],tempCube[1],tempCube[0]});
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
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[1],tempCube[0],tempCube[2]});
					
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
							cubeCornersMapCopy.put(newPos, new char[] {tempCube[2],tempCube[1],tempCube[0]});
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

			switch (face){
				case 'Y':{				
					for (int i=0; i<4; i++){
						currPos =faceYE[i];
						newPos = (i==3)? faceYE[0] :faceYE[i+1];
						if(state.containsKey(currPos)){
						tempCube = state.get(currPos);
						cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
				case 'W':{
					for (int i=0; i<4; i++){
						
						currPos =faceWE[i];
						newPos = (i==3)? faceWE[0] :faceWE[i+1];
						if(state.containsKey(currPos)){
						tempCube = state.get(currPos);
						cubeEdgesMapCopy.put(newPos, new char[] {tempCube[0],tempCube[2],tempCube[1]});
						}
					}
					break;
				}
				case 'R':{
					for (int i=0; i<4; i++){
						
	
						currPos =faceRE[i];
						newPos = (i==3)? faceRE[0] :faceRE[i+1];
						if(state.containsKey(currPos)){
							tempCube = state.get(currPos);
							//cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[1],tempCube[0],tempCube[2]});
						}
					}
					break;
				}
				case 'B':{
					for (int i=0; i<4; i++){
		
						currPos =faceBE[i];
						newPos = (i==3)? faceBE[0] :faceBE[i+1];
						if(state.containsKey(currPos)){
						tempCube = state.get(currPos);
						cubeEdgesMapCopy.put(newPos, new char[] {tempCube[2],tempCube[1],tempCube[0]});
						}
					}
					break;
				}
				case 'O':{
					for (int i=0; i<4; i++){
												
						currPos =faceOE[i];
						newPos = (i==3)? faceOE[0] :faceOE[i+1];
						if(state.containsKey(currPos)){
							tempCube = state.get(currPos);
						//	cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[1],tempCube[0],tempCube[2]});
						}
					}
					break;
				}
				case 'G':{
					for (int i=0; i<4; i++){
						
						currPos =faceGE[i];
						newPos = (i==3)? faceGE[0] :faceGE[i+1];
						if(state.containsKey(currPos)){
							tempCube = state.get(currPos);
							//cubeEdgesMapCopy.remove(currPos);
							cubeEdgesMapCopy.put(newPos, new char[] {tempCube[2],tempCube[1],tempCube[0]});
						}
					}
					break;
				}
			}
			return cubeEdgesMapCopy;
		}

}
		
	
	
	/*
	 * Converts rotated Cube back to string state
	 * */
	@Override
	public String toString(){
		String result ="";
		result+=""+cubeCornersMap.get(2)[2] + cubeEdgesMap.get(6)[2] + cubeCornersMap.get(6)[2];//012
		result+=""+cubeEdgesMap.get(1)[2] + "R" +  cubeEdgesMap.get(9)[2]; //345
		result+=""+cubeCornersMap.get(0)[2]+ cubeEdgesMap.get(4)[2] + cubeCornersMap.get(4)[2]; //678
		result+=""+cubeCornersMap.get(2)[1] + cubeEdgesMap.get(1)[1] + cubeCornersMap.get(0)[1];//01011
		result+=""+cubeCornersMap.get(0)[0]+cubeEdgesMap.get(4)[0]+cubeCornersMap.get(4)[0]; //121314
		result+=""+cubeCornersMap.get(4)[1]+cubeEdgesMap.get(9)[1]+cubeCornersMap.get(6)[1]; //151617
		result+=""+cubeEdgesMap.get(3)[1]+"G"+cubeEdgesMap.get(0)[1]; //181920
		result+=""+cubeEdgesMap.get(0)[0]+"Y"+cubeEdgesMap.get(8)[0];//212223
		result+=""+cubeEdgesMap.get(8)[1]+"B"+cubeEdgesMap.get(11)[1];//242526
		result+=""+cubeCornersMap.get(3)[1]+cubeEdgesMap.get(2)[1]+cubeCornersMap.get(1)[1];//272829
		result+=""+cubeCornersMap.get(1)[0]+cubeEdgesMap.get(5)[0]+cubeCornersMap.get(5)[0];//303132
		result+=""+cubeCornersMap.get(5)[1]+cubeEdgesMap.get(10)[1]+cubeCornersMap.get(7)[1];//333435
		result+=""+cubeCornersMap.get(1)[2]+cubeEdgesMap.get(5)[2]+cubeCornersMap.get(5)[2];//363738
		result+=""+cubeEdgesMap.get(2)[2]+"O"+cubeEdgesMap.get(10)[2];//394041
		result+=""+cubeCornersMap.get(3)[2]+cubeEdgesMap.get(7)[2]+cubeCornersMap.get(7)[2];//424344
		result+=""+cubeCornersMap.get(3)[0]+cubeEdgesMap.get(7)[0]+cubeCornersMap.get(7)[0];//454647
		result+=""+cubeEdgesMap.get(3)[0]+"W"+cubeEdgesMap.get(11)[0];//484950
		result+=""+cubeCornersMap.get(2)[0]+cubeEdgesMap.get(6)[0]+cubeCornersMap.get(6)[0];//515253
		
		
		return result;
		
	}
	
	public String toString (HashMap<Integer,char[]> cornersMap, HashMap<Integer,char[]> edgesMap){
		String result ="";
		result+=""+cornersMap.get(2)[2] + edgesMap.get(6)[2] + cornersMap.get(6)[2];//012
		result+=""+edgesMap.get(1)[2] + "R" +  edgesMap.get(9)[2]; //345
		result+=""+cornersMap.get(0)[2]+ edgesMap.get(4)[2] + cornersMap.get(4)[2]; //678
		result+=""+cornersMap.get(2)[1] + edgesMap.get(1)[1] + cornersMap.get(0)[1];//01011
		result+=""+cornersMap.get(0)[0]+edgesMap.get(4)[0]+cornersMap.get(4)[0]; //121314
		result+=""+cornersMap.get(4)[1]+edgesMap.get(9)[1]+cornersMap.get(6)[1]; //151617
		result+=""+edgesMap.get(3)[1]+"G"+edgesMap.get(0)[1]; //181920
		result+=""+edgesMap.get(0)[0]+"Y"+edgesMap.get(8)[0];//212223
		result+=""+edgesMap.get(8)[1]+"B"+edgesMap.get(11)[1];//242526
		result+=""+cornersMap.get(3)[1]+edgesMap.get(2)[1]+cornersMap.get(1)[1];//272829
		result+=""+cornersMap.get(1)[0]+edgesMap.get(5)[0]+cornersMap.get(5)[0];//303132
		result+=""+cornersMap.get(5)[1]+edgesMap.get(10)[1]+cornersMap.get(7)[1];//333435
		result+=""+cornersMap.get(1)[2]+edgesMap.get(5)[2]+cornersMap.get(5)[2];//363738
		result+=""+edgesMap.get(2)[2]+"O"+edgesMap.get(10)[2];//394041
		result+=""+cornersMap.get(3)[2]+edgesMap.get(7)[2]+cornersMap.get(7)[2];//424344
		result+=""+cornersMap.get(3)[0]+edgesMap.get(7)[0]+cornersMap.get(7)[0];//454647
		result+=""+edgesMap.get(3)[0]+"W"+edgesMap.get(11)[0];//484950
		result+=""+cornersMap.get(2)[0]+edgesMap.get(6)[0]+cornersMap.get(6)[0];//515253
		
		return result;
	}

	
	/*
	 * encodes cube corners to a number
	 * */
	public int encodeCorners (){
		int result =0;
		initCornerValues();
		int orientation=0;
		int simpleState=0;
		for (int i=0; i< 8; i++){
			int ortVal =0; //orientation value
			char[] cubie = cubeCornersMap.get(i);
			//System.out.println(new String(cubie));
			int indexOfCubies = findCubie(cubie, cornerValues);
			//System.out.println(new String(cornerValues.get(indexOfCubies))+" " +new String (cubie));
			if (cornerValues.get(indexOfCubies)[0] == cubie[0])
				ortVal=0;
			else if(cornerValues.get(indexOfCubies)[0]==cubie[1])
				ortVal=1;
			else if (cornerValues.get(indexOfCubies)[0]==cubie[2])
				ortVal=2;
			
			//remove cube orientation values from cornerValues 
			cornerValues.remove(indexOfCubies);
			simpleState+= indexOfCubies * factorial(cornerValues.size());
			
			//multiple ortVal by the size of cornerValues
			//System.out.println("Current Value of Cubie " +indexOfCubies+" " +" Current Size"+ (cornerValues.size()));
			//System.out.println(findCubie(cubie, cornersFaces));
			
			orientation+= ortVal* Math.pow(3, findCubie(cubie, cornersFaces));
			//System.out.println(ortVal +" "+orientation);
		}
		//System.out.println(orientation);
		//System.out.println(""+simpleState);
		result = simpleState + factorial(8) *orientation;
		
		//System.out.println(result);
		return result;
		
	}
	
	/*
	 * Encodes cube edges to a number
	 */
	public int encodeEdges(int base){
		int result =0;
		initEdgeValues();
		if (base==0){
		for (int i=0; i< 6; i++){
			//System.out.println(i);
			char[] cubie = cubeEdgesMap.get(i);
			int indexOfCubies = findEdgeCubie(cubie, edgeValues);
			int ortVal =0;
			char[] temp = new char[2];
			int k=0;
			for (int j=0; j<3; j++){
				if (cubie[j] !='0'){
					temp[k]=cubie[j];
					k++;
				}
			}
			if (Arrays.equals(edgeValues.get(indexOfCubies), temp))
				ortVal = indexOfCubies;
			else
				ortVal = indexOfCubies+1;
			
			int rem= edgeValues.size();
			
			//System.out.println("indexOfCubies " +indexOfCubies);
			//System.out.println(ortVal *(rem -2) * (rem -4) *(rem-6) *(rem-8) *(rem -10));
			
			result+= ortVal *(rem -2) * (rem -4) *(rem-6) *(rem-8) *(rem -10);

			edgeValues.remove(indexOfCubies+1);
			edgeValues.remove(indexOfCubies);
			
		}
		//System.out.println(result);
		return result;
		}
		else {
			for (int i=6; i< 11; i++){
				//System.out.println(i);
				char[] cubie = cubeEdgesMap.get(i);
				int indexOfCubies = findEdgeCubie(cubie, edgeValues);
				int ortVal =0;
				char[] temp = new char[2];
				int k=0;
				for (int j=0; j<3; j++){
					if (cubie[j] !='0'){
						temp[k]=cubie[j];
						k++;
					}
				}
				if (Arrays.equals(edgeValues.get(indexOfCubies), temp))
					ortVal = indexOfCubies;
				else
					ortVal = indexOfCubies+1;
				
				int rem= edgeValues.size();
				
				//System.out.println("indexOfCubies " +indexOfCubies);
				//System.out.println(ortVal *(rem -2) * (rem -4) *(rem-6) *(rem-8) *(rem -10));
				
				result+= ortVal *(rem -2) * (rem -4) *(rem-6) *(rem-8) *(rem -10);

				edgeValues.remove(indexOfCubies+1);
				edgeValues.remove(indexOfCubies);
				
			}
			
			return result;
		}
	}
	
	public static int findEdgeCubie(char[] cubie, List<char[]> cornerValuesCopy){
		char[] tempCubie = new char[2];
		int k=0;
		for (int j=0; j<3; j++){
			//strip 0's
			if (cubie[j] !='0'){
				tempCubie[k]=cubie[j];
				k++;
			}
		}
		Arrays.sort(tempCubie);
		char[] temp1 = new char[3];
		//System.out.println(new String(tempCubie));
		for (int i=0; i<cornerValuesCopy.size(); i++){
			temp1 =cornerValuesCopy.get(i).clone();
			Arrays.sort(temp1);
			//System.out.println(temp1);
			if (Arrays.equals(temp1, tempCubie)){
				return i;
			}
			
		}
		return -1;
	}
	
	public static int findCubie(char[] cubie,List<char[]> cornerValuesCopy ){
		char[] tempCubie = new char[3];
		int k=0;
		for (int j=0; j<3; j++){
			//strip 0's
			if (cubie[j] !='0'){
				tempCubie[k]=cubie[j];
				k++;
			}
		}
		
		Arrays.sort(tempCubie);
		
		char[] temp1 = new char[3];
		for (int i=0; i<cornerValuesCopy.size(); i++){
			temp1 =cornerValuesCopy.get(i).clone();
			Arrays.sort(temp1);
			//System.out.println(Arrays.equals(temp1, tempCubie));
			if (Arrays.equals(temp1, tempCubie)){
				return i;
			}
			
		}
		return -1;
	}

	public static int findCubie(char[] cubie, char[][] cornerValuesCopy ){

		char[] tempCubie = new char[3];
		tempCubie =cubie.clone();
		Arrays.sort(tempCubie);
		char[] temp1 = new char[3];
		//System.out.println(new String (cubie));
		for (int i=0; i<cornerValuesCopy.length; i++){
			temp1 =cornerValuesCopy[i].clone();
			Arrays.sort(temp1);
			if (Arrays.equals(temp1, tempCubie)){
				
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