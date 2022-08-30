import java.util.*;

public class GUI {
	GUI(int[][] m, int[][] solvedM, List<Integer> removedSpots){
		
		for(int c=0; c<9; c++) {
			System.out.print(Arrays.toString(m[c]));
			System.out.print(" | " + Arrays.toString(solvedM[c]));
			System.out.println();
		}
		
		System.out.println();
		
	}
}
