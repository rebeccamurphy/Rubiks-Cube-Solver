package rubik;

import java.util.Arrays;

public class Cubies {
	static char[][] A =new char[][] {
			
			{'R', 'Y', 'G'},//0
			{'Y', 'G'},		//1
			{'O', 'Y','G'},	//2
			
			{'R', 'G'},		//3
			{'G'},			//4 important!
			{'O', 'G'},		//5
			
			{'R', 'W', 'G'},//6 
			{'G', 'W'},		//7 important!
			{'G', 'W','O'}	//8
	};
	static char[][] B =new char[][] {
			
			{'Y', 'R',},	//0
			{'Y'},			//1 important!
			{'Y', 'O'},		//2
			
			{'R'},			//3 important!
			null,			//4 important!
			{'O'},			//5 important!
			
			{'R', 'W'},		//6 
			{'W'},			//7 important!
			{'O','W'}		//8
	};
	static char[][] C =new char[][] {
			
			{'R', 'B', 'Y'},//0
			{'Y', 'B'},		//1 
			{'Y', 'B', 'O'},//2
			
			{'R', 'B'},		//3 
			{'B'},			//4 important!
			{'O', 'B'},			//5 
			
			{'R', 'B','W'},		//6 
			{'B','W'},			//7 important!
			{'B','W','O'}		//8
	};
	public static void makeCube(String cubeString, char[][] left, char[][] middle, char[][] right){
		System.out.println(cubeString);
		
		left[0][0]=cubeString.charAt(6);  left[0][1]=cubeString.charAt(11); left[0][2]=cubeString.charAt(11);//A0
		left[1][0]=cubeString.charAt(20); left[1][1]=cubeString.charAt(19);									 //A1
		left[2][0]=cubeString.charAt(35); left[2][1]=cubeString.charAt(29); left[2][2]=cubeString.charAt(28);//A2
				
		left[3][0]=cubeString.charAt(3);  left[3][1]=cubeString.charAt(10);		             				 //A3
		left[4][0]=cubeString.charAt(18);																	 //A4
		left[5][0]=cubeString.charAt(27); left[5][1]=cubeString.charAt(38);									 //A5
				
		left[6][0]=cubeString.charAt(0);  left[6][1]=cubeString.charAt(50); left[6][2]=cubeString.charAt(9); //A6
		left[7][0]=cubeString.charAt(17); left[7][1]=cubeString.charAt(47);									 //A7
		left[8][0]=cubeString.charAt(26); left[8][1]=cubeString.charAt(44); left[8][2]=cubeString.charAt(41);//A8
		
		
		middle[0][0]=cubeString.charAt(12); middle[0][1]=cubeString.charAt(7);							 	 //B0
		middle[1][0]=cubeString.charAt(21);																	 //B1
		middle[2][0]=cubeString.charAt(30); middle[2][1]=cubeString.charAt(36);							     //B2
				
		middle[3][0]=cubeString.charAt(4);																	 //B3
		middle[4]=null;																					     //B4
		middle[5][0]=cubeString.charAt(39);																	 //B5
				
		middle[6][0]=cubeString.charAt(1);  middle[6][1]=cubeString.charAt(51);								 //B6
		middle[7][0]=cubeString.charAt(48);																	 //B7
		middle[8][0]=cubeString.charAt(42); middle[8][1]=cubeString.charAt(45);								 //B8
		
		
		right[0][0]=cubeString.charAt(8);  right[0][1]=cubeString.charAt(14); right[0][2]=cubeString.charAt(13); //C0
		right[1][0]=cubeString.charAt(22); right[1][1]=cubeString.charAt(23);		         					 //C1
		right[2][0]=cubeString.charAt(31); right[2][1]=cubeString.charAt(32); right[2][2]=cubeString.charAt(37); //C2
				
		right[3][0]=cubeString.charAt(5);  right[3][1]=cubeString.charAt(15);									 //C3
		right[4][0]=cubeString.charAt(24);																		 //C4
		right[5][0]=cubeString.charAt(40); right[5][1]=cubeString.charAt(33);									 //C5
				
		right[6][0]=cubeString.charAt(2);  right[6][1]=cubeString.charAt(16); right[6][2]=cubeString.charAt(52); //C6
		right[7][0]=cubeString.charAt(25); right[7][1]=cubeString.charAt(49);									 //C7
		right[8][0]=cubeString.charAt(34); right[8][1]=cubeString.charAt(46); right[8][2]=cubeString.charAt(43); //C8
		
	}
	public static boolean checkValid(char[][] left, char[][] middle, char[][] right){
		if (left[4][0]!='G' || middle[1][0]!='Y' || middle[3][0]!='R' || middle[4]!=null || middle[5][0]!='O'||middle[7][0]!='W'||right[4][0]!='B')		
			return false;
		
		return true;
	}
}
