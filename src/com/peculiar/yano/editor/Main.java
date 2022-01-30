package com.peculiar.yano.editor; 


public class Main{
  public static void main(String[] args){
    try{
      String arg1 = args[0];
      if(arg1.equals("--help") || arg1.equals("-h") || arg1.equals("-?")){
          System.out.println("Yano Text Editor v0.1");
          System.out.println("\tyano <fileName>");
      }else{
        new App(arg1);
      }
      
    }catch(IndexOutOfBoundsException ex){
      System.out.println("yano --souce-file");
    }
  }
}