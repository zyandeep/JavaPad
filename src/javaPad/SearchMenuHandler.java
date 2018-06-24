// this class handles all the Search menu items' events
// NOTE: if the Find dialog is displayed, the Replace dialog shouldn't be and vice-versa  
// date: 17-09-14

package javaPad;

import java.awt.Toolkit;
import java.awt.event.* ;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

public class SearchMenuHandler implements ActionListener {

    MainClass mc;
    
    // these variables are declared as static so that they can be accessed directly from "FindDialog" and "ReplaceDialog"
    // class without instantiating "SearchMenuHandler" class.
    // they can be accessed by mentioning their class name
    
    // needed while performing "Find Next" and "Find Previous"
    static String searchWord;
    
    // this variable keep track of whether "match case" is checked or not
    // needed while performing "Find Next" and "Find Previous"
    static boolean caseMatch;

    // needed to make sure only one instance of the "Find dialod" is created
    static FindDialog fd;
    
    // needed to make sure only one instance of the "Find and Replace dialod" is created
    static ReplaceDialog rd;
    
    
    public SearchMenuHandler( MainClass mc) 
    {
        this.mc = mc;
    }

    // this method is overwrittten and it gets called when an action event is thrown by a registered source
    public void actionPerformed(ActionEvent e) 
    {
        if( e.getSource() == mc.find )
        {
            // create a FindDialog instane if  both "fd" and "rd" is null; which proves that no ohter instance of 
            // Find dialog and Replace dialog is currently exist.
            
            if( fd == null && rd == null )
            {
                fd = new FindDialog( mc );         
            }
                
        }
        
        else if(e.getSource() == mc.findNext)
        {
            // "find next" refers to normal forward search 
            // we've got two types of variation:  forward search with Match case and forward search without Match case
            
            // if "caseMatch" is true than call dwnMactchCase(query) of FindDialog class
            // else call dwnNoMactchCase(query)
            
            // check the "searchWord" is null or not
            if( searchWord != null )
            {
                if( caseMatch )
                    FindDialog.dwnMactchCase( searchWord );
                else
                    FindDialog.dwnNoMactchCase( searchWord );
            
            }
            else
            {
                // create a beep
                Toolkit.getDefaultToolkit().beep();
                
                JOptionPane.showMessageDialog(mc, "Search word is missing !", "JavaPad", JOptionPane.WARNING_MESSAGE ); 
            }
            
        }
        
        else if(e.getSource() == mc.findPrev)
        {
            // "find previous" refers to backward search
            // we've got two types of variation:  backward search with Match case and backward search without Match case
            
            // if "caseMatch" is true than call bkwMactchCase(query) of FindDialog class
            // else call bkwNoMactchCase(query)
            
            // check is "searchWord" is null or not
            if( searchWord != null )
            {
                if( caseMatch )
                    FindDialog.bkwMactchCase( searchWord ); 
                else
                    FindDialog.bkwNoMactchCase( searchWord );
             
            }
            else
            {
                // create a beep
                Toolkit.getDefaultToolkit().beep();
                
                JOptionPane.showMessageDialog(mc, "Search word is missing !", "JavaPad", JOptionPane.WARNING_MESSAGE ); 
            }
            
        }
        
        else if( e.getSource() == mc.replace )
        {
            // create a ReplaceDialog instane if both "rd" and "fd" are null; which proves no ohter instance of 
            // Replace dialog and Find dialog are currently exist.
            
            if( fd == null && rd == null  )
            {
                rd = new ReplaceDialog( mc );   
            }
            
        }
        
        else if( e.getSource() == mc.gotoLine)
        {
           gotoLine();
        }
    }

    
    
    // this method moves the caret to a particular line number acc. to user's choice
    private void gotoLine() 
    {
        String input = JOptionPane.showInputDialog( mc, "Enter line number:", "Go to line...", JOptionPane.PLAIN_MESSAGE ); 
        
        int lineNumber, offset;
        
        
        // if nothing is entered and "cancel" is pressed, input will be null
        if( input == null)
            return;
        
        
        // if nothing is entered and "OK" is pressed, the length of the "input" string will be zero
        if( input.length() > 0 )
        {
            try {
                
                // convert the input string to number. it may through NumnberFormat Exception.
                // in JtextArea line numbering stars with zero, so 1 need be subtracted from the given line number
                // to make it fit with the JtextArea's line numbering scheme.
                
                // trim() removes unnecessary while-space after and before the actual string
                lineNumber = Integer.parseInt( input.trim() ) - 1;
        
                
               // in JtextArea caret position is marked as offset
               // so the given line number is converted to offset or more presiesly to the line starting offset 
               // this may throw BadLocation exception
                offset = MainClass.ta.getLineStartOffset( lineNumber );
            
                
                // before placing the caret in the proper line, the text area should be in the 
                // current focus
                MainClass.ta.requestFocus();
                MainClass.ta.setCaretPosition(offset); 
            
            }
            catch ( NumberFormatException nfe ) 
            {
                // create a beep
                Toolkit.getDefaultToolkit().beep();
                
                JOptionPane.showMessageDialog(mc, "User Error:  Line number should be integer !", "JavaPad", JOptionPane.ERROR_MESSAGE ); 
            }
            catch ( BadLocationException ex) 
            {
                // create a beep
                Toolkit.getDefaultToolkit().beep();
                
                JOptionPane.showMessageDialog(mc, "The line doesn't exist !", "JavaPad", JOptionPane.ERROR_MESSAGE );
            }
        
        }  
    
    }
    
}
