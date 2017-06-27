package com.lab;

import java.util.ArrayList;
import java.util.Arrays;

public final class Resource extends Segment {
	private ArrayList<String> subResources;
	private ArrayList<String> methods;
	private ArrayList<String> uriParameters;
	private ArrayList<String> attributes;
	
	public Resource(String name){
		super("uri",name);
		subResources = new ArrayList<>();
		methods = new ArrayList<>();
		uriParameters = new ArrayList<>();
		attributes = new ArrayList<>();
		
		for(String s:extractURIParameter(name)){
			try {
				addEmptyURIParameter(s);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public ArrayList<String> getSubResources() {
		return subResources;
	}
	public ArrayList<String> getMethods() {
		return methods;
	}
	public ArrayList<String> getUriParameters() {
		return uriParameters;
	}
	public ArrayList<String> getAttributes() {
		return attributes;
	}
	
	@Override
	public Resource addAttribute(String key,String value) throws Exception{
		super.addAttribute(key, value);
		attributes.add(key);
		return this;
	}

	public Resource addURIParameter(Segment uri) throws Exception{
		if(getSegment("uriParameters") != null){
			uri.setType("uriParam");
			getSegment("uriParameters").addSegment(uri);
		}
		else{
			addEmptySegment("uriParameters")
				.addSegment(uri);
		}
		uriParameters.add(uri.getName());
		return this;
	}
	public Segment addEmptyURIParameter(String uriParam) throws Exception{
		Segment s = new Segment("uriParam",uriParam);
		addURIParameter(s);
		return s;
	}
	public Segment getURIParameter(String key){
		if(getSegment("uriParameters") != null){
			Segment s = getSegment("uriParameters").getSegment(key);
			if(s != null && s.getType().equals("uriParam"))
				return getSegment("uriParameters").getSegment(key);
		}
			return null;
			
	}
	public Resource removeURIParameter(String key) throws Exception{
		Segment s = getSegment("uriParameters").getSegment(key);
		if(s.getType().equals("uriParam")){
			getSegment("uriParameters").removeSegment(key);
			uriParameters.remove(key);
			if(uriParameters.size() == 0){
				removeSegment("uriParameters");
			}
		}
			
		else{
			throw new Exception("No URI Parameter named "+key);
		}
		return this;
	}


	public Resource addMethod(Method method) throws Exception{
		if(getSegment(method.getName()) == null){
			addSegment(method);
			methods.add(method.getName());
			return this;
		}
		else{
			throw new Exception("Method is already added.");
		}
	}
	public Method addEmptyMethod(String method) throws Exception{
		if(getSegment(method) == null){
			Method m = new Method(method);
			addMethod(m);
			return m;
		}
		else{
			throw new Exception("Method is already added.");
		}
	}
	public Method getMethod(String key){
		Segment method = getSegment(key);
		if(method != null){
			if(method instanceof Method)
				return (Method)getSegment(key);
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
	}
	public Resource removeMethod(String key) throws Exception{
		if(getMethod(key) != null){
			removeSegment(getMethod(key).getName());
			return this;
		}
		else{
			return null;
		}
	}

	
	private Resource addSubResource(Resource subResource) throws Exception{
		Resource r;
//		System.out.println("addResource method:"+getSubResources());
		for(String sr : getSubResources()){
			r= getSubResource(sr);
			String parent = r.getCommonParent(subResource.getName());
			if(parent.length() > 0){
				r = r.mergeResource(subResource);
				
				removeSegment(sr);
				subResources.remove(sr);
				
				addSegment(r);
				subResources.add(r.getName());
				return this;
			}
		}
			addSegment(subResource);
			subResources.add(subResource.getName());
		return this;
	}
	private Resource getSubResource(String key){
		Segment resource = getSegment(key);
		if(resource != null){
			if(resource instanceof Resource)
				return (Resource)resource;
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}
	
	public Resource mergeResource(Resource resource) throws Exception{
		String parent = getCommonParent(resource.getName());
		Resource result=null;
		if(parent.length() != 0){
			if(this.getName().equals(parent)){
				removeURIParameters(this, resource);
//					removeSegment(resource.getName());
				result = addSubResource(resource);

					
//				result = this;
			}
			else{
				if(resource.getName().equals(parent)){
					// add this object as subResource to resource
					removeURIParameters(resource, this);
					resource.addSubResource(this);
					result = resource;
				}
				else{
					// create a new resource and add both of the resources to it
					result =new Resource(parent);
					removeURIParameters(result, resource);
					removeURIParameters(result, this);
					result.addSubResource(resource);
					result.addSubResource(this);
				}
			}
		}
		return result;
	}

	private String getCommonParent(String resource){
		String self = getName();
		String resourceName = resource;
		String[] selfTemp = self.split("/");
		String[] resourceNameTemp = resourceName.split("/");
		String result = "";
		int min = Math.min(selfTemp.length, resourceNameTemp.length);
		for(int i = 0; i < min; i++){
			if(selfTemp[i].length()> 0 && resourceNameTemp[i].length() > 0 ){
				if(selfTemp[i].equals(resourceNameTemp[i])){
					result = result + "/" + selfTemp[i];
				}
				else{
					return result;
				}
			}
			
		}
		
		return result;
	}
	
	
	private ArrayList<String> extractURIParameter(String uri){
		ArrayList<String> result = new ArrayList<>();
		if(uri.contains("}")){
			String []temp = uri.split("}");
			if(temp.length > 0){
				for(String s: temp){
					if(s.length() > 2 && s.contains("{")){
						result.add(s.substring(s.indexOf("{")+1));
//						System.out.println(s.substring(s.indexOf("{")+1)+" uri param extracted");
					}
				}
			}
		}
		return result;
	}
	private Resource removeURIParameters(Resource parent,Resource child){
		child.setName(child.getName().replace(parent.getName(), ""));
		for(String param : parent.getUriParameters()){
			if(child.getURIParameter(param) != null){
				try {
					child.removeURIParameter(param);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return child;
	}


}
