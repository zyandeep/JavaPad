// This class simulates the "find" dialog box of the JavaPad app
// date: 04-10-14

/* we have four types of search here... forwards with No Match case,  forwards with Match case,
 backwards with No Match case and backwards with Match case, 
 so we have four methods to perform those four kind of searches. 
 */

package javaPad;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FindDialog extends JDialog implements ActionListener, WindowListener {

    // components and containers for the find dialog 
    JPanel p1, p2, p3;
    JLabel l1 ;
    JTextField searchBox;
    JCheckBox matchCase, bkwdSearch;
    JButton close, find;
    
    MainClass mc;
    
    // these two variable keep track whether "match case" and "backwards search" are checked or not
    boolean caseMatch, bkwS;
    

    public FindDialog(MainClass mc) 
    {
        // this constructor of the JDialog gives the dialog its owner, name and modality 
        // the modality property of a dialog helps it to block input to its owner
        super(mc, "Find...", false);

        this.mc = mc;

        createDialog();
    }

    
    // this method creates the Find dialog box
    private void createDialog() 
    {
        this.setLayout( new BorderLayout(5, 10) );
        this.setSize(380, 200);
        this.setResizable(false);
        this.setLocationRelativeTo(mc);
        
        // a window listener is attached to the find dialog to keep track of the window closing event
        this.addWindowListener(this); 

        // 3 panels are there to decorate the JDialog
        // 1st panel contains the query/search box, 2nd panel contains the options viz. "Match case" and "backward search"
        // 3rd panel contains the "find" and "close" buttons  

        p1 = new JPanel( new FlowLayout( FlowLayout.CENTER, 15, 15));
        p2 = new JPanel();
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
        p3 = new JPanel(new FlowLayout(FlowLayout.TRAILING, 10, 10));

        // setting up the 1st panel 
        l1 = new JLabel("Search what ? ");

        // a text field also throws action event while ENTER/RETURN key is pressed
        searchBox = new JTextField(20);
        searchBox.addActionListener(this);

        p1.add(l1);
        p1.add(searchBox);
        this.add(p1, BorderLayout.NORTH);


        // setting up the 3rd panel 
        close = new JButton("Close");
        close.addActionListener(this);
        find = new JButton("Find");
        find.addActionListener(this);

        p3.add(find);
        p3.add(close);
        this.add(p3, BorderLayout.SOUTH);


        // setting up the 2nd panel 
        matchCase = new JCheckBox(" Match Case ");
        bkwdSearch = new JCheckBox(" Search Backwards ");
        matchCase.addActionListener(this);
        bkwdSearch.addActionListener(this);

        p2.add(Box.createHorizontalStrut(15));
        p2.add(matchCase);
        p2.add(Box.createHorizontalStrut(15));
        p2.add(bkwdSearch);
        this.add(p2, BorderLayout.CENTER);

        // in case of a jdialog, the setVisible() method should be called at last after adding all components to it
        // otherwise its components won't visible

        this.setVisible(true);

    }

    
    // this method is overwritten and it gets called when an action event is thrown by a registered source
    public void actionPerformed(ActionEvent e) 
    {
        // the first two for the "close" and "find" buttons
        if (e.getSource() == close) 
        {
            this.dispose();
            
            // assigning "fd" to null so that later we are able to create another Find dialog 
            // after closing the current one.
            SearchMenuHandler.fd = null;
        }
        
        // the text field is also considered  
        else if (e.getSource() == find || e.getSource() == searchBox) 
        {
            // the string variable "query" holds the search query of the user
            String query = searchBox.getText();

            
            // if the query is empty, show a warning message followed by a beep and return from  "actionPerformed()"
            if ( query.isEmpty() ) 
            {    
                // creats a beep
                Toolkit.getDefaultToolkit().beep();

                JOptionPane.showMessageDialog(mc, "Empty search query !", "JavaPad", JOptionPane.WARNING_MESSAGE);

                // as "SearchMenuHandler.searchWord" is used for "find next" and "find previous" of a previously
                // given search query, so if search query is null than we should not be able to perform
                // "find next" and "find previous".
                SearchMenuHandler.searchWord = null;
                
                return;
            }

            
            // user's search query is preserved for "Find Next" and "Find Previous" 
            SearchMenuHandler.searchWord = query;
            

            // among the four kind of searches, a particular search is chosen on the basis of "bkwS" and "caseMatch"
            // variables. These two variable tells us how, this way...
            // bkwS : backward search enable
            // caseMatch : Match case enable
            // bkwS == false : backward search disable or normal downward/forward search enable
            // caseMatch == false : Match case disable
            
            
            // these four methods takes the search query as argument.
            if (bkwS && caseMatch) 
            {
                bkwMactchCase(query);
            }
            else if (bkwS && caseMatch == false) 
            {
                bkwNoMactchCase(query);
            } 
            else if (bkwS == false && caseMatch) 
            {
                dwnMactchCase(query);
            } 
            else if (bkwS == false && caseMatch == false) 
            {
                dwnNoMactchCase(query);
            }

        } 
        
        
        // these two "else if" to take care of the "search backwards" and "match case" options.
        // if one gets selected than that option is enabled and if deselected than disabled.
        
        else if (e.getSource() == matchCase) 
        {
            if ( matchCase.isSelected() ) 
            {
                caseMatch = true;
                
                // inform the static "caseMatch" variable of the SearchMenuHandler class that 
                // "Match case" option is enabled. 
                // required while performing "F3" and "F2" searching.
                SearchMenuHandler.caseMatch = caseMatch;
                
            } 
            else 
            {
                caseMatch = false;
                
                // inform the static "caseMatch" variable of the SearchMenuHandler class that 
                // "Match case" option is disabled.
                // required while performing "F3" and "F2" searching.
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

    
    // this method gets called when "search backward" and "Match case" are deselected
    // this is the most frequently used "NORMAL SEARCH".
    static void dwnNoMactchCase(String query) 
    {
        // the text area should be in focused to select and highlight the query word
        MainClass.ta.requestFocusInWindow();

        // these two variable determine the starting end ending index of the query word in the text, 
        // necessary while highlighting the word.
        
        int start, end;

        // as "Match case" is disabled, both the query word and the text of the text area are converted to the same case 
        String text = MainClass.ta.getText().toLowerCase();
        query = query.toLowerCase();

        
        // the indexOf() takes two arguments: query string and the index from where the the searching should begun
        // here the current caret position be the  "index from where the the searching should begun"
        // it returns the index of the first occurrence of the query string in the text.
        // after getting the starting the starting index, the ending index of the query string is calculated 
        
        start = text.indexOf(query, MainClass.ta.getCaretPosition());
        end = start + query.length();

        
        // if start == -1, than there is no occurrence of the search query in the text
        // otherwise, select the search word in the text using the select() 
        
        if (start == -1) 
        {
            // creats a beep
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(null, "Nothing found!!", "JavaPad", JOptionPane.INFORMATION_MESSAGE );
        } 
        else 
        {
            MainClass.ta.select(start, end);
        }

    }

    
    // this method gets called when "search backward" is deselected but "Match case" is selected
    static void dwnMactchCase(String query) 
    {
        MainClass.ta.requestFocusInWindow();

        int start, end;

        // this time "Match case" is enable
        // so no need to convert the text and the search string to the same case.
        String text = MainClass.ta.getText();

        
        start = text.indexOf( query, MainClass.ta.getCaretPosition() );
        end = start + query.length();

        if (start == -1) 
        {
            // creats a beep
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(null, "Nothing found!!", "JavaPad", JOptionPane.INFORMATION_MESSAGE );
        } 
        else 
        {
            MainClass.ta.select(start, end);
        }

    }

    
    // this method gets called when "search backward" is selected but "Match case" is deselected
    static void bkwNoMactchCase(String query) 
    {
        MainClass.ta.requestFocusInWindow();

        int start, end, carPos;

        // getCarPos() method determines the caret position or the index from where the "backward search" should begun
        // take the length of the search word as argument.  
        carPos = getCarPos(query.length() );

        String text = MainClass.ta.getText().toLowerCase();
        query = query.toLowerCase();

        
        // lastIndexOf() looks for the last occurrence of the "query" from a given index( here it is specified 
        // by getCarPos() ) in bottom to top manner and returns the starting index of the query in the text as soon as
        // it gets, returns -1 if nothing is found
        
        start = text.lastIndexOf(query, carPos);
        end = start + query.length();

        if (start == -1) 
        {
            // creats a beep
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(null, "Nothing found!!", "JavaPad", JOptionPane.INFORMATION_MESSAGE );
        } 
        else 
        {
            MainClass.ta.select(start, end);
        }

    }

    
    // this method gets called when "search backward" and "Match case" both are selected 
    static void bkwMactchCase(String query) 
    {
        MainClass.ta.requestFocusInWindow();

        int start, end, carPos;

        carPos = getCarPos( query.length() );

        // this time "Match case" is enable
        // so no need to convert the text and the search string to the same case.
        String text = MainClass.ta.getText();

        
        start = text.lastIndexOf(query, carPos);
        end = start + query.length();

        if (start == -1) 
        {
            // creats a beep
            Toolkit.getDefaultToolkit().beep();

            JOptionPane.showMessageDialog(null, "Nothing found!!", "JavaPad", JOptionPane.INFORMATION_MESSAGE );
        } 
        else 
        {
            MainClass.ta.select(start, end);
        }

    }

    
    // this method returns the index from where the "backward search" should begun
    private static int getCarPos(int strlen) 
    {

        // if there is no selected text currently in the text area then the current caret position should be the index
        // otherwise index should be the position  which is "length of the search query + 1" place 
        // before the current caret position.
        
        if (MainClass.ta.getSelectedText() == null) 
        {
            return MainClass.ta.getCaretPosition();
        } 
        else 
        {
            return (MainClass.ta.getCaretPosition() - (strlen + 1));
        }

    }

    
    // we keep track of the "windowClosing" event because of the following reason:
    // we need to make sure that only one instance of the "Find dialog" can be created. For this puspose
    // while creating a find dialog, we store its reference in a variable name "fd" of the SearchMenuHandler class.
    // but we only create the find dialog if "fd == null" which proves no ohter instance of Find dialog is created before.
    // and hence while closeing the find dialog we must make the "fd" variable null 
    //so that later we are able to create another Find dialog 
    
    public void windowClosing(WindowEvent e) 
    {
        this.dispose();
        
        // assigning "fd" to null so that later we are able to create another Find dialog 
        // after closing the current one.  
       SearchMenuHandler.fd = null;
     
    }

    
    public void windowOpened(WindowEvent e) {}

    public void windowClosed(WindowEvent e) {}
    
    public void windowIconified(WindowEvent e) {}

    public void windowDeiconified(WindowEvent e) {}

    public void windowActivated(WindowEvent e) {}

    public void windowDeactivated(WindowEvent e) {}

}
