package deeplearning.main;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainWindow extends JFrame {
    public static int WINDOW_W = 800;
    public static int WINDOW_H = 600;
    public static int WINDOW_X = 100;
    public static int WINDOW_Y = 100;
    public static String TITLE = "GANAnnotationTool";

    public MainWindow(){
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);  
        
        JPanel allPanel = new JPanel();
        JLabel test = new JLabel("test");
        allPanel.add(test);
        setContentPane(allPanel);
        

    }
    
    
    
    
}
