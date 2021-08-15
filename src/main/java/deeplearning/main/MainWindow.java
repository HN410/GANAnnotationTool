package deeplearning.main;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import deeplearning.panels.ImagesPanel;
import javafx.scene.layout.Border;

public class MainWindow extends JFrame {
    private static int WINDOW_W = 800;
    private static int WINDOW_H = 340;
    private static int WINDOW_X = 100;
    private static int WINDOW_Y = 100;

    private static int MESSAGE_FONT_SIZE = 16;

    private static String TITLE = "GANAnnotationTool";

    private JLabel messageLabel;

    public MainWindow(){
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);  
        
        JPanel allPanel = getAllPanel();

        setContentPane(allPanel);
        

    }

    public JPanel getAllPanel(){
        //メイン画面の作成
        JPanel allPanel = new JPanel();
        allPanel.setLayout(new BorderLayout());

        JPanel messagePanel = getMessagePanel();
        allPanel.add(BorderLayout.SOUTH, messagePanel);

        JPanel interactivePanel = new JPanel();
        interactivePanel.setLayout(new BoxLayout(interactivePanel, BoxLayout.X_AXIS));
        
        ImagesPanel imagesPanel = new ImagesPanel(this);
        interactivePanel.add(imagesPanel);

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
    
    
    
    
}
