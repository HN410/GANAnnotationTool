package deeplearning.panels;

import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

//画像一枚分のJPanel
public class ImagePanelUnit extends JPanel{
    private static int IMAGE_SIZE = 200;
    private static int LABEL_MARGIN_W = 10;

    private JLabel imageLabel;

    public ImagePanelUnit(String labelText){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE + LABEL_MARGIN_W*2));

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        imageLabel = getImageLabel();
        setImage();
        imagePanel.add(imageLabel);

        JPanel label = getLabelPanel(labelText);

        add(BorderLayout.SOUTH, label);
        add(BorderLayout.CENTER, imagePanel);

    }

    private void setImage() {
        ImageIcon icon = getResizedImageIcon("./test.jpg");
        imageLabel.setIcon(icon);
    }

    private JLabel getImageLabel() {
        JLabel label = new JLabel();
        label.setMinimumSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        return label;
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
 
    
    private ImageIcon getResizedImageIcon(String imagePath){
        MediaTracker tracker = new MediaTracker(this); 

        ImageIcon icon = new ImageIcon(imagePath);
        int height = icon.getIconHeight();
        int width = icon.getIconWidth();
        Image image = icon.getImage();
        if(height > width){
            image = image.getScaledInstance(-1, IMAGE_SIZE, Image.SCALE_SMOOTH);
        }else{
            image = image.getScaledInstance(IMAGE_SIZE, -1, Image.SCALE_SMOOTH);
        }

        
        
        tracker.addImage(image, 1); //縮小処理終了まで待機
        icon = new ImageIcon(image);

        return icon;
    }

    public void imageDropped(File file){
        //画像がドロップされたら呼び出される

    }
}