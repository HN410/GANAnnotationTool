package deeplearning.dataInfo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;


import deeplearning.main.ErrorChecker;

public class DataInfo implements Serializable{
    public static String DATA_FILE_NAME = "config.data";


    public HashMap<String, Boolean> tagRule;
    public boolean isPair;
    public boolean isSourceZero;
    public HashSet<String> images;

    public DataInfo(String folderPath){
        // config.dataがあればそれを読み込み，そのデータセットの情報を読み込む
        Path folder = Paths.get(folderPath);
        Path file = folder.resolve(DATA_FILE_NAME);
        File 

    }
    
}
