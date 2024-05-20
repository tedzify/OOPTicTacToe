package main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainGame extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;

    private static final Font LABEL_FONT = new Font("Dialog", Font.BOLD, 15);
    private static final Font TURN_HOLDER_FONT = new Font("Dialog", Font.BOLD, 50);
    private static final Font BUTTON_FONT = new Font("Tahoma", Font.BOLD, 12);
    private static final Color FG_COLOR = new Color(255, 143, 51);
    private static final Color BG_COLOR = new Color(69, 25, 82);
    private static final Color BOARD_BG_COLOR = new Color(102, 37, 73);

    private final JLabel lblSelectedMode;
    private final JLabel lblTurnHolder;
    private final JLabel lblScore_X;
    private final JLabel lblScore_O;
    private final JPanel boardPanel;
    private final JButton btnBackButton;
    private final GamePanel gamePanel;

    private JLabel lblPlayerToMove;
    private JPanel panelMain;
    private JPanel containerPanel;
    private JButton[][] buttons;
    private int boardSize;
    private String currentPlayer;
    private int scoreX;
    private int scoreO;
    private boolean isBotMode;
    private Timer botMoveTimer;

    public MainGame(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.currentPlayer = "X";
        this.scoreX = 0;
        this.scoreO = 0;
        this.isBotMode = false;

        // Initialize final fields
        this.lblSelectedMode = createLabel("", LABEL_FONT, FG_COLOR, 126, 186, 245, 30);
        this.lblScore_X = createLabel("X Score: 0", LABEL_FONT, FG_COLOR, 355, 116, 130, 30);
        this.lblScore_O = createLabel("O Score: 0", LABEL_FONT, FG_COLOR, 355, 145, 130, 30);
        this.lblTurnHolder = createLabel("X", TURN_HOLDER_FONT, FG_COLOR, 26, 93, 118, 65);
        this.boardPanel = new JPanel();
        this.btnBackButton = createButton("Back", BUTTON_FONT, FG_COLOR, BG_COLOR, 10, 21, 89, 23);

        setupPanel();
        setupContainerPanel();
        setupMainPanel();
        setupBoardPanel();
        setupLabels();
        setupButtons();
        setupBackground();
    }

    private void setupPanel() {
        setVisible(false);
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(495, 600));
        setLayout(null);
    }

    private void setupContainerPanel() {
        containerPanel = new JPanel();
        containerPanel.setBounds(0, 0, 495, 600);
        containerPanel.setLayout(null);
        add(containerPanel);
    }

    private void setupMainPanel() {
        panelMain = new JPanel();
        panelMain.setBounds(0, 0, 495, 600);
        panelMain.setLayout(null);
        containerPanel.add(panelMain);
    }

    private void setupBoardPanel() {
        boardPanel.setBackground(BOARD_BG_COLOR);
        boardPanel.setBounds(97, 227, 300, 300);
        panelMain.add(boardPanel);
    }

    private void setupLabels() {
        lblPlayerToMove = createLabel("To move", LABEL_FONT, FG_COLOR, 50, 145, 71, 30);

        panelMain.add(lblSelectedMode);
        panelMain.add(lblScore_X);
        panelMain.add(lblScore_O);
        panelMain.add(lblTurnHolder);
        panelMain.add(lblPlayerToMove);
    }

    private JLabel createLabel(String text, Font font, Color fg, int x, int y, int width, int height) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(fg);
        label.setBounds(x, y, width, height);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        return label;
    }

    private void setupButtons() {
        btnBackButton.addActionListener(this);
        panelMain.add(btnBackButton);
    }

    private JButton createButton(String text, Font font, Color fg, Color bg, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setBounds(x, y, width, height);
        button.setBorder(new LineBorder(fg, 2));
        button.setFocusPainted(false);
        return button;
    }

    private void setupBackground() {
        JLabel background = new JLabel(new ImageIcon(new ImageIcon(MainGame.class.getResource("/tictac2/bg/GamePanelbg.png"))
                .getImage().getScaledInstance(495, 600, Image.SCALE_DEFAULT)));
        background.setBounds(0, 0, 495, 600);
        panelMain.add(background);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnBackButton) {
            handleBackButton();
        } else {
            handleBoardButton(e);
        }
    }

    private void handleBackButton() {
        setVisible(false);
        gamePanel.showMainGamePanel();
        lblScore_X.setText("X Score: " + 0 * scoreX);
        lblScore_O.setText("O Score: " + 0 * scoreX);
    }

    private void handleBoardButton(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        if (buttonClicked.getText().equals("")) {
            buttonClicked.setText(currentPlayer);
            buttonClicked.setFont(new Font("Tahoma", Font.BOLD, (int) (buttonClicked.getPreferredSize().height * 0.6)));
            if (checkWin()) {
                updateScore();
                JOptionPane.showMessageDialog(this, "Player " + currentPlayer + " wins!");
                resetBoard();
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "It's a draw!");
                resetBoard();
            } else {
                switchPlayer();
                if (isBotMode && currentPlayer.equals("O")) {
                    botMove();
                }
            }
        }
    }

    private void updateScore() {
        if (currentPlayer.equals("X")) {
            scoreX++;
            lblScore_X.setText("X Score: " + scoreX);
        } else {
            scoreO++;
            lblScore_O.setText("O Score: " + scoreO);
        }
    }

    private void switchPlayer() {
        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
        lblTurnHolder.setText(currentPlayer);
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
                JButton button = createBoardButton(buttonSize);
                buttons[row][col] = button;
                boardPanel.add(button);
            }
        }

        boardPanel.revalidate();
        boardPanel.repaint();
    }

    private JButton createBoardButton(int buttonSize) {
        JButton button = new JButton();
        button.setBorder(new LineBorder(FG_COLOR, 2));
        button.setFont(new Font("Tahoma", Font.BOLD, (int) (buttonSize * 0.6)));
        button.setForeground(FG_COLOR);
        button.setBackground(BG_COLOR);
        button.setPreferredSize(new Dimension(buttonSize, buttonSize));
        button.setFocusPainted(false);
        button.addActionListener(this);
        return button;
    }

    private void botMove() {
        botMoveTimer = new Timer(1000, e -> {
            if (tryToWin() || blockPlayerWin()) {
                if (checkWin()) {
                    scoreO++;
                    lblScore_O.setText("O Score: " + scoreO);
                    JOptionPane.showMessageDialog(MainGame.this, "Player " + currentPlayer + " wins!");
                    resetBoard();
                } else {
                    switchPlayer();
                }
                botMoveTimer.stop();
            } else {
                makeRandomMove();
                botMoveTimer.stop();
            }
        });
        botMoveTimer.setRepeats(false);
        botMoveTimer.start();
    }

    private void makeRandomMove() {
        Random rand = new Random();
        int row, col;
        do {
            row = rand.nextInt(boardSize);
            col = rand.nextInt(boardSize);
        } while (!buttons[row][col].getText().equals(""));

        buttons[row][col].setText(currentPlayer);
        buttons[row][col].setFont(new Font("Tahoma", Font.BOLD, (int) (buttons[row][col].getPreferredSize().height * 0.6)));

        if (checkWin()) {
            scoreO++;
            lblScore_O.setText("O Score: " + scoreO);
            JOptionPane.showMessageDialog(MainGame.this, "Player " + currentPlayer + " wins!");
            resetBoard();
        } else {
            switchPlayer();
        }
    }

    private boolean tryToWin() {
        return checkAndMakeMove(currentPlayer);
    }

    private boolean blockPlayerWin() {
        return checkAndMakeMove("X");
    }

    private boolean checkAndMakeMove(String player) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (buttons[row][col].getText().equals(player) && buttons[row][col + 1].getText().equals(player)
                        && buttons[row][col + 2].getText().equals("")) {
                    buttons[row][col + 2].setText(currentPlayer);
                    buttons[row][col + 2].setFont(new Font("Tahoma", Font.BOLD,
                            (int) (buttons[row][col + 2].getPreferredSize().height * 0.6)));
                    return true;
                }
            }
        }

        for (int col = 0; col < boardSize; col++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (buttons[row][col].getText().equals(player) && buttons[row + 1][col].getText().equals(player)
                        && buttons[row + 2][col].getText().equals("")) {
                    buttons[row + 2][col].setText(currentPlayer);
                    buttons[row + 2][col].setFont(new Font("Tahoma", Font.BOLD,
                            (int) (buttons[row + 2][col].getPreferredSize().height * 0.6)));
                    return true;
                }
            }
        }

        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (buttons[row][col].getText().equals(player) && buttons[row + 1][col + 1].getText().equals(player)
                        && buttons[row + 2][col + 2].getText().equals("")) {
                    buttons[row + 2][col + 2].setText(currentPlayer);
                    buttons[row + 2][col + 2].setFont(new Font("Tahoma", Font.BOLD,
                            (int) (buttons[row + 2][col + 2].getPreferredSize().height * 0.6)));
                    return true;
                }
            }
        }

        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = 2; col < boardSize; col++) {
                if (buttons[row][col].getText().equals(player) && buttons[row + 1][col - 1].getText().equals(player)
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
        return checkLines(currentPlayer);
    }

    private boolean checkLines(String player) {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (buttons[row][col].getText().equals(player)
                        && buttons[row][col + 1].getText().equals(player)
                        && buttons[row][col + 2].getText().equals(player)) {
                    return true;
                }
            }
        }

        for (int col = 0; col < boardSize; col++) {
            for (int row = 0; row < boardSize - 2; row++) {
                if (buttons[row][col].getText().equals(player)
                        && buttons[row + 1][col].getText().equals(player)
                        && buttons[row + 2][col].getText().equals(player)) {
                    return true;
                }
            }
        }

        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = 0; col < boardSize - 2; col++) {
                if (buttons[row][col].getText().equals(player)
                        && buttons[row + 1][col + 1].getText().equals(player)
                        && buttons[row + 2][col + 2].getText().equals(player)) {
                    return true;
                }
            }
        }

        for (int row = 0; row < boardSize - 2; row++) {
            for (int col = 2; col < boardSize; col++) {
                if (buttons[row][col].getText().equals(player)
                        && buttons[row + 1][col - 1].getText().equals(player)
                        && buttons[row + 2][col - 2].getText().equals(player)) {
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