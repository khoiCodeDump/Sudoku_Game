import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Sudoku {
	static int EASY = 32;
	int MEDIUM = 30;
	int HARD = 28;
	
	static int[][] m = new int[9][9];
	static int[][] row = new int[9][10];
	static int[][] col = new int[9][10];
	static int[][][] box = new int[3][3][10];
	static HashMap<Integer, Integer> solvedM = new HashMap<>();
	static List<int[][]> uniqueCheck = new ArrayList<>();
	static int[][] matrix = new int[9][9];
	
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		List<Integer> removedSpots = new ArrayList<>();
		
		while( init(EASY, removedSpots) == false);
		//print board
		
		new GUI(m, uniqueCheck);
		
	}
	private static void mergeSolvedMToM() {
		for(int i=0; i<m.length; i++) {
			for(int j=0; j<m[0].length; j++) {
				m[i][j] = matrix[i][j] + m[i][j];
				matrix[i][j] = 0;
			}
		}
	}
	private static boolean init(int difficulty) throws FileNotFoundException {
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
		PrintStream o = new PrintStream(new File("array.txt"));

	    PrintStream console = System.out;

	    System.setOut(o);	        
		System.out.println(Arrays.toString(array));
//		System.setOut(console);
		int counter = 0;
		while(counter < 81 - 66) {
			if(findUniqueSol(array, difficulty, counter)) return true;
			counter++;
		}
		
		return false;
	}
	private static List<Integer> fillDiagonal() {
		
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
	private static boolean findUniqueSol(int[] arr, int difficulty, int index) {
		System.out.println("Index " + index);

		if(index >= 81 && solvedM.size() < difficulty) return false;
		
		else if(solvedM.size() == difficulty) return true;

		solvedM.put(arr[index], 0);
		
		int y = arr[index]/9;
		int x = arr[index] - y*9;
		int temp = m[y][x];
		m[y][x] = 0;
		
		row[y][temp] = 0;
		col[x][temp] = 0;
		box[y/3][x/3][temp] = 0;
		
//		System.out.println(list.toString());
		
		new GUI(m, uniqueCheck);
		solve(0, difficulty);
		

		if(uniqueCheck.size() > 1) {
			list.remove(list.size()-1);
			uniqueCheck = new ArrayList<>();
			m[y][x] = temp;
			row[y][temp] = 1;
			col[x][temp] = 1;
			box[y/3][x/3][temp] = 1;
			return false;
		}
		
		new GUI(m, uniqueCheck);
		System.out.println(list.toString());

		int counter = 1;
		
		while(index + counter < 81) {
			if(findUniqueSol(arr, difficulty, list, index + counter) )return true;
			System.out.println(list.toString());
			counter++;
		} 

		list.remove(list.size()-1);
		System.out.println(list.toString());

		return false;
	}

	private static void solve(int i, int difficulty) {
		if(i == difficulty && solvedM.size() == difficulty) {
			List<Integer> keyList = new ArrayList(solvedM.keySet());
		    List<Integer> valueList = new ArrayList(solvedM.values());
		    int[][] map = new int[32][2];
		    for(int index = 0; i<32; i++) {
		    	map[index][0] = keyList.get(index);
		    	map[index][1] = valueList.get(index);
		    } 
		    
		    uniqueCheck.add(map);
			return;
		}
		
		
		int position = list.get(i);
		int y = position/9;
		int x = position - y*9; 
		for(int value=1; value<10; value++) {
			if( row[y][value] == 0 && col[x][value] == 0 && box[y/3][x/3][value] == 0) {
				
				solvedM.put(position, value);
										
				solve(list, i+1, difficulty);
				if(uniqueCheck.size() > 1) {
					break;
				}
				
			}
		}	
		
		solvedM.remove(position);
		
	}
	private static boolean fillRemaining(List<Integer> list,  int i) {
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

