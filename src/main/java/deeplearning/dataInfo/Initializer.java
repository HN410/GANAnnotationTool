package deeplearning.dataInfo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
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

    private static final int TAG_LABEL_MARGIN_W = 75;
    private static final int CONTINUOUS_LABEL_MARGIN_W = 10;
    private static final int LABEL_MARGIN_H = 5;

    private static final int MAX_TAGRULEUNIT_N_IN_PANEL = 7;
    private static final int ADDITIONAL_PANEL_H = 40;
    


    private static final String TITLE = "Initializer";
    private static final String OK_BUTTON = "OK";
    private static final String ADD_BUTTON = "追加";
    private static final String EXPLAINT = "タグ名とそれが連続値かを入力してください．";
    private static final String TAG_NAME = "タグ名";
    private static final String IS_CONTINUOUS = "連続値";
    
    public JButton okButton;
    private Initializer window;
    private JPanel tagRulesPanel;
    private JScrollPane pane;
    private LinkedList<TagRuleUnit> tagRuleList;
    private static Object lock = new Object(); //以下の排他制御用
    private DataInfo dataInfo;

    public Initializer(DataInfo dataInfo){
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);  

        window = this;
        tagRuleList = new LinkedList();
        this.dataInfo = dataInfo;

    
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

        JPanel labelsPanel = getLabelsPanel();

        panel.add(BorderLayout.NORTH, labelsPanel);
        panel.add(BorderLayout.CENTER, innerPanel);
        panel.add(BorderLayout.SOUTH, buttonsPanel);

        return panel;
    }

    private JPanel getLabelsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));


        JPanel exPanel = new JPanel();
        exPanel.setLayout(new BoxLayout(exPanel, BoxLayout.X_AXIS));
        JLabel exLabel = new JLabel(EXPLAINT);
        exPanel.add(exLabel);
        // exLabel.setHorizontalAlignment(JLabel.CENTER);
        // exLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel columnsPanel = new JPanel();
        columnsPanel.setLayout(new BoxLayout(columnsPanel, BoxLayout.X_AXIS));
        
        JLabel tagLabel = new JLabel(TAG_NAME);
        JLabel continuousLabel = new JLabel(IS_CONTINUOUS);
        columnsPanel.add(Utilities.getPanelWithMargin(tagLabel, TAG_LABEL_MARGIN_W, LABEL_MARGIN_H));
        columnsPanel.add(Utilities.getPanelWithMargin(continuousLabel, CONTINUOUS_LABEL_MARGIN_W, LABEL_MARGIN_H));

        panel.add(exPanel);
        panel.add(columnsPanel);

        return panel;
    }

    private JScrollPane getTagRulesPanel() {
        pane = new JScrollPane();
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
        if(tagRuleList.size() > MAX_TAGRULEUNIT_N_IN_PANEL){
            Dimension d = tagRulesPanel.getSize();
            tagRulesPanel.setPreferredSize(new Dimension((int)d.getWidth(),
                 WINDOW_H + ADDITIONAL_PANEL_H * (tagRuleList.size() -MAX_TAGRULEUNIT_N_IN_PANEL)));
            // pane.setPreferredSize(new Dimension((int)d.getWidth(), (int)d.getHeight() + ADDITIONAL_PANEL_H));
        }
        tagRulesPanel.repaint();
        tagRulesPanel.validate();
    }

    protected void setDataInfo() {
        LinkedHashMap<String, Boolean> tagRule = new LinkedHashMap();
        for(TagRuleUnit tru: tagRuleList){
            String tagName = tru.text.getText();
            if(!tagName.equals("")){
                tagRule.put(tagName, tru.check.isSelected());
            }
        }
        dataInfo.tagRule = tagRule;
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
