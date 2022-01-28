package com.peculiar.yano.editor; 

import java.nio.file.*;
import java.io.FileWriter;

public class TextFileHandler{
  public static String getTextFromFile(String filePath){
    String textContent = null;
    try{
      textContent = new String(Files.readAllBytes(Paths.get(filePath)));
      return textContent;
    }catch(java.io.IOException ex){
      System.out.println("No Such File: "+filePath);
      System.exit(0);
    }
    return textContent;
  }

  public static boolean writeTextToFile(String content,String filePath){
    try{
      var mywriter = new FileWriter(filePath);
      mywriter.write(content);
      mywriter.close();
      return true;
    }catch(Exception ex){
      System.out.println(ex);
    }
    return false;
  }
  public static String getFileSize(String filePath){
    long bytes = 0;
    try{
      bytes = Files.size(Paths.get(filePath));
      return String.format("%,d bytes", bytes);
    }catch(Exception ex){
      System.out.println(ex);
    }
    return "No Size";
  }
}