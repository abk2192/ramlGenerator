package com.lab;


public class Response extends Segment {
	private Response(String name){
		super("response",name);
	}

	@Override
	public Response addAttribute(String key,String value) throws Exception{
		super.addAttribute(key, value);
		return this;
	}
	
	public Response addBody(Body responseBody) throws Exception{
		if(getSegment(responseBody.getName()) == null){
			addSegment(responseBody);
			return this;
		}
		else{
			throw new Exception("Already has a body of given mime type.");
		}
	}
	public Body addEmptyBody(String bodyType) throws Exception{
		if(getSegment(bodyType) == null){
			Body b = new Body("responseBody", bodyType);
			addBody(b);
			return b;
		}
		else{
			throw new Exception("The request already has a body");
		}
		
	}
	public Body getBody(String key){
		Segment responseBody = getSegment(key);
		if(responseBody != null){
			if(responseBody instanceof Body)
				return (Body)getSegment(key);
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
	}
	public Response removeBody(String key) throws Exception{
		if(getBody(key) != null){
			removeSegment(key);
			return this;
		}
		else{
			throw new Exception("No Body of type "+key);
		}
	}
	
	public static Response getResponse(String status) throws Exception{
		if(status.matches("[0-9]{3}")){
				return new Response(status);
			}
			else{
			throw new Exception("Response status should be a three digit number");
		}
	}
}
