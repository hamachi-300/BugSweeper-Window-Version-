import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class HistoryWindow extends PopUpWindow{

    public HistoryWindow(){
        setLabel(this);
        pack();
        setVisible(true);
    }

    @Override
    public void setLabel(PopUpWindow window){
        JPanel panel = new JPanel();
        JLabel historyLabel = new JLabel("History");
        String filePath = "history.txt";
        
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // make history label center hollizontally
        historyLabel.setAlignmentX(CENTER_ALIGNMENT);

        // set Font
        historyLabel.setFont(bigFont);

        // set color
        panel.setBackground(new Color(color1R, color1G, color1B));
        historyLabel.setForeground(new Color(color4R, color4G, color4B));

        panel.add(Box.createVerticalStrut(20));
        panel.add(historyLabel);
        // add space between historyLabel and history info
        panel.add(Box.createVerticalStrut(20));

        // create Exception for handle when read file
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;

            while ((line = bufferedReader.readLine()) != null){
                JLabel label = new JLabel(line);
                label.setAlignmentX(CENTER_ALIGNMENT);
                label.setFont(mediumFont);
                label.setForeground(new Color(color4R, color4G, color4B));
                panel.add(label);
                panel.add(Box.createVerticalStrut(5));
            }
            bufferedReader.close();

        } catch (IOException e){
            System.out.println("No history recorded");
        }
        // create scroll when size of infomation more than preferred size
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}