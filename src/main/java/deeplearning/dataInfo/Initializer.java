package deeplearning.dataInfo;

import javax.swing.JFrame;

public class Initializer extends JFrame{
    private static final int WINDOW_W = 300;
    private static final int WINDOW_H = 400;
    private static final int WINDOW_X = 100;
    private static final int WINDOW_Y = 100;


    private static final String TITLE = "Initializer";

    public Initializer(DataInfo dataInfo){
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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

    
}
