package com.github.dawidstankiewicz.forum.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HashMapSort<Integer,Double> {

	private HashMap <Integer,Double> mapToSort;

	public HashMapSort(HashMap<Integer, Double> mapToSort) {
		super();
		this.mapToSort = mapToSort;
	}
	
	public void sortHashMap(){
		 System.out.println("Initial Map: " + Arrays.toString(mapToSort.entrySet().toArray()));
		 /*	Map<Integer,Double> sortedMap=(mapToSort.entrySet().stream()).sorted().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2) -> e1, LinkedHashMap::new));
		System.out.println("Sorted Map: " + Arrays.toString(sortedMap.entrySet().toArray()));
	*/
		 Map<Integer,Double> sortedMap= new TreeMap(new ValueComparator((HashMap<java.lang.Integer, java.lang.Double>) mapToSort));
		 System.out.println("Sorted Map: " + Arrays.toString(sortedMap.entrySet().toArray()));
	}
}
