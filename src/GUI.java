
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class GUI implements ActionListener{

	JFrame frame;
	JRadioButton easy, med, hard;
	JTextField[][][][] textFields;
	JPanel board;
	Sudoku game;
	int gameState;
	int[][] row;
	int[][] col;
	int[][][] box;
	List<Integer> removedSpots;
	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}
	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		
		
		//print board
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
//		new GUI(m, uniqueCheck);
		
	}
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 797, 497);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel[][] panels = new JPanel[3][3];
		textFields = new JTextField[3][3][3][3];
		board = new JPanel(new GridLayout(3, 3, 3, 3));
		board.setBounds(10, 10, 300, 243);
		
		for(int i=0; i<9; i++) {
			int y = i/3;
			int x = i - y*3;
			
			panels[y][x] = new JPanel();
			board.add(panels[y][x]);
			panels[y][x].setLayout(new GridLayout(3, 3, 0, 0));
			
			for(int j=0; j<9; j++) {
				
				int suby = j/3;
				int subx = j - suby*3;
				textFields[y][x][suby][subx] = new JTextField();
				panels[y][x].add(textFields[y][x][suby][subx]);
			}
		}		
		
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				
				textFields[i/3][j/3][i%3][j%3].setEditable(false);;
			}
		}
		
		
		frame.getContentPane().add(board);
		
		easy = new JRadioButton("Easy");
		easy.setBounds(316, 20, 103, 21);
		frame.getContentPane().add(easy);
		
		med = new JRadioButton("Medium");
		med.setBounds(316, 43, 103, 21);
		frame.getContentPane().add(med);
		
		hard = new JRadioButton("Hard");
		hard.setBounds(316, 66, 103, 21);
		frame.getContentPane().add(hard);
		
		ButtonGroup bg = new ButtonGroup(); 
		
		easy.addActionListener(this);
		med.addActionListener(this);
		hard.addActionListener(this);
		
		JButton solve = new JButton("Solve");
		solve.setBounds(320, 116, 115, 21);
		solve.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(gameState == 1) {
					removedSpots = Sudoku.removedSpots;
					row = Sudoku.row;
					col = Sudoku.col;
					box = Sudoku.box;
					gameState = 0;
					solve(removedSpots, 0);
				}
				
			}
			
		});
		frame.getContentPane().add(solve);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
        
    	
        if(easy.isSelected()) {
        	 game = new Sudoku(32);
        	 fillBoard(game.getM());
        	 gameState = 1;
        }
        else if(med.isSelected()) {
        	game = new Sudoku(30);
       		fillBoard(game.getM());
       		gameState = 1;
        }
        else {
        	game = new Sudoku(28);
       	 	fillBoard(game.getM());
       	 	gameState = 1;
        }
		
	}
	private void fillBoard(int[][] m)
	{
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				JTextField temp = textFields[i/3][j/3][i%3][j%3];
				temp.setEditable(true);
				if(m[i][j] != 0) {
					temp.setText(String.valueOf(m[i][j]));
					temp.setHorizontalAlignment(JTextField.CENTER);
					temp.setEditable(false);
				}
				else {
					((AbstractDocument)temp.getDocument()).setDocumentFilter(new DocumentFilter(){
				        Pattern regEx = Pattern.compile("\\d*");
				       
				        @Override
				        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {          
				            Matcher matcher = regEx.matcher(text);
				            if(!matcher.matches()){
				                return;
				            }
				            int currentLength = fb.getDocument().getLength();
				            int overLimit = (currentLength + text.length()) - 1 - length;
				            if (overLimit > 0) {
				                text = text.substring(0, text.length() - overLimit);
				            }
				            super.replace(fb, offset, length, text, attrs);
				        }
				        
				    });
				}
			}
		}
	}
	private boolean solve(List<Integer> removedSpots, int index) {
		if(index == removedSpots.size()) return true;
		int position = removedSpots.get(index);
		int y = position/9;
		int x = position - y*9; 
		for(int value=1; value<10; value++) {
			if( row[y][value] == 0 && col[x][value] == 0 && box[y/3][x/3][value] == 0) {
				
				row[y][value] = 1;
				col[x][value] = 1;
				box[y/3][x/3][value] = 1;
				textFields[y/3][x/3][y%3][x%3].setText(String.valueOf(value));
				if(solve(removedSpots, index+1)) {
					textFields[y/3][x/3][y%3][x%3].setEditable(false);
					textFields[y/3][x/3][y%3][x%3].setHorizontalAlignment(JTextField.CENTER);
					return true;
				}
				row[y][value] = 0;
				col[x][value] = 0;
				box[y/3][x/3][value] = 0;
				textFields[y/3][x/3][y%3][x%3].setText("");
			}
		}	
		return false;
	}
	
}
