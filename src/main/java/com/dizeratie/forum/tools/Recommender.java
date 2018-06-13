package com.dizeratie.forum.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.dizeratie.forum.entity.Topic;
import com.dizeratie.forum.tools.TFIDFCalculator;
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
	
	public HashMap<Integer, HashMap<String, Double>> getTFIDFforProjectTerms (){
		List<List<String>> dataset= new ArrayList<List<String>>();
		projects.put(99999999, this.target);
		//build dataset
		for (Integer key : projects.keySet()){
			ArrayList <String> terms= new ArrayList<String>(projects.get(key));
			dataset.add(terms);
		}
		dataset.add(this.target);
		//compute tf-idf for each term in target. 
		
		//store results : integer : id of the topic, and for each topic, store a hashmap with the term and it s tf-idf
		HashMap<Integer,HashMap<String,Double>> allResultsForTopicsByTerms =new  HashMap<Integer,HashMap<String,Double>>();
		for (Integer projectId : projects.keySet()) {
			System.out.println("on project " + projectId);
			HashMap<String,Double> termTFIDF = new HashMap <String,Double>();
			ArrayList<String> doc= new ArrayList<String>(projects.get(projectId));
			for (String term:projects.get(projectId)){
				
				
				termTFIDF.put(term,TFIDFCalculator.tfIdf(doc, dataset, term));
			}
			
			allResultsForTopicsByTerms.put(projectId, termTFIDF);
			
		
		}
		/*for (Integer projectKey:allResultsForTopicsByTerms.keySet()){
			HashMap<String,Double> termtfidfproject=allResultsForTopicsByTerms.get(projectKey);
			double sum=0;
			for (String termKey: termtfidfproject.keySet()){
				sum=sum+Math.pow(termtfidfproject.get(termKey), 2);
			}
			sum=Math.sqrt(sum);
			for (String termKey: termtfidfproject.keySet()){
				termtfidfproject.put(termKey, termtfidfproject.get(termKey)/sum);
			}
		}*/
		return allResultsForTopicsByTerms;
	}
	public HashMap<Integer,Double> getRecommendations () {
		
		List<List<String>> dataset= new ArrayList<List<String>>();
		projects.put(99999999, this.target);
		//build dataset
		for (Integer key : projects.keySet()){
			ArrayList <String> terms= new ArrayList<String>(projects.get(key));
			dataset.add(terms);
		}
		dataset.add(this.target);
		//compute tf-idf for each term in target. 
		
		//store results : integer : id of the topic, and for each topic, store a hashmap with the term and it s tf-idf
		HashMap<Integer,HashMap<String,Double>> allResultsForTopicsByTerms =new  HashMap<Integer,HashMap<String,Double>>();
		for (Integer projectId : projects.keySet()) {
			System.out.println("on project " + projectId);
			HashMap<String,Double> termTFIDF = new HashMap <String,Double>();
			ArrayList<String> doc= new ArrayList<String>(projects.get(projectId));
			for (String term:projects.get(projectId)){
				
				
				termTFIDF.put(term,TFIDFCalculator.tfIdf(doc, dataset, term));
			}
			
			allResultsForTopicsByTerms.put(projectId, termTFIDF);
			
		
		}
		/*for (Integer projectKey:allResultsForTopicsByTerms.keySet()){
			HashMap<String,Double> termtfidfproject=allResultsForTopicsByTerms.get(projectKey);
			double sum=0;
			for (String termKey: termtfidfproject.keySet()){
				sum=sum+Math.pow(termtfidfproject.get(termKey), 2);
			}
			sum=Math.sqrt(sum);
			for (String termKey: termtfidfproject.keySet()){
				termtfidfproject.put(termKey, termtfidfproject.get(termKey)/sum);
			}
		}*/
		HashMap<String,Double> termTFIDFforTarget=allResultsForTopicsByTerms.get(99999999);
		for (Integer projectId : projects.keySet()){
			HashMap <String,Double> termTFIDFforProject=allResultsForTopicsByTerms.get(projectId);
			double resultCalc=0;	
			for (String term : this.target) {
				double result1=0;
				if (termTFIDFforProject.get(term)!=null){
					result1=termTFIDFforProject.get(term);
				}
				double result2=0;
				if (termTFIDFforTarget.get(term)!=null){
					result2=termTFIDFforTarget.get(term);;
				}
				
				resultCalc+= result1* result2;
			}
			this.result.put(projectId, resultCalc);
		}
		return this.result; 
	}
	
	
}
