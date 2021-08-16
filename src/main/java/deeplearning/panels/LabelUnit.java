package deeplearning.panels;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javafx.scene.layout.Border;

import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
public class LabelUnit extends JPanel{
    //ラベル付けをする部分の1項目分
    private static final int PANEL_W = 200;
    private static final int PANEL_H = 20;
    private final static int EDGE_MARGIN_W = 15;
    private final static int OUTER_GRID_W = 2;
    private final static int OUTER_GRID_H = 1;
    private final static int SLIDER_MIN = 0; 
    private final static int SLIDER_MAX = 100;
    private final static String VALUE_LABEL_TEXT = "0.00";
    private final static String VALUE_FORMAT = "%.02f";


    private boolean isContinuous;    
    private JCheckBox checkBox = null;
    private JSlider slider = null;
    private JLabel valueLabel = null;

    public LabelUnit(String label, boolean isContinuous){
        this.isContinuous = isContinuous;
        setPreferredSize(new Dimension(PANEL_W, PANEL_H));
        setLayout(new BorderLayout());
        Component edgeBox = Box.createRigidArea(new Dimension(EDGE_MARGIN_W, EDGE_MARGIN_W));
        add(BorderLayout.EAST, edgeBox);
        add(BorderLayout.WEST, edgeBox);

        JPanel inner = getInnerPanel(label);
        add(BorderLayout.CENTER, inner);
    }

    private JPanel getInnerPanel(String labelName) {
        //左右のパディングを抜いた部分
        JPanel outerPanel = new JPanel();
        outerPanel.setLayout(new GridLayout(OUTER_GRID_H, OUTER_GRID_W));
        
        JLabel label = new JLabel(labelName);
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(BorderLayout.EAST, Box.createRigidArea(new Dimension(EDGE_MARGIN_W, EDGE_MARGIN_W)));
        labelPanel.add(BorderLayout.CENTER, label);

        JPanel valuePanel = getValuePanel();
        outerPanel.add(labelPanel);
        outerPanel.add(valuePanel);
        
        return outerPanel;
    }

    private JPanel getValuePanel() {
        //スライダーかチェックボックスが入る部分
        JPanel panel = new JPanel();
        if(!isContinuous){
            panel.setLayout(new BorderLayout());
            checkBox = new JCheckBox();
            panel.add(BorderLayout.CENTER, checkBox);
            checkBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        }else{
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            valueLabel = new JLabel(VALUE_LABEL_TEXT);
            getSlider();
            panel.add(slider);
            panel.add(valueLabel);
        }
        return panel;
    }

    private void getSlider() {
        //スライダーを作成
        slider = new JSlider(SLIDER_MIN, SLIDER_MAX);
        slider.setValue(SLIDER_MIN);
        slider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                float value = (float) slider.getValue() / SLIDER_MAX;
                String text = String.format(VALUE_FORMAT, value);
                valueLabel.setText(text);
            }
        });
    }
    
}