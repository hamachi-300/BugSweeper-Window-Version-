import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MineClearedWindow extends Window {

    private MineSweeper parent;
    private int rowTiles;
    private int colTiles;
    private int mines;
    private int difficulty;

    public MineClearedWindow(int rowTiles, int colTiles, int mines, MineSweeper parent, GameWindow game, int difficulty, String time){
        this.parent = parent;
        this.rowTiles = rowTiles;
        this.colTiles = colTiles;
        this.mines = mines;
        this.difficulty = difficulty;

        saveHistory(time.substring(7), difficulty);
        setLayout(new GridBagLayout());
        setLabel(this);
        parent.attachWindow(game, this);
        setVisible(true);
    }
    
    // save score to history.txt
    public void saveHistory(String time, int difficulty){
        // DD//MM/YY : DIFFCULTY : TIME
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH));
        String difficultyStr;
        String filePath = "history.txt";

        // convert difficulty integer to str
        switch (difficulty) {
            case 1:
                difficultyStr = "Easy";
                break;
            case 2:
                difficultyStr = "Normal";
                break;
            case 3:
                difficultyStr = "Hard";
                break;
            case 4:
                difficultyStr = "Extreme";
                break;
            default:
                difficultyStr = "Easy";
                break;
        }

        // sum everything together
        String info = String.format("%s : %s : %s \n", date, difficultyStr, time);
        try {
            FileWriter writer = new FileWriter(filePath, true);
            writer.write(info);
            writer.close();
        } catch (IOException e){
            System.err.println("Error while load file path : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    public void setLabel(Window window){
        JPanel gameClearedPanel = new JPanel(new BorderLayout());
        gameClearedPanel.setLayout(new BoxLayout(gameClearedPanel, BoxLayout.Y_AXIS));
        JLabel mineClearedLabel = new JLabel("Mine Cleared");
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));;
        JButton newGameButton = new JButton("New Game");
        JButton exitButton = new JButton("     Exit     ");

        MineClearedWindow mineClearedWindow = this;

        exitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                System.exit(0);
            }
        });

        newGameButton.addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e){
                parent.attachWindow(mineClearedWindow, new GameWindow(rowTiles, colTiles, mines, parent, difficulty));
            }
        });

        buttonPanel.add(newGameButton);
        buttonPanel.add(Box.createHorizontalStrut(30));
        buttonPanel.add(exitButton);

        mineClearedLabel.setAlignmentX(CENTER_ALIGNMENT);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);

        gameClearedPanel.add(mineClearedLabel, BorderLayout.CENTER);
        gameClearedPanel.add(Box.createVerticalStrut(20));
        gameClearedPanel.add(buttonPanel, BorderLayout.SOUTH);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        // decoration
        gameClearedPanel.setBackground(new Color(color1R, color1G, color1B));
        buttonPanel.setBackground(new Color(color1R, color1G, color1B));
        mineClearedLabel.setFont(doubleBigFont);
        mineClearedLabel.setForeground(new Color(color4R, color4G, color4B));

        newGameButton.setForeground(new Color(color1R, color1G, color1B));
        newGameButton.setBackground(new Color(color4R, color4G, color4B));
        newGameButton.setFocusable(false);
        newGameButton.setFont(bigFont);

        exitButton.setForeground(new Color(color1R, color1G, color1B));
        exitButton.setBackground(new Color(color4R, color4G, color4B));
        exitButton.setFocusable(false); 
        exitButton.setFont(bigFont);

        add(gameClearedPanel, gbc);
    }
}