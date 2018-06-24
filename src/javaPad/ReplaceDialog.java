// This class simulates the "find and replace" dialog box of the JavaPad app
// date: 04-10-14

/* As the Replace dialog contains "Find" feature, so we just called the static methods of the
 * Find dialog to find the "search query" and replace, if necessary
 * without creating methods for searching in the Replace dialog.
 */

package javaPad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ReplaceDialog extends JDialog implements ActionListener, WindowListener {

    // components and containers for the find dialog 
    JPanel p1, p2, p3,  basePanel;
    JLabel l1, l2;
    JTextField searchBox, replaceBox;
    JCheckBox matchCase, bkwdSearch;
    JButton close, find, replace, replaceAll;
    
    // to hold the "search" and "replace" query as given by the user 
    String searchQ, replaceQ; 
   
    MainClass mc;
     
    // these two variable keep track of whether "match case" and "backwards search" are checked or not
    boolean caseMatch, bkwS;
    

    public ReplaceDialog( MainClass mc) 
    {
        // this constructor of the JDialog gives the dialog its owner, name and modality 
        // the modality property of a dialog helps it to block input to its owner
        super( mc, "Find and Replace...", false );

        this.mc = mc;

        createDialog();
    }

    
    // this method creates the Find dialog box
    private void createDialog() 
    {  
        this.setSize( 400, 237 );
        this.setLayout( new BorderLayout() ); 
        this.setResizable(false);
        this.setLocationRelativeTo(mc);
        
        // a window listener is attached to the find dialog to keep track of the window closing event
        this.addWindowListener( this ); 
        
        // 4 panels are there to decorate the JDialog.
        // "basePanel" goes to CENTER of the JDialog and holds "p1" and "p2" panels. 
        // "p3" panel goes SOUTH of the JDialog and holds action buttons.
        // "p2" panel contains the options viz. "Match case" and "backward search".
        // "p1" panel contains the text fields: "searchbox" and "replacebox". 
        
        basePanel = new JPanel( new GridLayout(2, 0) ); 
        basePanel.setBorder( BorderFactory.createEmptyBorder(5, 10, 5, 10) ); 
        this.add( basePanel, BorderLayout.CENTER );
        
        p3 = new JPanel( new FlowLayout( FlowLayout.CENTER, 10, 5 ) );
        p3.setBorder( BorderFactory.createEmptyBorder(10, 10, 10, 10) ); 
        this.add(p3, BorderLayout.SOUTH );
        
        p1 = new JPanel( new FlowLayout(FlowLayout.LEADING) ) ;  
        p1.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) ); 
        p2 = new JPanel();
        p2.setBorder( BorderFactory.createEmptyBorder(5, 5, 5, 5) ); 
        p2.setLayout( new BoxLayout(p2, BoxLayout.Y_AXIS ) ); 
        
        // adding p1 and p2 to the basePanel
        basePanel.add(p1);
        basePanel.add( p2 );
        
       
        // setting up the p1 panel
        l1 = new JLabel("Search for:                ");
        l2 = new JLabel("Replace with:            ");
        
         // a text field also throws action event while ENTER/RETURN key is pressed
        searchBox = new JTextField(20);
        replaceBox = new JTextField(20);
        searchBox.addActionListener(this);
        replaceBox.addActionListener(this);

        p1.add(l1);     p1.add(searchBox);
        p1.add(l2);     p1.add(replaceBox);
        
        
         // setting up the 2nd panel 
        matchCase = new JCheckBox(" Match Case ");
        bkwdSearch = new JCheckBox(" Search Backwards ");
        matchCase.addActionListener(this);
        bkwdSearch.addActionListener(this);

        p2.add(Box.createHorizontalStrut(15));
        p2.add(matchCase);
        p2.add(Box.createHorizontalStrut(15));
        p2.add(bkwdSearch);
        
        
         // setting up the 3rd panel 
        replace = new JButton("Replace");
        replaceAll = new JButton("Replace All");
        close = new JButton("Close");
        find = new JButton("Find");
        replace.addActionListener(this);
        replaceAll.addActionListener(this);
        close.addActionListener(this);
        find.addActionListener(this);

        p3.add(replace);  p3.add(replaceAll);  p3.add(find);  p3.add(close);
       
        // in case of a jdialog, the setVisible() method should be called at last after adding all components to it
        // otherwise its components won't visible.
        this.setVisible(true);
    }

    
    // this method is overwritten and it gets called when an action event is thrown by a registered source
    public void actionPerformed(ActionEvent e) 
    {
        // these first fours for the "replace", "replace all", "close" and "find" buttons
        if ( e.getSource() == close) 
        {
            this.dispose();
          
            // assigning "rd" to null so that later we are able to create another Replace dialog 
            // after closing the current one.
            SearchMenuHandler.rd = null;
        }
        
        else if ( e.getSource() == replace )
        {
            // while replacing "search word" by "replace word", we need to search for the "serarch word" first.
            // As four kind of searching is there( detailed is given in "FindDialog.java" ), we take the help 
            // of the four searcing methods of the Find Dialog just by calling them; when required.
            
            // if checkQs() returns true than only "find and replace" is done.
            // replaceText() method just replaces the "search word" by "replace word".
            
            if( checkQs() )
            {
                if ( bkwS && caseMatch ) 
                {
                    replaceText();
                    
                    FindDialog.bkwMactchCase( searchQ );     
                }
                else if (bkwS && caseMatch == false) 
                {
                    replaceText();
                      
                    FindDialog.bkwNoMactchCase( searchQ );    
                } 
                else if (bkwS == false && caseMatch) 
                { 
                    replaceText();
                    
                    FindDialog.dwnMactchCase( searchQ );    
                } 
                else if (bkwS == false && caseMatch == false) 
                {
                    replaceText();
                    
                    FindDialog.dwnNoMactchCase( searchQ ); 
                }
            }
            
        }
        
        else if( e.getSource() == replaceAll )
        {
            // if checkQs() returns true than only "replace all" is done.
            // if "Match Case" is checked than we use a Java API otherwise "replaceAllIgnoreCase()" 
            
            if( checkQs() )
            {
                if( caseMatch )
                {
                    // here we first get the JavaPad's text in a String variable, use String.replaceAll() to replace
                    // all "searchQ" in the text by "replaceQ" and then put back the modified text in JavaPad's
                    // text area.
                    String text = MainClass.ta.getText();
                    
                    MainClass.ta.setText( text.replaceAll( searchQ, replaceQ ) ); 
                    
                    // after putting back the modified text in JavaPad's, the caret of "ta" and "taLine" is
                    // positioned at the beggining to display line number and the file from the starting.
                    
                    MainClass.ta.setCaretPosition(0);
                    mc.taLine.setCaretPosition(0);
                }
                else
                {
                    replaceAllIgnoreCase();
                    
                    mc.taLine.setCaretPosition(0);
                }
            }
        }
        
        // searching is also done when ENTER/RETURN key is pressed in the "searchbox" or "replace box".
        else if( e.getSource() == find || e.getSource() == searchBox || e.getSource() == replaceBox )
        {
           // if checkQs() returns true than only "Find" is perform.
           // Again, the methods of the Find dialog are invoked. 
            
            if( checkQs() )
            {
                if (bkwS && caseMatch) 
                {
                    FindDialog.bkwMactchCase( searchQ );
                }
                else if (bkwS && caseMatch == false) 
                {
                    FindDialog.bkwNoMactchCase( searchQ );
                } 
                else if (bkwS == false && caseMatch) 
                {
                    FindDialog.dwnMactchCase( searchQ );
                } 
                else if (bkwS == false && caseMatch == false) 
                {
                    FindDialog.dwnNoMactchCase( searchQ );
                }
            }
        }
        
        
        // these two "else if" to take care of the "search backwards" and "match case" options.
        // if one gets selected than that option is enabled and if deselected than disabled.
        
        else if ( e.getSource() == matchCase ) 
        {
            if ( matchCase.isSelected() ) 
            {
                caseMatch = true;
                
                // inform the static "caseMatch" variable of the SearchMenuHandler class that 
                // "Match case" option is enabled 
                SearchMenuHandler.caseMatch = caseMatch;
                
            } 
            else 
            {
                caseMatch = false;
                
                // inform the static "caseMatch" variable of the SearchMenuHandler class that 
                // "Match case" option is disabled 
                SearchMenuHandler.caseMatch = caseMatch;
                
            }
        } 
        else if ( e.getSource() == bkwdSearch ) 
        {
            if ( bkwdSearch.isSelected() ) 
            {
                bkwS = true;
                
            } 
            else 
            {
                bkwS = false;
                
            }
        }
    }

    
    // checkQs() checks whether "searchBox" and/or "MainClass.ta" are empty or not.
    // If empty gives warning message to the user and returns false.
    // otherwise return true to its caller.
   
    private boolean checkQs()
    {
        String temp = searchBox.getText();
        String txt = MainClass.ta.getText();
                
        // check if the "searchBox" is empty or not
        if( temp.isEmpty() )
        {
            // creats a beep
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(mc, "Empty search query !", "JavaPad", JOptionPane.WARNING_MESSAGE);

            // as "SearchMenuHandler.searchWord" is used for "find next" and "find previous" of a previously
            // given search query, so if search query is null than we should not be able to perform
            // "find next" and "find previous".
            SearchMenuHandler.searchWord = null;
            
            return false;
        }
        
        // check if JavaPad is empty or not
        if( txt.isEmpty() )
        {
            // creats a beep
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(mc, "Empty JavaPad !", "JavaPad", JOptionPane.WARNING_MESSAGE);

            return false;
        }
        
        
        // everything is ok, so the user's "search" and "replace" query is stored in the two String variables
        this.searchQ = temp;
        this.replaceQ = replaceBox.getText();
            
        // user's search query is preserved for "Find Next" and "Find Previous" 
        SearchMenuHandler.searchWord = searchQ;
            
        return true;
     
    }
   
    // replaceText() method replaces highlighted words ( which is nothing but the "search word" ) by "replace word".
    // while doing this, "Match case" option is considered.
   
    private void replaceText()
    {
        MainClass.ta.requestFocusInWindow();
         
        String selText = MainClass.ta.getSelectedText();
         
        // if "Match case" is checked, then "selText", which stors the selected text, is compared with the "searchQ"
        // using String.equals(). if matches then "searchQ" is replaced by "replaceQ" using
        // JTextArea.replaceSelection().
        
        // if "Match case" isn't checked then the comparison is done using String.equalsIgnoreCase() and the rest
        // is the same.
        
        if( caseMatch )
        {
            if( selText != null && selText.equals( searchQ ) )
            {
                MainClass.ta.replaceSelection( replaceQ ); 
            }
        }
        else
        {
            if( selText != null && selText.equalsIgnoreCase(searchQ ) )
            {
                MainClass.ta.replaceSelection( replaceQ );
            }
        }
        
    }
   
    
    // replaceAllIgnoreCase() is invoked to perform "replace all" when "Match case" is unchecked.
    // There is a one-to-one mapping of the characters( and there index ofcourse ) between the text in the MainClass.ta
    // and the String variable that we get using "MainClass.ta.getText()"; so if we find "search word" and its
    // position in the String variable, then we can use that position to track "search word" in the text 
    // of MainClass.ta.  
    
    private void replaceAllIgnoreCase()
    {
        // these two store starting and ending index of the "search word".
        // searching for the next occurence of the "serach word" should begin from index "end + 1" and in the beggening
        // it should start from the index 0. So, "end" is initialised with -1.
        
        int start = 0, end = -1;
     
        String text = MainClass.ta.getText();
       
        MainClass.ta.requestFocusInWindow();
        
        while(true)
        {
            // while searching for the "search word" in the String variable, both the "searchQ" and the text refered 
            // by the String variable should be of same case.
            
            text = text.toLowerCase();
            searchQ = searchQ.toLowerCase();
            
            start = text.indexOf( searchQ, end + 1 );
            end = start + searchQ.length();
           
            if( start == -1 )
                break;
            
           // replace the word in JavaPad determined by "start" and "end" by "replaceQ" using JTextArea.replaceRange(). 
          // the modified text of the JavaPad is stored again in the String variable for futher searching.
            
            MainClass.ta.replaceRange( replaceQ, start, end );
            text = MainClass.ta.getText();
            
        }
    }
            
    
    // why use WIndowListener?  The reason is given in the "FindDialog.java" file.
    
    public void windowClosing(WindowEvent e) 
    {
        this.dispose();
        
        // assigning "rd" to null so that later we are able to create another Replace dialog 
        // after closing the current one.  
        SearchMenuHandler.rd = null;   
    }

    public void windowOpened(WindowEvent e) {}
    
    public void windowClosed(WindowEvent e) {}
    
    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}

}
