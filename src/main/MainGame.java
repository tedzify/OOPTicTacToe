package main;

import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGame extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final JLabel lblSelectedMode, lblTurnHolder, lblScore_X, lblScore_O;
	private final JPanel boardPanel;
	private final JButton btnBackButton;
	private JLabel lblPlayerToMove;
	private JPanel panelMain, containerPanel;
	private JButton[][] buttons;
	private int boardSize;
	private String currentPlayer;
	private int scoreX, scoreO;
	private boolean isBotMode;
	private Timer botMoveTimer;
	private final GamePanel gamePanel; // Reference to the GamePanel instance

	public MainGame(GamePanel gamePanel) {
		this.gamePanel = gamePanel; // Initialize the reference

		setVisible(false);
		setBackground(Color.LIGHT_GRAY);
		setPreferredSize(new Dimension(495, 600));
		setLayout(null);

		containerPanel = new JPanel();
		containerPanel.setBounds(0, 0, 495, 600);
		add(containerPanel);
		containerPanel.setLayout(null);

		panelMain = new JPanel();
		panelMain.setBounds(0, 0, 495, 600);
		containerPanel.add(panelMain);
		panelMain.setLayout(null);

		boardPanel = new JPanel();
		boardPanel.setBackground(new Color(102, 37, 73));
		boardPanel.setBounds(97, 227, 300, 300);
		panelMain.add(boardPanel);

		lblSelectedMode = new JLabel();
		lblSelectedMode.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectedMode.setHorizontalTextPosition(SwingConstants.CENTER);
		lblSelectedMode.setForeground(new Color(255, 143, 51));
		lblSelectedMode.setFont(new Font("Dialog", Font.BOLD, 15));
		lblSelectedMode.setBounds(126, 186, 245, 30);
		panelMain.add(lblSelectedMode);

		lblScore_X = new JLabel("X Score: 0");
		lblScore_X.setHorizontalTextPosition(SwingConstants.LEFT);
		lblScore_X.setHorizontalAlignment(SwingConstants.LEFT);
		lblScore_X.setForeground(new Color(255, 143, 51));
		lblScore_X.setFont(new Font("Dialog", Font.BOLD, 15));
		lblScore_X.setBounds(355, 116, 130, 30);
		panelMain.add(lblScore_X);

		lblScore_O = new JLabel("O Score: 0");
		lblScore_O.setHorizontalTextPosition(SwingConstants.LEFT);
		lblScore_O.setHorizontalAlignment(SwingConstants.LEFT);
		lblScore_O.setForeground(new Color(255, 143, 51));
		lblScore_O.setFont(new Font("Dialog", Font.BOLD, 15));
		lblScore_O.setBounds(355, 145, 130, 30);
		panelMain.add(lblScore_O);

		lblTurnHolder = new JLabel("X");
		lblTurnHolder.setHorizontalTextPosition(SwingConstants.CENTER);
		lblTurnHolder.setHorizontalAlignment(SwingConstants.CENTER);
		lblTurnHolder.setForeground(new Color(255, 143, 51));
		lblTurnHolder.setFont(new Font("Dialog", Font.BOLD, 50));
		lblTurnHolder.setBounds(26, 93, 118, 65);
		panelMain.add(lblTurnHolder);

		lblPlayerToMove = new JLabel("To move");
		lblPlayerToMove.setHorizontalTextPosition(SwingConstants.CENTER);
		lblPlayerToMove.setHorizontalAlignment(SwingConstants.CENTER);
		lblPlayerToMove.setForeground(new Color(255, 143, 51));
		lblPlayerToMove.setFont(new Font("Dialog", Font.BOLD, 15));
		lblPlayerToMove.setBounds(50, 145, 71, 30);
		panelMain.add(lblPlayerToMove);

		btnBackButton = new JButton("Back");
		btnBackButton.setBorder(new LineBorder(new Color(255, 143, 51), 2));
		btnBackButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnBackButton.setForeground(new Color(255, 143, 51));
		btnBackButton.setBackground(new Color(69, 25, 82));
		btnBackButton.setBounds(10, 21, 89, 23);
		btnBackButton.setFocusPainted(false);
		btnBackButton.addActionListener(this);
		panelMain.add(btnBackButton);

		JLabel background = new JLabel();
		background.setBounds(0, 0, 495, 600);
		background.setIcon(new ImageIcon(new ImageIcon(MainGame.class.getResource("/tictac2/bg/GamePanelbg.png"))
				.getImage().getScaledInstance(495, 600, Image.SCALE_DEFAULT)));
		background.setPreferredSize(new Dimension(495, 600));
		background.setMinimumSize(new Dimension(495, 600));
		background.setMaximumSize(new Dimension(500, 700));
		background.setAlignmentX(Component.CENTER_ALIGNMENT);
		panelMain.add(background);

		currentPlayer = "X";
		scoreX = 0;
		scoreO = 0;
		isBotMode = false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnBackButton) {
			setVisible(false);
			gamePanel.showMainGamePanel();
			lblScore_X.setText("X Score: " + 0 * scoreX);
			lblScore_O.setText("O Score: " + 0 * scoreX);
		} else {
			JButton buttonClicked = (JButton) e.getSource();
			if (buttonClicked.getText().equals("")) {
				buttonClicked.setText(currentPlayer);
				buttonClicked
						.setFont(new Font("Tahoma", Font.BOLD, (int) (buttonClicked.getPreferredSize().height * 0.6)));
				if (checkWin()) {
					if (currentPlayer.equals("X")) {
						scoreX++;
						lblScore_X.setText("X Score: " + scoreX);
					} else {
						scoreO++;
						lblScore_O.setText("O Score: " + scoreO);
					}
					JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!");
					resetBoard();
				} else if (isBoardFull()) {
					JOptionPane.showMessageDialog(this, "It's a draw!");
					resetBoard();
				} else {
					currentPlayer = currentPlayer.equals("X") ? "O" : "X";
					lblTurnHolder.setText(currentPlayer);
					if (isBotMode && currentPlayer.equals("O")) {
						botMove();
					}
				}
			}
		}
	}

	public void updateGameBoard(String selectedMode, int boardSize) {
		this.boardSize = boardSize;
		buttons = new JButton[boardSize][boardSize];

		lblSelectedMode.setText(selectedMode);
		isBotMode = selectedMode.equals("Human vs Bot");

		boardPanel.removeAll();
		boardPanel.setLayout(new GridLayout(boardSize, boardSize, 5, 5));

		int buttonSize = Math.min(boardPanel.getWidth() / boardSize, boardPanel.getHeight() / boardSize);

		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				JButton button = new JButton();
				button.setBorder(new LineBorder(new Color(255, 143, 51), 2));
				button.setFont(new Font("Tahoma", Font.BOLD, (int) (buttonSize * 0.6)));
				button.setForeground(new Color(255, 143, 51));
				button.setBackground(new Color(69, 25, 82));
				button.setPreferredSize(new Dimension(buttonSize, buttonSize));
				button.setFocusPainted(false);
				button.addActionListener(this);
				buttons[row][col] = button;
				boardPanel.add(button);
			}
		}

		boardPanel.revalidate();
		boardPanel.repaint();
	}

	private void botMove() {
		// Delay the bot move by 1 second (1000 milliseconds)
		botMoveTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Try to win if possible
				if (tryToWin()) {
					if (checkWin()) {
						scoreO++;
						lblScore_O.setText("O Score: " + scoreO);
						JOptionPane.showMessageDialog(MainGame.this, "Player " + currentPlayer + " wins!");
						resetBoard();
					} else {
						currentPlayer = "X"; // Ensure the currentPlayer is set to "X" after the bot's move
						lblTurnHolder.setText(currentPlayer);
					}
					botMoveTimer.stop();
					return;
				}

				// Check if the player is close to winning and block their move
				if (blockPlayerWin()) {
					currentPlayer = "X"; // Ensure the currentPlayer is set to "X" after the bot's move
					lblTurnHolder.setText(currentPlayer);
					botMoveTimer.stop();
					return;
				}

				// If no winning or blocking move is needed, make a random move
				Random rand = new Random();
				int row, col;
				do {
					row = rand.nextInt(boardSize);
					col = rand.nextInt(boardSize);
				} while (!buttons[row][col].getText().equals(""));

				buttons[row][col].setText(currentPlayer);
				buttons[row][col].setFont(
						new Font("Tahoma", Font.BOLD, (int) (buttons[row][col].getPreferredSize().height * 0.6)));

				if (checkWin()) {
					scoreO++;
					lblScore_O.setText("O Score: " + scoreO);
					JOptionPane.showMessageDialog(MainGame.this, "Player " + currentPlayer + " wins!");
					resetBoard();
				} else {
					currentPlayer = "X"; // Ensure the currentPlayer is set to "X" after the bot's move
					lblTurnHolder.setText(currentPlayer);
				}
				botMoveTimer.stop();
			}
		});
		botMoveTimer.setRepeats(false); // Ensure the timer only runs once
		botMoveTimer.start();
	}

	private boolean tryToWin() {
		// Check rows
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize - 2; col++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row][col + 1].getText().equals(currentPlayer)
						&& buttons[row][col + 2].getText().equals("")) {
					buttons[row][col + 2].setText(currentPlayer);
					buttons[row][col + 2].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row][col + 2].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		// Check columns
		for (int col = 0; col < boardSize; col++) {
			for (int row = 0; row < boardSize - 2; row++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row + 1][col].getText().equals(currentPlayer)
						&& buttons[row + 2][col].getText().equals("")) {
					buttons[row + 2][col].setText(currentPlayer);
					buttons[row + 2][col].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row + 2][col].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		// Check diagonals (top-left to bottom-right)
		for (int row = 0; row < boardSize - 2; row++) {
			for (int col = 0; col < boardSize - 2; col++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row + 1][col + 1].getText().equals(currentPlayer)
						&& buttons[row + 2][col + 2].getText().equals("")) {
					buttons[row + 2][col + 2].setText(currentPlayer);
					buttons[row + 2][col + 2].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row + 2][col + 2].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		// Check diagonals (top-right to bottom-left)
		for (int row = 0; row < boardSize - 2; row++) {
			for (int col = 2; col < boardSize; col++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row + 1][col - 1].getText().equals(currentPlayer)
						&& buttons[row + 2][col - 2].getText().equals("")) {
					buttons[row + 2][col - 2].setText(currentPlayer);
					buttons[row + 2][col - 2].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row + 2][col - 2].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		return false;
	}

	private boolean blockPlayerWin() {
		String opponent = "X";
		// Check rows
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize - 2; col++) {
				if (buttons[row][col].getText().equals(opponent) && buttons[row][col + 1].getText().equals(opponent)
						&& buttons[row][col + 2].getText().equals("")) {
					buttons[row][col + 2].setText(currentPlayer);
					buttons[row][col + 2].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row][col + 2].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		// Check columns
		for (int col = 0; col < boardSize; col++) {
			for (int row = 0; row < boardSize - 2; row++) {
				if (buttons[row][col].getText().equals(opponent) && buttons[row + 1][col].getText().equals(opponent)
						&& buttons[row + 2][col].getText().equals("")) {
					buttons[row + 2][col].setText(currentPlayer);
					buttons[row + 2][col].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row + 2][col].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		// Check diagonals (top-left to bottom-right)
		for (int row = 0; row < boardSize - 2; row++) {
			for (int col = 0; col < boardSize - 2; col++) {
				if (buttons[row][col].getText().equals(opponent) && buttons[row + 1][col + 1].getText().equals(opponent)
						&& buttons[row + 2][col + 2].getText().equals("")) {
					buttons[row + 2][col + 2].setText(currentPlayer);
					buttons[row + 2][col + 2].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row + 2][col + 2].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		// Check diagonals (top-right to bottom-left)
		for (int row = 0; row < boardSize - 2; row++) {
			for (int col = 2; col < boardSize; col++) {
				if (buttons[row][col].getText().equals(opponent) && buttons[row + 1][col - 1].getText().equals(opponent)
						&& buttons[row + 2][col - 2].getText().equals("")) {
					buttons[row + 2][col - 2].setText(currentPlayer);
					buttons[row + 2][col - 2].setFont(new Font("Tahoma", Font.BOLD,
							(int) (buttons[row + 2][col - 2].getPreferredSize().height * 0.6)));
					return true;
				}
			}
		}

		return false;
	}

	private boolean checkWin() {
		// Check rows
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize - 2; col++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row][col + 1].getText().equals(currentPlayer)
						&& buttons[row][col + 2].getText().equals(currentPlayer)) {
					return true;
				}
			}
		}

		// Check columns
		for (int col = 0; col < boardSize; col++) {
			for (int row = 0; row < boardSize - 2; row++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row + 1][col].getText().equals(currentPlayer)
						&& buttons[row + 2][col].getText().equals(currentPlayer)) {
					return true;
				}
			}
		}

		// Check diagonals (top-left to bottom-right)
		for (int row = 0; row < boardSize - 2; row++) {
			for (int col = 0; col < boardSize - 2; col++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row + 1][col + 1].getText().equals(currentPlayer)
						&& buttons[row + 2][col + 2].getText().equals(currentPlayer)) {
					return true;
				}
			}
		}

		// Check diagonals (top-right to bottom-left)
		for (int row = 0; row < boardSize - 2; row++) {
			for (int col = 2; col < boardSize; col++) {
				if (buttons[row][col].getText().equals(currentPlayer)
						&& buttons[row + 1][col - 1].getText().equals(currentPlayer)
						&& buttons[row + 2][col - 2].getText().equals(currentPlayer)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean isBoardFull() {
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				if (buttons[row][col].getText().equals("")) {
					return false;
				}
			}
		}
		return true;
	}

	private void resetBoard() {
		for (int row = 0; row < boardSize; row++) {
			for (int col = 0; col < boardSize; col++) {
				buttons[row][col].setText("");
			}
		}
		currentPlayer = "X";
		lblTurnHolder.setText(currentPlayer);
	}
}