package deeplearning.panels;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.TransferHandler;

import deeplearning.main.MainWindow;


public class DropFileHandler extends TransferHandler{
    private static final String MULTI_FILE_ERROR = "画像は一枚だけドロップしてください";
    private static final String NOT_IMAGE_ERROR = "適切な拡張子のものをドロップしてください";

    private static final String[] VALID_EXTS = {".jpg", ".png", ".gif"};


    private ImagePanelUnit ipu;
    private MainWindow mainWindow;

    public DropFileHandler(ImagePanelUnit ipu, MainWindow mainWindow){
        super();
        this.ipu = ipu;
        this.mainWindow = mainWindow;
    }

    @Override
    public boolean canImport(TransferSupport support){
        //ドロップされたものが受け取れるものか
        if (!support.isDrop()){
            return false;
        }
        if (!support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            // ドロップされたのがファイルでない場合は受け取らない
            return false;
        }
        return true;
    }

    @Override
		public boolean importData(TransferSupport support) {
			// 受け取っていいものか確認する
			if (!canImport(support)) {
		        return false;
		    }
			// ドロップ処理
			Transferable t = support.getTransferable();
			try {
				// ファイルを受け取る
				List files = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
                if(files.size() != 1){
                    //一枚の時のみ受け取る
                    mainWindow.setMessage(MULTI_FILE_ERROR);
                    return false;
                }
                File file = (File) files.get(0);
                if(!isImage(file)){
                    mainWindow.setMessage(NOT_IMAGE_ERROR);
                    return false;
                }
                ipu.imageDropped(file);


			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
			return true;
		}
    
        private boolean isImage(File file){
            //ファイルの拡張子が適切な画像のものか
            String name = file.getName();
            String ext = name.substring(name.lastIndexOf('.'));
            for(String e : VALID_EXTS){
                if(ext.equals(e)){
                    return true;
                }
            }
            return false;
        }

}
