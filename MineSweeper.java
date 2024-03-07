import java.awt.BorderLayout;

import javax.swing.JFrame;

public class MineSweeper extends JFrame {

    private int rowTiles;
    private int colTiles;
    private int mines;

    public MineSweeper(){

        // set default of rowTile colTiles and mines
        this.rowTiles = 10;
        this.colTiles = 10;
        this.mines = 10;

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Bugsweeper");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        attachWindow(null, new Menu(this.rowTiles, this.colTiles, this.mines, this, null));

        setVisible(true);
    }

    // this method will delete old Window and add new Window instead of old Window
    public void attachWindow(Window oldWindow, Window newWindow){
        if (!(oldWindow == null)){
            this.remove(oldWindow);
        }
        add(newWindow, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    // will set colTiles and rowTiles according difficulty
    public void setDifficulty(int colTiles, int rowTiles, int mines){
        this.colTiles = colTiles;
        this.rowTiles = rowTiles;
        this.mines = mines;
    }
}

