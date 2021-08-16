package deeplearning.dataInfo;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;


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
import java.util.LinkedList;

import deeplearning.main.ErrorChecker;

public class DataInfo implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE_NAME = "config.data";
    private static final String SOURCE_FOLDER = "source\\";
    private static final String TARGET_FOLDER = "target\\";
    private static final String TAGS_FILE_NAME = "tags.json";


    public LinkedHashMap<String, Boolean> tagRule; //タグ規則 タグ名と連続値かのboolean
    public boolean isPair; //データがsourceとtargetのペアか
    public boolean isSourceZero; //sourceのラベルがすべて0か
    public HashSet<String> sourceImages; //sourceフォルダに入っている画像名
    public HashSet<String> targetImages; 
    public transient LinkedList<HashMap<String, Float[]>> tags;

    public String folderPath;
    public String sourcePath;
    public String targetPath;
    public String tagsPath;
    public String configFilePath;

    private DataInfo(String filePath, String folderPath){
        this.configFilePath = filePath;
        setFilePath(folderPath);
        tagRule = new LinkedHashMap<>();
        isPair = true;
        isSourceZero = true;
        sourceImages = new HashSet<>();
        targetImages = new HashSet<>();
        DataInitializer.init(this);
        getTags(folderPath);
    }

    private void setFilePath(String folderPath) {
        //パス系のメンバを設定する
        this.folderPath = folderPath;
        Path folder = Paths.get(folderPath);
        Path filePathS = folder.resolve(SOURCE_FOLDER);
        Path filePathT = folder.resolve(TARGET_FOLDER);
        Path filePathTag = folder.resolve(TAGS_FILE_NAME);
        this.sourcePath = filePathS.toString();
        this.targetPath = filePathT.toString();
        this.tagsPath = filePathTag.toString();
        
    }

    public static DataInfo getDataInfo(String folderPath){
        // config.dataがあればそれを読み込み，そのデータセットの情報を読み込む
        Path folder = Paths.get(folderPath);
        Path filePath = folder.resolve(DATA_FILE_NAME);
        String fileName = filePath.toString();
        File file = new File(fileName);
        if(!file.exists()){
            return new DataInfo(fileName, folderPath);
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
        dataInfo.getTags(folderPath);
        return dataInfo;
    }
    
    
    private void getTags(String folderName) {
        //tagsを読み込みor作成
        Path folder = Paths.get(folderPath);
        Path tagFilePath = folder.resolve(TAGS_FILE_NAME);
        if(!tagFilePath.toFile().exists()){
            tags = new LinkedList<>();
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            tags = mapper.readValue(tagFilePath.toFile(), new TypeReference<LinkedList<HashMap<String, Float[]>>>(){});
        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            ErrorChecker.errorCheck(e);
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            ErrorChecker.errorCheck(e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            ErrorChecker.errorCheck(e);
        }
    }

    public void save() {
        //ファイルに保存
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(configFilePath));
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
