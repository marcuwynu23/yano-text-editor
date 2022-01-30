package com.peculiar.yano.editor; 


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

import java.io.IOException;
import javax.swing.JTextArea;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.Element;
import javax.swing.text.*;
import javax.swing.undo.*;

public class App extends JFrame{
  static class UI{
    private static final String TITLE = "Yano Text Editor";
    private static final int HEIGHT = 500;
    private static final int WIDTH = 1000;
    private static final Color BG_COLOR = new Color(40,40,40);
    private static final Color FG_COLOR = Color.WHITE;
    private static final String ICON_PATH = "/com/peculiar/yano/editor/icon.png";
  }
  private JPanel menuPanel,statusPanel;
  private Container ctpane;
  private MyTextArea textArea,lineArea;
  private JLabel statuslabel;
  private String filePath,fileContent;
  private int fontSize = 12;

  public App(String filePath){
    this.filePath = filePath;
    this.fileContent = TextFileHandler.getTextFromFile(filePath);
    try{
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }catch(Exception ex){
      System.out.println(ex);
    }
    setSize(UI.WIDTH,UI.HEIGHT);
    setTitle(UI.TITLE);
    setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource(UI.ICON_PATH)));
    setLocationRelativeTo(null);
    setDefaultCloseOperation(3);
    setComponents();
    setComponentUI();
    setComponentEvent();
    setVisible(true);
  }
  private void setComponents(){
    ctpane = getContentPane();
    menuPanel = new JPanel();
    statusPanel = new JPanel();
    menuPanel.setBackground(new Color(33,33,33));
    statusPanel.setBackground(new Color(33,33,33));

    textArea = new MyTextArea();
    lineArea = new MyTextArea("1");
    lineArea.setEditable(false);
    lineArea.setMargin(new Insets(10,10,10,10));
    
    textArea.setMargin(new Insets(10,10,10,10));
    textArea.setCaretColor(Color.WHITE);
    textArea.setTabSize(2);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.getDocument().addDocumentListener(new MyDocumentListener(textArea,lineArea));


    textArea.setText(fileContent);

    textArea.setFont(new Font("Consolas",1,fontSize));
    lineArea.setFont(new Font("Consolas",1,fontSize));

    var jsp = new JScrollPane();
    jsp.setBorder(BorderFactory.createEmptyBorder());
    jsp.setRowHeaderView(lineArea);
    jsp.getViewport().add(textArea);


    statuslabel = new JLabel("Name: "+filePath +" "+"Size: "+TextFileHandler.getFileSize(filePath));
    statuslabel.setFont(new Font("Consolas",1,14));
    statuslabel.setForeground(Color.WHITE);
    statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
    statusPanel.add(statuslabel);


    ctpane.setLayout(new BorderLayout());
    ctpane.add(menuPanel,BorderLayout.NORTH);
    ctpane.add(jsp,BorderLayout.CENTER);
    ctpane.add(statusPanel,BorderLayout.SOUTH);

  }
  private void setComponentUI(){
    ctpane.setBackground(UI.BG_COLOR);
    textArea.setBackground(UI.BG_COLOR);
    textArea.setForeground(UI.FG_COLOR);
    lineArea.setBackground(new Color(35,35,35));
    lineArea.setForeground(UI.FG_COLOR);
  }
  

  private void fontResize(int type){
    if(type == 1){
      fontSize++;
    }else{
      fontSize--;
    }
    textArea.setFont(new Font("Consolas",1,fontSize));
    lineArea.setFont(new Font("Consolas",1,fontSize));
  }

  private void setComponentEvent(){
    final UndoManager undoManager = new UndoManager();
    textArea.getDocument().addUndoableEditListener(undoManager);
    textArea.addKeyListener(new KeyAdapter(){
      @Override
      public void keyPressed(KeyEvent e){
        if(KeyEventHandler.isClickSave(e)){
          var isWritten = TextFileHandler.writeTextToFile(textArea.getText(),filePath);
          statuslabel.setText(filePath+" Saved.");
        }
        if(KeyEventHandler.isFontMinimize(e)){
          fontResize(0);
        }
        if(KeyEventHandler.isFontMaximize(e)){
          fontResize(1);
        }

        if(KeyEventHandler.isOpenTerminal(e)){
          try{
            Runtime.getRuntime().exec("cmd /K start");
          }catch(IOException ex){
            System.out.println(ex);
          }
        }
        if(KeyEventHandler.isOpenHelp(e)){
          System.out.println("ctrl h\t\tshow this information");
          System.out.println("ctrl s\t\tsave file");
          System.out.println("ctrl +\t\tmaximize font size");
          System.out.println("ctrl -\t\tminimize font size");
          System.out.println("ctrl t\t\topen terminal");
          System.out.println("ctrl e\t\texit");
        }
        if(KeyEventHandler.isExitClicked(e)){
          var isWritten = TextFileHandler.writeTextToFile(textArea.getText(),filePath);
          statuslabel.setText(filePath+"auto Saved.");
          System.exit(0);
        }
        // System.out.println(e.getKeyCode());
      }

      public void keyReleased(KeyEvent e){
        if(KeyEventHandler.isClickSave(e)){
          statuslabel.setText("Name: "+filePath +" "+"Size: "+TextFileHandler.getFileSize(filePath));
        }
        // System.out.println(e.getKeyCode());
      }

      @Override
      public void keyTyped(KeyEvent e){
        try{
          if(e.getKeyChar() == 26){
            undoManager.undo();
          }
          if(e.getKeyChar() == 25){
            undoManager.redo();
          }
        }catch(Exception ex){

        }
      }

    });

    textArea.addMouseWheelListener(new MouseWheelListener(){
     public void mouseWheelMoved(MouseWheelEvent e) {
      int notches = e.getWheelRotation();
      String message = "";
      if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
        if (notches < 0) {
          fontSize++;
        } else {
          fontSize--;
        }
        textArea.setFont(new Font("Consolas",1,fontSize));
        lineArea.setFont(new Font("Consolas",1,fontSize));
      }
    }
  });
  }
}



