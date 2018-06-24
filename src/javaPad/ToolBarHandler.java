// this class handles all the ToolBar actions' event
// date: 19-09-14

// JavaPad's tool bar contains the following actions:  new file, open file, save file, undo, redo, 
// cut, copy, paste, find, find and replace.


package javaPad;

import java.awt.event.*;

public class ToolBarHandler implements ActionListener {

    MainClass mc;
    
    public ToolBarHandler( MainClass mc )
    {
        this.mc = mc;  
    }
    
    
    // this method is overwritten and it gets called when an action event is thrown by a registered source
    public void actionPerformed(ActionEvent e)
    {
        if( e.getSource() == mc.newFileB )
        {
            // as "newFileHandler()" is an instance method of FileMenuHandler class and there is an instance of 
            // FileMenuHandler, "fmh", is created in MainClass, so we called newFileHandler() through "fmh".
            
            // we have to make sure that only one instance of every class; if necessary;  got created.
           
            // to create an empty file in JavaPad, "newFileHandler()" is called through "fmh".
            mc.fmh.newFileHandler();
        }
        else if( e.getSource() == mc.openFileB )
        {
            // to select and open a file in JavaPad, "openFileHandler()" is called through "fmh". 
            mc.fmh.openFileHandler();
        }
        else if( e.getSource() == mc.saveFileB )
        {
            // to save a file, "saveFileHandler()" is called through "fmh".
            mc.fmh.saveFileHandler();
        }
        else if( e.getSource() == mc.printB )
        {
           // to print a file, "printFileHandler()" is called through "fmh". 
           mc.fmh.printFile();
        }
        else if( e.getSource() == mc.undoB )
        {
            // "undo" and "redo" are implemented in these two instance methods of EditMenuHandler class:
            // "doUndo()" and "doRedo()". As there is an instance of EditMenuHandler, "emh", in MainClass
            // so we call these two method through "emh".
            
            mc.emh.doUndo();            // perform the "undo" action  
        }
        else if( e.getSource() == mc.redoB )
        {
            mc.emh.doRedo();            // perform the "redo" action
        }
        else if( e.getSource() == mc.cutB )
        {
             mc.ta.cut();           // to perform "cut" action, we call the instance method JTextArea.cut() 
        }
        else if( e.getSource() == mc.copyB )
        {
             mc.ta.copy();          // to perform "copy" action, we call the instance method JTextArea.copy()
        }
        else if( e.getSource() == mc.pasteB )
        {
             mc.ta.paste();      // to perform "paste" action, we call the instance method JTextArea.paste()   
        }
        else if( e.getSource() == mc.findB )
        {
            // create a FindDialog instance if  both "fd" and "rd" are null; which proves that no other instance of 
            // Find dialog and Replace dialog is currently exist.
            
            if( SearchMenuHandler.fd == null && SearchMenuHandler.rd == null)
            {
                SearchMenuHandler.fd = new FindDialog(mc);      
            }
        }
        else if( e.getSource() == mc.findNRepB )
        {
            // create a Replace Dialog insane if  both "rd" and "fd" are null; which proves that no ohter instance of 
            // Find dialog and Replace dialog is currently exist.
            
            if( SearchMenuHandler.rd == null && SearchMenuHandler.fd == null )
            {
                SearchMenuHandler.rd = new ReplaceDialog(mc);
            }
        }
        
    }
    
}
