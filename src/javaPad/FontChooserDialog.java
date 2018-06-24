// this class creates the font chooser dialog of the JavaPad
// date: 21-09-14

// in JDialog, the setVisible() method must be call at the end, after adding all components to the dialog
// else the components will not be displayed.

package javaPad;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.* ;

public class FontChooserDialog extends JDialog implements ActionListener, ListSelectionListener 
{

    // components and containers for the fc dialog
    private JPanel fontP, checkP, approvP;
    private JList nameList, typeList, sizeList ;
    private JButton ok, cancel;     
    private JLabel lb;
    private Font newFont;         // this variable holds the font created by the user
    
    FormatMenuHandler frmh;
    
    public FontChooserDialog( MainClass f, String t, boolean m ) 
    {
        // here, f = frame owner, t = title of the fc, m = modality of the JDialog (in this case "true")
        super(f, t, m);
        
        this.frmh = f.frmh;
       
        // method to design the fc dialog
        setUpDailog();
    }

    
    // this method is overwritten and it gets called when an action event is thrown by a registered source
    public void actionPerformed( ActionEvent e)
    {
        // handling font chooser events
        if(e.getSource() == ok)
        {
            //set the font of the notpad to the newly created font and quit the fc dialog
            
            if( newFont != null )
            {
                this.frmh.newFont = this.newFont;
            }
                
            this.dispose();
        }
         
        else if(e.getSource() == cancel)
        {   
            this.dispose();
        }
    }

    
    // this method is overwritten and it gets called when selection changes in a registered JList
    public void valueChanged(ListSelectionEvent e) 
    {
        String fontName = (String) nameList.getSelectedValue();     // holds font name
        int index = typeList.getSelectedIndex();              // holds an index that represent font type in "typeList" 
        int fontSize = (Integer) sizeList.getSelectedValue();     // holds font size
        
        int fontType = 0;
        
        // selecting the font type on the basis of the "index"
        if(index == 0)
            fontType = Font.PLAIN;
        else if(index == 1)
            fontType = Font.BOLD;
        else if(index == 2)
            fontType = Font.ITALIC;
        else
            fontType = Font.BOLD | Font.ITALIC;
        
        
        // creating the requested font, save it in "newFont" and display it to user through the Jlabel "lb".
         newFont =  new Font( fontName, fontType, fontSize );
         lb.setFont(newFont);
    }

    
    // this method creates the fc dialog box
    private void setUpDailog() 
    {
        setSize( 480, 380);
        setResizable(false); 
        setLocationRelativeTo( this.frmh.mc ); 
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        // 3 panels are there to decorate the fc dialog.
        // "fontP" panel goes NORTH of the JDialog and holds font name. type and size list.
        // "checkP" panel shows the sample text.
        // "approvP" panel holds "ok" and "cancle" buttons 
        
        fontP = new JPanel( new FlowLayout( FlowLayout.CENTER, 10, 5) );  
        approvP = new JPanel( new FlowLayout( FlowLayout.CENTER, 15, 5 )); 
        checkP = new JPanel();
        checkP.setBorder( BorderFactory.createTitledBorder("<html><b> Sample Text </b></html>") );
        
        
        // setting up the "fontP" panel
        // "GraphicsEnvironment.getAvailableFontFamilyNames()" returns all the installed fonts names of the system
        
        GraphicsEnvironment gev = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = gev.getAvailableFontFamilyNames();         
        nameList = new JList( fonts );
        nameList.setSelectedValue("Courier New", true);
        
        // JList.setSelectionMode() help us to decide how many items we can select in a list at a time.
        // by default, MULTIPLE SELECTION is allowed.
        
        nameList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        nameList.addListSelectionListener(this); 
        
        // after creating a JList, it should be added to a JScrolPane to support hoeizontal and vertical scrolbar.
        JScrollPane scp1 = new JScrollPane(nameList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scp1.setBorder( BorderFactory.createTitledBorder("Font Name")); 
        fontP.add(scp1);
        
        // the names of font types are formatted using HTML acc to their type
        String[] fontTypes = {"Plain", "<html><b>Bold</b></html>", "<html><i>Italic</i><html>", "<html><b><i>Bold-Italic</i></b></html>"};
        
        typeList = new JList( fontTypes ); 
        typeList.setSelectedValue( fontTypes[0], true );
        typeList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        typeList.setFixedCellWidth(100);
        typeList.addListSelectionListener(this);
        typeList.setBorder( BorderFactory.createTitledBorder("Font Style")); 
        fontP.add(typeList);
        
        
        // "DefaultListModel" allow us to add, remove items in/from a list individually. and which is helpfull here
        // in adding font sizes individually by a loop in "sizeList". 
        DefaultListModel model = new DefaultListModel();
        
        for(int i = 10; i <= 70; i++)
        {
            model.addElement(i); 
        }
       
        sizeList = new JList(model);
        sizeList.setSelectedValue(23, true );
        sizeList.setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        sizeList.setFixedCellWidth(40); 
        sizeList.addListSelectionListener(this);
        
        JScrollPane scp2 = new JScrollPane(sizeList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scp2.setBorder( BorderFactory.createTitledBorder("Font Size")); 
        fontP.add(scp2);
        
        this.add(fontP, BorderLayout.NORTH); 
        
        
        // setting up the "checkP" panel
        // the text displayed by "lb" is for testing how the currently chosen font is look like. 
        
        lb = new JLabel(" Java is Plate-Form independent !!");
        checkP.add(lb);
        
        this.add(checkP, BorderLayout.CENTER);
        
        
        // setting up approve buttons in the "approvP" panel
        ok = new JButton("<html><b> OK </b></html>");
        ok.addActionListener(this);
        cancel = new JButton("<html><b> Cancel </b></html>");
        cancel.addActionListener(this);
              
        approvP.add(ok);
        approvP.add(cancel);
        
        this.add(approvP, BorderLayout.SOUTH);
    }
    
    
}
