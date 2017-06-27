package com.lab;

import java.util.ArrayList;

public final class Method extends Segment {
	private ArrayList<String> attributes;
	private ArrayList<String> queryParams;
	private ArrayList<String> requestBodies;
	private ArrayList<String> responses;
	
	public Method(String name) {
		super("method", name);
		
		attributes = new ArrayList<>();
		queryParams = new ArrayList<>();
		requestBodies = new ArrayList<>();
		responses = new ArrayList<>();
	}

	public ArrayList<String> getAttributes() {
		return attributes;
	}
	public ArrayList<String> getQueryParams() {
		return queryParams;
	}
	public ArrayList<String> getRequestBodies() {
		return requestBodies;
	}
	public ArrayList<String> getResponses() {
		return responses;
	}

	
	@Override
	public Method addAttribute(String key, String value) throws Exception {
		super.addAttribute(key, value);
		attributes.add(key);
		return this;
	}

	public Method addQueryParameter(Segment query) throws Exception {
		if (getSegment("queryParameters") != null) {
			query.setType("queryParam");
			getSegment("queryParameters").addSegment(query);
		} else {
			addEmptySegment("queryParameters").addSegment(query);
		}
		queryParams.add(query.getName());
		return this;
	}
	public Segment addEmptyQueryParameter(String queryParam) throws Exception {
		Segment s = new Segment("queryParam", queryParam);
		addQueryParameter(s);
		return s;
	}
	public Segment getQueryParameter(String param) {
		if(getSegment("queryParameters") != null){
			Segment s = getSegment("queryParameters").getSegment(param);
			if(s.getType().equals("queryParam"))
				return getSegment("queryParameters").getSegment(param);
		}
		return null;
	}
	public Method removeQueryParameter(String param) throws Exception{
		// handle the case when queryParameters segments is empty
		Segment s = getSegment("queryParameters").getSegment(param);
		if(s.getType().equals("queryParam")){
			getSegment("queryParameters").removeSegment(param);
			queryParams.remove(param);
			if(queryParams.size() ==  0){
				removeSegment("queryParameters");
			}
		}
			
		else{
			throw new Exception("No Query Parameter named "+param);
		}
		return this;
	}
	
	public Method addRequestBody(Body body) throws Exception{
		Segment s;
		if(getSegment("body") == null){
			s = new Segment("requestBody","body");
			addSegment(s);
		}
		else{
			s = getSegment("body");
		}
		if(s.getSegment(body.getName()) == null){
			s.addSegment(body);
			return this;
		}
		else{
			throw new Exception("The request already has a body");
		}
		
	}
	public Body addEmptyRequestBody(String bodyType) throws Exception{
		Body b = new Body("requestBody", bodyType);
		addRequestBody(b);
		requestBodies.add(bodyType);
		return b;
	}
	public Body getRequestBody(String key){
		// handle when body segment is null
		Segment requestBody = getSegment("body").getSegment(key);
		if(requestBody != null){
			if(requestBody instanceof Body)
				return (Body)requestBody;
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
	}
	public Method removeRequestBody(String bodyType) throws Exception{
		Segment body = getSegment("body");
		if(body.getSegment(bodyType) != null){
			if(body.getSegment(bodyType) instanceof Body){
				body.removeSegment(bodyType);
				requestBodies.remove(bodyType);
				if(requestBodies.size() == 0){
					removeSegment("body");
				}
			}
			else{
				throw new Exception("No Body of type "+bodyType);
			}
		}
		else{
			throw new Exception("No Body of type "+bodyType);
		}
		return this;
	}
	
	public Method addResponse(Response response) throws Exception{
		Segment s;
		if(getSegment("responses") != null){
			s = getSegment("responses");
		}
		else{
			s = new Segment("response", "responses");
			addSegment(s);
		}
		if(s.getSegment(response.getName()) == null ){
			s.addSegment(response);
			return this;
		}
		else{
			throw new Exception(response.getName()+" is already added.");
		}

	}
	public Response addEmptyResponse(String status) throws Exception{
		Response r = Response.getResponse(status);
		addResponse(r);
		responses.add(status);
		return r;
	}
	public Response getResponse(String key){
		Segment s;
		if(getSegment("responses") != null){
			s = getSegment("responses");
			if(s.getSegment(key)!= null && s.getSegment(key) instanceof Response){
				return (Response)s.getSegment(key);
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
		
	}
	public Method removeResponse(String status) throws Exception{
		if(getResponse(status) != null){
			getSegment("responses").removeSegment(status);
			responses.remove(status);
			if(responses.size() == 0){
				removeSegment("responses");
			}
			return this;
		}
		else{
			throw new Exception("no response for status code "+status);
		}
		
	}
}
