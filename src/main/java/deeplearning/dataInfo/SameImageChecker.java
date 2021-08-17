package deeplearning.dataInfo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SameImageChecker extends JFrame{
    private static final int WINDOW_W = 400;
    private static final int WINDOW_H = 400;
    private static final int WINDOW_X = 200;
    private static final int WINDOW_Y = 200;

    private static final int BUTTON_W = 20;
    private static final int BUTTON_H = 20;

    private static final int LABEL_M_W = 10;
    private static final int LABEL_M_H = 10;

    private static final String TITLE = "同一画像の検出";
    private static final String CONFIRM_TEXT1 = 
        "同一ファイル名を検出しました．同じ画像として扱いますか?";
    private static final String CONFIRM_TEXT2 = "（違う画像として扱う場合，ファイル名を変更します．）";
    private static final String YES_BUTTON = "はい";
    private static final String NO_BUTTON = "いいえ";


    private JButton yesButton;
    private JButton noButton;

    public SameImageChecker(String[] files){
        //filesは今ドロップされたファイル，すでにあったファイルの順
        super();
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(WINDOW_X, WINDOW_Y, WINDOW_W, WINDOW_H);  

        JPanel allPanel = new JPanel(new BorderLayout());

        JPanel buttonsPanel = getButtonsPanel();
        JPanel imagesPanel = getImagesPanel(files);

        JPanel labelsPanel = getLabelsPanel();

        allPanel.add(BorderLayout.CENTER, imagesPanel);
        allPanel.add(BorderLayout.SOUTH, buttonsPanel);
        allPanel.add(BorderLayout.NORTH, labelsPanel);

        setContentPane(allPanel);
    }

    private JPanel getLabelsPanel() {
        JLabel confirmLabel1 = new JLabel(CONFIRM_TEXT1);
        JLabel confirmLabel2 = new JLabel(CONFIRM_TEXT2);
        confirmLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        confirmLabel2.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        innerPanel.add(confirmLabel1);
        innerPanel.add(confirmLabel2);
        
        return getPanelWithMargin(innerPanel, LABEL_M_W, LABEL_M_H);
    }

    private JPanel getImagesPanel(String[] files) {
        JPanel panel = new JPanel();
        

        return panel;
    }

    private JPanel getButtonsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        
        yesButton = new JButton(YES_BUTTON);
        noButton = new JButton(NO_BUTTON);
        yesButton.setPreferredSize(new Dimension(BUTTON_W, BUTTON_H));
        noButton.setPreferredSize(new Dimension(BUTTON_W, BUTTON_H));

        panel.add(getPanelWithMargin(yesButton, BUTTON_W, BUTTON_H/2));
        panel.add(getPanelWithMargin(noButton, BUTTON_W, BUTTON_H/2));
        return panel;
    }

    private JPanel getPanelWithMargin(JComponent comp, int marginW, int marginH){
        JPanel panel = new JPanel(new BorderLayout());

        panel.add(BorderLayout.NORTH, Box.createVerticalStrut(marginH));
        panel.add(BorderLayout.SOUTH, Box.createVerticalStrut(marginH));
        panel.add(BorderLayout.WEST, Box.createHorizontalStrut(marginW));
        panel.add(BorderLayout.EAST, Box.createHorizontalStrut(marginW));
        panel.add(BorderLayout.CENTER, comp);

        return panel;
    }
    
}

