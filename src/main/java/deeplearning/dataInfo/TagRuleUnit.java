package deeplearning.dataInfo;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import deeplearning.main.Utilities;

public class TagRuleUnit extends JPanel{
    private static final int TEXT_MARGIN_W = 40;
    private static final int TEXT_MARGIN_H = 5;
    private static final int CHECK_MARGIN_W = 40;
    private static final int CHECK_MARGIN_H = 5;

    public JTextField text;
    public JCheckBox check;
    
    public TagRuleUnit(){
        super();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        JTextField text = new JTextField();
        JCheckBox check = new JCheckBox();

        add(Utilities.getPanelWithMargin(text, TEXT_MARGIN_W, TEXT_MARGIN_H));
        add(Utilities.getPanelWithMargin(check, CHECK_MARGIN_W, CHECK_MARGIN_H));
    }
    
}
