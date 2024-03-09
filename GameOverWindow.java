import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

class GameOverWindow extends Window {
    private BugSweeper parent;
    private int rowTiles;
    private int colTiles;
    private int bugs;
    private int difficulty;

    public GameOverWindow(int rowTiles, int colTiles, int bugs, BugSweeper parent, GameWindow game, int difficulty) {
        this.parent = parent;
        this.rowTiles = rowTiles;
        this.colTiles = colTiles;
        this.bugs = bugs;
        this.difficulty = difficulty;

        setLayout(new GridBagLayout());
        setLabel(this);
        // attact gameOverWindow in BugSweeper
        parent.attachWindow(game, this);
        setVisible(true);
    }

    @Override
    public void setLabel(Window window) {
        JPanel gameOverPanel = new JPanel();
        gameOverPanel.setLayout(new BoxLayout(gameOverPanel, BoxLayout.Y_AXIS));
        JLabel gameOverLabel = new JLabel("Game Over");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));;
        JButton newGameButton = new JButton("New Game");
        JButton exitButton = new JButton("     Exit     ");

        GameOverWindow gameOverWindow = this;

        // add mouseListener for exit button
        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.exit(0);
            }
        });

        // add mouseListner for newGame button
        newGameButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                parent.attachWindow(gameOverWindow, new GameWindow(rowTiles, colTiles, bugs, parent, difficulty));
            }
        });

        // add newGame and exit button to buttonPanel
        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(exitButton);

        gameOverLabel.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

        // add gameOverPanel and buttonPanel to gameOverPanel
        gameOverPanel.add(gameOverLabel);
        gameOverPanel.add(Box.createVerticalStrut(20));
        gameOverPanel.add(buttonPanel);

        // create GridBagConstraints to center gameOverPanel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        // decoration
        gameOverPanel.setBackground(new Color(color1R, color1G, color1B));
        buttonPanel.setBackground(new Color(color1R, color1G, color1B));
        gameOverLabel.setFont(doubleBigFont);
        gameOverLabel.setForeground(new Color(color4R, color4G, color4B));

        newGameButton.setForeground(new Color(color1R, color1G, color1B));
        newGameButton.setBackground(new Color(color4R, color4G, color4B));
        newGameButton.setFocusable(false);
        newGameButton.setFont(bigFont);

        exitButton.setForeground(new Color(color1R, color1G, color1B));
        exitButton.setBackground(new Color(color4R, color4G, color4B));
        exitButton.setFocusable(false);
        exitButton.setFont(bigFont);

        // add game with gcb for add gammOverPanel at center of window
        add(gameOverPanel, gbc);
    }
}
