/**
 * this class is the starting point of the JavaPad application. It contains the
 * main method and creates the JavaPads window. This class instantiates all the
 * other modules/class.
 *
 * date: 04-09-14
 */
package javaPad;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;

public class MainClass extends JFrame {

    JMenuBar menuBar;
    JMenu fileMenu, editMenu, viewMenu, searchMenu, optionMenu, helpMenu, caseConvert, changeLnF;
    JToolBar toolBar;
    JButton newFileB, openFileB, saveFileB, printB, undoB, redoB, cutB, copyB, pasteB, findB, findNRepB;

    // textarea is decleared as static so that it can be accessed from a class without instantiating MainClass there
    static JTextArea ta;

    JTextArea taLine;
    JScrollPane sp;
    JPanel statusBar;
    JLabel rowColNum, fileLength, selection;
    JPopupMenu popupMenu;
    JMenuItem cut, copy, paste, del, undo, redo, selectAll, dataNtime,
            newFile, openFile, saveFile, saveAsFile, print, pageSetup, quit,
            cutE, copyE, pasteE, delE, undoE, redoE, selectAllE, dataNtimeE, upper, lower, fileSum,
            find, findNext, findPrev, replace, gotoLine, font, tColor, bColor, cColor, tabSize, about,
            restor;

    JCheckBoxMenuItem tBar, stBar, lineNum, wordWrap;
    JRadioButtonMenuItem java, motif;

    // true if a file is edited (inseterd or deleted any character)
    boolean isFileEdit = false;

    //font of the textarea, color of the caret, text and body of the text area 
    Font textFont;
    Color body, text, caret;

    // represents current file that is being displyed by the textarea
    File file;

    // instantiating caratListner object
    CaratHandler carat;

    // instantiating documentListener object
    DocumentHandler doc;

    // it controls all the File menu operation  
    FileMenuHandler fmh;

    // controls all the Edit menu operation  
    EditMenuHandler emh;

    // controls all the View menu operation  
    ViewMenuHandler vmh;

    // controls all the Format menu operation  
    FormatMenuHandler frmh;

    // controls all the Toolbar operation  
    ToolBarHandler tbh;

    // controls all the PopUp menu operation  
    PopUpHandler puh;

    // controls all the Help menu operation  
    HelpMenuHandler hmh;

    // controls all the Search menu operation  
    SearchMenuHandler smh;

    // true if the file is being opened. It helps displaying line number after opening a file 
    boolean isOpenFile;

