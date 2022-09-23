import java.util.*;

public class GUI {
	GUI(int[][] m, List<int[][]> list){
		
		int[][] solvedM = new int[9][9];
		int[][] arr = list.get(0);
		for(int i = 0; i<arr.length; i++) {
			int row = arr[i][0]/9;
			int col = arr[i][0] - row*9;
			solvedM[row][col] = arr[i][1];
		}
		
		
		for(int c=0; c<9; c++) {
			System.out.print(Arrays.toString(m[c]));
			System.out.print(" | " + Arrays.toString(solvedM[c]));
			System.out.println();
		}
		
		System.out.println();
		
	}
}
