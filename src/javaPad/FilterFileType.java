// A class to implements filter file types in the JFileChooser
// Used by the JFileChooser to filter file to display on the basis of its "File type" option.
// date: 27-07-14

package javaPad;

import java.io.File;


// must be implemented javax.swing.filechooser.FileFilter interface

public class FilterFileType extends javax.swing.filechooser.FileFilter {

    public FilterFileType()
    {
        // FilterFileType object is got created
    }

    // method to implement.
    // called by JFileChooser for each file to filter when a file type is selected in the JFileChooser dialog.
    // if the method returns true than display the file otherwise not.
    
    public boolean accept( File f )
    {
        //if the file is a directory or .txt then return "true"
        if( f.isDirectory() || f.getName().endsWith(".txt") )
            return true;
        else
            return false;    
    }

    
    // this method returns a String ( or description of a file type ) necessary to display 
    // it in "File type" option of the JFileChooser.
    
    public String getDescription() 
    {
        return "text files (.txt)";
         
    }
}
