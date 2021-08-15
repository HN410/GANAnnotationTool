package deeplearning.panels;
import java.awt.EventQueue;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

import javax.swing.TransferHandler;

import deeplearning.main.MainWindow;


public class DropFileHandler extends TransferHandler{
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
                    return false;
                }


			} catch (UnsupportedFlavorException | IOException e) {
				e.printStackTrace();
			}
			return true;
		}

}
