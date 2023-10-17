//Author Khoi Vu
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Sudoku{
	
	
	int[][] m = new int[9][9];
	int[][] row = new int[9][10];
	int[][] col = new int[9][10];
	int[][][] box = new int[3][3][10];
	List<Integer> removedSpots = new ArrayList<>();
	List<Integer> solutionList = new ArrayList<>();
	int uniqueCheck = 0;
	int[][] matrix = new int[9][9];
	int[] ans;
	int[][] solvedM;
	Sudoku(int difficulty) {
		while(init(difficulty) == false);
		
	}
	public int[][] getM(){
		return m;
	}

	private void mergeSolvedMToM() {
		for(int i=0; i<m.length; i++) {
			for(int j=0; j<m[0].length; j++) {
				m[i][j] = matrix[i][j] + m[i][j];
				matrix[i][j] = 0;
			}
		}
	}
	private  boolean init(int difficulty){
		ans = new int[difficulty];
		solvedM = new int[9][9];
		int[] array = new int[81];
		for(int i=0; i<81; i++) {
			array[i] = i;
		}
		Random rand = new Random();
		
		for(int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			int temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		List<Integer> toExplore = fillDiagonal();	
		fillRemaining(toExplore, 0);
		
		mergeSolvedMToM();
//		PrintStream o = new PrintStream(new File("all_combinations.txt"));

//	    PrintStream console = System.out;

//	    System.setOut(o);	        
		System.out.println(Arrays.toString(array));
//		System.setOut(console);
		int counter = 0;
		while(counter < 81 - difficulty) {
			if(findUniqueSol(array, difficulty, counter)) return true;
			counter++;
		}
		
		return false;
	}
	private  List<Integer> fillDiagonal() {
		
		int[] array = new int[81];
		for(int i=0; i<81; i++) {
			array[i] = i;
		}
		
		Random random = new Random();
		
		for(int i=0; i<9;i = i+3) {
			for(int y = 0; y < 3; y++) {
				for(int x = 0; x < 3; x++) {
					
					int value = random.nextInt(9) + 1;
					
					while(box[i/3][i/3][value] == 1)
					{	
						value = random.nextInt(9) + 1;
						
					}
					array[(i+y)*9 + i+x] = 0;
					row[i+y][value] = 1;
					col[i+x][value] = 1;
					box[i/3][i/3][value] = 1;  
					m[i+y][i+x] = value;
				
				}
			}
		}
		
				
		for(int i = 0; i < array.length; i++) {
			int randomIndexToSwap = random.nextInt(array.length);
			int temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		
		
		List<Integer> list = new ArrayList<>();
		
		for(int index = 0; index < 81; index++) {
			if(array[index] > 0) list.add(array[index]);
		}
		
		
		return list;
	}
	private  boolean findUniqueSol(int[] arr, int difficulty, int index) {
		if(index >= 81 && removedSpots.size() < difficulty) return false;

		removedSpots.add(arr[index]);
		int y = arr[index]/9;
		int x = arr[index] - y*9;
		int temp = m[y][x];
		m[y][x] = 0;
		
		row[y][temp] = 0;
		col[x][temp] = 0;
		box[y/3][x/3][temp] = 0;

		if(removedSpots.size() == difficulty) {
			if(solve(0)) return true;
			
			m[y][x] = temp;
			row[y][temp] = 1;
			col[x][temp] = 1;
			box[y/3][x/3][temp] = 1;
			removedSpots.remove(removedSpots.size()-1);

			if(uniqueCheck > 1) {
				uniqueCheck = 0;
				

			}
			return false;
 
		}

		int counter = 1;
		
		while(index + counter < 81) {
			if(findUniqueSol(arr, difficulty, index + counter) )return true;
			
			counter++;
		} 
		m[y][x] = temp;
		row[y][temp] = 1;
		col[x][temp] = 1;
		box[y/3][x/3][temp] = 1;
		removedSpots.remove(removedSpots.size()-1);

		return false;
	}

	private  boolean solve(int i) {
		if(removedSpots.size() == solutionList.size()) {
			System.out.println(removedSpots);
			for(int c=0; c<9; c++) {
				System.out.print(Arrays.toString(m[c]));
				System.out.print(" | " + Arrays.toString(solvedM[c]));
				System.out.println();
			}
			
			System.out.println();
		    uniqueCheck++;
		    if(uniqueCheck > 1) return false;
		    
			return true;
		}
		
		
		int position = removedSpots.get(i);
		int y = position/9;
		int x = position - y*9; 
		for(int value=1; value<10; value++) {
			if( row[y][value] == 0 && col[x][value] == 0 && box[y/3][x/3][value] == 0) {
				
				row[y][value] = 1;
				col[x][value] = 1;
				box[y/3][x/3][value] = 1;
				solvedM[y][x] = value;
				solutionList.add(value);
										
				solve(i+1);
				
				row[y][value] = 0;
				col[x][value] = 0;
				box[y/3][x/3][value] = 0;
				solvedM[y][x] = 0;
				solutionList.remove(solutionList.size()-1);
					
				if( uniqueCheck > 1) return false;
				
			}
		}	
		if(uniqueCheck == 1) return true;
		
		return false;
	}
	private  boolean fillRemaining(List<Integer> list,  int i) {
		if(i >= list.size()) {
			return true;
		}
		
		int val = list.get(i);
		int y = val/9;
		int x = val - y*9; 
		for(int value=1; value<10; value++) {
			if( row[y][value] == 0 && col[x][value] == 0 && box[y/3][x/3][value] == 0) {
				
				row[y][value] = 1;
				col[x][value] = 1;
				box[y/3][x/3][value] = 1;
				matrix[y][x] = value ;
				
				if(fillRemaining(list, i+1) == true) return true;
				
				matrix[y][x] = 0;
				row[y][value] = 0;
				col[x][value] = 0;
				box[y/3][x/3][value] = 0;				
			}
		}	
		
		if(matrix[y][x] == 0) return false;
	
		return true; 
	}
	
	
}