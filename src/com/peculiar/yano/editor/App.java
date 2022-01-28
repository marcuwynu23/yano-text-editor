package com.peculiar.yano.editor; 


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

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
  private String filePath;
  private int fontSize = 12;

  public App(String filePath){
    this.filePath = filePath;
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

    textArea = new MyTextArea(TextFileHandler.getTextFromFile(filePath));
    lineArea = new MyTextArea("1");
    lineArea.setEditable(false);
    lineArea.setMargin(new Insets(10,10,10,10));
    
    textArea.setMargin(new Insets(10,10,10,10));
    textArea.setCaretColor(Color.WHITE);
    textArea.setTabSize(2);
    textArea.setLineWrap(true);
    textArea.setWrapStyleWord(true);
    textArea.getDocument().addDocumentListener(new MyDocumentListener(textArea,lineArea));



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
  private boolean isClickSave(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != 's' && e.getKeyCode() == 83;
  }

  private boolean isFontMinimize(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != '-' && e.getKeyCode() == 45;
  }

  private boolean isFontMaximize(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != '+' && e.getKeyCode() == 61;
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
        if(isClickSave(e)){
          var isWritten = TextFileHandler.writeTextToFile(textArea.getText(),filePath);
          statuslabel.setText(filePath+" Saved.");
        }
        if(isFontMinimize(e)){
          fontResize(0);
        }
        if(isFontMaximize(e)){
          fontResize(1);
        }
        // System.out.println(e.getKeyCode());
      }

      public void keyReleased(KeyEvent e){
        if(isClickSave(e)){
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
  }
}



class MyTextArea extends javax.swing.JTextArea{
 MyTextArea(String name){
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