class MyTextArea extends javax.swing.JTextArea{
 public MyTextArea(String name){
  super(name);
}
public MyTextArea(){
  super();
  setUIColor();
  setUI();
}
public void setUIColor(){
  setBackground(new Color(45,45,45));
  setForeground(Color.WHITE);
  setCaretColor(Color.WHITE);
  setSelectedTextColor(Color.BLACK);
  setSelectionColor(Color.WHITE);
}
public void setUI(){
  setTabSize(2);
  setLineWrap(true);
  setWrapStyleWord(true);

}
}





class MyDocumentListener implements DocumentListener{
  private JTextArea textArea,lineArea;
  MyDocumentListener(JTextArea textArea,JTextArea lineArea){
    this.textArea = textArea;
    this.lineArea = lineArea;
  }
  public String getText() {
    int caretPosition = textArea.getDocument().getLength();
    Element root = textArea.getDocument().getDefaultRootElement();
    String text = "1" + System.getProperty("line.separator");
    for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
      text += i + System.getProperty("line.separator");
    }
    return text;
  }
  @Override
  public void changedUpdate(DocumentEvent de) {
    lineArea.setText(getText());
  }
  @Override
  public void insertUpdate(DocumentEvent de) {
    lineArea.setText(getText());
  }
  @Override
  public void removeUpdate(DocumentEvent de) {
    lineArea.setText(getText());
  }
}


class MyButton extends JButton{
  Color oldFColor,oldBColor;
  Color gFColor,gBColor;
  private FocusListener focusListener = new FocusListener(){
    public void focusGained(FocusEvent ev){
      setBackground(gBColor);
      setForeground(gFColor);
    }
    public void focusLost(FocusEvent ev){
      setBackground(oldBColor);
      setForeground(oldFColor);
    }
  };
  MyButton(String name,Color gBColor,Color gFColor){
    super(name);
    this.gBColor = gBColor;
    this.gFColor = gFColor;
    
    oldBColor = getBackground();
    oldFColor = getForeground();
    setFocusPainted(false);
    addFocusListener(focusListener);
  }
}