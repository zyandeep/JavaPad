// this class handles all the Format menu items' events
// date: 17-09-14

package javaPad;

import javax.swing.*;
import java.awt.event.* ;
import java.awt.*;
import java.io.*;


public class FormatMenuHandler implements ActionListener {

    Font newFont;            // this holds the font objected chosen by the user using the "Font chooser" dialog
    
    
    /*  this File object "homeDir" represents a directory (JavaPad) under the User's "home directory" in his OS 
     *  so that we can store his/her choices as a binary file and it will help the JavaPad app to load with
     *  the user's preferences next time it gonna run.
     */
    
    File homeDir;            
    
    MainClass mc;
    
    
    public FormatMenuHandler( MainClass mc ) 
    {
        this.mc = mc;
        
        // here we create the directory "JavaPad" under the user's home directory. 
        
        // System.getProperty("user.home") return the path to the "home director" as a String.
        //  File.separatorChar returns the system-specific name-seperator character. For Windows it's "\\"
        // and for UNIX descendent, it's "/" .
        
        homeDir = new File( System.getProperty("user.home") + File.separatorChar + "JavaPad" );
        
        if( ! homeDir.exists() )      // if "JavaPad" doesn't exist, create it.
        {
            homeDir.mkdir();
        }
    }
    
    
    // implementing ActionListener to check for the recently occured Format menu events
    public void actionPerformed(ActionEvent e)
    {
        // as the menu item "word wrap" is a checkbox, we gotta take care of its both selection and deselection. 
        if(e.getSource() == mc.wordWrap)
        {
            if( mc.wordWrap.isSelected() )
            {
                MainClass.ta.setLineWrap(true);           // "Word Wrap" is enabled.
                MainClass.ta.setWrapStyleWord(true);
            }
            else
            {
                MainClass.ta.setLineWrap(false);          // "Word Wrap" is disabled.
                MainClass.ta.setWrapStyleWord(false);
            }             
        }
        
        else if(e.getSource() == mc.tabSize)
        {
             // displayTabWIndow() returns the integer which is to be set as the new "tab size" .
           MainClass.ta.setTabSize( displayTabWIndow() ); 
        }
        
        else if( e.getSource() == mc.font )
        {
            // create the font chooser dialog and let it to visible.
            
            /*  here the parameters....
            *   mc  =  The frame owener ( in this case, the the JavaPad JFrame )
            *   "Font Chooser"  =   name if the dialog
            *   true   =    make the dialog as "model" 
            */
            
            new FontChooserDialog(mc, "Font Chooser", true).setVisible(true); 
            
            if( this.newFont != null )
            {
                // user-selected font is applied to the text area and the text area that displays line numbers.  
                MainClass.ta.setFont(newFont); 
                this.mc.taLine.setFont(newFont);
            
               // the newly created font along with text and backbround color of the text area and the color of the caret 
               // is preserved to save them in the disk as binary file; name "settings.bin"; inside the 
               // JavaPad directory.
                
                // ANYTHING CHANGE (COLOR, FONT), SAVE ALL OF THEM!!
                
                mc.textFont = newFont;
                mc.text = MainClass.ta.getForeground();
                mc.body = MainClass.ta.getBackground();
                mc.caret = MainClass.ta.getCaretColor();
            
                
                // saved the user preference in the diak.
                saveSetting();
            }
            
        }
        
        else if(e.getSource() == mc.tColor)
        {
            
            // JColorChooser lets user to select a color by displaying a "Color choose" dialog.
            // here the user select the text color of the JavaPad.
            
            this.mc.text = JColorChooser.showDialog( mc, "Change text color...", MainClass.ta.getForeground() );
            
            if( this.mc.text != null )
            {
                // the selected color is applied in JavaPad and  changes (color, font) is saved in "settings.bin"
                
                MainClass.ta.setForeground( this.mc.text );
            
                mc.textFont = MainClass.ta.getFont();
                mc.body = MainClass.ta.getBackground();
                mc.caret = MainClass.ta.getCaretColor();
                
                saveSetting();
            }
            
        }
        else if(e.getSource() == mc.bColor)
        {
            // here the user selects the body color of the JavaPad.
            
            this.mc.body = JColorChooser.showDialog( mc, "Change body color...", MainClass.ta.getBackground() );
            
            if( this.mc.body != null )
            {
                MainClass.ta.setBackground( this.mc.body );
            
                mc.textFont = MainClass.ta.getFont();
                mc.text = MainClass.ta.getForeground();
                mc.caret = MainClass.ta.getCaretColor();
            
                saveSetting();
            }
            
        }
        else if( e.getSource() == mc.cColor)
        {
             // here the user selects the caret color of the JavaPad.
            
            this.mc.caret = JColorChooser.showDialog( mc, "Change caret color...", MainClass.ta.getCaretColor() );
            
            if( this.mc.caret != null )
            {
                MainClass.ta.setCaretColor( this.mc.caret );
            
                mc.textFont = MainClass.ta.getFont();
                mc.text = MainClass.ta.getForeground();
                mc.body = MainClass.ta.getBackground();
            
                saveSetting();
            }
            
        }
        else if( e.getSource() == mc.restor)
        {
            // Get the confirmation of the user about resoting the default setting of JavaPad
            
            int result = JOptionPane.showConfirmDialog( mc, "Sure to resote the default setting?", "JavaPad", 
                                     JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE );
                 
            
            if( result ==  JOptionPane.YES_OPTION )   // user says "YES"
            {
                // the default font, color of the JavaPad is restored by loadDefaultSetting()
                this.mc.loadDefaultSetting();      
                
                // this change is also refected in the "settings.bin", as because the next time JavaPad is gonna run
                // it loads user preferences from "settings.bin", if it exist.
                
                this.saveSetting();
            }
            
        }
        else if(e.getSource() == mc.java)
        {
            // change the LnF of the app to Java if the checkbox "java" is selected.
            if( mc.java.isSelected() )
            {
                changeLnf("javax.swing.plaf.metal.MetalLookAndFeel");      
            }
            
        }
        else if(e.getSource() == mc.motif)
        {
            // change the LnF of the app to Motif if the checkbox "motif" is selected.
            if( mc.motif.isSelected() )
            {
                changeLnf("com.sun.java.swing.plaf.motif.MotifLookAndFeel"); 
            }
        }
        
    }

    
    
