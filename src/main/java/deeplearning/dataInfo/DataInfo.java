package deeplearning.dataInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;

import deeplearning.main.ErrorChecker;

public class DataInfo implements Serializable{
    public static String DATA_FILE_NAME = "config.data";


    public LinkedHashMap<String, Boolean> tagRule;
    public boolean isPair;
    public boolean isSourceZero;
    public HashSet<String> images;

    private String filePath;

    private DataInfo(String filePath){
        this.filePath = filePath;
        tagRule = new LinkedHashMap<>();
        isPair = true;
        isSourceZero = true;
        images = new HashSet<>();
        DataInitializer.init(this);
    }

    public static DataInfo getDataInfo(String folderPath){
        // config.dataがあればそれを読み込み，そのデータセットの情報を読み込む
        Path folder = Paths.get(folderPath);
        Path filePath = folder.resolve(DATA_FILE_NAME);
        String fileName = filePath.toString();
        File file = new File(fileName);
        if(!file.exists()){
            return new DataInfo(fileName);
        }

        ObjectInputStream ois;
        DataInfo dataInfo = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            dataInfo = (DataInfo) ois.readObject();
        } catch (FileNotFoundException e) {
            ErrorChecker.errorCheck(e);
        } catch (IOException e) {
            ErrorChecker.errorCheck(e);
        } catch (ClassNotFoundException e) {
            ErrorChecker.errorCheck(e);
        }
        return dataInfo;
    }
    
    
    public void save() {
        //ファイルに保存
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
            oos.writeObject(this);
            oos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            ErrorChecker.errorCheck(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ErrorChecker.errorCheck(e);
        }
    }

}
