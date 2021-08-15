package deeplearning.panels;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

//画像一枚分のJPanel
public class ImagePanelUnit extends JPanel{

    public ImagePanelUnit(String labelText){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(labelText);
        panel.add(BorderLayout.SOUTH, label);

    }
    
}
