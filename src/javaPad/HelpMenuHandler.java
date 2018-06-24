// this class handles all the Help menu items' events
// date: 24-09-14
package javaPad;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.io.*;

public class HelpMenuHandler implements ActionListener {

    MainClass mc;
    JDialog about;
    JPanel p1, p2, p3;
    JButton lis, close;
    JLabel image;

    // explicitly created card layout object so that the object it can be used later while flipping through the cards
    CardLayout cl;

    // JTextArea object is created as member so that it can be used later while reading the gpl 
    JTextArea ta;

    public HelpMenuHandler(MainClass mc) {
        this.mc = mc;

        cl = new CardLayout();
    }

    // this method is overwritten and it gets called when an action event is thrown by a registered source
    public void actionPerformed(ActionEvent e) {
        // handling Help menu items' event  
        if (e.getSource() == mc.about) {
            displayAbout();
        } // handle about-dialog-buttons' events 
        else if (e.getSource() == this.lis) {
            // method to display the GPL 
            displayGPL();
        } else if (e.getSource() == this.close) {
            about.dispose();
        }

    }

    // creates the "about" dialog box
    private void displayAbout() {
        about = new JDialog(mc, "About", true);
        about.setSize(380, 350);
        about.setResizable(false);
        about.setLayout(new BorderLayout(5, 10));
        about.setLocationRelativeTo(mc);
        about.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // created the three panels which are to add at the NORTH, CENTER, SOUTH of the dialog  
        // 1st panel contains app's icon, name and its version.
        // 2nd panel employs cardLayout manager and holds two cards
        // 3rd panel holds "License" and "Close" buttons
        p1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        p1.setBackground(Color.white);
        p2 = new JPanel(cl);
        p3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 80, 10));

        // retrieving the image icon of the JavaPad from the jar as a resource.
        // argument to the getResource() is a path relative to the class that invokes it.
        // getResource returns a URL to the image and it'll like  "javaPad.icons.aboutJpad" 
        URL imageURL = HelpMenuHandler.class.getResource("icons/aboutJpad.png");
        image = new JLabel(new ImageIcon(imageURL));
        String javapad = "<html><font size = 10 color = red><b> JavaPad </b></font><br>"
                + "<b><font size = 5> version: 1.0 </font></b></html>";

        p1.add(image);
        p1.add(new JLabel(javapad));

        about.add(p1, BorderLayout.NORTH);

        // cardLayout prefers panels as cards 
        // components to be shown should be added to the panels
        JPanel card1 = new JPanel();
        JPanel card2 = new JPanel();

        // 1st card contains info about JavaPad and its developers 
        String aboutJpad1 = "<html><b><font size = 4> JavaPad is a small and lightweight text editor. </font></b></html>";
        String aboutJpad2 = "<html><b><font size = 4> JavaPad requires JRE 7 (or above) to run. </font></b></html>";

        String aboutJpad3 = "<html><b><font size = 4><br> Developers:&nbsp Puspendra Pandey <br>"
                + "&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp"
                + "&nbsp&nbsp  Zyandeep Baruah</font></b></html>";

        card1.add(new JLabel(aboutJpad1));
        card1.add(new JLabel(aboutJpad2));
        card1.add(new JLabel(aboutJpad3));

        // 2nd card displays the gpl in a textarea wrapped up by a scrollpane
        ta = new JTextArea();
        ta.setFont(new Font("consolas", Font.PLAIN, 12));
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        ta.setEditable(false);

        // method to display the GNU-GPL in the text area
        readGPL();

        // a layout manager determines a component's dimension from its "preferred size"
        // so the "preferred size" of the scrolpane is customised to meet our need
        JScrollPane sp = new JScrollPane(ta);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        sp.setPreferredSize(new Dimension(350, 130));

        card2.add(sp);

        // add the both "cards" to the panel which is in the CENTER of the dialog along with a name
        // name helps to refer to a particular "card" directly 
        p2.add(card1, "card1");
        p2.add(card2, "card2");

        // add the panel with cardLayout to the centre of the dialog 
        about.add(p2, BorderLayout.CENTER);

        lis = new JButton("Lisense");
        lis.addActionListener(this);
        close = new JButton("Close");
        close.addActionListener(this);

        p3.add(lis);
        p3.add(close);
        about.add(p3, BorderLayout.SOUTH);

        // in case of a jdialog, the setVisible() method should be called at last after adding all components to it
        // otherwise its components won't visible
        about.setVisible(true);
    }

    // this method shows the next "card" of the cardLayout
    // if gpl license is currently visible, pressing "License" button shows the next card i.e. the panel containing
    // info about the app and developers and vice-versa
    private void displayGPL() {
        // show the next "card"
        cl.next(p2);
    }

    // this method reads the gpl license and display its contents in the text area
    private void readGPL() {
        InputStream is = null;

        // InputStreamReader is a byte-to-character "bridge" streams
        // It wraps up a byte stream with character stream
        // As "is" is byte stream and "gpl.txt" is s text file, so a character stream is required to read the text file
        // this need is fulfilled with InputStream class
        InputStreamReader isr = null;
        BufferedReader br = null;

        String text = "";

        try {
            // getResourceAsStream() is used to read a file from a jar. 
            // Its connects the file with byte stream and returns a InputStream instance
            // argument to the getResourceAsStream() is a path relative to the class that invokes it.

            // its recommended to open and read a file within the same try block
            is = HelpMenuHandler.class.getResourceAsStream("files/gpl.txt");
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            while (true) {
                text = br.readLine();

                if (text == null) {
                    break;
                }

                ta.append(text + "\n");
            }

           // after reading the whole file the caret will be at the end of the file and we will see the end of the file 
            // hence it is positioned at starting of the file so that we can see the file from its starting
            ta.setCaretPosition(0);
        } catch (IOException io) {
            System.err.println(io.getMessage());
        } finally {
            // a file stream should be closed inside a finally block
            // which ensures that the file will always get closed even if there is an exception while opening
            // or reading the file

            // if br == null, there is an exception while opening the file
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
        }

    }

}
