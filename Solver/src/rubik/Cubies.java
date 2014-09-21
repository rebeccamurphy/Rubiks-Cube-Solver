package rubik;

import java.util.Arrays;

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
	public static int[] centers = new int[] {22, 49, 4,25, 40, 19};
	public static char[] centersColors = new char[] {'Y', 'W', 'R','B', 'O', 'G'};
	public static char[][]cubeCorner =new char[8][3];
	public static boolean makeCube(String cubeString/*, char[][][] left, char[][][] middle, char[][][] right*/){
		for (int i =0; i<6; i++ ){
			//check centers
			char center = cubeString.charAt(centers[i]);
			if (center != centersColors[i])
				return false;
		}
		for (int i =0; i< corners.length; i++){
			//check corners
			cubeCorner[i][0] = cubeString.charAt(corners[i][0]);
			cubeCorner[i][1] = cubeString.charAt(corners[i][1]);
			cubeCorner[i][2] = cubeString.charAt(corners[i][2]);
			
			for(int j=0; j< cornersFaces.length; j++){
				if (checkFaces(cubeCorner[i], cornersFaces[j])){
					if (checkFaceRotation(cubeCorner[i],i, j)){
						System.out.println("pos " +j +" check face rotation " +checkFaceRotation(cubeCorner[i], j, i));
						j = cornersFaces.length;
					}
					else 
						return false;
					
				}
			}
			
		}
				
		return true;
	}
	public static boolean checkValid(String cubeString/*char[][] left, char[][] middle, char[][] right*/){
		return makeCube(cubeString);
	}
	private static boolean checkFaces(char[] cubie, char[] defaultCubieFaces){
		for(int i =0; i< cubie.length; i++){
			if ((new String (defaultCubieFaces)).indexOf(cubie[i])==-1){
				return false;
			}
		}
			
		return true;
	}
	private static boolean checkFaceRotation(char[] cubieFaces, int currPos, int origin){
		String faceStr = (new String(cubieFaces));
		if ( ((origin==0||origin==3||origin==6||origin==7) &&(currPos ==0 ||  currPos ==3 ||currPos==6||currPos==7))
			||((origin==1||origin==2||origin==4||origin==5)&&(currPos==1|| currPos==2||currPos==4||currPos==5))){
			//System.out.println(currPos +" " + faceStr + " " + new String(cornersFaces[currPos]));
			//System.out.println(faceStr.equals(new String(cornersFaces[pos])));
			return faceStr.equals(new String(cornersFaces[currPos])) 
					|| faceStr.equals(cornersFaces[currPos][2] +cornersFaces[currPos][0]+cornersFaces[currPos][0])
					|| faceStr.equals(cornersFaces[currPos][1]+ cornersFaces[currPos][2] + cornersFaces[currPos][0]);
		}
		else if (((origin==0 || origin==3 ||origin==6||origin==7) && (currPos==1 || currPos==2 || currPos==4 ||currPos==5))
				||((origin==1 ||origin==2||origin==4||origin==5)&&(currPos==0 || currPos==3 || currPos==6||currPos==7))){
			//System.out.println(currPos + " " +faceStr);
			return faceStr.equals(cornersFaces[currPos][0]+cornersFaces[currPos][2]+cornersFaces[currPos][1]) 
					|| faceStr.equals(cornersFaces[currPos][1] +cornersFaces[currPos][0]+cornersFaces[currPos][2])
					|| faceStr.equals(cornersFaces[currPos][2]+ cornersFaces[currPos][1] + cornersFaces[currPos][0]);
		}
		return false;
	}
}
