package deeplearning.main;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorChecker {
    public static void errorCheck(Exception e){
        JFrame frame = new JFrame();

        String text = getStackTrace(e);
        JOptionPane.showMessageDialog(frame, text);
        System.exit(-1);
    }
    
    public static void errorCheck(String s){
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, "Error:"+s);
        System.exit(-1);
    }

    static String getStackTrace(Exception e){
        StringWriter sw = null;
        PrintWriter pw = null;
        sw = new StringWriter();
        pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }
}
