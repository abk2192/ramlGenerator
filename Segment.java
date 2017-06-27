package com.lab;

import java.util.ArrayList;
import java.util.HashMap;

public class Segment{
  private String type;
  private String name;
  private HashMap<String, String> attributes;
  private HashMap<String, Segment> segments;
  private ArrayList<String> attributeOrder;
  private ArrayList<String> segmentOrder;
  public Segment(String type, String name) {
    super();
    this.type = type;
    this.name = name;
    attributeOrder=new ArrayList<>();
    attributes = new HashMap<>();
    segmentOrder=new ArrayList<>();
    segments=new HashMap<>();
  }
  protected String getType(){
	  return type;
  }
  protected void setAttributeOrder(ArrayList<String> attributeOrder) {
    this.attributeOrder = attributeOrder;
  }
  protected void setName(String name){
	  this.name = name;
  }
  protected String getName() {
    return name;
  }
  protected void setType(String type){
	  this.type = type;
  }
  protected Segment getSegment(String key){
	  Segment s = segments.get(key);
	  return s;
  }
  public Segment addAttribute(String key, String value) throws Exception {
    if(!attributes.containsKey(key)){
    	attributeOrder.add(key);
        attributes.put(key, value);
        return this;
    }
    else{
    	throw new Exception("segment "+key+" is already added.");
    } 
  }
  protected Segment addSegment(Segment value) throws Exception {
	  String key = value.name;
    if(!segments.containsKey(key)){
    	segmentOrder.add(key);
        segments.put(key, value);
        return this;
    }
    else{
    	throw new Exception("segment "+key+" is already added.");
    } 
  }

  protected Segment addEmptySegment(String type, String segmentName) throws Exception {
	    if(!segments.containsKey(segmentName)){
		    Segment s = new Segment(type, segmentName);
		    addSegment(s);
		    return s;
	    }
	    else{
	    	throw new Exception("segment "+segmentName+" is already added.");
	    }
	     
	  }
  protected Segment addEmptySegment(String segmentName) throws Exception {
	    if(!segments.containsKey(segmentName)){
		    Segment s = new Segment(null, segmentName);
		    addSegment(s);
		    return s;
	    }
	    else{
	    	throw new Exception("segment "+segmentName+" is already added.");
	    }
	      
	  }
  protected void removeAttribute(String key) {
    if(attributes.containsKey(key)){
    	attributeOrder.remove(attributeOrder.indexOf(key));
        attributes.remove(key);
    }
  }
  protected void removeSegment(String key) {
	    if(segments.containsKey(key)){
	    	segmentOrder.remove(key);
	    	segments.remove(key);
	    }
	    
	  }
  protected Segment replaceSegment(String key,Segment value) throws Exception{
	  if(segments.containsKey(key)){
	        segments.put(key, value);
	        return this;
	    }
	    else{
	    	throw new Exception("segment "+key+" is already added.");
	    }
  }
  protected Segment replaceWithEmptySegment(String type, String segmentName) throws Exception {
	    if(segments.containsKey(segmentName)){
		    Segment s = new Segment(type, segmentName);
		    addSegment(s);
		    return s;
	    }
	    else{
	    	throw new Exception("segment "+segmentName+" is already added.");
	    }
	     
	  }
  protected Segment replaceWithEmptySegment(String segmentName) throws Exception {
	    if(segments.containsKey(segmentName)){
		    Segment s = new Segment(null, segmentName);
		    segments.put(segmentName, s);
		    return s;
	    }
	    else{
	    	throw new Exception("segment "+segmentName+" is already added.");
	    }
	      
	  }
  public String toString(String parentPrefix) {
    String indent = "  ";
    String prefix=parentPrefix+indent;
    
    String result=parentPrefix+name+":\n";
    for(String key: attributeOrder){
      if(attributes.get(key) != null)
        if(!attributes.get(key).contains("\n") && !attributes.get(key).contains("{")){
          result = result+parentPrefix+indent+key+": "+attributes.get(key)+"\n";
        }
        else{
          result = result+parentPrefix+indent+key+": |\n";
          result = result + indentMultiline(attributes.get(key), prefix+indent);
        }
    }
    for(String key: segmentOrder){
      if(segments.get(key) != null)
        result = result  + segments.get(key).toString(prefix);
    }
    return result;
  }
  
  private String indentMultiline(String value,String prefix){
    String result="";
    String []temp=value.split("\n");
    for(String s:temp){
      result=result+prefix+s+"\n";
    }
    return result;
  }

  public static void main(String[] args) {

    
    
  }

  @Override
  public String toString(){
	  return toString("");
  }

}
