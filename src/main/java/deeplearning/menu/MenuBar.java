package deeplearning.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import deeplearning.main.MainWindow;
import deeplearning.main.PropertiesClass;

public class MenuBar extends JMenuBar{
    private static final String FILE_MENU_NAME = "ファイル";
    private static final String OPTION_MENU_NAME = "オプション";
    private static final String ORIGIN_REMOVE_CHECK = "元画像を削除";
    private static final String SAVE = "保存";
    private static final String OPEN = "開く";

    private static final String SAVE_COMPLETED = "保存に成功しました";
    private static final String FOLDER_CHOICE_TITLE = "データのあるフォルダ，あるいはデータを作るフォルダを選択してください．";
    
    private MainWindow mainWindow;

    private JMenuItem saveMenu;
    private JMenuItem openMenu;
    public JCheckBoxMenuItem originRemoveCheck;
    
    public MenuBar(MainWindow mainWindow){
        super();
        this.mainWindow = mainWindow;

        JMenu fileMenu = new JMenu(FILE_MENU_NAME);
        JMenu optionMenu = new JMenu(OPTION_MENU_NAME);
        this.add(fileMenu);
        this.add(optionMenu);

        originRemoveCheck = new JCheckBoxMenuItem(ORIGIN_REMOVE_CHECK);
        initOriginRemoveCheck();
        
        saveMenu = getSaveMenu();
        openMenu = getOpenMenu();

        fileMenu.add(openMenu);
        fileMenu.add(saveMenu);
        optionMenu.add(originRemoveCheck);        

    }

    private JMenuItem getOpenMenu() {
        JMenuItem item = new JMenuItem(OPEN);
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                fileChooser.setDialogTitle(FOLDER_CHOICE_TITLE);
                if(fileChooser.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION){
                    mainWindow.dataSave();
                    mainWindow.properties.setProperty(PropertiesClass.CONFIG_PATH,
                     fileChooser.getSelectedFile().getAbsolutePath());
                    
                    Thread t = new Thread() {
                        public void run() {
                            mainWindow.reset();
                        }
                    };
                    t.start();
                }
            }
        });
        return item;
    }

    private JMenuItem getSaveMenu() {
        JMenuItem item = new JMenuItem(SAVE);
        item.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                mainWindow.dataSave();
                mainWindow.setMessage(SAVE_COMPLETED);
            }
        });
        return item;
    }


    private void initOriginRemoveCheck() {
        boolean value = mainWindow.properties.getProperty(PropertiesClass.IMAGE_REMOVE)
            .equals(PropertiesClass.TRUE);
        originRemoveCheck.setSelected(value);
        originRemoveCheck.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = originRemoveCheck.isSelected() ? PropertiesClass.TRUE : PropertiesClass.FALSE;
                mainWindow.properties.setProperty(PropertiesClass.IMAGE_REMOVE, value);
            }
        });
    }
    
}
