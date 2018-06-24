// this class implements CaretListener and tracks down the caret position
// date: 05-09-14

package javaPad;

import javax.swing.event.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class CaratHandler implements CaretListener {

    private MainClass mc;
    
    // A Document instance repersents the document(data/text) displayed by a text component eg. textArea
    private Document doc;           
    
    // "rowNum" holds the current line numbers of the textarea. 
    // "chars" holds the current total characters (visible/not visible) of the textarea.
    int rowNum, chars;
    
    
    public CaratHandler(MainClass mc) 
    {
        this.mc = mc;
    }
    
    
    // calculates file length in terms of total characters in the textarea.
    private void setFileLen()
    {
        // get an instance of the Document that the textarea "ta" represents.
        doc = MainClass.ta.getDocument();   
        
       // Document.getLength() returns the number total characters contain currently in a Document.
        chars = doc.getLength();
       
        // the label "fileLength" is set with a formatted text to displays the lenght of the file 
        // in the JavaPad's status bar.
        mc.fileLength.setText( String.format("|      File Length:  " + chars ));
    }
    
    
    // calculates starting and ending index of a selection in the textarea.
    private void setTextSel()
    {
        if( MainClass.ta.getSelectedText() != null )        // some text is selected            
        {
            // JtextArea.getSelectionStart() returns the starting index/postions of the selected text.
            // JtextArea.getSelectionEnd() returns the ending position.
            
            int start =  MainClass.ta.getSelectionStart();   
            int end = MainClass.ta.getSelectionEnd();
            
           // the label "selection" is set with a formatted text to displays the start and end position of a selection
           // in the JavaPad's status bar.
            mc.selection.setText( String.format("|      Selection from: %d  to  %d ", start, end) );
        }
        
        // nothing is selected
        else
        {
            // "selection" is set to emplty start and end position.
            mc.selection.setText("|      Selection from: ");
        }
        
    }
    
    
    // calcaulate the current row and column numbers
    private void setRowCol()
    {
        int offset, lineNumber, colNumber;
        
        // offset is nothing but the current position of the caret, starts with ZERO
        offset = MainClass.ta.getCaretPosition();
        
        
        try {
            lineNumber = MainClass.ta.getLineOfOffset( offset) ;    // converts an offset to line number
            
            // to calculate column number, the offset/position of the caret is substracted from the 
            // strating offset of the line in which the caret is currently present.
            // since offset strats from zero, ONE must be added to it.
            
            // JTextArea.getLineStartOffset() takes line number as input and returns the starting offset of the line
            
            colNumber = ( offset - MainClass.ta.getLineStartOffset( lineNumber ) )  + 1;  
            
            // acc. to text area's line numbering scheme, line numbers starts with ZERO.
            // but while displaying line numbers we need to start from ONE so ONE must be added to it.
            rowNum = lineNumber + 1;
            
            
            // the label "rowColNum" displays the current row and column number
            mc.rowColNum.setText( String.format("|       row: %d   col: %d ", rowNum, colNumber) ); 
        }
        
        
        // getLineOfOffset(), getLineStartOffset() might throws "BadLocationException" 
        
        catch( BadLocationException ce)    
        {
            System.err.println(ce);
        }
        
    }
    
    
   // this method gets called when the caret in a text component moves or when the selection in a text component changes. 
    public void caretUpdate( CaretEvent e )
    {
        // get the no of character in the file
        setFileLen();
        
        // get the start and end postion of the selected text
         setTextSel();
         
        // determine the current row and column numbers
         setRowCol();
         
        
         // if text is selected then enable the following actions..
          if( MainClass.ta.getSelectedText() != null )
          {
              // edit menu items:       cut, copy, delete, tolowercase, touppercase 
              mc.cutE.setEnabled(true); 
              mc.copyE.setEnabled(true);
              mc.delE.setEnabled(true); 
              mc.lower.setEnabled(true); 
              mc.upper.setEnabled(true); 
             
              // toolbar items:         cut, copy
              mc.cutB.setEnabled(true); 
              mc.copyB.setEnabled(true); 
             
              // popup menu items:      cut, copy, delete
              mc.cut.setEnabled(true); 
              mc.copy.setEnabled(true);
              mc.del.setEnabled(true);
          }
          
          // if text is not selected then disable the actions
          else
          {
              // edit menu items
              mc.cutE.setEnabled(false); 
              mc.copyE.setEnabled(false);
              mc.delE.setEnabled(false); 
              mc.lower.setEnabled(false); 
              mc.upper.setEnabled(false);
              
              // toolbar items
              mc.cutB.setEnabled(false); 
              mc.copyB.setEnabled(false);
              
              // popup menu items
              mc.cut.setEnabled(false); 
              mc.copy.setEnabled(false);
              mc.del.setEnabled(false);
          }
      
    }
  
}