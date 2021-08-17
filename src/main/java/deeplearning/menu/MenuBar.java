package deeplearning.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import deeplearning.main.MainWindow;
import deeplearning.main.PropertiesClass;

public class MenuBar extends JMenuBar{
    private static final String FILE_MENU_NAME = "ファイル";
    private static final String OPTION_MENU_NAME = "オプション";
    private static final String ORIGIN_REMOVE_CHECK = "元画像を削除";
    
    private MainWindow mainwWindow;

    public JCheckBoxMenuItem originRemoveCheck;
    public MenuBar(MainWindow mainWindow){
        super();
        this.mainwWindow = mainWindow;

        JMenu fileMenu = new JMenu(FILE_MENU_NAME);
        JMenu optionMenu = new JMenu(OPTION_MENU_NAME);
        this.add(fileMenu);
        this.add(optionMenu);

        originRemoveCheck = new JCheckBoxMenuItem(ORIGIN_REMOVE_CHECK);
        initOriginRemoveCheck();
        optionMenu.add(originRemoveCheck);        
    }
    private void initOriginRemoveCheck() {
        boolean value = mainwWindow.properties.getProperty(PropertiesClass.IMAGE_REMOVE)
            .equals(PropertiesClass.TRUE);
        originRemoveCheck.setSelected(value);
        originRemoveCheck.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = originRemoveCheck.isSelected() ? PropertiesClass.TRUE : PropertiesClass.FALSE;
                mainwWindow.properties.setProperty(PropertiesClass.IMAGE_REMOVE, value);
            }
        });
    }
    
}
