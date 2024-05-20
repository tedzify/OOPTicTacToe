package main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Font TEXT_FONT_SMALL = new Font("Press Start 2P", Font.PLAIN, 10);
    private static final Font TEXT_FONT_MEDIUM = new Font("Press Start 2P", Font.PLAIN, 15);
    private static final Font TEXT_FONT_LARGE = new Font("Press Start 2P", Font.PLAIN, 30);
    private static final Color FG1 = new Color(243, 159, 90);
    private static final Color FG2 = new Color(232, 188, 185);
    private static final Color FG3 = new Color(255, 143, 51);
    private static final Color BG1 = new Color(102, 37, 73);
    private static final Color BG2 = new Color(69, 25, 82);

    private JButton btnHvH;
    private JButton btnHvB;
    private JComboBox<String> boardSizeDropDown;
    private JButton btnPlay;
    private JLabel boardSelectedModeLabel;
    private JPanel gamePanel;
    private JPanel containerPanel;
    private JPanel mainGamePanel;

    public GamePanel() {
        setupPanel();
        setupContainerPanel();
        setupMainGamePanel();
        setupGamePanel();
        setupButtons();
        setupLabels();
        setupBackground();
        setupActionListeners();
    }

    private void setupPanel() {
        setVisible(false);
        setLayout(null); // Using absolute positioning
        setPreferredSize(new Dimension(495, 600));
    }

    private void setupContainerPanel() {
        containerPanel = new JPanel();
        containerPanel.setBounds(0, 0, 495, 600);
        containerPanel.setLayout(null);
        add(containerPanel);
    }

    private void setupMainGamePanel() {
        mainGamePanel = new JPanel();
        mainGamePanel.setBounds(0, 0, 495, 600);
        mainGamePanel.setLayout(null);
        containerPanel.add(mainGamePanel);
    }

    private void setupGamePanel() {
        gamePanel = new MainGame(this);
        gamePanel.setBounds(0, 0, 495, 600);
        containerPanel.add(gamePanel);
    }

    private void setupButtons() {
        btnHvH = createButton("Human vs Human", FG2, BG1, 122, 200, 250, 50, TEXT_FONT_MEDIUM);
        btnHvB = createButton("Human vs Bot", FG2, BG1, 122, 275, 250, 50, TEXT_FONT_MEDIUM);
        btnPlay = createButton("PLAY", FG3, BG1, 122, 450, 250, 75, TEXT_FONT_LARGE);

        mainGamePanel.add(btnHvH);
        mainGamePanel.add(btnHvB);
        mainGamePanel.add(btnPlay);
    }

    private JButton createButton(String text, Color fg, Color bg, int x, int y, int width, int height, Font font) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBounds(x, y, width, height);
        button.setFont(font);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setBorder(new LineBorder(bg));
        return button;
    }

    private void setupLabels() {
        JLabel boardSizeLabel = createLabel("Board Size:", FG3, TEXT_FONT_MEDIUM, 122, 370, 250, 30);
        boardSelectedModeLabel = createLabel("Mode Selected: ", FG3, new Font("Dialog", Font.PLAIN, 15), 122, 159, 250, 30);

        mainGamePanel.add(boardSizeLabel);
        mainGamePanel.add(boardSelectedModeLabel);

        boardSizeDropDown = new JComboBox<>(new String[]{"3x3", "4x4", "5x5", "6x6", "8x8"});
        boardSizeDropDown.setBounds(122, 400, 250, 25);
        boardSizeDropDown.setForeground(FG2);
        boardSizeDropDown.setBackground(BG1);
        boardSizeDropDown.setFont(new Font("Tahoma", Font.PLAIN, 15));
        mainGamePanel.add(boardSizeDropDown);
    }

    private JLabel createLabel(String text, Color fg, Font font, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        label.setForeground(fg);
        return label;
    }

    private void setupBackground() {
        JLabel background = new JLabel(new ImageIcon(new ImageIcon(MainGame.class.getResource("/tictac2/bg/GamePanelbg.png"))
                .getImage().getScaledInstance(495, 600, Image.SCALE_DEFAULT)));
        background.setBounds(0, 0, 495, 600);
        mainGamePanel.add(background);
    }

    private void setupActionListeners() {
        btnPlay.addActionListener(this);
        btnHvB.addActionListener(this);
        btnHvH.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHvH) {
            boardSelectedModeLabel.setText("Mode Selected: " + btnHvH.getText());
        } else if (e.getSource() == btnHvB) {
            boardSelectedModeLabel.setText("Mode Selected: " + btnHvB.getText());
        } else if (e.getSource() == btnPlay) {
            handlePlayAction();
        }
    }

    private void handlePlayAction() {
        String selectedMode = boardSelectedModeLabel.getText().substring("Mode Selected: ".length());
        String selectedBoardSizeText = (String) boardSizeDropDown.getSelectedItem();
        int boardSize = extractBoardSize(selectedBoardSizeText);

        ((MainGame) gamePanel).updateGameBoard(selectedMode, boardSize);

        gamePanel.setVisible(true);
        mainGamePanel.setVisible(false);
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
                return 3; // Default value
        }
    }

    public void showMainGamePanel() {
        mainGamePanel.setVisible(true);
    }
}