    // this method takes the "tab size" from the user and returns it to its caller.
    private int displayTabWIndow()
    {
        int tab = 8;         // it's the default size of the JavaPad's tab.
    
        String temp = JOptionPane.showInputDialog(this.mc, "Enter tab size... \n( Default tab size is 8 )", "Tab setting", JOptionPane.PLAIN_MESSAGE );
        
        // if nothing is entered and "cancel" is pressed, temp will be null
        if( temp == null)
            return tab;
        
        
        // if nothing is entered and "ok" is pressed, the lenght of the "temp" string will be zero
        if( temp.length() > 0 )
        {
            try 
            {    
                // trim() removes unnecessary whilespace after and befor the actual string
                tab = Integer.parseInt( temp.trim() );
            }
            catch( NumberFormatException noe)
            {
                // create a beep and display the ERROR message.
                Toolkit.getDefaultToolkit().beep();
                
                JOptionPane.showMessageDialog(null, "USER ERROR: Repalce the user and then click OK!", "JavaPad", JOptionPane.ERROR_MESSAGE ); 
            }
            
        }
        
        return tab;
    }

    
    // this method is called when User wnats to change the LnF of the JavaPad's app  
    private void changeLnf( String lnfClass ) 
    {
        // the String lnfClass holds the class name of the LookAndFeel class as specified by the user
        
        try {
            UIManager.setLookAndFeel( lnfClass );   // set the app's LnF 
            
            SwingUtilities.updateComponentTreeUI( this.mc );
           
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(this.mc, "ERROR: Can't set the LnF!!", "JavaPad", JOptionPane.ERROR_MESSAGE );
        }
        
        this.mc.pack();
    }

    
    /*
     *  the job of this method is to save the user's preference to the disk in the file "setting.bin".
     * 
     *  User's preference includes: Font of the app and text, body and the caret color of the app.
     *  To save these objects in the disk, we use Java's Object serialisation mechanism.
     */
    
    private void saveSetting()
    {  
        // create/refere to the "setting.bin", which path is specified by "homeDir".
        File setting = new File( homeDir, "setting.bin" );
        
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        
        // its recommended to open and read a file within the same try block
        try {
            
            fos = new FileOutputStream( setting );     // create a "strem" btw "setting.bin" and the program
            oos = new ObjectOutputStream( fos );    // ObjectOutputStream writes objects to the file through "fos". 
            
            
            oos.writeObject( mc.textFont );     // writes (or saves) Font object
            oos.writeObject( mc.body );         // writes (or saves) body color object
            oos.writeObject( mc.text );         // writes (or saves) text color object
            oos.writeObject( mc.caret );        // writes (or saves) caret color object
        }
        catch( IOException ioe )
        {
            System.out.println( ioe.getMessage() );
        }
        finally
        {
            // a file stream should be closed inside a finally block
            // which ensures that the file will always get closed even if there is an exception while opening
            // or reading the file.
            
            // if oos == null, there is an exception while opening the file "setting.bin"
            try {
                
                if( oos != null )
                {
                    oos.close();
                }
                
            } 
            catch (Exception e) 
            {
                System.out.println( e.getMessage() );
            }
     
        }
      
    }        
}