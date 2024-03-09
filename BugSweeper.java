import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class BugSweeper extends JFrame {

    private int rowTiles;
    private int colTiles;
    private int bugs;

    public BugSweeper(){

        // set default of rowTile colTiles and bugs
        this.rowTiles = 10;
        this.colTiles = 10;
        this.bugs = 10;

        // set image icon
        ImageIcon icon = new ImageIcon("images/bug-icon.png");
        setIconImage(icon.getImage());

        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Bugsweeper");
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        attachWindow(null, new Menu(this.rowTiles, this.colTiles, this.bugs, this, null));

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
    public void setDifficulty(int colTiles, int rowTiles, int bugs){
        this.colTiles = colTiles;
        this.rowTiles = rowTiles;
        this.bugs = bugs;
    }
}

