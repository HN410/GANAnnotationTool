package deeplearning.panels;

import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.Image;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deeplearning.dataInfo.DataInfo;
import deeplearning.main.ErrorChecker;
import deeplearning.main.MainWindow;

//画像一枚分のJPanel
public class ImagePanelUnit extends JPanel{
    private static final int IMAGE_SIZE = 200;
    private static final int LABEL_MARGIN_W = 10;

    private boolean isTagged = false; //今表示されている画像はdataInfo.tagsにデータとして入っているか
    private boolean isSource;//ソースの方の画面か
    private String imageDatafile = null; //現在表示されている画像のdataフォルダに入っているほうのパス
    private String imageOriginFile = null; //現在表示されている画像のドラッグ元のパス

    private MainWindow mainWindow;
    private JLabel imageLabel;

    public ImagePanelUnit(MainWindow mainWindow, String labelText, Boolean isSource){
        this.isSource = isSource;
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE + LABEL_MARGIN_W*2));

        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        imageLabel = getImageLabel();
        setImage("./test.jpg");
        imagePanel.add(imageLabel);

        JPanel label = getLabelPanel(labelText);

        add(BorderLayout.SOUTH, label);
        add(BorderLayout.CENTER, imagePanel);

    }

    private void setImage(String filePath) {
        ImageIcon icon = getResizedImageIcon(filePath);
        imageLabel.setIcon(icon);
    }

    private JLabel getImageLabel() {
        JLabel label = new JLabel();
        label.setMinimumSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setTransferHandler(new DropFileHandler(this, mainWindow));
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
        DataInfo dataInfo = mainWindow.dataInfo;
        if(isSource){
            inImageDropped(dataInfo.sourceImages, dataInfo.sourcePath, file);
        }else{
            inImageDropped(dataInfo.targetImages, dataInfo.targetPath, file);
        }
    }

    private void inImageDropped(HashSet<String> images, String folderName, File imageFile){
        String fileName = imageFile.getName();
        Path folderPath = Paths.get(folderName);
        Path imagePath = folderPath.resolve(fileName);
        boolean conflict = false;
        if(images.contains(fileName)){
            //同名ファイルあり
            conflict = conflictImageName(imageFile, imagePath.toFile());
            if(!conflict){
                //同名でこれ以上の処理をしない場合
                return;
            }
        }
        //コピーはタグ付け確定時

        this.imageDatafile = imagePath.toString();
        this.imageOriginFile = imageFile.getPath();
        setImage(imageFile.getPath());

    }

    private boolean conflictImageName(File newImage, File oldImage){
        //ドロップされたものと同じファイル名のものが存在したとき
        //tagetでのconflictはエラー処理
        //sourceで，同名だけど違う画像なら上と同じ処理
        //sourceでは，コピーする必要なし，同じ画像群だったらsameImages.jsonにsource画像をキーとする辞書を作って保存
        if(!isSource){
            return false;
        }
        return true;
    }
}
