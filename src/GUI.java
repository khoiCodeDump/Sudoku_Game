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
	int[][] row;
	int[][] col;
	int[][][] box;
	List<Integer> removedSpots;
	JButton check, solve, replay;
	JLabel status;
	/**
	 * Author Khoi Vu
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
		frame.setSize(700, 500);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel[][] panels = new JPanel[3][3];
		textFields = new JTextField[3][3][3][3];
		board = new JPanel(new GridLayout(3, 3, 3, 3));
		board.setBounds(10, 10,  439, 439);
		
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
		easy.setBounds(455, 17, 109, 23);
		frame.getContentPane().add(easy);
		
		med = new JRadioButton("Medium");
		med.setBounds(455, 43, 109, 23);
		frame.getContentPane().add(med);
		
		hard = new JRadioButton("Hard");
		hard.setBounds(455, 69, 109, 23);
		frame.getContentPane().add(hard);
		
		ButtonGroup bg = new ButtonGroup(); 
		
		easy.addActionListener(this);
		med.addActionListener(this);
		hard.addActionListener(this);
		bg.add(easy);
		bg.add(med);
		bg.add(hard);
		
		solve = new JButton("Solve");
		solve.setBounds(465, 99, 89, 23);
		
		check = new JButton("Check");
		check.setBounds(465, 130, 89, 23);
		
		replay = new JButton("Replay difficulty");
		replay.setBounds(465, 350, 89, 23);
		solve.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
					removedSpots = game.removedSpots;
					row = game.row;
					col = game.col;
					box = game.box;
					check.setVisible(false);
					solve.setVisible(false);
					solve(removedSpots, 0);
					status.setText("Game solved!");
					replay.setVisible(true);
				
				
			}
			
		});
		
		check.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				removedSpots = game.removedSpots;
				row = game.row;
				col = game.col;
				box = game.box;
				for(int i=0; i<removedSpots.size(); i++) {
					int position = removedSpots.get(i);
					int y = position/9;
					int x = position - y*9; 
					String text = textFields[y/3][x/3][y%3][x%3].getText();
					if(text.equals("") || text == null) {
						status.setText("Please fill in all spots");
						return;
					}
					int value = Integer.valueOf(text);
					if( row[y][value] == 1 || col[x][value] == 1 || box[y/3][x/3][value] == 1) {
						return;
					}
					
				}
				
				status.setText("You win!");
				solve.setVisible(false);
				check.setVisible(false);
				replay.setVisible(true);
			}
			
		});
		
		status = new JLabel("");
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setBounds(465, 160, 200, 23);
		status.setFont(new Font("Tahoma", Font.PLAIN, 16));
		frame.getContentPane().add(status);
		
		Data data = new Data(status);
		Thread loading = new Thread(new LoadingLevel(data));
		loading.start();
		
		replay.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				replay.setVisible(false);
				
				
				if(easy.isSelected()) {
		        	game = new Sudoku(32);
		        	data.send();
		        	fillBoard(game.getM());
					status.setText("Game start");

		        }
		        else if(med.isSelected()) {
		        	game = new Sudoku(34);
		        	data.send();
		       		fillBoard(game.getM());
					status.setText("Game start");

		        }
		        else {
		        	game = new Sudoku(36);
		        	data.send();
		       	 	fillBoard(game.getM());
					status.setText("Game start");

		        }
			}
			
		});
		
		frame.getContentPane().add(solve);
		frame.getContentPane().add(check);
		frame.getContentPane().add(replay);
		replay.setVisible(false);

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		check.setVisible(true);
		solve.setVisible(true);
		replay.setVisible(false);

		
		if(easy.isSelected()) {
			game = new Sudoku(32);
			
        	fillBoard(game.getM());
			status.setText("Game start");

        }
        else if(med.isSelected()) {
        	game = new Sudoku(34);
       		fillBoard(game.getM());
			status.setText("Game start");

        }
        else {
        	game = new Sudoku(36);
       	 	fillBoard(game.getM());
			status.setText("Game start");

        }
		
	}
	private void fillBoard(int[][] m)
	{
		for(int i=0; i<9; i++) {
			for(int j=0; j<9; j++) {
				JTextField temp = textFields[i/3][j/3][i%3][j%3];
				temp.setEditable(true);
				temp.setText("");
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
