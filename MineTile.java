import java.awt.Image;

import javax.swing.ImageIcon;

public class MineTile extends Tile {

    boolean isRevealed = false;

    public MineTile(int row, int col, GameWindow parent, int difficulty) {
        super(row, col, parent, difficulty);
    }

    // render mine image
    @Override
    public void renderTile(Tile[][] tileList){

        int heightIcon = difficulty > 2 ? 20 : 30;
        int widthIcon = difficulty > 2 ? 20 : 30;
        // load bug image
        ImageIcon originalBug02 = new ImageIcon("images/bug02.png");
        // set bug image scale
        ImageIcon bug02 = new ImageIcon(originalBug02.getImage().getScaledInstance(widthIcon, heightIcon, Image.SCALE_SMOOTH));
        setIcon(bug02);
    }
}
