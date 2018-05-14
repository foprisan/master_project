package com.github.dawidstankiewicz.forum.tools;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class ValueComparator implements Comparator {

	private Map<Integer,Double> map;
	
	public ValueComparator(HashMap<Integer,Double> mapToSort){
		this.map=mapToSort;
	}


	@Override
	public int compare(Object k1, Object k2) {
		// TODO Auto-generated method stub
		return map.get(k1).compareTo(map.get(k2));
	}}
