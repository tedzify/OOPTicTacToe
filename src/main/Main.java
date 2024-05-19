package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Main extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	Font text = new Font("Press Start 2P", Font.PLAIN, 15);
	Color FG1 = new Color(243, 159, 90);
	Color FG2 = new Color(232, 188, 185);
	Color BG1 = new Color(102, 37, 73);
	Color BG2 = new Color(69, 25, 82);

	private JButton btnNewGame;
	private JButton btnExit;
	private JPanel mainPanel;
	private GamePanel gamePanel;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main newFrame = new Main();
					SwingUtilities.updateComponentTreeUI(newFrame);
					newFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Main() {
		setTitle("Tic Tac Toe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(495, 600);
		setVisible(true);

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		mainPanel = new JPanel();
		mainPanel.setBounds(0, 0, 479, 561);
		contentPane.add(mainPanel);
		mainPanel.setLayout(null);

		btnNewGame = new JButton("NEW GAME");
		btnNewGame.setFocusPainted(false);
		btnNewGame.setBounds(114, 300, 250, 75);
		btnNewGame.setFont(text);
		btnNewGame.setForeground(FG1);
		btnNewGame.setBackground(BG1);
		btnNewGame.setBorder(new LineBorder(BG1));
		mainPanel.add(btnNewGame);
		btnNewGame.addActionListener(this);

		btnExit = new JButton("EXIT");
		btnExit.setFocusPainted(false);
		btnExit.setBounds(114, 400, 250, 75);
		btnExit.setFont(text);
		btnExit.setForeground(FG2);
		btnExit.setBackground(BG2);
		btnExit.setBorder(new LineBorder(BG2));
		mainPanel.add(btnExit);
		btnExit.addActionListener(this);

		gamePanel = new GamePanel();
		gamePanel.setBounds(0, 0, 495, 600);
		mainPanel.add(gamePanel);

		// Load background image for GamePanel
		JLabel background;

		background = new JLabel(new ImageIcon(new ImageIcon(MainGame.class.getResource("/tictac2/bg/GamePanelbg.png"))
				.getImage().getScaledInstance(495, 600, Image.SCALE_DEFAULT)));
		background.setLocation(0, 0);
		background.setSize(479, 561);
		mainPanel.add(background);

		// Initialize and add game panel
		gamePanel = new GamePanel();
		gamePanel.setBounds(0, 0, 495, 600); // Adjust bounds as necessary
		gamePanel.setVisible(false); // Initially hide
		contentPane.add(gamePanel); // Add game panel to content pane

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnNewGame) {
			// Hide the current panel and show the game panel
			mainPanel.setVisible(false);
			gamePanel.setVisible(true);
		}

		if (e.getSource() == btnExit) {

			exitGame();
		}
	}

	private void exitGame() {
		System.exit(0);
	}

}
