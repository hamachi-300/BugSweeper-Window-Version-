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
    private int mines;
    private int mineCount;
    private int difficulty;
    private int fleshTime = 0;
    private boolean gameOver = false;
    protected boolean flagMode = true;

    protected Timer timer;
    private JLabel mineCounter;
    private Tile[][] tileList;
    Random rand = new Random();

    public GameWindow(int rowTiles, int colTiles, int mines, MineSweeper parent, int difficulty) {
        this.rowTiles = rowTiles;
        this.colTiles = colTiles;
        this.mines = mines;
        this.mineCount = mines;
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

        // create mineCounter label
        mineCounter = new JLabel(String.format("Bug Remain : %d", mines));
        JLabel timeCounter = new JLabel("Time : 00:00:00");
        JButton settingBtn = new JButton();
        JButton mineBtn = new JButton();
        JButton flagBtn = new JButton();

        // set icon
        settingBtn.setIcon(setting);
        mineBtn.setIcon(bug02);
        flagBtn.setIcon(glasses);

        // set Monospaced font
        mineCounter.setFont(doubleBigFont);
        timeCounter.setFont(doubleBigFont);

        // create timer when game start
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fleshTime++;
                updateTimerLabel(timeCounter);
            }
        });

        // switch bettween flag mode and mine mode
        flagMode(flagBtn, mineBtn);
        flagBtn.addActionListener(e -> flagMode(flagBtn, mineBtn));
        mineBtn.addActionListener(e -> flagMode(flagBtn, mineBtn));
        settingBtn.addActionListener(e -> new SettingWindow(rowTiles, colTiles, mines, parent, this));
        // start timer
        timer.start();

        // add all component
        setFlagPanel.add(mineBtn, BorderLayout.WEST);
        setFlagPanel.add(flagBtn, BorderLayout.EAST);

        header.add(mineCounter, BorderLayout.EAST);
        header.add(timeCounter, BorderLayout.WEST);
        footer.add(setFlagPanel, BorderLayout.EAST);
        footer.add(settingBtn, BorderLayout.WEST);

        // set tile and mine
        placeTiles(tileList, parent);
        placeMine(tileList, parent);

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
                                    // expose all mine if tile is mine
                                    if (MineTile.class.isAssignableFrom(tile.getClass())){
                                        for (Tile[] tileRow : tileList){
                                            for (Tile tile_c : tileRow) {
                                                if (MineTile.class.isAssignableFrom(tile_c.getClass())){
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
                                        if ((rowTiles * colTiles) - tileCount - mines == 0) {
                                            // call gameClearWindow if game was cleared
                                            timer.stop();
                                            playSound("sounds/win8bit.wav");
                                            new MineClearedWindow(rowTiles, colTiles, mines, parent, gameWindow, difficulty, timeCounter.getText());
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
        mineCounter.setForeground(new Color(color4R, color4G, color4B));

        settingBtn.setBackground(new Color(color1R, color1G, color1B));
        settingBtn.setFocusable(false);

        mineBtn.setBackground(new Color(color2R, color2G, color2B));
        mineBtn.setFocusable(false);

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
    private void startOverWindow(MineSweeper parent){
        GameWindow game = this;
        Timer gameOverDelay = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // call gameOverWindow
                new GameOverWindow(rowTiles, colTiles, mines, parent, game, difficulty);
            }
        });
        gameOverDelay.setRepeats(false);
        gameOverDelay.start();
        
    }

    // place tiles method
    private void placeTiles(Tile[][] tileList, MineSweeper parent){
        for (int i = 0; i < rowTiles; i++){
            for (int j = 0; j < colTiles; j++){
                // check mine tile
                Tile tile = new NormalTile(i, j, rowTiles, colTiles, this, difficulty);
                tileList[i][j] = tile;
            }
        }
    }

    // place mines method
    private void placeMine(Tile[][] tileList, MineSweeper parent){

        for (int i = 0; i < mines; i++){
            int row = rand.nextInt(rowTiles);
            int col = rand.nextInt(colTiles);

            if (!MineTile.class.isAssignableFrom(tileList[row][col].getClass())){
                tileList[row][col] = new MineTile(row, col, this, difficulty);
            } else {
                i--;
            }
        }
    }

    // method handle flagMode
    // if flagMode is true it will set false
    // if flagMode is false it will set true
    public void flagMode(JButton flagBtn, JButton mineBtn){
        if (flagMode == false){
            mineBtn.setEnabled(true);
            flagBtn.setEnabled(false);
            flagBtn.setBackground(new Color(color2R, color2G, color2B));
            mineBtn.setBackground(new Color(color1R, color1G, color1B));
            flagMode = true;
        } else {
            mineBtn.setEnabled(false);
            flagBtn.setEnabled(true);
            mineBtn.setBackground(new Color(color2R, color2G, color2B));
            flagBtn.setBackground(new Color(color1R, color1G, color1B));
            flagMode = false;
        }
    }

    // method will update mineCounter
    // when num is 0 mean mineCount will increase
    public void updateMineCounter(int num){
        if (num == 0) {
            mineCount+=1;
            if (mineCount >= 0){
                mineCounter.setText(String.format("Bug Remain : %d", mineCount));
            }
        }

        if  (num == 1) {
            mineCount-=1;
            if (mineCount >= 0){
                mineCounter.setText(String.format("Bug Remain : %d", mineCount));
            }
        }
    }

    @Override
    public void setLabel(Window window){
        // do nothing
    }
}