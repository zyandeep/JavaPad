// this class handles all the File menu items' events
// members are declared as static so that we can use them in another class/object without object-instantiation.
// date: 10-09-14

package javaPad;

import java.io.*;
import javax.swing.*;
import java.awt.event.*;

public class FileMenuHandler implements ActionListener {

    MainClass mc;
    File file;
    static BufferedReader br;
    static PrintWriter pw;
  
    JFileChooser fc;                      //  for the "open" and the "save / save as" dialog

    public FileMenuHandler(MainClass mc) 
    {
        this.mc = mc;
        
        // create the JFileChooser
        setUpFileChooser();  
         
    }

    
    // implementing ActionListener to check for the recently occured File menu events
    public void actionPerformed(ActionEvent e) 
    {           
        if (e.getSource() == mc.newFile) 
        {
            newFileHandler();
        } 
        else if (e.getSource() == mc.openFile) 
        {
            openFileHandler();
        }
        else if (e.getSource() == mc.saveFile) 
        {
            saveFileHandler();
        } 
        else if (e.getSource() == mc.saveAsFile) 
        {
            saveAsFileHandler();
        } 
        else if (e.getSource() == mc.print)
        {
            printFile();
        } 
        else if (e.getSource() == mc.pageSetup)
        {
            printFile();
        } 
        else if (e.getSource() == mc.quit)
        {
            quitApp();
        }

    }

    
    // method to instantiate a JFileChooser and customize it
    private void setUpFileChooser() 
    {
        fc = new JFileChooser();
        fc.addChoosableFileFilter( new FilterFileType() );
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fc.setFileHidingEnabled(true);
    }

    
    // method to handle "new file" event 
    void newFileHandler() 
    {
        // if file name starts with '*' then the file is edited
        
        if ( mc.getTitle().startsWith("*") )       
        {
            int result = JOptionPane.showConfirmDialog(mc, "Do you want saving " + mc.file.getName() + " ??", "JavaPad",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            
            // user wants to save the edited file
            if (result == JOptionPane.YES_OPTION) 
            {
                saveFileHandler();          // method deals with saving file.
            }
            
            // user doesn't want to save the edited file, so creat a empty new file
            else if (result == JOptionPane.NO_OPTION) 
            {
                mc.ta.setText("");
                
                // creats a file object to represent the "new file" and 
                //sets the app title with the newly created file name
                
                mc.file = new File("New File");
                mc.setTitle(mc.file.getName() + " - JavaPad");
            }
        }
        
        // file isn't edited so directly create a new file
        else 
        {
            mc.ta.setText("");

            mc.file = new File("New File");
            mc.setTitle (mc.file.getName() + " - JavaPad");
            
            // an empty file starts with line number 1
            mc.taLine.setText(+ 1 + "\n");
        }
    }

    
    //// open and read the file within from the same try block and close the file in the finally block 
    void openFileHandler() 
    {
        // checking whether the current opened file is edited
        if( mc.getTitle().startsWith("*") ) 
        {
            int result = JOptionPane.showConfirmDialog( mc, "Do you want saving " + mc.file.getName() + " ??", "JavaPad",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) 
            {
                saveFileHandler();
            } 
            
            // user doesn't want to save the edited file, so call the FileChooser
            else if( result == JOptionPane.NO_OPTION )
            {
                openNReadFile();         // method to handle opening of a text file
            }
                
        }
        else                        // file isn't edited so we acn open another file safely
        {
            openNReadFile();
        }
        
    }

    
    // this method gets called when user wants to save a file
    void saveFileHandler() 
    {
        /*
         *  JavaPad automatically do "save as.." when................
         *         1. a "new file" is edited
         *         2. user wants to save a unedited "new file"
         */
        
        if(( mc.isFileEdit &&  mc.file.getName().equals("New File") ) || mc.ta.getText().isEmpty() ) 
        {
            saveAsFileHandler();       // helps user to save a file with a name and desired location 
        }
        
        // the edited file is saved previously. So only save the file, don't show the FileChooser 
        else
        {
           // save the contents of the file to disk
           saveFile();
            
        }
    }

    
    // called automatically when an edited file is not saved previously (i,e saveAs... )
     void saveAsFileHandler() 
     {
        int result = fc.showSaveDialog( null ); 
        
        if ( result == JFileChooser.APPROVE_OPTION )
        {
            file = fc.getSelectedFile();
            
            // check if the user returns null value
            if( file != null )
            {
                            
               String fileName = file.getName();
               
               // if the user doen't give file extension create a new file of that name
               // and set file extension to ".txt" 
               if( fileName.lastIndexOf('.') <= 0) 
               {
                   file = new File( file.getAbsoluteFile() + ".txt" );
               }
               
               
               if( fileName.endsWith("."))  
               {
                   file = new File(file.getAbsoluteFile() + ".txt");
               }

               
               // get the selected file with extension and set the app title accordingly
               mc.file = this.file;
               mc.setTitle( file.getAbsolutePath() + " - JavaPad" ); 
               
               saveFile();      // save the file content on the disk
            }
           
        }
        
    }
    
    
    // this method reads the content of a file and displays it on the JavaPad's textarea
    private void openNReadFile()
    {
        
        int result = fc.showOpenDialog(null);             // let user to open a file with the FileChooser 

        if (result == JFileChooser.APPROVE_OPTION) 
        {
            file = fc.getSelectedFile();

            // if the selected file is a directory, don't do anything
            if( file == null || file.isDirectory() )        
                return;
            
            mc.file = file;
            mc.setTitle( file.getAbsolutePath() + " - JavaPad");

            // this variable keeps track that a file is about to open
            // this helps to display line numbers of a opened file.
            
            mc.isOpenFile = true;
        
            // file is opend and read
            try 
            {
                FileReader fr = new FileReader(file);
                br = new BufferedReader( fr );

                mc.ta.setText("");

                while (true) 
                {
                    String text = br.readLine();
                    
                    if (text == null) {
                        break;
                    }

                    // to prevent unnecessary new line at the end of the file
                    if( mc.ta.getText().length() == 0)
                        mc.ta.append(text);
                    else
                        mc.ta.append("\n" + text );
                    
                }
                
               // after reading the whole file the caret will be at the end of the file 
               // and we will see the end of the file 
               // hence it is positioned at starting of the file so that we can see the file from its starting
              
               MainClass.ta.setCaretPosition(0); 
             
               // display line numbers of the opened file
               addLineNumbers();
               
               // It confirms that file opening is done so that we can use the default line-number-display method 
               mc.isOpenFile = false;
                
            } 
            catch (IOException ioe) 
            {
                System.err.println( ioe.getMessage() ); 
            } 
            finally 
            {  
                // the reading file stream is closed here 
                try 
                {
                    if( br != null )
                       br.close();
                    
                    // to prevent a file from getting edit while it's opening
                    br = null;
                    
                }
                catch(IOException ioe)
                {
                    ioe.printStackTrace();
                }
              
            }
        }
    }

    
    // method to display the print dialog 
    void printFile()
    {
         try 
         {
            mc.ta.print();           // display the default print dialog of JavaPad  
         } 
         catch(Exception ex)
         {
            System.err.println(ex);
         }
    }

    
    // this method handles the closing of JavaPad
    private void quitApp()
    {
        if( mc.getTitle().startsWith("*") )         // make sure if the file is edited earlier or not 
        {
            int result = JOptionPane.showConfirmDialog(mc, "Close without saving " + mc.file.getName() + " ??", "JavaPad", 
                                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            // user wants to close the app without saving the file
            if(result == JOptionPane.YES_OPTION)
            {
               System.exit( 0 ); 
            }    
            else if(result == JOptionPane.NO_OPTION)
            {
                 saveFileHandler();
            }
           
        }
          
        // no file is edited, so simply close the app
        else            
            mc.dispose();
        
    }
    
    
    // method to save the contents of a file on the disk 
    void saveFile()
    {
        try {  
               pw = new PrintWriter( file ) ;
               
               String text = mc.ta.getText();
               
               if( text.isEmpty() )        // if nothing is in the textarea then "text" will be null
               {
                   text = "";
               }
               
               pw.print( text ); 
                    
               mc.setTitle( file.getAbsolutePath() + " - JavaPad");
               
              } 
              catch (IOException ioe)
              {
                 System.err.println( ioe.getMessage() );
              } 
              finally 
              {
                  //make file edit is false
                  mc.isFileEdit = false;
                  
                  // the writing file stream is closed here
                  pw.close();
                  
              }
        
    }

    
    // method to add line numbers after opening a file 
    // this method counts total number of '\n' in the text
    
    private void addLineNumbers()
    {  
        int count = MainClass.ta.getLineCount();
        
        mc.taLine.setText("");
           
        for (int i = 1; i <= count; i++) 
        {
             mc.taLine.append(i + "\n");
        }
       
    }
            
}

