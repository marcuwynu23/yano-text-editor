package com.peculiar.yano.editor; 


public class Main{
  public static void main(String[] args){
    try{
      new App(args[0]);
    }catch(IndexOutOfBoundsException ex){
      System.out.println("yano --souce-file");
    }
  }
}