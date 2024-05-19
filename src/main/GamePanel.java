package main;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	Font text = new Font("Press Start 2P", Font.PLAIN, 10);
	Font text1 = new Font("Press Start 2P", Font.PLAIN, 15);
	Font text2 = new Font("Press Start 2P", Font.PLAIN, 30);
	Color FG1 = new Color(243, 159, 90);
	Color FG2 = new Color(232, 188, 185);
	Color FG3 = new Color(255, 143, 51);
	Color BG1 = new Color(102, 37, 73);
	Color BG2 = new Color(69, 25, 82);

	private JButton btnHvH;
	private JButton btnHvB;
	private JComboBox boardSizeDropDown;
	private JButton btnPlay;
	private JLabel boardSelectedModeLabel;
	private JPanel gamePanel;
	private JPanel containerPanel;
	private JPanel mainGamePanel;

	public void showMainGamePanel() {
		mainGamePanel.setVisible(true);
	}

	public GamePanel() {
		setVisible(false);
		setLayout(null); // Using absolute positioning

		containerPanel = new JPanel();
		containerPanel.setBounds(0, 0, 495, 600);
		add(containerPanel);
		containerPanel.setLayout(null);

		mainGamePanel = new JPanel();
		mainGamePanel.setBounds(0, 0, 495, 600);
		containerPanel.add(mainGamePanel);
		mainGamePanel.setLayout(null);

		gamePanel = new MainGame(this);
		gamePanel.setBounds(0, 0, 495, 600);
		containerPanel.add(gamePanel);

		btnHvH = new JButton("Human vs Human");
		btnHvH.setFocusPainted(false);
		btnHvH.setBounds(122, 200, 250, 50);
		mainGamePanel.add(btnHvH);
		btnHvH.setForeground(FG2);
		btnHvH.setBackground(BG1);
		btnHvH.setBorder(new LineBorder(BG1));
		btnHvH.setFont(text1);

		btnHvB = new JButton("Human vs Bot");
		btnHvB.setFocusPainted(false);
		btnHvB.setBounds(122, 275, 250, 50);
		mainGamePanel.add(btnHvB);
		btnHvB.setForeground(FG2);
		btnHvB.setBackground(BG1);
		btnHvB.setBorder(new LineBorder(BG1));
		btnHvB.setFont(text1);

		JLabel boardSizeLabel = new JLabel("Board Size:");
		boardSizeLabel.setBounds(122, 370, 250, 30);
		mainGamePanel.add(boardSizeLabel);
		boardSizeLabel.setFont(text1);
		boardSizeLabel.setForeground(FG3);

		boardSelectedModeLabel = new JLabel("Mode Selected: ");
		boardSelectedModeLabel.setBounds(122, 159, 250, 30);
		mainGamePanel.add(boardSelectedModeLabel);
		boardSelectedModeLabel.setForeground(new Color(255, 143, 51));
		boardSelectedModeLabel.setFont(new Font("Dialog", Font.PLAIN, 15));

		boardSizeDropDown = new JComboBox();
		boardSizeDropDown.setBounds(122, 400, 250, 25);
		mainGamePanel.add(boardSizeDropDown);
		boardSizeDropDown.setForeground(new Color(232, 188, 185));
		boardSizeDropDown.setBackground(new Color(102, 37, 73));
		boardSizeDropDown.setFont(new Font("Tahoma", Font.PLAIN, 15));
		boardSizeDropDown.setModel(new DefaultComboBoxModel(new String[] { "3x3", "4x4", "5x5", "6x6", "8x8" }));

		btnPlay = new JButton("PLAY");
		btnPlay.setFocusPainted(false);
		btnPlay.setBounds(122, 450, 250, 75);
		mainGamePanel.add(btnPlay);
		btnPlay.setForeground(FG3);
		btnPlay.setBackground(BG1);
		btnPlay.setBorder(new LineBorder(BG1));
		btnPlay.setFont(text2);

		JLabel background = new JLabel(
				new ImageIcon(new ImageIcon(MainGame.class.getResource("/tictac2/bg/GamePanelbg.png")).getImage()
						.getScaledInstance(495, 600, Image.SCALE_DEFAULT)));
		background.setBounds(0, 0, 495, 600);
		mainGamePanel.add(background);

		btnPlay.addActionListener(this);
		btnHvB.addActionListener(this);
		btnHvH.addActionListener(this);

		// Set preferred size to ensure the panel has enough space for the background
		// image
		setPreferredSize(new Dimension(495, 600));

		// Load background image
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnHvH) {
			boardSelectedModeLabel.setText("Mode Selected: " + btnHvH.getText());
		} else if (e.getSource() == btnHvB) {
			boardSelectedModeLabel.setText("Mode Selected: " + btnHvB.getText());
		} else if (e.getSource() == btnPlay) {
			// Get the selected mode
			String selectedMode = boardSelectedModeLabel.getText().substring("Mode Selected: ".length());

			// Get the selected board size
			String selectedBoardSizeText = (String) boardSizeDropDown.getSelectedItem();
			int boardSize = extractBoardSize(selectedBoardSizeText);

			// Update the game board and labels in the MainGame panel
			((MainGame) gamePanel).updateGameBoard(selectedMode, boardSize);

			// Hide the current panel (GamePanel) and show the MainGame panel
			gamePanel.setVisible(true);
			mainGamePanel.setVisible(false);
		}
	}

	private int extractBoardSize(String boardSizeText) {
		switch (boardSizeText) {
		case "3x3":
			return 3;
		case "4x4":
			return 4;
		case "5x5":
			return 5;
		case "6x6":
			return 6;
		case "8x8":
			return 8;
		default:
			// Handle invalid board size
			return 3; // Or any other default value you prefer
		}
	}

}
