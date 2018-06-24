// this class handles all the Edit menu items' events
// date: 14-09-14

/*
 *  UndoableEditListener listens for any edit that occur in the text editor. Its only method "undoableEditHappened()"
 *  is called as soon as an edit happens in the text editor.
 */

package javaPad;

import java.awt.event.*;
import java.util.Date;
import javax.swing.event.*;
import javax.swing.undo.*;


public class EditMenuHandler implements ActionListener, UndoableEditListener {

    MainClass mc;
    
    private UndoManager um;      // to perform "undo" and "redo" and also to remember the EDITS 
    
    
    public EditMenuHandler(MainClass mc) 
    {
        this.mc = mc;   
        
        um = new UndoManager();
    }
    
    
    // implementing ActionListener to check for the recently occured File menu events
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == mc.undoE) 
        {
            // peform "undo"
            doUndo();
        } 
        
        else if (e.getSource() == mc.redoE) 
        {
            // peform "redo"
            doRedo();
        }
        
        else if (e.getSource() == mc.cutE) 
        {
            MainClass.ta.cut();       
        } 
        else if (e.getSource() == mc.copyE) 
        {
            MainClass.ta.copy();
        } 
        else if (e.getSource() == mc.pasteE)
        {
            MainClass.ta.paste();
        } 
        else if (e.getSource() == mc.delE)
        {
            // to delete a word/character, replace the selected text with whitespace
            MainClass.ta.replaceSelection(""); 
        } 
        else if (e.getSource() == mc.selectAllE)
        {
            // the textarea needed to be focused first, otherwise won't happnes 
            MainClass.ta.requestFocusInWindow();
            MainClass.ta.selectAll();
        }
        else if (e.getSource() == mc.dataNtimeE)
        {
            // a Date objected is created, which holds the current date and time, its converted into String and
            // then inserted in the current position of the caret.
            MainClass.ta.insert( new Date().toString(), MainClass.ta.getCaretPosition() ); 
        }
        
        else if (e.getSource() == mc.lower)
        {
            // get the selected text, convert it to appropiate case and replace the selected text with that text 
            String temp = MainClass.ta.getSelectedText();
            MainClass.ta.replaceSelection ( temp.toLowerCase() );
        }
        else if (e.getSource() == mc.upper)
        {
            String temp = MainClass.ta.getSelectedText();
            MainClass.ta.replaceSelection ( temp.toUpperCase() );
        }

    }
    
    
    /*  Implementing the UndoLinstner interface.
     *  
     *  the main task of this method is to call UndoManager.addEdit() to remember every edit and to update
     *  the "undo" and "redo" buttons/nenu items accordingly. 
     */
    
    public void undoableEditHappened( UndoableEditEvent e )
    {
        //Remember the edit 
        um.addEdit( e.getEdit() );
        
        updateRedoUndo();
    }
    
    //
    private void updateRedoUndo()
    {
        
        /*  XX = un / re
         * 
         *  UndoManager.canXXdo() method returns true if XX can be done, else false. This method is set as a 
         *  parameter for the setEnable() which helps it to determine whether to enable undo/redo 
         *  buttons/ menu items or not. 
         */
        
        
        // update edit menu items
        mc.undoE.setEnabled( um.canUndo() );
        mc.redoE.setEnabled( um.canRedo() );
        
        // update tool bar items
        mc.undoB.setEnabled( um.canUndo());
        mc.redoB.setEnabled( um.canRedo());
        
        
        // update popup menu items
        mc.undo.setEnabled( um.canUndo());
        mc.redo.setEnabled( um.canRedo());
    }
    
    
    
    // this methos performs the "Undo action" through UndoManager.undo()
    void doUndo()
    {
        try {
               um.undo();
           }
           catch( CannotUndoException cue )
           {
               System.err.println( cue.getMessage() );
           }
           
           // after performing "undo", update the undo/redo buttons  
           updateRedoUndo(); 
    }
    
    
    
   // this methos performs the "Redo action" through UndoManager.redo()
    void doRedo()
    {
        try {
               um.redo();
            }
            catch( CannotRedoException cre )
            {
                System.err.println( cre.getMessage() );
            }
           
         // after performing "redo", update the undo/redo buttons  
         updateRedoUndo();
    }
    
}
