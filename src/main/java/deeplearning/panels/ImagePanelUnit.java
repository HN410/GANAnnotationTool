package deeplearning.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

//画像一枚分のJPanel
public class ImagePanelUnit extends JPanel{
    private static int LABEL_MARGIN_W = 10;

    public ImagePanelUnit(String labelText){
        setLayout(new BorderLayout());

        JPanel label = getLabelPanel(labelText);
        add(BorderLayout.SOUTH, label);

    }

    private JPanel getLabelPanel(String labelText){
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel(labelText);
        label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(BorderLayout.CENTER, label);

        panel.add(BorderLayout.SOUTH,
             Box.createRigidArea(new Dimension(LABEL_MARGIN_W, LABEL_MARGIN_W)));

        return panel;
    }
    
}
