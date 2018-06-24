// this class handles all the View menu items' events
// date: 15-09-14

package javaPad;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;
import java.util.Scanner;

public class ViewMenuHandler implements ActionListener {
    
    MainClass mc;
    
    // variables to hold the number of lines, words and characters 
    int lines, words, characters ;
    
    
    public ViewMenuHandler( MainClass mc) 
    {
        this.mc = mc;
    }
    
    
    // implementing ActionListener to handle the action events occured by View menu items
    public void actionPerformed(ActionEvent e) 
    {
        // the first 3 menu items are checkbox, so we have to check for both selection and deselection
        
        if ( e.getSource() == mc.tBar )         ///// checking for the Toolbar        
        {
           if( mc.tBar.isSelected() )
           {
               mc.toolBar.setVisible(true);     // checked; so make it visible 
           }
           else
           {
               mc.toolBar.setVisible(false);        // unchecked; make it invisible 
           }
           
        } 
        else if (e.getSource() == mc.stBar)         ///// checking for the Status bar
        {
           if( mc.stBar.isSelected() )
           {
               mc.statusBar.setVisible(true);       // checked; so make it visible 
           }
           else
           {
               mc.statusBar.setVisible(false);       // unchecked; make it invisible 
           }
        }
        else if (e.getSource() == mc.lineNum)        //// checking for the linenumber 
        {
           if( mc.lineNum.isSelected() )
           {
               // add the line number "taLine",  to the row-header of the scrollPane "sp" and make it visible.
               mc.sp.setRowHeaderView(mc.taLine);
               mc.taLine.setVisible(true); 
           }
           else
           {
               // set the row-header of the scrollPane "sp" to null and make "taLine" invisible.
               mc.sp.setRowHeaderView(null);
               mc.taLine.setVisible(false);
           }
         
        } 
        else if (e.getSource() == mc.fileSum)
        {
            // call "showFileSum()" to display the summary of the file 
            showFileSum();
        }
        
    }

    
    /*
     * this method gives the summary of a file, which includes the following info:  name of the file with absolute path, 
     * file creation and last modification date, file size, total number of lines, words and characters.
     */
    private void showFileSum()
    {
        // doc = date of creation(file)
        // doc = date of modifition(file)
        
        String fileName, dom = null, size = null, words = null, characters = null, lines = null;
        
        // count words, characters and lines
        calculate();
        
        
        // if the file is not saved then dom and doc must be empty 
        if( mc.file.getName().equals("New File") ) 
        {
            dom = "Last modified:  ";   
        }
        else
        {
            // File.lastModified() retruns file modification time in miliseconds as a long value
            // so the time is converted to Date.
            Date date = new Date( mc.file.lastModified() ); 
        
            dom = "Last modified:   " + date.toString();
        }
        
        
        fileName = "Full file path:   " + mc.file.getAbsolutePath();     // getting the absolute path of the file
        
         // File.lenght() returns the size in bytes( as a long value). Its converted into KB if size >= 1024.
        
       long tempSize = mc.file.length();
        
        if( tempSize >= 1024 )
        {
            tempSize /= 1024;
            size = "File size:   " + tempSize + " KB ";  
        }
        else
        {
            size = "File size:   " + tempSize + " Bytes ";  
        }
            
        lines = "Total lines:   " + this.lines;
        words = "Total words:   " + this.words;
        characters = "Total characters:   " + this.characters;
        
        String[] msg = { fileName, dom, size, words, characters, lines };
        
        // every string of the "msg" array is displayed in a seperate row by the message dialog 
        JOptionPane.showMessageDialog(mc, msg, "File Summary...", JOptionPane.PLAIN_MESSAGE );  
        
    }

    
    // this methos calculate total number of words, characters and lines of the file currently displayed by
    // JavaPad and assign them in "lines", "words", "characters" respectively
    
    private void calculate()
    {
        // get the entier text of the textarea in a String variable to perform the counting operations.
        String text = this.mc.ta.getText();      
        
        // every time this method gets called, "words" must be set to zero as word is counted manually
        this.words = 0;
        
        
        if( text.isEmpty() )     // this means JavaPad is empty 
        {
            this.words = 0;
            this.lines = 1;
            this.characters = 0;
        }
        else
        {
            this.lines = this.mc.ta.getLineCount();     // get the total number of lines currently present.
            
            // The instance variable CaretHandler.chars holds the total number of characters of the textarea.
            // as an instance of CaretHandler, "caret", is already instantiated so we access "chars" throuh
            // "caret".
            
            this.characters = this.mc.carat.chars;
            
            // to count words Scanner is used.
            // Scanner consider whitespace as delimiter and consecutive delimiters as one delimiter.
            Scanner sc = new Scanner(text);
            
            // Scanner.hasNext() returns true if Scanner has another "token"/word to read 
            while( sc.hasNext() )    
            {
                // read the next token/word and count the word number
                sc.next();     
                this.words++;
            }
         
        }
       
    }
   
}
