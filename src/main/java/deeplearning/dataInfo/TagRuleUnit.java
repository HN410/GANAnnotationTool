package deeplearning.dataInfo;

import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;

import deeplearning.main.Utilities;

public class TagRuleUnit extends JPanel{
    private static final int TEXT_MARGIN_W = 20;
    private static final int TEXT_MARGIN_H = 5;
    private static final int CHECK_MARGIN_W = 30;
    private static final int CHECK_MARGIN_H = 5;
    private static final int TEXT_SIZE_W = 150;
    private static final int TEXT_SIZE_H = 30;
    

    public JTextField text;
    public JCheckBox check;
    
    public TagRuleUnit(){
        super();
        setMaximumSize(new Dimension(TEXT_SIZE_W*3, TEXT_SIZE_H + TEXT_MARGIN_H*2));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        text = new JTextField();
        text.setPreferredSize(new Dimension(TEXT_SIZE_W, TEXT_SIZE_H));
        check = new JCheckBox();

        add(Utilities.getPanelWithMargin(text, TEXT_MARGIN_W, TEXT_MARGIN_H));
        add(Utilities.getPanelWithMargin(check, CHECK_MARGIN_W, CHECK_MARGIN_H));
    }
    
}
