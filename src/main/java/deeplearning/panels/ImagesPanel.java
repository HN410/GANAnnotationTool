package deeplearning.panels;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import deeplearning.main.MainWindow;
import javafx.scene.image.Image;

//画像部分のパネル
public class ImagesPanel extends JPanel{

    private static final String SOURCE_LABEL = "Source image";
    private static final String TARGET_LABEL = "Target image";

    public ImagesPanel(MainWindow mainWindow){
        super();
        setLayout(new GridLayout(1, 2));

        ImagePanelUnit ipu1 = new ImagePanelUnit(mainWindow, SOURCE_LABEL, true);
        ImagePanelUnit ipu2 = new ImagePanelUnit(mainWindow, TARGET_LABEL, false);
        
        add(ipu1);
        add(ipu2);

    }
    
}
