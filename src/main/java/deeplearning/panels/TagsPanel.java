package deeplearning.panels;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.BorderLayout;

import deeplearning.main.MainWindow;

public class TagsPanel extends JPanel{
    private static final String NEXT_BUTTON_TEXT = "→";
    private static final String PREV_BUTTON_TEXT = "←";
    private static final int PANEL_W = 200;
    private static final int PANEL_H = 300;
    private static final int LABEL_MARGIN_W = 10;

    private JButton nextButton;
    private JButton previousButton;
    public TagsPanel(MainWindow mainWindow){
        super();
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

        panel.add(BorderLayout.EAST, nextButton);
        panel.add(BorderLayout.WEST, previousButton);

        return panel;
    }


    private JScrollPane getTagsScrollPane() {
        JScrollPane pane = new JScrollPane();
        return pane;
    }
    
}
