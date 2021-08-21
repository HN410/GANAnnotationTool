package deeplearning.main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Properties;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import deeplearning.dataInfo.DataInfo;
import deeplearning.menu.MenuBar;
import deeplearning.panels.ImagesPanel;
import deeplearning.panels.TagsPanel;
import javafx.scene.layout.Border;

public class MainWindow extends JFrame implements WindowListener{
    private static final int WINDOW_W = 750;
    private static final int WINDOW_H = 400;
    private static final int WINDOW_X = 100;
    private static final int WINDOW_Y = 100;

    private static final int MESSAGE_FONT_SIZE = 16;

    private static final String TITLE = "GANAnnotationTool";

    private JLabel messageLabel;

    public Properties properties;
    public DataInfo dataInfo;
    public MenuBar menuBar;
    public ImagesPanel imagesPanel;
    public TagsPanel tagsPanel;
    public int tagsInd;  //現在何番目のタグの操作をしているか


    public MainWindow(){
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);  

        properties = PropertiesClass.getProperties();
        dataInfo = DataInfo.getDataInfo(properties.getProperty(PropertiesClass.CONFIG_PATH), this);
        tagsInd = dataInfo.tags.size();
        

        JPanel allPanel = getAllPanel();
        setContentPane(allPanel);

        menuBar = new MenuBar(this);
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(this);
    }
        

    public JPanel getAllPanel(){
        //メイン画面の作成
        JPanel allPanel = new JPanel();
        allPanel.setLayout(new BorderLayout());

        JPanel messagePanel = getMessagePanel();
        allPanel.add(BorderLayout.SOUTH, messagePanel);

        JPanel interactivePanel = new JPanel();
        interactivePanel.setLayout(new BoxLayout(interactivePanel, BoxLayout.X_AXIS));
        
        imagesPanel = new ImagesPanel(this);
        interactivePanel.add(imagesPanel);

        tagsPanel = new TagsPanel(this);
        tagsPanel.setIndexLabel(tagsInd);
        interactivePanel.add(tagsPanel);

        allPanel.add(BorderLayout.CENTER, interactivePanel);
                
        return allPanel;
    }

    public JPanel getMessagePanel(){
        //メッセージ表示部の作成
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        messageLabel = new JLabel("test");
        messageLabel.setFont(new Font("ＭＳ ゴシック", Font.PLAIN, MESSAGE_FONT_SIZE));

        panel.add(sep);
        panel.add(messageLabel);
        return panel;
    }

    public void setMessage(String text){
        // messageLabelにメッセージを表示できる
        messageLabel.setText(text);
    }

    public void dataSave(){
        //現在の進行をファイルに保存
        PropertiesClass.setProperties(properties);
        tagsPanel.tagChangeIfNecessary();
        imagesPanel.imageCopyMove();
        dataInfo.save();
        dataInfo.tagsSave();
        dataInfo.sameImagesSave();
    }

    @Override
    public void windowOpened(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowClosing(WindowEvent e) {
        dataSave();
        dispose();
        System.exit(0);
        
    }

    @Override
    public void windowClosed(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowIconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowActivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        // TODO Auto-generated method stub
        
    }
    
       
    
}
