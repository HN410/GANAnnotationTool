package deeplearning.main;


import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Utilities {
    public static JPanel getPanelWithMargin(JComponent comp, int marginW, int marginH){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(BorderLayout.NORTH, Box.createVerticalStrut(marginH));
        panel.add(BorderLayout.SOUTH, Box.createVerticalStrut(marginH));
        panel.add(BorderLayout.WEST, Box.createHorizontalStrut(marginW));
        panel.add(BorderLayout.EAST, Box.createHorizontalStrut(marginW));
        panel.add(BorderLayout.CENTER, comp);

        return panel;
    }
    
}
