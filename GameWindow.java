import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.util.Random;

public class GameWindow extends Window {
    private int rowTiles;
    private int colTiles;
    private int bugs;
    private int bugCount;
    private int difficulty;
    private int fleshTime = 0;
    private boolean gameOver = false;
    protected boolean flagMode = true;

    protected Timer timer;
    private JLabel bugCounter;
    private Tile[][] tileList;
    Random rand = new Random();

    public GameWindow(int rowTiles, int colTiles, int bugs, BugSweeper parent, int difficulty) {
        this.rowTiles = rowTiles;
        this.colTiles = colTiles;
        this.bugs = bugs;
        this.bugCount = bugs;
        this.difficulty = difficulty;
        tileList = new Tile[rowTiles][colTiles];

        setLayout(new BorderLayout());

        // load bug image
        ImageIcon originalBug02 = new ImageIcon("images/bug02.png");
        ImageIcon originalGlasses = new ImageIcon("images/glasses.png");
        ImageIcon originalSetting = new ImageIcon("images/setting.png");

        // set bug image scale
        int heightIcon = difficulty > 2 ? 20 : 40;
        int widthIcon = difficulty > 2 ? 20 : 40;

        ImageIcon bug02 = new ImageIcon(originalBug02.getImage().getScaledInstance(widthIcon, heightIcon, Image.SCALE_SMOOTH));
        ImageIcon glasses = new ImageIcon(originalGlasses.getImage().getScaledInstance(widthIcon, heightIcon, Image.SCALE_SMOOTH));
        ImageIcon setting = new ImageIcon(originalSetting.getImage().getScaledInstance(widthIcon, heightIcon, Image.SCALE_SMOOTH));

        JPanel header = new JPanel(new BorderLayout());
        JPanel footer = new JPanel(new BorderLayout());
        JPanel setFlagPanel = new JPanel(new BorderLayout());
        JPanel board = new JPanel(new GridLayout(rowTiles, colTiles, 0, 0));

        // create bugCounter label
        bugCounter = new JLabel(String.format("Bug Remain : %d", bugs));
        JLabel timeCounter = new JLabel("Time : 00:00:00");
        JButton settingBtn = new JButton();
        JButton bugBtn = new JButton();
        JButton flagBtn = new JButton();

        // set icon
        settingBtn.setIcon(setting);
        bugBtn.setIcon(bug02);
        flagBtn.setIcon(glasses);

        // set Monospaced font
        bugCounter.setFont(doubleBigFont);
        timeCounter.setFont(doubleBigFont);

        // create timer when game start
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fleshTime++;
                updateTimerLabel(timeCounter);
            }
        });

        // switch bettween flag mode and bug mode
        flagMode(flagBtn, bugBtn);
        flagBtn.addActionListener(e -> flagMode(flagBtn, bugBtn));
        bugBtn.addActionListener(e -> flagMode(flagBtn, bugBtn));
        settingBtn.addActionListener(e -> new SettingWindow(rowTiles, colTiles, bugs, parent, this));
        // start timer
        timer.start();

        // add all component
        setFlagPanel.add(bugBtn, BorderLayout.WEST);
        setFlagPanel.add(flagBtn, BorderLayout.EAST);

        header.add(bugCounter, BorderLayout.EAST);
        header.add(timeCounter, BorderLayout.WEST);
        footer.add(setFlagPanel, BorderLayout.EAST);
        footer.add(settingBtn, BorderLayout.WEST);

        // set tile and bug
        placeTiles(tileList, parent);
        placeBug(tileList, parent);

        GameWindow gameWindow = this;
        // loop for add all Tile and add MouseListener
        for (int i = 0; i < rowTiles; i++){
            for (int j = 0; j < colTiles; j++){
                Tile tile = tileList[i][j];
                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.addMouseListener(new MouseAdapter() {
                    // method handle when mouse is pressed
                    @Override
                    public void mousePressed(MouseEvent e){
                        Tile tile = (Tile) e.getSource();

                        // if game over cannot click button
                        if (!gameOver){
                            // check flagMode
                            // if true both left or right will set flag
                            if (flagMode == true){
                                if (e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3){
                                    Tile thisTile = (Tile) e.getSource();
                                    if (thisTile.isEnabled()){
                                        if (tile.isFlag()){
                                            tile.removeFlag();
                                        } else {
                                            tile.setFlag();
                                        }
                                    }
                                }
                            } else {
                                // left click mouse will reveal tile
                                if (e.getButton() == MouseEvent.BUTTON1 && !tile.isFlag()){
                                    // expose all bug if tile is bug
                                    if (BugTile.class.isAssignableFrom(tile.getClass())){
                                        for (Tile[] tileRow : tileList){
                                            for (Tile tile_c : tileRow) {
                                                if (BugTile.class.isAssignableFrom(tile_c.getClass())){
                                                    tile_c.revealTile(tileList);
                                                }
                                            }
                                        }
                                        gameOver = true;
                                        timer.stop();
                                        playSound("sounds/lose8bit.wav");
                                        startOverWindow(parent);
                                    } else {
                                        tile.revealTile(tileList);
                                        // count all tile was revealed
                                        int tileCount = 0;
                                        for (Tile[] tileRow : tileList){
                                            for (Tile thisTile : tileRow){
                                                if (thisTile.isRevealed()){
                                                    tileCount += 1;
                                                }
                                            }
                                        }
    
                                        // check game is cleared
                                        if ((rowTiles * colTiles) - tileCount - bugs == 0) {
                                            // call gameClearWindow if game was cleared
                                            timer.stop();
                                            playSound("sounds/win8bit.wav");
                                            new BugClearedWindow(rowTiles, colTiles, bugs, parent, gameWindow, difficulty, timeCounter.getText());
                                        }
                                    }
                                }

                                // right click mouse will set flag
                                if (e.getButton() == MouseEvent.BUTTON3){
                                    Tile thisTile = (Tile) e.getSource();
                                    if (thisTile.isEnabled()){
                                        if (tile.isFlag()){
                                            tile.removeFlag();
                                        } else {
                                            tile.setFlag();
                                        }
                                    }
                                }
                            }
                        }

                        parent.revalidate();
                        parent.repaint();
                    }
                });

                
                board.add(tile);
            }
        }

        // decoration components
        header.setBackground(new Color(color1R, color1G, color1B));
        board.setBackground(new Color(color1R, color1G, color1B));
        footer.setBackground(new Color(color1R, color1G, color1B));
        setFlagPanel.setBackground(new Color(color1R, color1G, color1B));
        timeCounter.setForeground(new Color(color4R, color4G, color4B));
        bugCounter.setForeground(new Color(color4R, color4G, color4B));

        settingBtn.setBackground(new Color(color1R, color1G, color1B));
        settingBtn.setFocusable(false);

        bugBtn.setBackground(new Color(color2R, color2G, color2B));
        bugBtn.setFocusable(false);

        flagBtn.setBackground(new Color(color1R, color1G, color1B));
        flagBtn.setFocusable(false);

        add(header, BorderLayout.NORTH);
        add(board, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
    }


    // play sound
    public void playSound(String path){
        try {
            File soundFile = new File(path);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // set timerLabel text
    public void updateTimerLabel(JLabel timeCounter){
        int hour = fleshTime/360;
        int minute = (fleshTime/60) % 60;
        int second = fleshTime % 60;
        String hourStr = String.format("%02d", hour);
        String minuteStr = String.format("%02d", minute);
        String secondStr = String.format("%02d", second);
        timeCounter.setText(String.format("Time : %s:%s:%s", hourStr, minuteStr, secondStr));
    }
    
    // delay 1 second before create GameOverWindow
    private void startOverWindow(BugSweeper parent){
        GameWindow game = this;
        Timer gameOverDelay = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // call gameOverWindow
                new GameOverWindow(rowTiles, colTiles, bugs, parent, game, difficulty);
            }
        });
        gameOverDelay.setRepeats(false);
        gameOverDelay.start();
        
    }

    // place tiles method
    private void placeTiles(Tile[][] tileList, BugSweeper parent){
        for (int i = 0; i < rowTiles; i++){
            for (int j = 0; j < colTiles; j++){
                // check bug tile
                Tile tile = new NormalTile(i, j, rowTiles, colTiles, this, difficulty);
                tileList[i][j] = tile;
            }
        }
    }

    // place bugs method
    private void placeBug(Tile[][] tileList, BugSweeper parent){

        for (int i = 0; i < bugs; i++){
            int row = rand.nextInt(rowTiles);
            int col = rand.nextInt(colTiles);

            if (!BugTile.class.isAssignableFrom(tileList[row][col].getClass())){
                tileList[row][col] = new BugTile(row, col, this, difficulty);
            } else {
                i--;
            }
        }
    }

    // method handle flagMode
    // if flagMode is true it will set false
    // if flagMode is false it will set true
    public void flagMode(JButton flagBtn, JButton bugBtn){
        if (flagMode == false){
            bugBtn.setEnabled(true);
            flagBtn.setEnabled(false);
            flagBtn.setBackground(new Color(color2R, color2G, color2B));
            bugBtn.setBackground(new Color(color1R, color1G, color1B));
            flagMode = true;
        } else {
            bugBtn.setEnabled(false);
            flagBtn.setEnabled(true);
            bugBtn.setBackground(new Color(color2R, color2G, color2B));
            flagBtn.setBackground(new Color(color1R, color1G, color1B));
            flagMode = false;
        }
    }

    // method will update bugCounter
    // when num is 0 mean bugCount will increase
    public void updateBugCounter(int num){
        if (num == 0) {
            bugCount+=1;
            if (bugCount >= 0){
                bugCounter.setText(String.format("Bug Remain : %d", bugCount));
            }
        }

        if  (num == 1) {
            bugCount-=1;
            if (bugCount >= 0){
                bugCounter.setText(String.format("Bug Remain : %d", bugCount));
            }
        }
    }

    @Override
    public void setLabel(Window window){
        // do nothing
    }
}