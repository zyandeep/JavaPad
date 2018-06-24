// this class implements DocumentListener and responsible for generating/updating line numbers.
// date: 05-09-14


/***
 *  The LOGIC:
 *  we calculate the number of wrapped lines between two "actual lines" that end with newLine (\n). And that number
 *  of \n are added to the text area that shows line numbers. This is done by calculating the vertical distance
 *  ( distance in y-axis ) between the two points from where an "actual line" starts and the dividing the number
 *  by the height of the font.
 */


package javaPad;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class DocumentHandler implements DocumentListener {

    private MainClass mc;
    private int newLine = 1;       // variable to hold the current total number of lines 
    
    
    Rectangle r1;               // converts offset of a '\n' to point(x, y) of the view coordinate system.
    int[] offsetArr;            // stores the offsets of '\n' of the text.
    Point p1, p2;               // position of the lines in the view coordinate system ( as x,y )
    

    public DocumentHandler(MainClass mc) 
    {
        this.mc = mc;
        
        
        // instaning the array that stores the offset of "\n" within the text
        // the size of the array is gonna vary with the no. of "\n"
        offsetArr = new int[1];
    }
    
    
    /*
     *  we count the total number of lines on the basis of '\n' and store it in a variable "count" .
     *  if "count" != "newLine" than, line number is updated. so we put the current number of lines in "newLine"
     *  and show the line numbers in "taLine" with setLines()
     */
   
    
    // method to count the current total no of "new lines" and their offset and display them with setLines()
    void updateLines()
    {
        String text = MainClass.ta.getText();
        int j = 0;

        // calculate offsets of the "new lines" 
        
        for (int i = 0; i < text.length(); i++) 
        {
            if (text.charAt(i) == '\n') 
            {   
                // stores the offset of '\n'
                offsetArr[j++] = i;

            }
        }
        
        setLines();

    }
    
    
    // this method calculates the number of wrapped lines between two actual lines and returns
    //the no of wapped lines to its caller.
    
    int calWrappedLines( int offset ) 
     {
        try 
        {
            // offset of '\n' to point(x, y)
            r1 = MainClass.ta.modelToView( offset );     

            // the new point/position where the new line number should be displayed
            p2 = new Point( 5, r1.y + r1.height );
            
            // calcutale the number of wrapped line between two points that represents two actual lines
            int nl = ( p2.y - p1.y ) / r1.height;

            
            // the second point will be first point while calculating wapped lines from that point
            p1 = p2;

            return nl;      // return the no of wapped lines to its caller
            
        } 
        catch (Exception e) 
        {
            // when two( or more) consecutive new lines are added to text area then exception occures (?)
            // the offset of the new lines (except the frist one) can't be converted to point. (??)
        }

        return -1;         // if exception occures return -1
    }
   
    
    // this method is called when a file is edited.
    // it puts a '*' if the file is got edited. 
    void fileEdit()
    {
        
        /* to prevent from getting edited while opening a file, we have to check for this "if" condition.
         * Because, while opening a file text is inserted in "ta", so apperently the file edition takes place,
         * but logically it is not. Again at the same time, "br" is also not "null". So a file is considered to
         * be edited if any insertion/deletion of character happens and most importantly "br" is null.
         */
        
        if( FileMenuHandler.br == null )
        {
            // "isFileEdit" is true if any insertion/deletion of character takes place in a file.
            if( mc.isFileEdit ) 
            { 
                // a file name starting with '*' reprents the file is edited
                // '*' should be given when the file is edited for the first time
                
                if( mc.getTitle().startsWith("*") == false )    // check if the file name already has a '*'
                {
                    mc.setTitle("*" + mc.getTitle());
                }
                
            }
        }
    }
    
    
    // this method actually displays line numbers in the text area
    void setLines() 
    {
        if( newLine != MainClass.ta.getLineCount() )
        {
            newLine = MainClass.ta.getLineCount();
        }
        
        
        mc.taLine.setText("");
        
        // the fisrt point that represents a actual line.
        // It's the view-coordinate position of the line number "1"
        p1 = new Point(5, 1);
        
        
        for ( int i = 1; i <= newLine; i++ ) 
        {
            if (i == 1) 
            {
                mc.taLine.append("1" + "\n");
            } 
            else 
            {
                // get the number of wrapped line s between two actual lines ( between two (offset) of "\n" )
                
                // parameter of calWrappedLines() is the offset of the second "\n" upto which we need to 
                // count the number of wrapped lines.
                
                int wpLn = calWrappedLines( offsetArr[i - 2] );
                
                // inserting wrapped lines between two "actual lines".
                for (int j = 1; wpLn > 0 && j <= wpLn; j++) 
                {
                    mc.taLine.append("\n");
                }
                
                
                if( wpLn == -1)              // if two or more consecutive new lines are added
                {
                    mc.taLine.append("\n");
                }

                
                // inserting the line number in its actual place
                mc.taLine.append( Integer.toString(i) );
            }

        }
    }

    
    
    // implementing DocumenListeners methods
    public void insertUpdate(DocumentEvent e) 
    {
        
         // (in real) no of newline  =  textarea.getLineCount() - 1
        
         if ( offsetArr.length != ( MainClass.ta.getLineCount() - 1 ) ) 
         {
             offsetArr = new int[ MainClass.ta.getLineCount() - 1 ];
         }

        
        mc.isFileEdit = true;
        
        fileEdit();      // the file might be edited
        
        // if the file is being opening don't call "updateLines()", as it causes slower opening of the file.
        // "isOpenFile" is true if opening of the file is not completed yet, otherwise false.
        
        if( mc.isOpenFile == false && newLine != MainClass.ta.getLineCount() )
        {
            newLine = MainClass.ta.getLineCount();
                    
            updateLines();
        }
           
        
        // enable "find", "find and Replace", "find prev", "find next" actions as "ta" is not empty
        
        mc.findB.setEnabled(true);          // of Toolbar
        mc.findNRepB.setEnabled(true);
        
        // of Search menu
        mc.find.setEnabled(true); 
        mc.findNext.setEnabled(true); 
        mc.findPrev.setEnabled(true); 
        mc.replace.setEnabled(true);
        
    }

    
     // implementing DocumenListeners methods
    public void removeUpdate(DocumentEvent e) 
    {
        mc.isFileEdit = true;
        
        fileEdit();
        
        
        if ( offsetArr.length != ( MainClass.ta.getLineCount() - 1 ) ) 
        {
             offsetArr = new int[ MainClass.ta.getLineCount() - 1 ];
        }

        
        if ( newLine != MainClass.ta.getLineCount() )
        {
            newLine = MainClass.ta.getLineCount();
                    
            updateLines();
        }
        
        
        
        // if "ta" is empty, then disable these actions: cut, copy, del, lowercase and uppercase, find, 
        // find prev, find next and replace.
        
        if( MainClass.ta.getText().isEmpty() )
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
            mc.findB.setEnabled(false); 
            mc.findNRepB.setEnabled(false); 
            
            // popup menu items
            mc.cut.setEnabled(false); 
            mc.copy.setEnabled(false); 
            mc.del.setEnabled(false);
            
            
            // Search menu
            mc.find.setEnabled(false); 
            mc.findNext.setEnabled(false); 
            mc.findPrev.setEnabled(false); 
            mc.replace.setEnabled(false);
            
            
            // set the line number to 1
            mc.taLine.setText("1\n");
            
        }
        
    }

     // implementing DocumenListeners methods
    public void changedUpdate(DocumentEvent e) 
    {
        // never gonna call for PlainTextDocument
    }

}