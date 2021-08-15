package deeplearning.panels;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.scene.image.Image;

//画像部分のパネル
public class ImagesPanel extends JPanel{

    private static String SOURCE_LABEL = "Source image";
    private static String TARGET_LABEL = "Target image";

    public ImagesPanel(){
        super();
        setLayout(new GridLayout(1, 2));

        ImagePanelUnit ipu1 = new ImagePanelUnit(SOURCE_LABEL);
        ImagePanelUnit ipu2 = new ImagePanelUnit(TARGET_LABEL);
        
        add(ipu1);
        add(ipu2);

    }
    
}
