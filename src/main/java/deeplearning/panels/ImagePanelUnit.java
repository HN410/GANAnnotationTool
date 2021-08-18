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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.MediaTracker;
import java.awt.Image;
import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import deeplearning.dataInfo.DataInfo;
import deeplearning.dataInfo.SameImageChecker;
import deeplearning.main.ErrorChecker;
import deeplearning.main.MainWindow;

//画像一枚分のJPanel
public class ImagePanelUnit extends JPanel{
    private static final String SAME_TARGET_IMAGE_ERROR = "同じtarget画像を登録することはできません．";
    private static final String ANOTHER_PATH_LAST = "_0";
    private static final Pattern REGEX_PATTERN = Pattern.compile("(\\A.*_)(\\d*)\\z");
    private static final int SAME_IMAGE_SET_N_LIMIT = 2;
    private static final int REGEX_FIRST_GROUP = 1;
    private static final int REGEX_SECOND_GROUP = 2;
    private static final int IMAGE_SIZE = 200;
    private static final int LABEL_MARGIN_W = 10;
    private static final int COMPARE_IMAGES_N = 2;

    private boolean hasSameImage = false; //現在表示中の画像は使いまわしか
    private boolean isSource;//ソースの方の画面か
    private String beforeImageFile = null; //現在のインデックスでtagsに登録されているパスを保持する
    private String imageDatafile = null; //現在表示されている画像のdataフォルダに入っているほうのパス これがあれば画像がある
    private String imageOriginFile = null; //現在表示されている画像のドラッグ元のパス すでにdataフォルダないにある画像を使う場合はnull
    private String fileName = null;

    private MainWindow mainWindow;
    private JLabel imageLabel;
    private DropFileHandler dropFileHandler;
    private JPanel imagePanel;

