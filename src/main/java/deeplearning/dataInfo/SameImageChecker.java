package deeplearning.dataInfo;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import deeplearning.main.Configs;
import deeplearning.main.ErrorChecker;
import deeplearning.main.MainWindow;
import deeplearning.panels.ImagePanelUnit;

public class SameImageChecker extends JDialog{
    private static final int WINDOW_W = 400;
    private static final int WINDOW_H = 400;
    private static final int WINDOW_X = 200;
    private static final int WINDOW_Y = 200;

    private static final int IMAGE_N_W = 2;
    private static final int IMAGE_N_H = 1;

    private static final int BUTTON_W = 20;
    private static final int BUTTON_H = 20;

    private static final int LABEL_M_W = 10;
    private static final int LABEL_M_H = 10;

    private static final String TITLE = "同一画像の検出";
    private static final String CONFIRM_TEXT1 = "同一ファイル名を検出しました．同じ画像として扱いますか?";
    private static final String CONFIRM_TEXT2 = "（違う画像として扱う場合，ファイル名を変更します．）";
    private static final String YES_BUTTON = "はい";
    private static final String NO_BUTTON = "いいえ";
    private static final String DROPPED_LABEL = "現在受けとったファイル";
    private static final String SAVED_LABEL = "すでにある同名ファイル";

    private static Object lock = new Object(); //以下の排他制御用
    private static boolean isSame; // 応答をここで受け取る


    public JButton yesButton;
    public JButton noButton;

    public SameImageChecker(String[] files, MainWindow mainWindow){
        //filesは今ドロップされたファイル，すでにあったファイルの順
        super(mainWindow, Dialog.DEFAULT_MODALITY_TYPE);
        setTitle(TITLE);
        setResizable(false);
        setDefaultCloseOperation(0);
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
        JPanel panel = new JPanel(new GridLayout(IMAGE_N_H, IMAGE_N_W));
        panel.add(getImagePanelUnit(files[0], DROPPED_LABEL));
        panel.add(getImagePanelUnit(files[1], SAVED_LABEL));
        
        return panel;
    }

    private JPanel getImagePanelUnit(String file, String labelText){
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.Y_AXIS));

        JLabel imageLabel = getImageLabel(file);
        imagePanel.add(imageLabel);

        JLabel textLabel = new JLabel(labelText);
        textLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(BorderLayout.SOUTH, getPanelWithMargin(textLabel, 0, BUTTON_H));
        panel.add(BorderLayout.CENTER, imagePanel);

        return panel;
    }

    private JLabel getImageLabel(String fileName) {
        JLabel label = new JLabel();
        label.setMinimumSize(new Dimension(Configs.IMAGE_SIZE, Configs.IMAGE_SIZE));
        label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        label.setIcon(ImagePanelUnit.getResizedImageIcon(fileName, this));
        return label;
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

    public static boolean isSame(String[] files, MainWindow mainWindow){
        //二つのファイルを受け取り，それが同じかをユーザに判断してもらう
        SameImageChecker dialog = new SameImageChecker(files, mainWindow);

        Thread t = new Thread() {
            public void run() {
                synchronized(lock) {
                    while (dialog.isVisible())
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            ErrorChecker.errorCheck(e);
                        }
                }
            }
        };
        t.start();
        dialog.yesButton.addActionListener(getActionListener(true, dialog));
        dialog.noButton.addActionListener(getActionListener(false, dialog));
        
        dialog.setVisible(true);

        try {
            t.join();
        } catch (InterruptedException e) {
            ErrorChecker.errorCheck(e);
        }
        return SameImageChecker.isSame;

    }

    private static ActionListener getActionListener(boolean b, SameImageChecker frame) {
        //何かアクションが起きたら isSame にbを書き込み，lockにnotifyする
        ActionListener al = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (lock) {
                    isSame = b;
                    frame.dispose();
                    lock.notify();
                }
            }
        };
        return al;
    }
    
}

