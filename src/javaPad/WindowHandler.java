// this class extends WindowAdapter and tracks window event
// date: 05-09-14

package javaPad;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JOptionPane;

public class WindowHandler extends WindowAdapter {

    MainClass mc;
    
    
    public WindowHandler( MainClass mc) 
    {
        this.mc = mc;   
    }
    
    
    // this method gets called when a window is about to close
    public void windowClosing( WindowEvent e) 
    {
        // if a file gets '*' in front of it's name, the file is get edited.
        // then give warning to user while he/she about to close the JavaPad app's window
        
        if( mc.getTitle().startsWith("*") )
        {
            int result = JOptionPane.showConfirmDialog(mc, "Close without saving " + mc.file.getName() + " ??", "JavaPad", 
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            
            // user does't want to save the file, so quit the app
            if(result == JOptionPane.YES_OPTION)
            {
               mc.dispose();
            }
           
            // as "saveFileHandler()" is an instance method of FileMenuHandler class and there is an instance of 
            // FileMenuHandler, "fmh", is created in MainClass, so we called saveFileHandler() through "fmh".
            
            // we have to make sure that only one instance of every class; if nedessary;  got created.
            
            else if(result == JOptionPane.NO_OPTION)
            {
                mc.fmh.saveFileHandler();       // user wants to save the file
            }
           
        }
        
        // the file isn't edited, so close the app straight 
        else
        {
            mc.dispose();
        }
         
    }
    
}
