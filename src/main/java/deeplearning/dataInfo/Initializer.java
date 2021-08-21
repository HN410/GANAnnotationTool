package deeplearning.dataInfo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

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
    
    public JButton okButton;
    private Initializer window;
    private JPanel tagRulesPanel;
    private LinkedList<TagRuleUnit> tagRuleList;
    private static Object lock = new Object(); //以下の排他制御用

    public Initializer(DataInfo dataInfo){
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);  

        window = this;
        tagRuleList = new LinkedList();

    
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

        JScrollPane innerPanel = getTagRulesPanel();

        panel.add(BorderLayout.CENTER, innerPanel);
        panel.add(BorderLayout.SOUTH, buttonsPanel);

        return panel;
    }

    private JScrollPane getTagRulesPanel() {
        JScrollPane pane = new JScrollPane();
        tagRulesPanel = new JPanel();
        tagRulesPanel.setLayout(new BoxLayout(tagRulesPanel, BoxLayout.Y_AXIS));
        pane.setViewportView(tagRulesPanel);
        addTagRuleUnit();


        return pane;
    }

    protected void addTagRuleUnit() {
        TagRuleUnit tru = new TagRuleUnit();
        tagRuleList.add(tru);
        tagRulesPanel.add(tru);
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
        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            ErrorChecker.errorCheck(e);
        }

    }

    
}
