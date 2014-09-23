package rubik;

import java.util.Arrays;
import java.util.HashMap;

public class Cubies {
	static char[][][] A =new char[][][] {
		{
			{'R', 'Y', 'G'},//0
			{'Y', 'G'},		//1
			{'O', 'Y','G'},	//2
		},
		{
			{'R', 'G'},		//3
			{'G'},			//4 important!
			{'O', 'G'},		//5
		},
		{
			{'R', 'W', 'G'},//6 
			{'G', 'W'},		//7 important!
			{'G', 'W','O'}	//8
		}
	};
	static char[][][] B =new char[][][] {
		{
			{'Y', 'R',},	//0
			{'Y'},			//1 important!
			{'Y', 'O'},		//2
		},
		{
			{'R'},			//3 important!
			null,			//4 important!
			{'O'},			//5 important!
		},
		{
			{'R', 'W'},		//6 
			{'W'},			//7 important!
			{'O','W'}		//8
		}
	};
	static char[][][] C =new char[][][] {
		{
			{'R', 'B', 'Y'},//0
			{'Y', 'B'},		//1 
			{'Y', 'B', 'O'},//2
		},
		{
			{'R', 'B'},		//3 
			{'B'},			//4 important!
			{'O', 'B'},			//5 
		},
		{
			{'R', 'B','W'},		//6 
			{'B','W'},			//7 important!
			{'B','W','O'}		//8
		}
	};
	static int[][] corners =new int[][]{
		//starting 0,0
		{12, 6, 11}, //A-00
		{30, 36, 29},//A-02
		
		{51, 0, 9 }, //A-20
		{45, 42, 27},//A-22
		
		{14, 8, 15}, //C-00
		{32, 33, 38},//C-02
		
		{53, 2, 17}, //C-20
		{47, 35, 44} //C-22
	};
	static char[][] cornersFaces = new char[][]{
		{'Y', 'R', 'G'}, //A-00
		{'Y', 'O', 'G'}, //A-02
		{'W', 'R', 'G'}, //A-20
		{'W', 'O', 'G'}, //A-22
		
		{'Y', 'R', 'B'}, //C-00
		{'Y', 'B', 'O'}, //C-02
		{'W', 'R', 'B'}, //C-20
		{'W', 'B', 'O'}
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
		{'Y','0','0','0','0','G'},	//A-01
		{'0','0','R','0','0','G'}, //A-10
		{'0','0','0','0','O','G'}, //A-12
		{'0','W','0','0','0','G'}, //A-21
		
		{'Y','0','R','0','0','0'}, //B-00
		{'Y','0','0','0','O','0'}, //B-02
		{'0','W','R','0','0','0'}, //B-20
		{'0','W','0','0','O','0'}, //B-22
		
		{'Y','0','0','B','0','0'}, //C-01
		{'0','0','R','B','0','0'}, //C-10
		{'0','0','0','B','0','O'}, //C-12
		{'0','W','0','B','0','0',}, //C-21
	};
	public static int[] centers = new int[] {22, 49, 4,25, 40, 19};
	public static char[] centersColors = new char[] {'Y', 'W', 'R','B', 'O', 'G'};
	public static char[][]cubeCornerFaces =new char[8][3];
	public static char[][]cubeEdgeFaces = new char[12][6];
	public static HashMap ce= new HashMap();
	public static int cubeCorners[]= new int[8];
	public static int cubeEdges[] = new int[12];
	public static int cubeFlippedEdges =0;
	public static int cubeFlippedCornerOrigin=0;
	public static int cubeCornerParity=0;
	public static int cubeEdgeParity=0;
	public static boolean makeCube(String cubeString/*, char[][][] left, char[][][] middle, char[][][] right*/){
		for (int i =0; i<6; i++ ){
			//check centers
			char center = cubeString.charAt(centers[i]);
			if (center != centersColors[i]){
				System.out.println("Center color wrong: " + center + " " + i);
				return false;
			}
		}
		for (int i =0; i< corners.length; i++){
			//check corners for sticker swap
			cubeCornerFaces[i][0] = cubeString.charAt(corners[i][0]);
			cubeCornerFaces[i][1] = cubeString.charAt(corners[i][1]);
			cubeCornerFaces[i][2] = cubeString.charAt(corners[i][2]);
			
			for(int j=0; j< cornersFaces.length; j++){
				if (checkFaces(cubeCornerFaces[i], cornersFaces[j])){
					checkFaceRotation(cubeCornerFaces[i],i, j);
				}
					
			}
		}
		
		//parse corners
		for(int i=0; i<edges.length;i++){
			int curColor =0;
			for (int j=0; j<6; j++){
				if (edgesFace[i][j]!='0'){
					cubeEdgeFaces[i][j] = cubeString.charAt(edges[i][curColor]);
					curColor++;
				}
				else 
					cubeEdgeFaces[i][j] = '0';
			}
			checkEdgeRotation(cubeEdgeFaces[i], i);
		}	
		
		System.out.println((getInvCount(cubeEdges)+getInvCount(cubeCorners)));
		
		if (((getInvCount(cubeEdges)+getInvCount(cubeCorners)) %2 != 0)){
			//permutation check
			System.out.println("Corners swapped illegally " + getInvCount(cubeCorners));
			System.out.println("Edges swapped illegally " + getInvCount(cubeEdges));
			return false;
		}
		
		System.out.println("cubeEdgeParity " +cubeEdgeParity);
		if(cubeEdgeParity%2!=0)
			return false;
		
		System.out.println("cubeCornerParity " + cubeCornerParity);
		if (cubeCornerParity%3!=0)//corner parity check
			return false;
			
		
		return true;
	}
	public static boolean checkValid(String cubeString/*char[][] left, char[][] middle, char[][] right*/){
		return makeCube(cubeString);
	}
	private static boolean checkFaces(char[] cubie, char[] defaultCubieFaces){
		for(int i =0; i< cubie.length; i++){
			if ((new String (defaultCubieFaces)).indexOf(cubie[i])==-1){
				//System.out.println(new String(cubie) + " " + new String(defaultCubieFaces));
				return false;
			}
		}
			
		return true;
	}
	private static void checkFaceRotation(char[] cubieFaces, int currPos, int origin){
		//String faceStr = (new String(cubieFaces));
		//System.out.println(currPos + " " +origin);
		/*if (currPos==origin && !(faceStr.equals(new String(cornersFaces[origin])))){
			System.out.println(faceStr +" " +(new String(cornersFaces[origin])));
			//cubeFlippedCornerOrigin++;
		}*/
		if (cubieFaces[0]==cornersFaces[origin][0]) //correct
			cubeCornerParity+=0;
		else if (cubieFaces[0]==cornersFaces[origin][2]) //clockwise
			cubeCornerParity+=1;
		else if (cubieFaces[0]==cornersFaces[origin][1]) //counter-clockwise
			cubeCornerParity+=2;
		else
			System.out.println("Corner Parity went wrong");
		
	}
	private static void checkEdgeRotation(char[]cubieFaces, int currCubeIndex){
		char[] origin = new char[6];
		for (int i=0; i<edgesFace.length; i++){
			if (checkFaces(cubieFaces, edgesFace[i])){
				origin = edgesFace[i];
				cubeEdges[currCubeIndex]=i;
				//originNum=i;
				break;
			}
		}
		for(int j=0; j<origin.length; j++){
			if (origin[j]!='0' && cubieFaces[j]!='0'){
				if (origin[j]==cubieFaces[j])
					cubeEdgeParity+=0;
				else
					cubeEdgeParity++; //edge is incorrectly orientated
			}
		}
		
	}
	private static int getInvCount(int arr[])
	{
	  int inv_count = 0;
	  int i, j;
	  int n = arr.length;
	  for(i = 0; i < n - 1; i++){
		  //System.out.println(arr[i]);
		  for(j = i+1; j < n; j++)
	      if(arr[i] > arr[j])
	        inv_count++;
	  }
	  return inv_count;
	}
}