    public MainClass() {
        // creating a file object
        file = new File("New File");

        // Must be called before creating any JDialog for the desired effect
        // it decorates the border of every jdialog in the Java/Basic LnF
        JDialog.setDefaultLookAndFeelDecorated(true);

        setTitle(file.getName() + " - JavaPad");

        // JFrame will be appered maximised in starting 
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // retriving an image from within a jar as resource by getResource()
        // argument to the getResource() is a path relative to the class that invokes it.
        URL imageURL = MainClass.class.getResource("icons/javaPad.png");
        setIconImage(new ImageIcon(imageURL).getImage());

        addWindowListener(new WindowHandler(this));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        carat = new CaratHandler(this);
        doc = new DocumentHandler(this);

        // connects the app with its menu handlers 
        fmh = new FileMenuHandler(this);
        emh = new EditMenuHandler(this);
        vmh = new ViewMenuHandler(this);
        frmh = new FormatMenuHandler(this);
        tbh = new ToolBarHandler(this);
        puh = new PopUpHandler(this);
        hmh = new HelpMenuHandler(this);
        smh = new SearchMenuHandler(this);

        setUpMenuBar();
        setUpToolBar();
        setUpFileMenu();
        setUpEditMenu();
        setUpViewMenu();
        setUpSearchMenu();
        setUpFormatMenu();
        setUpHelpMenu();

        // instantiating the the textarea that create/displays a file
        ta = new JTextArea();
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        sp = new JScrollPane(ta);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(sp, BorderLayout.CENTER);

        // adding Listeners to the text area
        ta.addCaretListener(carat);
        ta.getDocument().addDocumentListener(doc);
        ta.getDocument().addUndoableEditListener(emh);         // to listne to "redo", "undo" actions

        setUpPopUpMenu();

        //set up the text area to show line numbers
        taLine = new JTextArea("1\n");
        taLine.setMargin(new Insets(1, 15, 1, 5));
        taLine.setBackground(Color.LIGHT_GRAY);
        taLine.setEditable(false);
        sp.setRowHeaderView(taLine);

        // load the user/default customisation of the text area
        loadSetting();

        // setting up the status bar
        statusBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 70, 2));
        rowColNum = new JLabel("|       row: 1   col: 1 ");
        fileLength = new JLabel("|      File Length:  0");
        selection = new JLabel("|      Selection from: ");

        statusBar.add(fileLength);
        statusBar.add(selection);
        statusBar.add(rowColNum);

        this.add(statusBar, BorderLayout.PAGE_END);

        // put focus on the textarea of the JavaPad 
        ta.requestFocusInWindow();

        // listne to resining event of the JavaPad's window
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                // after the resizing has finished update the line-no-display 
                doc.updateLines();
            }
        });

        // make the JFrame visible
        this.setVisible(true);

    }

    // method to setup the manu bar
    private void setUpMenuBar() {
        menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        // create the main menus for the app
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        searchMenu = new JMenu("Search");
        searchMenu.setMnemonic('S');
        optionMenu = new JMenu("Format");
        optionMenu.setMnemonic('F');
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);

        // add menus to the menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(searchMenu);
        menuBar.add(optionMenu);
        menuBar.add(helpMenu);
    }

    // method to setup the tool bar
    private void setUpToolBar() {
        toolBar = new JToolBar();
        toolBar.setBorderPainted(false);
        toolBar.setRollover(true);
        toolBar.setFloatable(false);
        this.add(toolBar, BorderLayout.PAGE_START);

        // adding actions to toolbar
        newFileB = new JButton(new ImageIcon(MainClass.class.getResource("icons/newFile.png")));
        newFileB.setToolTipText("New File");
        newFileB.addActionListener(tbh);

        openFileB = new JButton(new ImageIcon(MainClass.class.getResource("icons/openFile.png")));
        openFileB.setToolTipText("Open File...");
        openFileB.addActionListener(tbh);

        saveFileB = new JButton(new ImageIcon(MainClass.class.getResource("icons/saveFile.png")));
        saveFileB.setToolTipText("Save File...");
        saveFileB.addActionListener(tbh);

        printB = new JButton(new ImageIcon(MainClass.class.getResource("icons/print.png")));
        printB.setToolTipText("Print...");
        printB.addActionListener(tbh);

        undoB = new JButton(new ImageIcon(MainClass.class.getResource("icons/undo.png")));
        undoB.setToolTipText("Undo");
        undoB.addActionListener(tbh);

        redoB = new JButton(new ImageIcon(MainClass.class.getResource("icons/redo.png")));
        redoB.setToolTipText("Redo");
        redoB.addActionListener(tbh);

        cutB = new JButton(new ImageIcon(MainClass.class.getResource("icons/cut.png")));
        cutB.setToolTipText("Cut");
        cutB.addActionListener(tbh);

        copyB = new JButton(new ImageIcon(MainClass.class.getResource("icons/copy.png")));
        copyB.setToolTipText("Copy");
        copyB.addActionListener(tbh);

        pasteB = new JButton(new ImageIcon(MainClass.class.getResource("icons/paste.png")));
        pasteB.setToolTipText("Paste");
        pasteB.addActionListener(tbh);

        findB = new JButton(new ImageIcon(MainClass.class.getResource("icons/find.png")));
        findB.setToolTipText("Find...");
        findB.addActionListener(tbh);

        findNRepB = new JButton(new ImageIcon(MainClass.class.getResource("icons/findNRep.png")));
        findNRepB.setToolTipText("Find and replace...");
        findNRepB.addActionListener(tbh);

        // sets items disable 
        cutB.setEnabled(false);
        copyB.setEnabled(false);
        findB.setEnabled(false);
        findNRepB.setEnabled(false);
        undoB.setEnabled(false);
        redoB.setEnabled(false);

        // adding action buttons to the toolbar
        toolBar.add(newFileB);
        toolBar.add(openFileB);
        toolBar.add(saveFileB);
        toolBar.addSeparator();
        toolBar.add(printB);
        toolBar.addSeparator();
        toolBar.add(undoB);
        toolBar.add(redoB);
        toolBar.addSeparator();
        toolBar.add(copyB);
        toolBar.add(cutB);
        toolBar.add(pasteB);
        toolBar.addSeparator();
        toolBar.add(findB);
        toolBar.add(findNRepB);

    }

    private void setUpPopUpMenu() {
        popupMenu = new JPopupMenu();

        cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        cut.addActionListener(puh);

        copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        copy.addActionListener(puh);

        paste = new JMenuItem("paste");
        paste.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        paste.addActionListener(puh);

        del = new JMenuItem("Delete");
        del.addActionListener(puh);

        redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
        redo.addActionListener(puh);

        undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undo.addActionListener(puh);

        selectAll = new JMenuItem("Select all");
        selectAll.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
        selectAll.addActionListener(puh);

        dataNtime = new JMenuItem("Insert date & time   ");
        dataNtime.setAccelerator(KeyStroke.getKeyStroke("F5"));
        dataNtime.addActionListener(puh);

        undo.setEnabled(false);
        redo.setEnabled(false);
        cut.setEnabled(false);
        copy.setEnabled(false);
        del.setEnabled(false);

        // adding actions to the popup menu
        popupMenu.add(undo);
        popupMenu.add(redo);
        popupMenu.addSeparator();
        popupMenu.add(cut);
        popupMenu.add(copy);
        popupMenu.add(paste);
        popupMenu.add(del);
        popupMenu.addSeparator();
        popupMenu.add(selectAll);
        popupMenu.addSeparator();
        popupMenu.add(dataNtime);

        // adding the PopUPMenu with the target container
        ta.setComponentPopupMenu(popupMenu);
    }

    private void setUpFileMenu() {
        // creating the menu items of the file menu
        newFile = new JMenuItem("New File...", new ImageIcon(MainClass.class.getResource("icons/newFile16.png")));
        newFile.setIconTextGap(10);
        newFile.setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        newFile.setMnemonic(KeyEvent.VK_N);
        newFile.addActionListener(fmh);

        openFile = new JMenuItem("Open File...", new ImageIcon(MainClass.class.getResource("icons/openFile16.png")));
        openFile.setIconTextGap(10);
        openFile.setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        openFile.setMnemonic(KeyEvent.VK_O);
        openFile.addActionListener(fmh);

        saveFile = new JMenuItem("Save", new ImageIcon(MainClass.class.getResource("icons/save16.png")));
        saveFile.setIconTextGap(10);
        saveFile.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveFile.setMnemonic(KeyEvent.VK_S);
        saveFile.addActionListener(fmh);

        saveAsFile = new JMenuItem("Save As...", new ImageIcon(MainClass.class.getResource("icons/saveAs16.png")));
        saveAsFile.setIconTextGap(10);
        saveAsFile.setAccelerator(KeyStroke.getKeyStroke("ctrl alt S"));
        saveAsFile.setMnemonic(KeyEvent.VK_A);
        saveAsFile.addActionListener(fmh);

        print = new JMenuItem("Print...", new ImageIcon(MainClass.class.getResource("icons/print16.png")));
        print.setIconTextGap(10);
        print.setAccelerator(KeyStroke.getKeyStroke("ctrl P"));
        print.setMnemonic(KeyEvent.VK_P);
        print.addActionListener(fmh);

        pageSetup = new JMenuItem("Page Setup...");
        pageSetup.setIconTextGap(10);
        pageSetup.setMnemonic(KeyEvent.VK_U);
        pageSetup.addActionListener(fmh);

        quit = new JMenuItem("Exit", new ImageIcon(MainClass.class.getResource("icons/exit.png")));
        quit.setIconTextGap(10);
        quit.setAccelerator(KeyStroke.getKeyStroke("alt F4"));
        quit.setMnemonic(KeyEvent.VK_E);
        quit.addActionListener(fmh);

        // adding the menu items to the file menu
        fileMenu.add(newFile);
        fileMenu.add(openFile);
        fileMenu.addSeparator();
        fileMenu.add(saveFile);
        fileMenu.add(saveAsFile);
        fileMenu.addSeparator();
        fileMenu.add(print);
        fileMenu.add(pageSetup);
        fileMenu.addSeparator();
        fileMenu.add(quit);
    }

    private void setUpEditMenu() {
        // creating the menu items of the edit menu

        undoE = new JMenuItem("Undo", new ImageIcon(MainClass.class.getResource("icons/undo16.png")));
        undoE.setIconTextGap(10);
        undoE.setAccelerator(KeyStroke.getKeyStroke("ctrl Z"));
        undoE.setMnemonic(KeyEvent.VK_U);
        undoE.addActionListener(emh);

        redoE = new JMenuItem("Redo", new ImageIcon(MainClass.class.getResource("icons/redo16.png")));
        redoE.setIconTextGap(10);
        redoE.setAccelerator(KeyStroke.getKeyStroke("ctrl Y"));
        redoE.setMnemonic(KeyEvent.VK_R);
        redoE.addActionListener(emh);

        cutE = new JMenuItem("Cut", new ImageIcon(MainClass.class.getResource("icons/cut16.png")));
        cutE.setIconTextGap(10);
        cutE.setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        cutE.setMnemonic(KeyEvent.VK_T);
        cutE.addActionListener(emh);

        copyE = new JMenuItem("Copy", new ImageIcon(MainClass.class.getResource("icons/copy16.png")));
        copyE.setIconTextGap(10);
        copyE.setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        copyE.setMnemonic(KeyEvent.VK_C);
        copyE.addActionListener(emh);

        pasteE = new JMenuItem("Paste", new ImageIcon(MainClass.class.getResource("icons/paste16.png")));
        pasteE.setIconTextGap(10);
        pasteE.setAccelerator(KeyStroke.getKeyStroke("ctrl V"));
        pasteE.setMnemonic(KeyEvent.VK_P);
        pasteE.addActionListener(emh);

        delE = new JMenuItem("Delete", new ImageIcon(MainClass.class.getResource("icons/delete.png")));
        delE.setIconTextGap(10);
        delE.setMnemonic(KeyEvent.VK_D);
        delE.addActionListener(emh);

        selectAllE = new JMenuItem("Select All", new ImageIcon(MainClass.class.getResource("icons/selectAll.png")));
        selectAllE.setIconTextGap(10);
        selectAllE.setAccelerator(KeyStroke.getKeyStroke("ctrl A"));
        selectAllE.setMnemonic(KeyEvent.VK_A);
        selectAllE.addActionListener(emh);

        dataNtimeE = new JMenuItem("Insert Time/Date");
        dataNtimeE.setIconTextGap(10);
        dataNtimeE.setAccelerator(KeyStroke.getKeyStroke("F5"));
        dataNtimeE.setMnemonic(KeyEvent.VK_D);
        dataNtimeE.addActionListener(emh);

        caseConvert = new JMenu("Convert Case to...");
        caseConvert.setMnemonic(KeyEvent.VK_O);
        lower = new JMenuItem("   lower  ");
        lower.setAccelerator(KeyStroke.getKeyStroke("ctrl shift U"));
        lower.setMnemonic(KeyEvent.VK_L);
        lower.addActionListener(emh);

        upper = new JMenuItem("   UPPER  ");
        upper.setAccelerator(KeyStroke.getKeyStroke("ctrl U"));
        upper.setMnemonic(KeyEvent.VK_U);
        upper.addActionListener(emh);

        caseConvert.add(lower);
        caseConvert.add(upper);

        // adding the menu items to the file menu
        editMenu.add(undoE);
        editMenu.add(redoE);
        editMenu.addSeparator();
        editMenu.add(cutE);
        editMenu.add(copyE);
        editMenu.add(pasteE);
        editMenu.add(delE);
        editMenu.addSeparator();
        editMenu.add(selectAllE);
        editMenu.add(dataNtimeE);
        editMenu.addSeparator();
        editMenu.add(caseConvert);

        // these menu items don't require in the starting of the app ( 'cause the text area will be empty )
        undoE.setEnabled(false);
        lower.setEnabled(false);
        redoE.setEnabled(false);
        upper.setEnabled(false);
        cutE.setEnabled(false);
        copyE.setEnabled(false);
        delE.setEnabled(false);
    }

    private void setUpViewMenu() {
        // creating the menu items of the view menu
        tBar = new JCheckBoxMenuItem("Tool bar", true);
        tBar.setMnemonic(KeyEvent.VK_T);
        tBar.addActionListener(vmh);

        stBar = new JCheckBoxMenuItem("Status bar", true);
        stBar.setMnemonic(KeyEvent.VK_S);
        stBar.addActionListener(vmh);

        lineNum = new JCheckBoxMenuItem("Line numbers  ", true);
        lineNum.setMnemonic(KeyEvent.VK_L);
        lineNum.addActionListener(vmh);

        fileSum = new JMenuItem("File summary...   ");
        fileSum.setMnemonic(KeyEvent.VK_F);
        fileSum.setAccelerator(KeyStroke.getKeyStroke(" ctrl alt F"));
        fileSum.addActionListener(vmh);

        // adding the menu items to the view menu
        viewMenu.add(tBar);
        viewMenu.add(stBar);
        viewMenu.add(lineNum);
        viewMenu.addSeparator();
        viewMenu.add(fileSum);
    }

    private void setUpSearchMenu() {
        // creating the menu items of the search  menu
        find = new JMenuItem("Find...", new ImageIcon(MainClass.class.getResource("icons/find16.png")));
        find.setIconTextGap(10);
        find.setMnemonic(KeyEvent.VK_F);
        find.setAccelerator(KeyStroke.getKeyStroke("ctrl F"));
        find.addActionListener(smh);

        findNext = new JMenuItem(" Find Next");
        findNext.setMnemonic(KeyEvent.VK_N);
        findNext.setAccelerator(KeyStroke.getKeyStroke("F3"));
        findNext.addActionListener(smh);

        findPrev = new JMenuItem(" Find Previous");
        findPrev.setMnemonic(KeyEvent.VK_P);
        findPrev.setAccelerator(KeyStroke.getKeyStroke("F2"));
        findPrev.addActionListener(smh);

        replace = new JMenuItem("Replace...", new ImageIcon(MainClass.class.getResource("icons/findNRep16.png")));
        replace.setIconTextGap(10);
        replace.setMnemonic(KeyEvent.VK_R);
        replace.setAccelerator(KeyStroke.getKeyStroke("ctrl H"));
        replace.addActionListener(smh);

        gotoLine = new JMenuItem("Go to Line...");
        gotoLine.setMnemonic(KeyEvent.VK_G);
        gotoLine.setAccelerator(KeyStroke.getKeyStroke("ctrl G"));
        gotoLine.addActionListener(smh);

        // these menu items don't require in the starting of the app ( 'cause the text area will be empty )
        find.setEnabled(false);
        findNext.setEnabled(false);
        findPrev.setEnabled(false);
        replace.setEnabled(false);

        // adding the menu items to the search menu
        searchMenu.add(find);
        searchMenu.add(findNext);
        searchMenu.add(findPrev);
        searchMenu.add(replace);
        searchMenu.addSeparator();
        searchMenu.add(gotoLine);
    }

    private void setUpFormatMenu() {
        // creating the menu items of the format menu

        wordWrap = new JCheckBoxMenuItem("Word Wrap ", true);
        wordWrap.setMnemonic(KeyEvent.VK_W);
        wordWrap.setAccelerator(KeyStroke.getKeyStroke("ctrl W"));
        wordWrap.addActionListener(frmh);

        tabSize = new JMenuItem("Tab Size... ");
        tabSize.setMnemonic(KeyEvent.VK_S);
        tabSize.addActionListener(frmh);

        font = new JMenuItem("Font... ");
        font.setMnemonic(KeyEvent.VK_F);
        font.addActionListener(frmh);

        tColor = new JMenuItem("Text Color... ");
        tColor.setMnemonic(KeyEvent.VK_T);
        tColor.addActionListener(frmh);

        bColor = new JMenuItem("Body Color... ");
        bColor.setMnemonic(KeyEvent.VK_B);
        bColor.addActionListener(frmh);

        cColor = new JMenuItem("Caret Color... ");
        cColor.setMnemonic(KeyEvent.VK_C);
        cColor.addActionListener(frmh);

        restor = new JMenuItem("Restor default setting");
        restor.setMnemonic(KeyEvent.VK_C);
        restor.addActionListener(frmh);

        changeLnF = new JMenu("Change Look-N-Feel... ");
        java = new JRadioButtonMenuItem("Java", true);
        motif = new JRadioButtonMenuItem("Motif");

        //adding actionListeners to the radio buttons
        java.addActionListener(frmh);
        motif.addActionListener(frmh);

        // adding radio buttons to the same group so that only one of them can be selected at a time
        ButtonGroup bg = new ButtonGroup();
        bg.add(java);
        bg.add(motif);

        // adding radio buttons to the LnF menu
        changeLnF.add(java);
        changeLnF.add(motif);

        // adding menu items to the option menu
        optionMenu.add(wordWrap);
        optionMenu.add(tabSize);
        optionMenu.addSeparator();
        optionMenu.add(font);
        optionMenu.add(tColor);
        optionMenu.add(bColor);
        optionMenu.add(cColor);
        optionMenu.add(restor);
        optionMenu.addSeparator();
        optionMenu.add(changeLnF);

    }

    private void setUpHelpMenu() {
        about = new JMenuItem("About JavaPad ", new ImageIcon(MainClass.class.getResource("icons/about.png")));
        about.setIconTextGap(10);
        about.setAccelerator(KeyStroke.getKeyStroke("shift F1"));
        about.setMnemonic(KeyEvent.VK_J);
        about.addActionListener(hmh);

        // adding the menu items to the help menu
        helpMenu.add(about);

    }

    /*
     *  This method reads the "setting.bin" file or the default "setting.bin" file that stores the default 
     *  preference of the text area and apply the preferences.
     * 
     *  Preferences include:  font size, color, type of the text in the text area, And 
     *                        text, background and the caret color 
     */
    final void loadSetting() {
        // try to read from the file in the user's home directory that sotres user's preferences 
        File setting = new File(frmh.homeDir, "setting.bin");

        // if the file exist that means user has changed the default setting so, read from that file. 
        if (setting.exists()) {
            FileInputStream fis = null;     // binary file, hence binary input stream
            ObjectInputStream ois = null;   // for object serialisation

            // its recommended to open and read a file within the same try block
            try {
                fis = new FileInputStream(setting);
                ois = new ObjectInputStream(fis);

                textFont = (Font) ois.readObject();       // read the font of text area
                body = (Color) ois.readObject();         // read the body color text area
                text = (Color) ois.readObject();         // read the text color text area
                caret = (Color) ois.readObject();        // read the caret color 

                // appling font
                ta.setFont(textFont);
                taLine.setFont(textFont);

                // applyling text, body and caret color
                ta.setBackground(body);
                ta.setForeground(text);
                ta.setCaretColor(caret);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            } finally {
                // a file stream should be closed inside a finally block
                // which ensures that the file will always get closed even if there is an exception while opening
                // or reading the file

                // if ois == null, there is an exception while opening the file
                try {

                    if (ois != null) {
                        ois.close();
                    }

                } catch (IOException ioe) {
                    System.out.println(ioe.getMessage());
                }

            }
        } else {
            //load deafult setting
            loadDefaultSetting();
        }

    }

    // this method loads the default preference of the JavaPad from the file "setting.bin"
    void loadDefaultSetting() {
         // getResourceAsStream() is used to read a file from a jar. 
        // Its connects the file with byte stream and returns a InputStream instance
        // argument to the getResourceAsStream() is a path relative to the class that invokes it.

        InputStream is = null;
        ObjectInputStream ois = null;

        // 
        try {
            is = MainClass.class.getResourceAsStream("files/setting.bin");
            ois = new ObjectInputStream(is);

            textFont = (Font) ois.readObject();
            body = (Color) ois.readObject();
            text = (Color) ois.readObject();
            caret = (Color) ois.readObject();

            ta.setFont(textFont);
            taLine.setFont(textFont);

            ta.setBackground(body);
            ta.setForeground(text);
            ta.setCaretColor(caret);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            try {

                if (ois != null) {
                    ois.close();
                }

            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }

        }
    }

////////////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        /**
         * Swing is not thread-safe. So, all code that creates or interacts with
         * Swing components must run from the event dispatch thread. The initial
         * thread of an application starts the app by invoking main().
         *
         * SwingUtilities.invokeLater() should be provided with the event
         * dispatch thread ( Runnable object ) for scheduling it for execution.
         */

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainClass();
            }
        });

    }

}
