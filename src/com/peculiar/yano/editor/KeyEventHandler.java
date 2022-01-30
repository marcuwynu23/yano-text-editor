package com.peculiar.yano.editor; 



import java.awt.event.*;

public class KeyEventHandler{
  protected static boolean isClickSave(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != 's' && e.getKeyCode() == 83;
  }

  protected static boolean isFontMinimize(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != '-' && e.getKeyCode() == 45;
  }

  protected static boolean isFontMaximize(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != '+' && e.getKeyCode() == 61;
  }

  protected static boolean isOpenTerminal(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != 't' && e.getKeyCode() == 84;
  }
  protected static boolean isOpenHelp(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != 'h' && e.getKeyCode() == 72;
  }

  protected static boolean isExitClicked(KeyEvent e){
    return e.isControlDown() && e.getKeyChar() != 'e' && e.getKeyCode() == 69;
  }
}