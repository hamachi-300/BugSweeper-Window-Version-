public class MineSweeperTest {
    public static void main(String[] args){
        // send to EDT for handdle
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run(){
                new MineSweeper();
            }
        });
    }
}