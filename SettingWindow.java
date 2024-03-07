import java.awt.Color;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class SettingWindow extends PopUpWindow{

    private MineSweeper parent;
    private GameWindow game;
    private int rowTiles;
    private int colTiles;
    private int mines;

    public SettingWindow(int rowTiles, int colTiles, int mines, MineSweeper parent, GameWindow game) {
        this.parent = parent;
        this.game = game;
        this.rowTiles = rowTiles;
        this.colTiles = colTiles;
        this.mines = mines;

        setSize(200, 300);
        setLocationRelativeTo(null);
        setLabel(this);
        setVisible(true);
    }

    @Override
    public void setLabel(PopUpWindow window) {

        JPanel panel = new JPanel();
        JButton resumeBtn = new JButton("Resume");
        JButton menuBtn = new JButton("Menu");
        JButton exitBtn = new JButton("Exit");

        // center holizontally all btn 
        resumeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        resumeBtn.addActionListener(e -> resumeBtn());
        menuBtn.addActionListener(e -> menuBtn());
        exitBtn.addActionListener(e -> exitBtn());

        // set Font
        resumeBtn.setFont(mediumFont);
        menuBtn.setFont(mediumFont);
        exitBtn.setFont(mediumFont);

        // decoration components
        resumeBtn.setForeground(new Color(color1R, color1G, color1B));
        resumeBtn.setBackground(new Color(color4R, color4G, color4B));
        resumeBtn.setFocusable(false);
        
        menuBtn.setForeground(new Color(color1R, color1G, color1B));
        menuBtn.setBackground(new Color(color4R, color4G, color4B));
        menuBtn.setFocusable(false);

        exitBtn.setForeground(new Color(color1R, color1G, color1B));
        exitBtn.setBackground(new Color(color4R, color4G, color4B));
        exitBtn.setFocusable(false);        

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        // center vertically all btn 
        panel.add(Box.createVerticalGlue());
        panel.add(resumeBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(menuBtn);
        panel.add(Box.createVerticalStrut(10));
        panel.add(exitBtn);
        panel.add(Box.createVerticalGlue());

        panel.setBackground(new Color(color1R, color1G, color1B));
        resumeBtn.setForeground(new Color(color1R, color1G, color1B));
        menuBtn.setForeground(new Color(color1R, color1G, color1B));
        exitBtn.setForeground(new Color(color1R, color1G, color1B));

        window.add(panel);
    }
    
    // when click resumeBtn will dispose setting window
    private void resumeBtn(){
        dispose();
    }

    // when click menuBtn will change to menu window and dispose setting window
    private void menuBtn(){
        new Menu(rowTiles, colTiles, mines, parent, game);
        dispose();
    }

    // when click exitBtn will exit game
    private void exitBtn(){
        System.exit(0);
        dispose();
    }
}
