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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import deeplearning.dataInfo.DataInfo;
import deeplearning.main.Configs;
import deeplearning.main.ErrorChecker;
import deeplearning.main.MainWindow;

public class TagsPanel extends JPanel implements ActionListener{
    private static final String NO_IMAGE_SET_MESSAGE = "画像が２つ登録されていないのでページ移動できません";
    private static final String MINUS_INDEX_MESSAGE = "これ以上戻れません";
    private static final String NEXT_BUTTON_TEXT = "→";
    private static final String PREV_BUTTON_TEXT = "←";
    private static final String IN_PROGRESS = "工事中";
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
        nextButton.addActionListener(this);
        previousButton = new JButton(PREV_BUTTON_TEXT);
        previousButton.addActionListener(this);

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


    @Override
    public void actionPerformed(ActionEvent e) {
        int plusInd = -1;
        if(((JButton)e.getSource()).equals(nextButton)){
            plusInd = 1;
        }
        plusIndex(plusInd);
        
    }


    private void plusIndex(int plusInd) {
        //tagsのインデックスを増減させ，表示しているものを変える
        ImagesPanel imagesPanel = mainWindow.imagesPanel;
        int nextIndex = mainWindow.tagsInd + plusInd;
        if(nextIndex < 0){
            //負のインデックス
            mainWindow.setMessage(MINUS_INDEX_MESSAGE);
            return;
        }
        if(!imagesPanel.hasImages()){
            //画像がセットされてないので移動できない
            mainWindow.setMessage(NO_IMAGE_SET_MESSAGE);
            return;
        }
        if(imagesPanel.hasChanged() || labelChanged()){
            //何かしら変更があった
            addTags();
            addTagsImages();
        }
        if(nextIndex == mainWindow.dataInfo.tags.size()){
            //新規追加
            labelZeroReset();
            setChanged(true);
            imagesPanel.imageClear();
        }else{
            //既存のものの読み込み
            labelLoad(nextIndex);
            setChanged(false);
            imagesPanel.setImages(nextIndex);
        }
        setIndexLabel(nextIndex);
        mainWindow.tagsInd = nextIndex;
    }


    private void labelLoad(int nextIndex) {
        //tagsから既存のlabelを読み込む
        if(!mainWindow.dataInfo.isSourceZero){
            //ソース画像にもラベルを付けていたとき
            ErrorChecker.errorCheck(IN_PROGRESS);
            return;
        }
        boolean flag = false;
        for(Float[] tagValues: mainWindow.dataInfo.tags.get(nextIndex).values()){
            //LinkedHashMapで二番目に来るもの(Targetのラベル)のみ操作
            if(flag){
                for(int i = 0; i < tagValues.length; i++){
                    labelUnits.get(i).setValue(tagValues[i]);
                    labelUnits.get(i).hasChanged = false;
                }
            }
            flag = true;
        }
    }


    private void labelZeroReset() {
        //すべてのラベルを0にリセット
        for(LabelUnit lu: labelUnits){
            lu.hasChanged = true;
            lu.setValue(0.0f);
        }
    }

    private void setChanged(boolean b){
        for(LabelUnit lu: labelUnits){
            lu.hasChanged = b;
        }
    }


    private void addTagsImages() {
        //SourceImages, TargetImagesへの追加
        String[] fileNames = mainWindow.imagesPanel.getImageNames();
        mainWindow.dataInfo.sourceImages.add(fileNames[0]);
        mainWindow.dataInfo.targetImages.add(fileNames[1]);
    }


    private void addTags() {
        //tagsの変更
        DataInfo dataInfo = mainWindow.dataInfo;
        if(!dataInfo.isSourceZero){
            ErrorChecker.errorCheck(IN_PROGRESS);
        }
        Float[] allZero = new Float[dataInfo.tagRule.size()];
        Float[] tags = new Float[dataInfo.tagRule.size()];
        for(int i = 0; i < dataInfo.tagRule.size(); i++){
            tags[i] = labelUnits.get(i).getValue();
        }
        String[] imageNames = mainWindow.imagesPanel.getImageNames();

        LinkedHashMap<String, Float[]> newTags = new LinkedHashMap<>();
        newTags.put(imageNames[0], allZero);
        newTags.put(imageNames[1], tags);
        dataInfo.tags.set(mainWindow.tagsInd, newTags);
    }


    private boolean labelChanged() {
        //ラベルのうちいずれかが変わっていたとき
        for(LabelUnit lu: labelUnits){
            if(lu.hasChanged){
                return true;
            }
        }
        return false;
    }
    
}
