package main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class Main extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private static final Font TEXT_FONT = new Font("Press Start 2P", Font.PLAIN, 15);
    private static final Color FG1 = new Color(243, 159, 90);
    private static final Color FG2 = new Color(232, 188, 185);
    private static final Color BG1 = new Color(102, 37, 73);
    private static final Color BG2 = new Color(69, 25, 82);

    private JButton btnNewGame;
    private JButton btnExit;
    private JPanel mainPanel;
    private GamePanel gamePanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Main newFrame = new Main();
                SwingUtilities.updateComponentTreeUI(newFrame);
                newFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Main() {
        setupFrame();
        setupContentPane();
        setupMainPanel();
        setupButtons();
        setupBackground();
        setupGamePanel();
    }

    private void setupFrame() {
        setTitle("Tic Tac Toe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(495, 600);
        setVisible(true);
    }

    private void setupContentPane() {
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
    }

    private void setupMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setBounds(0, 0, 479, 561);
        getContentPane().add(mainPanel);
        mainPanel.setLayout(null);
    }

    private void setupButtons() {
        btnNewGame = createButton("NEW GAME", FG1, BG1, 114, 300, 250, 75);
        btnExit = createButton("EXIT", FG2, BG2, 114, 400, 250, 75);

        mainPanel.add(btnNewGame);
        mainPanel.add(btnExit);

        btnNewGame.addActionListener(this);
        btnExit.addActionListener(this);
    }

    private JButton createButton(String text, Color fg, Color bg, int x, int y, int width, int height) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBounds(x, y, width, height);
        button.setFont(TEXT_FONT);
        button.setForeground(fg);
        button.setBackground(bg);
        button.setBorder(new LineBorder(bg));
        return button;
    }

    private void setupBackground() {
        JLabel background = new JLabel(new ImageIcon(new ImageIcon(MainGame.class.getResource("/tictac2/bg/GamePanelbg.png"))
                .getImage().getScaledInstance(495, 600, Image.SCALE_DEFAULT)));
        background.setLocation(0, 0);
        background.setSize(479, 561);
        mainPanel.add(background);
    }

    private void setupGamePanel() {
        gamePanel = new GamePanel();
        gamePanel.setBounds(0, 0, 495, 600);
        gamePanel.setVisible(false);
        getContentPane().add(gamePanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnNewGame) {
            togglePanels();
        } else if (e.getSource() == btnExit) {
            exitGame();
        }
    }

    private void togglePanels() {
        mainPanel.setVisible(false);
        gamePanel.setVisible(true);
    }

    private void exitGame() {
        System.exit(0);
    }
}