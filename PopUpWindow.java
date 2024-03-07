import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

abstract class PopUpWindow extends JFrame{

    protected Font maruMonica;
    
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

    protected Font doubleBigFont;
    protected Font bigFont;
    protected Font mediumFont;
    protected Font smallFont;

    public PopUpWindow(){

        try {
            InputStream is = getClass().getResourceAsStream("fonts/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(30f);
        } catch (FontFormatException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }

        doubleBigFont = maruMonica.deriveFont(24f);
        bigFont = maruMonica.deriveFont(18f);
        smallFont = maruMonica.deriveFont(14f);
        mediumFont = maruMonica.deriveFont(16f);

        setSize(300, 400);
        setLocationRelativeTo(null);
    }

    abstract public void setLabel(PopUpWindow window);
}
