package deeplearning.panels;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.awt.BorderLayout;
import java.awt.Component;

import deeplearning.main.MainWindow;

public class TagsPanel extends JPanel{
    private static final String NEXT_BUTTON_TEXT = "→";
    private static final String PREV_BUTTON_TEXT = "←";
    private static final int PANEL_W = 200;
    private static final int PANEL_H = 300;
    private static final int LABEL_MARGIN_W = 10;

    private JButton nextButton;
    private JButton previousButton;
    private JLabel indexLabel;
    private MainWindow mainWindow;
    private LinkedList<LabelUnit> labelUnits;
    public TagsPanel(MainWindow mainWindow){
        super();
        this.mainWindow = mainWindow;
        this.labelUnits = new LinkedList<>();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        
        JScrollPane scroll = getTagsScrollPane();
        add(BorderLayout.CENTER, scroll);
        
        JPanel buttonsPanel = getButtonsPanel();
        add(BorderLayout.SOUTH, buttonsPanel);
    }


    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        nextButton = new JButton(NEXT_BUTTON_TEXT);
        previousButton = new JButton(PREV_BUTTON_TEXT);

        indexLabel= new JLabel("");
        indexLabel.setHorizontalAlignment(JLabel.CENTER);

        panel.add(BorderLayout.EAST, nextButton);
        panel.add(BorderLayout.WEST, previousButton);
        panel.add(BorderLayout.CENTER, indexLabel);

        return panel;
    }


    private JScrollPane getTagsScrollPane() {
        JScrollPane pane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                         JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        pane.setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        Box verticalBox = Box.createVerticalBox();
        pane.setViewportView(verticalBox);

        addLabelUnits(verticalBox);
        return pane;
    }


    private void addLabelUnits(Box verticalBox) {
        LinkedHashMap<String, Boolean> tagRule = mainWindow.dataInfo.tagRule;
        for(String tag: tagRule.keySet()){
            LabelUnit lu = new LabelUnit(tag, tagRule.get(tag));
            verticalBox.add(lu);
            labelUnits.add(lu);
        }
    }

    public void setIndexLabel(int i){
        indexLabel.setText("" + i);
    }
    
}
