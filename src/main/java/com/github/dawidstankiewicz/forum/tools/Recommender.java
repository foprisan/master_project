package com.github.dawidstankiewicz.forum.tools;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.github.dawidstankiewicz.forum.entity.Topic;

public class Recommender {
	
	public double threshold;
	public HashMap<Topic,List<String>> projects;
	public HashMap <Topic,Double>  result;
	public String target;
	
	
	
	
	public Recommender(double threshold, HashMap<Topic, List<String>> projects, String target) {
		super();
		this.threshold = threshold;
		this.projects = projects;
		this.target = target;
	}
	public double getThreshold() {
		return threshold;
	}
	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
	public HashMap<Topic, List<String>> getProjects() {
		return projects;
	}
	public void setProjects(HashMap<Topic, List<String>> projects) {
		this.projects = projects;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	
}
