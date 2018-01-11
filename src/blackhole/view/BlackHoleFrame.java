package blackhole.view;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

import blackhole.model.BlackHoleEngine;
import blackhole.model.util.Field;

public class BlackHoleFrame extends JFrame {
	private static final long serialVersionUID = 1598300301377037323L;
	private JButton[][] buttons;
	private BlackHoleEngine engine;
	private JFrame parent;

	public BlackHoleFrame(BlackHoleEngine engine) {
		super("BlackHole");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(true);
		this.engine = engine;
	}
	
	public void setParent(JFrame parent) {
		this.parent = parent;
	}

	// Does initial setup and shows this frame
	public void showFrame() {
		setLayout(new GridLayout(engine.getCurrentFieldSize(), engine.getCurrentFieldSize()));
		buttons = new JButton[engine.getCurrentFieldSize()][engine.getCurrentFieldSize()];
		addComponents();
		pack();
		setVisible(true);
		JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);
        
        JMenu gameMenu = new JMenu("Game");
        menuBar.add(gameMenu);
        JMenuItem backMenuItem = new JMenuItem("Back");
        backMenuItem.addActionListener((e) -> back());
        gameMenu.add(backMenuItem);
        gameMenu.addSeparator();
        JMenuItem exitMenuItem = new JMenuItem("Exit");
        gameMenu.add(exitMenuItem);
        exitMenuItem.addActionListener(e -> System.exit(0));
	}

	private void back() {
		parent.setVisible(true);
		this.dispose();
	}

	// Adds the buttons to the grid and stores them
	private void addComponents() {
		for(int row = 0; row < engine.getCurrentFieldSize(); row++) {
			for (int column = 0; column < engine.getCurrentFieldSize(); column++) {
				buttons[row][column] = mustHaveThis(row, column);
			}
		}
		//updateBoard();
	}

	// Updates the board
	private void updateBoard() {
		for(int i = 0; i < buttons.length; ++i) {
			for(int j = 0; j < buttons[i].length; ++j) {
				buttons[i][j].setEnabled(engine.isEnabled(i, j));
				buttons[i][j].setBackground(getBackground(i, j));
				buttons[i][j].setForeground(getForeground(i, j));
				buttons[i][j].setText(getText(i, j));
			}
		}
	}

	// Helper function to bypass an error in the updateBoard() function
	private JButton mustHaveThis(int row, int column) {
		return addField(row, column, (e) -> pushButton(row, column));
	}
	
	// Sets up a field then adds it to the grid
	private JButton addField(int row, int col, ActionListener listener) {
		JButton button = new JButton(getText(row, col));
		button.addActionListener(listener);
		button.setBackground(getBackground(row, col));
		button.setForeground(getForeground(row, col));
		button.setFont(button.getFont().deriveFont(Font.BOLD, 10f));
		button.setPreferredSize(new Dimension(50, 50));
		button.setEnabled(engine.isEnabled(row, col));
		getContentPane().add(button);
		return button;
	}
	
	private Color getBackground(int row, int col) {
		if(engine.isBlackhole(row, col)) {
			return Color.BLACK;
		} else {
			return engine.isWhite(row, col) ? Color.WHITE : Color.GRAY;
		}
	}
	
	// Does shit when you fuck around
	private void pushButton(int row, int col) {
		engine.pushButton(row, col);
		updateBoard();
		if(engine.selected() != null) {
			lightUpSelectedButton(engine.selected().getX(), engine.selected().getY());
		}
		gameOver();
	}
	
	// Lights up the piece which are selected to move
	private void lightUpSelectedButton(int row, int col) {
		buttons[row][col].setBackground(Color.ORANGE);
	}
	
	private Color getForeground(int row, int col) {
		return engine.getFieldType(row, col) == Field.RED ? Color.RED : Color.BLACK;
	}

	private String getText(int row, int col) {
		return (engine.getFieldType(row, col) == Field.RED || engine.getFieldType(row, col) == Field.BLACK) ? "O" : "";
	}
	
	// Checks if the game is over and evaluates who is the winner, then starts a new game
	private void gameOver() {
		if(engine.gameOver() != null) {
			JOptionPane.showMessageDialog(null, "The winner is: " + engine.gameOver());
			BlackHoleFrame bhf = new BlackHoleFrame(engine);
			engine.run(engine.getCurrentFieldSize());
			bhf.setParent(parent);
			bhf.showFrame();
			this.dispose();
		}
	}
}
