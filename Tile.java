import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;

abstract public class Tile extends JButton {
    private int row;
    private int  col;
    private boolean isRevealed = false;
    private boolean isFlag = false;
    private GameWindow parent;
    protected int difficulty;

    protected final int color1R = 44;
    protected final int color1G = 51;
    protected final int color1B = 51;
    protected final int color2R = 57;
    protected final int color2G = 91;
    protected final int color2B = 100;
    protected final int color3R = 165;
    protected final int color3G = 201;
    protected final int color3B = 202;
    protected final int color4R = 231;
    protected final int color4G = 246;
    protected final int color4B = 242;

    protected final Font bigFont = new Font("Monospaced", Font.BOLD, 18);
    protected final Font mediumFont = new Font("Monospaced", Font.BOLD, 12);
    protected final Font smallFont = new Font("Monospaced", Font.PLAIN, 12);

    public Tile(int row, int col, GameWindow parent, int difficulty){
        this.row = row;
        this.col= col;
        this.parent = parent;
        this.difficulty = difficulty;

        setBackground(new Color(color1R, color1G, color1B));
    }

    // reveal tile method
    public void revealTile(Tile[][] tileList){
        renderTile(tileList);
    }

    // set flag method
    public void setFlag(){
        renderFlag();
        isFlag = true;
        // parameter 1 mean bugCounter will decease
        parent.updateBugCounter(1);
    }

    // get row method
    public int getRow(){
        return this.row;
    }

    // get col method
    public int getCol(){
        return this.col;
    }

    // set reavealed true
    public void setRevealed(boolean bool){
        isRevealed = bool;
    }
    
    // check flag are set
    public boolean isFlag(){
        return this.isFlag;
    }

    // check is revealed
    public boolean isRevealed(){
        return this.isRevealed;
    }

    // remove flag
    public void removeFlag(){
        // render flag frome tile
        renderTileDefault();
        isFlag = false;
        parent.updateBugCounter(0);
    }

    // render flag 
    public void renderFlag(){
        int heightIcon = difficulty > 2 ? 20 : 30;
        int widthIcon = difficulty > 2 ? 20 : 30;
        ImageIcon originalGlasses = new ImageIcon("images/glasses.png");
        ImageIcon glasses = new ImageIcon(originalGlasses.getImage().getScaledInstance(widthIcon, heightIcon, Image.SCALE_SMOOTH));
        setIcon(glasses);
    }

    // render default tile
    public void renderTileDefault(){
        setIcon(null);
    }

    
    abstract public void renderTile(Tile[][] tileList);
}
