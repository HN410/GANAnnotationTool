package deeplearning.panels;
import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import deeplearning.main.MainWindow;
import javafx.scene.image.Image;

//画像部分のパネル
public class ImagesPanel extends JPanel{
    private static final int IMAGE_N = 2;

    private static final String SOURCE_LABEL = "Source image";
    private static final String TARGET_LABEL = "Target image";
    private ImagePanelUnit ipu1;
    private ImagePanelUnit ipu2;

    public ImagesPanel(MainWindow mainWindow){
        super();
        setLayout(new GridLayout(1, 2));

        ipu1 = new ImagePanelUnit(mainWindow, SOURCE_LABEL, true);
        ipu2 = new ImagePanelUnit(mainWindow, TARGET_LABEL, false);
        
        add(ipu1);
        add(ipu2);

    }

    public boolean hasImages(){
        //imagePanelUnit両方に画像が割り当てられ，さらに別のペアを追加できる状況か
        //両方に画像が割り当てられないと，前インデックスに戻ることも不可能
        return ipu1.hasImage() && ipu2.hasImage();
    }

    public void imageCopyMove(){
        //画像をドラッグ元からdataフォルダにコピーもしくは移動する処理
        //コピー，移動する必要があるかはこちらで判断する
        ipu1.imageCopyMove();
        ipu2.imageCopyMove();
    }

    public String[] getImageNames(){
        //ファイル名2つ（tags.json, tags集合に使う）を返す
        //source, targetの順
        String[] images = new String[IMAGE_N];
        images[0] = ipu1.getFileName();
        images[1] = ipu2.getFileName();
        return images;
    }

    public void setImages(int ind){
        //ページ移動の時などで表示する画像を変える
        //ind ... tagsのインデックス
    }
    
}