    public ImagePanelUnit(MainWindow mainWindow, String labelText, Boolean isSource){
        this.isSource = isSource;
        this.mainWindow = mainWindow;
        this.dropFileHandler = new DropFileHandler(this, mainWindow);
        setTransferHandler(dropFileHandler);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE + LABEL_MARGIN_W*2));

        imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        imageLabel = getImageLabel();
        imagePanel.add(imageLabel);

        JPanel label = getLabelPanel(labelText);

        add(BorderLayout.SOUTH, label);
        add(BorderLayout.CENTER, imagePanel);

    }

    public boolean hasImage(){
        //画像が表示されているか
        return !(imageDatafile == null);
    }

    public boolean hasChanged(){
        //画像が変わったか
        if(beforeImageFile == null){
            return imageDatafile != null;
        }
        return !beforeImageFile.equals(imageDatafile);
    }

    
    public void imageDropped(File file){
        //画像がドロップされたら呼び出される
        hasSameImage = false;
        
        DataInfo dataInfo = mainWindow.dataInfo;
        if(isSource){
            inImageDropped(dataInfo.sourceImages, dataInfo.sourcePath, file);
        }else{
            inImageDropped(dataInfo.targetImages, dataInfo.targetPath, file);
        }
    }
    
    public String getFileName(){
        //現在表示中の画像ファイルのファイル名だけを返す
        return fileName;
    }

    public void imageCopyMove(){
        //画像の移動，コピー処理
        //同時にsourceImages, targetImages, sameImagesの削除処理も行う
        //mainWindow.tagsIndを使うので変更しないこと
        if(hasChanged()){
            if(beforeImageFile != null){
                // 前のファイル，sourceImages内のファイル名の削除
                // かぶったイメージを使っている場合，他にこのイメージを使っている場合があるので，それを確認
                // かぶっていた場合，sameImagesから現在インデックスを削除し，その集合の要素数が1になった場合は，
                // そのキーごと削除を行う
                DataInfo dataInfo = mainWindow.dataInfo;
                String beforeFileName = (new File(beforeImageFile)).getName();
                boolean delete = true;
                if(isSource){
                    if(dataInfo.sameImages.keySet().contains(beforeFileName)){
                        //同じファイルが使われている
                        //この時はsourceImages内のファイル名およびファイルは削除しない
                        HashSet<Integer> indexSet = dataInfo.sameImages.get(beforeFileName);
                        if(indexSet.size() == SAME_IMAGE_SET_N_LIMIT){
                            // 要素数が1になるのでもう不要
                            dataInfo.sameImages.remove(beforeFileName);
                        }else{
                            // まだ要素数が十分にある
                            // indexの集合から現在インデックスを削除
                            indexSet.remove(mainWindow.tagsInd);
                        }
                        delete = false;
                    }else{
                        //同じファイル名はなし
                        dataInfo.sourceImages.remove(beforeFileName);
                    }

                }else{
                    dataInfo.targetImages.remove(beforeFileName);
                }
                if(delete){
                    try {
                        Files.delete(Paths.get(beforeImageFile));
                    } catch (IOException e) {
                        ErrorChecker.errorCheck(e);
                    }
                }
            }
            if(imageOriginFile != null){
                Path before = Paths.get(imageOriginFile);
                Path after = Paths.get(imageDatafile);
                //ファイル移動
                try {
                    if(mainWindow.menuBar.originRemoveCheck.isSelected()){
                        //移動
                        Files.move(before, after);
                    }else{
                        //コピー
                        Files.copy(before, after);
                    }
                } catch (IOException e) {
                    ErrorChecker.errorCheck(e);
                }
                beforeImageFile = imageDatafile;
            }
        }
    }

    public void imageClear(){
        beforeImageFile = null;
        imageDatafile = null;
        imageOriginFile = null;
        fileName = null;
        imageLabel.setIcon(null);        
    }

    public void setImageFile(String filePath){
        //ページ移動などで表示画像を変えるとき
        beforeImageFile = filePath;
        imageDatafile = filePath;
        imageOriginFile = null;
        File imageFile = new File(filePath);
        fileName = imageFile.getName();
        setImage(filePath);
    }
    
    private void setImage(String filePath) {
        ImageIcon icon = getResizedImageIcon(filePath, this);
        imageLabel.setIcon(icon);
        imagePanel.repaint();
        imagePanel.revalidate();
    }

    private JLabel getImageLabel() {
        JLabel label = new JLabel();
        label.setMinimumSize(new Dimension(IMAGE_SIZE, IMAGE_SIZE));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setTransferHandler(dropFileHandler);
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
 
    
    public static ImageIcon getResizedImageIcon(String imagePath, Component comp){
        //リサイズしたimageIconを返す
        MediaTracker tracker = new MediaTracker(comp); 

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


    private void inImageDropped(HashSet<String> images, String folderName, File imageFile){
        fileName = imageFile.getName();
        Path folderPath = Paths.get(folderName);
        Path imagePath = folderPath.resolve(fileName);
        if(images.contains(fileName)){
            //同名ファイルあり
            conflictImageName(imageFile, imagePath.toFile(), fileName, images);
            return;
        }
        //コピーはタグ付け確定時

        this.imageDatafile = imagePath.toString();
        this.imageOriginFile = imageFile.getPath();
        setImage(imageFile.getPath());

    }

    private void conflictImageName(File newImage, File oldImage, String fileName, HashSet<String> images){
        //ドロップされたものと同じファイル名のものが存在したとき
        //tagetでのconflictはエラー処理
        //sourceで，同名だけど違う画像なら名前を変えて同じ処理
        //sourceでは，コピーする必要なし，同じ画像群だったらsameImages.jsonにsource画像をキーとする辞書を作って保存
        if(!isSource){
            mainWindow.setMessage(SAME_TARGET_IMAGE_ERROR);
            return;
        }
        String imageDestination = null; //ファイルのコピー先
        String[] files = new String[COMPARE_IMAGES_N];
        files[0] = newImage.getAbsolutePath();
        files[1] = oldImage.getAbsolutePath();
        while(true){
            if(SameImageChecker.isSame(files, mainWindow)){
                //同じ画像を使いまわす
                hasSameImage = true;
                this.fileName = fileName;
                this.imageDatafile = files[1];
                this.imageOriginFile = null;
                setImage(files[1]);
                return;
            }else{
                //違う画像
                imageDestination = getAnotherFileName(oldImage.getAbsolutePath());
                fileName = (new File(imageDestination)).getName();
                if(images.contains(fileName)){
                    //名前を変えてもまた同じファイル名があったらまたループ
                    Path folderPath = Paths.get(mainWindow.dataInfo.sourcePath);
                    files[1] = folderPath.resolve(fileName).toString();

                }else{
                    //同じファイルがなければループ脱出
                    break;
                }
            }
        }
        this.fileName = fileName;
        this.imageDatafile = imageDestination;
        this.imageOriginFile = newImage.getPath();
        setImage(newImage.getPath());
    }

    public static String getAnotherFileName(String absolutePath) {
        int extInd = absolutePath.lastIndexOf(".");
        String ext = absolutePath.substring(extInd);
        String newPath = absolutePath.substring(0, extInd);
        
        Matcher m = REGEX_PATTERN.matcher(newPath);
        if(m.find()){
            int i = Integer.parseInt(m.group(REGEX_SECOND_GROUP)) + 1;
            newPath = m.group(REGEX_FIRST_GROUP) + i;
        }else{
            newPath += ANOTHER_PATH_LAST;
        }
        newPath +=  ext;
        return newPath;
    }

    public boolean hasSameImage() {
        return hasSameImage;
    }

    
}
