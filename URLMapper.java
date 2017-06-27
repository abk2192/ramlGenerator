package com.lab;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class URLMapper {
  private static class URL {
    String resource;
    URL child;
    Set<String> method;

    URL(String url) {
      if (url.length() > 0) {
        String[] parts = url.split("/");
        resource = parts[0];
        if (parts.length > 1) {
          child = new URL(url.substring(url.indexOf("/") + 1));
        }
      }
    }
    public void addURL(String url){
      method = new HashSet<>();
      if(url.length() > 0){
        String[] parts = url.split("/");
        if(resource != null){
          if(resource.equals(parts[0])){
            if(child != null)
              child.addURL(url.substring(url.indexOf("/")+1));
            else
              child = new URL(url.substring(url.indexOf("/")+1));
          }
          else{
            try {
              throw new Exception("can't have two sub-resource");
            } catch (Exception e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
          }
        }
        else{
          resource = parts[0];
          child = new URL(url.substring(url.indexOf("/")+1));
        }
        
      }
    }
    public void print(String prefix) {
      if(method != null && method.size() > 0){
        if (resource != null) {
          System.out.println(prefix + "/" +resource+":");
          printMethods(prefix);
          if (child != null) {
            child.print(prefix+"\t");
          }
        }
      }
      else{
        if (resource != null) {
          System.out.print(prefix + "/" +resource);
          printMethods(prefix);
          if (child != null) {
            child.print(prefix);
          }
        }
      }
    }
    private void printMethods(String prefix) {
      if(method != null && method.size() > 0 ){
        for(String mthd:method){
          System.out.println(prefix+"\t"+mthd+":");
        }
      }
        
      
    }
    public void addMethod(String url,ArrayList<String> methods){
      if(url.length() > 0){
        String[] parts = url.split("/");
        if(resource != null){
          if(resource.equals(parts[parts.length-1])){
            if(method != null)
              method.addAll(methods);
            else{
              method = new HashSet<String>();
              method.addAll(methods);
            }
              
          }
          else{
            child.addMethod(url.substring(url.indexOf("/")+1), methods);
          }
        }
      }
      else{
        try {
          throw new Exception("Invalid resource to add ");
        } catch (Exception e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      
    }
  }

  public static void main(String[] args) {
    String url = "confluence.hk.hsbc/pages/viewpage.action/just_testing";
    // confluence.hk.hsbc/pages/viewpage.action
    URL u = new URL(url);
    u.addURL("confluence.hk.hsbc/pages/viewpage.action");
    u.print("");
    
    
  }


}
