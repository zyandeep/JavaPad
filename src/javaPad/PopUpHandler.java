// this class handles all the actions of the JavaPad's popup menu
// date: 19-09-14

package javaPad;

import java.awt.event.*;
import java.util.Date;

public class PopUpHandler implements ActionListener {

    MainClass mc;
    
    public PopUpHandler( MainClass mc )
    {
        this.mc = mc;
    }
    
    
   // this method is overwritten and it gets called when an action event is thrown by a registered source
    public void actionPerformed(ActionEvent e)
    {
        if( e.getSource() == mc.undo )
        {
            // perform "undo" action by invoking the instance method of EditMenuHandler: doUndo()  
            mc.emh.doUndo();      
        }
        else if( e.getSource() == mc.redo )
        {
            // perform "redo" action by invoking the instance method of EditMenuHandler: doRedo()
            mc.emh.doRedo();
        }
        else if( e.getSource() == mc.cut )
        {
           mc.ta.cut();         // to perform "cut" action, we call the instance method JTextArea.cut() 
        }
        else if( e.getSource() == mc.copy )
        {
           mc.ta.copy();         // to perform "copy" action, we call the instance method JTextArea.copy() 
        }
        else if( e.getSource() == mc.paste )
        {
            mc.ta.paste();       // to perform "paste" action, we call the instance method JTextArea.paste() 
        }
        else if( e.getSource() == mc.selectAll )
        {
            // the textarea needed to be focused first.
            // Otherwise the instance method JTextArea.selectAll() won't work
            mc.ta.requestFocusInWindow();
            mc.ta.selectAll();
        }
        else if( e.getSource() == mc.del )
        {
            // to delete a word/character, replace the selected text with whitespace
            mc.ta.replaceSelection("");
        }
        else if( e.getSource() == mc.dataNtime )
        {
            // a Date object holds today's date and time.
            // Date.toString() gives the date/time as a string.
            // JTextArea.getCaretPosition() returns the current caret position (offset) in the textarea. 
            
            // So today's date/time is inserted at the current caret position 
            // using the instance method JTextArea.insert(). 
            
             mc.ta.insert( new Date().toString(), mc.ta.getCaretPosition() );
        }
        
    }
}