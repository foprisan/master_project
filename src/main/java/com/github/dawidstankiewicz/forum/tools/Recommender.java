package com.github.dawidstankiewicz.forum.tools;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.github.dawidstankiewicz.forum.entity.Topic;

public class Recommender {
	
	public double threshold;
	public HashMap<Integer,List<String>> projects;
	public HashMap <Integer,Double>  result;
	public List<String> target;
	
	
	
	
	public Recommender(double threshold, HashMap<Integer, List<String>> projects,
			List<String> target) {
		super();
		this.threshold = threshold;
		this.projects = projects;
		this.target = target;
		this.result=new HashMap<Integer,Double>();
	}
	
	

	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public HashMap<Integer, List<String>> getProjects() {
		return projects;
	}
	public void setProjects(HashMap<Integer, List<String>> projects) {
		this.projects = projects;
	}
	public List<String> getTarget() {
		return target;
	}
	public void setTarget(List<String> target) {
		this.target = target;
	}
	
	
	public HashMap<Integer,Double> getRecommendations () {
		
		
		return this.result;
	}
}
