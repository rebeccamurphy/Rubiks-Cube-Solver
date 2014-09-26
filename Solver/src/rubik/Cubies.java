package rubik;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Cubies {
	/*
	static int[][] corners =new int[][]{
		//starting 0,0
		//X  Y  Z
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
		//X=Y,W; Y=G,B, Z =O,R
		//X    Y    Z
		{'Y', 'R', 'G'}, //A-00
		{'Y', 'O', 'G'}, //A-02
		{'W', 'R', 'G'}, //A-20
		{'W', 'O', 'G'}, //A-22
		
		{'Y', 'R', 'B'}, //C-00
		{'Y', 'B', 'O'}, //C-02
		{'W', 'R', 'B'}, //C-20
		{'W', 'B', 'O'}
	};*/

	static int[][] corners =new int[][]{
		//starting 0,0
		//X  Y  Z
		{12, 11, 6}, //A-00
		{30, 29, 36},//A-02
		
		{51, 9, 0 }, //A-20
		{45, 27, 42},//A-22
		
		{14, 15, 8}, //C-00
		{32, 33, 38},//C-02
		
		{53, 17, 2}, //C-20
		{47, 35, 44} //C-22
	};
	static char[][] cornersFaces = new char[][]{
		//X=Y,W; Y=G,B, Z =O,R
		//X    Y    Z
		{'Y', 'G', 'R'}, //A-00
		{'Y', 'G', 'O'}, //A-02
		{'W', 'G', 'R'}, //A-20
		{'W', 'G', 'O'}, //A-22
		
		{'Y', 'B', 'R'}, //C-00
		{'Y', 'B', 'O'}, //C-02
		{'W', 'B', 'R'}, //C-20
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
		{'0','0','0','B','O','0'}, //C-12
		{'0','W','0','B','0','0',}, //C-21
	};
	public static int[] centers = new int[] {22, 49, 4,25, 40, 19};
	public static char[] centersColors = new char[] {'Y', 'W', 'R','B', 'O', 'G'};
	public static char[][]cubeCornerFaces =new char[8][3];
	public static char[][]cubeEdgeFaces = new char[12][6];
	public static HashMap<Integer, int[]> ce= new HashMap<Integer, int[]>();
	
	public static int cubeCorners[]= new int[8];
	public static int cubeEdges[] = new int[12];
	public static int cubeFlippedEdges =0;
	public static int cubeFlippedCornerOrigin=0;
	public static int cubeCornerParity=0;
	public static int cubeEdgeParity=0;
	static char[] cubieTest = new char[6];
	private static void generateEdgeHashMap(){
		ce.put(0, new int[]{0, 0,0});
		ce.put(1, new int[]{0, 1, 0});
		ce.put(2, new int[]{0, 1, 2});
		ce.put(3, new int[]{0, 2, 1});
		ce.put(4, new int[]{1, 0, 0});
		ce.put(5, new int[]{1, 0, 2});
		ce.put(6, new int[]{1, 2, 0});
		ce.put(7, new int[]{1, 2, 2});
		ce.put(8, new int[]{2, 0, 1});
		ce.put(9, new int[]{2, 1, 0});
		ce.put(10, new int[]{2, 1, 2});
		ce.put(11, new int[]{2, 2, 1});
	}
	
	public static boolean makeCube(String cubeString/*, char[][][] left, char[][][] middle, char[][][] right*/){
		cubeCornerParity=0;
		cubeEdgeParity=0;
		generateEdgeHashMap();
		
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
					cubeCorners[i] = j;
					checkFaceRotation(cubeCornerFaces[i],i, j);
					if (!checkFaceStickers(cubeCornerFaces[i], i, j)){
						//System.out.print("stick swap check failed"); TODO
						//return false;
					}
				}
					
			}
		}
		//System.out.println( new String(cubeEdgeFaces[4]));
		//parse edges
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
		/*
		////System.out.println((getInvCount(cubeEdges)+getInvCount(cubeCorners)));
		for (int i=0; i<cubeEdges.length; i++)
			////System.out.print(cubeEdges[i] +" ");
		////System.out.println();
		for (int i=0; i<cubeCorners.length; i++)
			////System.out.print(cubeCorners[i] +" ");*/
		if (((getInvCount(cubeEdges)+getInvCount(cubeCorners)) %2 != 0)){
			//permutation check
			System.out.println("Corners swapped illegally " + getInvCount(cubeCorners));
			System.out.println("Edges swapped illegally " + getInvCount(cubeEdges));
			return false;
		}
		////System.out.println("cubeEdgeParity " +cubeEdgeParity);
		/*if(cubeEdgeParity%2!=0){
			System.out.print("dang edge Parity " + cubeEdgeParity);
			return false;
		}*/
		////System.out.println("cubeCornerParity " + cubeCornerParity);
		if (cubeCornerParity%3!=0){//corner parity check
			System.out.print("dang corner Parity " + cubeCornerParity);
			return false;
		}
		
		return true;
	}
	public static boolean checkValid(String cubeString/*char[][] left, char[][] middle, char[][] right*/){
		return makeCube(cubeString);
	}
	private static boolean checkFaces(char[] cubie, char[] defaultCubieFaces){
		for(int i =0; i< cubie.length; i++){
			if ((new String (defaultCubieFaces)).indexOf(cubie[i])==-1){
				//////System.out.println(new String(cubie) + " " + new String(defaultCubieFaces));
				return false;
			}
		}
			
		return true;
	}
	private static void checkFaceRotation(char[] cubieFaces, int currPos, int origin){
		/*
		if ((cubieFaces[0]=='Y')|| cubieFaces[0]=='W'){
				//correct
				System.out.println("0 "+ (new String (cubieFaces)));
				cubeCornerParity+=0;
		}
		else if (cubieFaces[0]==cornersFaces[origin][2]||
				(cubieFaces[0]==cornersFaces[origin][1] &&cubieFaces[1]==cornersFaces[origin][0])){ //clockwise
			cubeCornerParity+=1;
			System.out.println("1 "+ (new String (cubieFaces)));
		}
		else if (cubieFaces[0]==cornersFaces[origin][1]||cubieFaces[0]==cornersFaces[origin][2]){ //counter-clockwise
			System.out.println("2 " +(new String (cubieFaces)));
			cubeCornerParity+=2;
		}
		else{
			System.out.println("Corner Parity went wrong");
		}*/
		System.out.println("checking " + currPos);
		if ((cubieFaces[0]=='Y')|| cubieFaces[0]=='W'){
			//correct
			System.out.println("0 "+ (new String (cubieFaces)));
			cubeCornerParity+=0;
		}
		else{
			switch (origin){
			case 0:{
				if (cubieFaces[0]=='R')
					cubeCornerParity+=2;
				else if (cubieFaces[0]=='G')
					cubeCornerParity+=1;
				break;
			}
			case 1:{
				if (cubieFaces[0]=='O')
					cubeCornerParity+=1;
				else if (cubieFaces[0]=='G')
					cubeCornerParity+=2;
				break;
			}
			case 2:{
				if(cubieFaces[0]=='R')
					cubeCornerParity+=1;
				else if (cubieFaces[0]=='G')
					cubeCornerParity+=2;
				break;
			}
			case 3:{
				if (cubieFaces[0]=='O')
					cubeCornerParity+=2;
				else if (cubieFaces[0]=='G')
					cubeCornerParity+=1;
				break;
			}
			case 4:{
				if (cubieFaces[0]=='R')
					cubeCornerParity+=1;
				else if (cubieFaces[0]=='B')
					cubeCornerParity+=2;
				break;
			}
			case 5:{
				if(cubieFaces[0]=='O')
					cubeCornerParity+=2;
				else if (cubieFaces[0] =='B')
					cubeCornerParity+=1;
				break;
			}
			case 6:{
				if (cubieFaces[0]=='R')
					cubeCornerParity+=2;
				else if (cubieFaces[0]=='B')
					cubeCornerParity+=1;
				break;
			}
			case 7:{
				if (cubieFaces[0]=='O')
					cubeCornerParity+=1;
				else if (cubieFaces[0]=='B')
					cubeCornerParity+=2;
				break;
			}
			default: 
				System.out.println("Corner Parity went wrong");
			}
		}	
		
	}
	private static char shiftBE(char[] cubieFaces){
		return cubieFaces[1];
	}
	private static char shiftEB(char[] cubieFaces){
		return cubieFaces[2];
	}
	
	private static boolean checkFaceStickers(char[] cubieFaces, int currPos, int origin){
		String faceStr = (new String(cubieFaces));
		//////System.out.println(currPos + " " +origin);
		if ( ((origin==0||origin==3||origin==6||origin==7) &&(currPos ==0 ||  currPos ==3 ||currPos==6||currPos==7))
			||((origin==1||origin==2||origin==4||origin==5)&&(currPos==1|| currPos==2||currPos==4||currPos==5))){
			//////System.out.println(currPos +" " + faceStr + " " + new String(cornersFaces[currPos]));
			//////System.out.println(faceStr.equals(new String(cornersFaces[pos])));
			return faceStr.equals(new String(cornersFaces[origin])) 
					|| faceStr.equals(""+cornersFaces[origin][2] +cornersFaces[origin][0]+cornersFaces[origin][1])
					|| faceStr.equals(""+cornersFaces[origin][1]+ cornersFaces[origin][2] + cornersFaces[origin][0]);
		}
		else if (((origin==0 || origin==3 ||origin==6||origin==7) && (currPos==1 || currPos==2 || currPos==4 ||currPos==5))
				||((origin==1 ||origin==2||origin==4||origin==5)&&(currPos==0 || currPos==3 || currPos==6||currPos==7))){
			//////System.out.println(currPos + " " +faceStr);
			//////System.out.println("" +cornersFaces[origin][0]+ cornersFaces[origin][2] + cornersFaces[currPos][1]);
			return faceStr.equals(""+ cornersFaces[origin][0]+cornersFaces[origin][2]+cornersFaces[origin][1]) 
					|| faceStr.equals("" +cornersFaces[origin][1] +cornersFaces[origin][0]+cornersFaces[origin][2])
					|| faceStr.equals("" +cornersFaces[origin][2]+ cornersFaces[origin][1] + cornersFaces[origin][0]);
		}
		return false;
	}
	
	private static void checkEdgeRotation(char[]cubieFaces, int currCubeIndex){
		char[] origin = new char[6];
		int original =0;
		for (int i=0; i<edgesFace.length; i++){
			if (checkFaces(cubieFaces, edgesFace[i])){
				origin = edgesFace[i];
				original=i;
				cubeEdges[currCubeIndex]=i;
				//originNum=i;
				break;
			}
		}
		
			
		
		simulateEdgeTurns(original, currCubeIndex);
		
	}
	private static void simulateEdgeTurns(int origin, int currPos){
		int x,y,z, xo, yo, zo, turnsBT =0, turnsR =0, turnsL =0;
		/*xo= ce.get(origin)[0];
		yo= ce.get(origin)[1];
		zo= ce.get(origin)[2];
		
		x = ce.get(currPos)[0];
		y = ce.get(currPos)[1];
		z = ce.get(currPos)[2];
		
		turnsBT = Math.abs(xo-x);
		if (y!=0 || y!=2){ //not top or bot turnable
			//turn whatever side x is on to get it turnable
			if (x==2)
				turnsR++;
			else
				turnsL++;
		}*/
		int turnedCubePos=currPos;
		int[] left = new int[] {0,1,2,3};
		int[] right = new int [] {8,9,10,11};
		int[] top = new int[] {0,4,5,8};
		int[] bot = new int[] {3,6,7, 11};
		char[] cubie = new char[6];
		cubieTest = cubeEdgeFaces[currPos];
		xo= ce.get(origin)[0];
		yo= ce.get(origin)[1];
		zo= ce.get(origin)[2];
		int turnsY =0;
		while (turnsY<1){
		//	////System.out.println("origin, turnpos " + origin +" " + turnedCubePos);
			
			x = ce.get(turnedCubePos)[0];
			y = ce.get(turnedCubePos)[1];
			z = ce.get(turnedCubePos)[2];
			//System.out.println(origin);
			//X=White/Yellow, Y= green/blue, Z=Red/Orange
			turnsBT = Math.abs(xo-x);
			if (origin==0 || origin==4 || origin==5 ||origin ==8){
				if (!(edgesFace[origin][0] == cubieTest[0] || edgesFace[origin][0] == cubieTest[1])){
					//An edge with a X side is oriented correctly if the X side is next to an X center
					if (x==0 && y==1){
						//tests if position would be correct if 
						//if the edge is in the middle layer and would become correctly oriented 
						//by a quarter turn of the Y
						turnLeft(turnedCubePos);
						turnsY++;
					}
					else if(x==2 &&y==1){
						//tests if position would be correct if 
						//if the edge is in the middle layer and would become correctly oriented 
						//by a quarter turn of the Y
						turnRight(turnedCubePos);
						turnsY++;
					}
					else{
						//else cube is incorrectly oriented
						System.out.println("Origin: "+ (new String(edgesFace[origin])) +" TurnedPos: " + (new String(cubieTest)));  
						System.out.println("Origin " + origin + " currpos " + currPos);
						cubeEdgeParity++;
						break;
					}
				}
				else //break because the edge is correctly orientated.
					break;
					
			}
			if (origin==3 || origin==6 || origin==7 ||origin ==11){
				if (!(edgesFace[origin][1] == cubieTest[1] || edgesFace[origin][0] == cubieTest[0])){
					//An edge with a X side is oriented correctly if the X side is next to an X center
					if (x==0 && y==1){
						//tests if position would be correct if 
						//if the edge is in the middle layer and would become correctly oriented 
						//by a quarter turn of the Y
						turnLeft(turnedCubePos);
						turnsY++;
					}
					else if(x==2 &&y==1){
						//tests if position would be correct if 
						//if the edge is in the middle layer and would become correctly oriented 
						//by a quarter turn of the Y
						turnRight(turnedCubePos);
						turnsY++;
					}
					else{
						//else cube is incorrectly oriented

						System.out.println("Origin: "+ (new String(edgesFace[origin])) +" TurnedPos: " + (new String(cubieTest)));  
						System.out.println("Origin " + origin + " currpos " + currPos);
						cubeEdgeParity++;
						break;
					}
				}
				else //break because the edge is correctly orientated.
					break;
			}
			
			if (origin==1 || origin==9){
				char northTest =edgesFace[origin][2];
				if (northTest==cubieTest[0]||northTest==cubieTest[1]){
					//A YZ edge is oriented correctly either if its Z side is next to an X center.
					break;
				}
				else if (!((new String(edgesFace[origin])).equals(new String (cubieTest)))){
					//YZ is not obviously matching

					System.out.println("Origin: "+ (new String(edgesFace[origin])) +" TurnedPos: " + (new String(cubieTest)));  
					System.out.println("Origin " + origin + " currpos " + currPos);
					cubeEdgeParity++;
					break;
				}
				else //YZ is obviously matching
					break;
			}
			if (origin==2 || origin==10){
				char southTest =edgesFace[origin][4];
				System.out.println((origin + " "+(new String(edgesFace[origin])) + " " +(new String(cubieTest))));
				if (southTest==cubieTest[0]||southTest==cubieTest[1]){
					//z-y is orientated correctly because z is next to an x or the opposite z
					break;
				}
				else if (!((new String(edgesFace[origin])).equals(new String (cubieTest)))){
					System.out.println("Origin: "+ (new String(edgesFace[origin])) +" TurnedPos: " + (new String(cubieTest)));  
					System.out.println("Origin " + origin + " currpos " + currPos);
					cubeEdgeParity++;
					break;
				}
				else
					break;
			}
			
			
		}
		
		if (turnsY==1){
			System.out.println("Origin: "+ (new String(edgesFace[origin])) +" TurnedPos: " + (new String(cubieTest)));  
			System.out.println("Origin " + origin + " currpos " + currPos);
			cubeEdgeParity++;
		}
		
	}
	private static int turnRight(int currPos){
		List<Integer> right = Arrays.asList(8,9,11,10);
		int nextPos;
		char temp;
		if (right.indexOf(currPos) ==3){
			nextPos= right.get(0);
		}
		else
			nextPos = right.get(right.indexOf(currPos)+1);
		switch (nextPos){
		case 8:{
			//switch S and T
			temp= cubieTest[0];
			cubieTest[0]=cubieTest[4];
			cubieTest[4]= temp;
			break;
		}
		case 9:{
			//switch T and N
			temp = cubieTest[0];
			cubieTest[0] = cubieTest[2];
			cubieTest[2] = temp;
			break;
			}
		case 10:{
			//switch B and S
			temp = cubieTest[4];
			cubieTest[4] = cubieTest[1];
			cubieTest[1] = temp;
			break;
		}
		case 11:{
			//switch N and B
			temp = cubieTest[1];
			cubieTest[1] = cubieTest[2];
			cubieTest[2] = temp;
			break;
		}
		
		}
		return nextPos;
	}
	private static int turnLeft(int currPos){
		List<Integer> left = Arrays.asList(0,1,3,2);
		int nextPos;
		char temp;
		if (left.indexOf(currPos) ==3)
			nextPos= left.get(0);
		else
			nextPos= left.get(left.indexOf(currPos)+1);
		switch(nextPos){
		//same as right
			case 0:{
				//switch S and T
				temp= cubieTest[0];
				cubieTest[0]=cubieTest[4];
				cubieTest[4]= temp;
				break;
			}
			case 1:{
				//switch T and N
				temp = cubieTest[0];
				cubieTest[0] = cubieTest[2];
				cubieTest[2] = temp;
				break;
				}
			case 2:{
				//switch B and S
				temp = cubieTest[4];
				cubieTest[4] = cubieTest[1];
				cubieTest[1] = temp;
				break;
			}
			case 3:{
				//switch N and B
				temp = cubieTest[1];
				cubieTest[1] = cubieTest[2];
				cubieTest[2] = temp;
				break;
			}
				
		}
		
		return nextPos;
	}
	private static int turnTop(int currPos){
		List<Integer> top = Arrays.asList(0, 4, 8, 5);
		//////System.out.println(currPos);
		int nextPos;
		char temp;
		if (top.indexOf(currPos) ==3)
			nextPos= top.get(0);
		else
			nextPos = top.get(top.indexOf(currPos)+1);
		switch(nextPos){
		
			case 0:{
				//switch S and W
				temp= cubieTest[4];
				cubieTest[4]=cubieTest[3];
				cubieTest[3]= temp;
				break;
			}
			case 4:{
				//switch W and N
				temp = cubieTest[5];
				cubieTest[5] = cubieTest[2];
				cubieTest[2] = temp;
				break;
				}
			case 5:{
				//switch E and S
				temp = cubieTest[3];
				cubieTest[3] = cubieTest[4];
				cubieTest[4] = temp;
				break;
			}
			case 8:{
				//switch N and E
				temp = cubieTest[2];
				cubieTest[2] = cubieTest[3];
				cubieTest[3] = temp;
				break;
			}
		}
		return nextPos;
	}
	private static int turnBot(int currPos){
		List<Integer> bot = Arrays.asList(3, 6, 11, 7);
		int nextPos;
		char temp;
		if (bot.indexOf(currPos) ==3)
			nextPos= bot.get(0);
		else
			nextPos= bot.get(bot.indexOf(currPos)+1);
		
		switch(nextPos){
		
		case 3:{
			//switch S and W
			temp= cubieTest[4];
			cubieTest[4]=cubieTest[3];
			cubieTest[3]= temp;
			break;
		}
		case 6:{
			//switch W and N
			temp = cubieTest[5];
			cubieTest[5] = cubieTest[2];
			cubieTest[2] = temp;
			break;
			}
		case 7:{
			//switch E and S
			temp = cubieTest[3];
			cubieTest[3] = cubieTest[4];
			cubieTest[4] = temp;
			break;
		}
		case 11:{
			//switch N and E
			temp = cubieTest[2];
			cubieTest[2] = cubieTest[3];
			cubieTest[3] = temp;
			break;
		}
	}
	return nextPos;
	}
	private static int getInvCount(int arr[])
	{
	  int inv_count = 0;
	  int i, j;
	  int n = arr.length;
	  for(i = 0; i < n - 1; i++){
		  //////System.out.println(arr[i]);
		  for(j = i+1; j < n; j++)
	      if(arr[i] > arr[j])
	        inv_count++;
	  }
	  return inv_count;
	}
}
