package deeplearning.dataInfo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import deeplearning.main.ErrorChecker;
import deeplearning.main.Utilities;

public class Initializer extends JFrame{
    private static final int WINDOW_W = 300;
    private static final int WINDOW_H = 400;
    private static final int WINDOW_X = 100;
    private static final int WINDOW_Y = 100;

    private static final int BUTTON_MARGIN_W = 30;
    private static final int BUTTON_MARGIN_H = 5;


    private static final String TITLE = "Initializer";
    private static final String OK_BUTTON = "OK";
    private static final String ADD_BUTTON = "追加";
    
    private Initializer window;
    private static Object lock = new Object(); //以下の排他制御用
    public JButton okButton;

    public Initializer(DataInfo dataInfo){
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);  

        window = this;

    
        JPanel allPanel = getAllPanel();
        setContentPane(allPanel);

    }

    private JPanel getAllPanel() {
        okButton = new JButton(OK_BUTTON);

        JButton addButton = new JButton(ADD_BUTTON);
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                window.addTagRuleUnit();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
        buttonsPanel.add(Utilities.getPanelWithMargin(addButton, BUTTON_MARGIN_W, BUTTON_MARGIN_H));
        buttonsPanel.add(Utilities.getPanelWithMargin(okButton, BUTTON_MARGIN_W, BUTTON_MARGIN_H));

        JPanel innerPanel = getTagRulesPanel();

        panel.add(BorderLayout.CENTER, innerPanel);
        panel.add(BorderLayout.SOUTH, buttonsPanel);

        return panel;
    }

    private JPanel getTagRulesPanel() {
        return null;
    }

    protected void addTagRuleUnit() {
    }

    protected void setDataInfo() {
    }

    public static void init(DataInfo dataInfo) {
        Initializer window = new Initializer(dataInfo);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (window.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            ErrorChecker.errorCheck(e);
                        }
                }
            }
        };
        t.start();
        window.okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (lock) {
                    window.setDataInfo();
                    window.dispose();
                    lock.notify();
                }
            }
        });
        window.setVisible(true);

        try {
            t.join();
        } catch (InterruptedException e) {
            ErrorChecker.errorCheck(e);
        }

    }

    
}
