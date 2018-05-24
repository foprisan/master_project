package com.dizeratie.forum.tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class HashMapSort<Integer,Double> {

	private HashMap<java.lang.Integer, java.lang.Double> mapToSort;

	public HashMapSort(HashMap<java.lang.Integer, java.lang.Double> mapToSort) {
		super();
		this.mapToSort = mapToSort;
	}
	
	@SuppressWarnings("rawtypes")
	public LinkedHashMap<Integer,Double> sortHashMap(){
		 System.out.println("Initial Map: " + Arrays.toString(mapToSort.entrySet().toArray()));
		 /*	Map<Integer,Double> sortedMap=(mapToSort.entrySet().stream()).sorted().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,(e1,e2) -> e1, LinkedHashMap::new));
		System.out.println("Sorted Map: " + Arrays.toString(sortedMap.entrySet().toArray()));
	*/
		 @SuppressWarnings({ "unchecked", "rawtypes", "rawtypes", "rawtypes" })
		LinkedHashMap<Integer,Double> sortedMap= (LinkedHashMap<Integer, Double>) ValueComparator.sortByValue(mapToSort);
		 
		 System.out.println("Sorted Map: " + Arrays.toString(sortedMap.entrySet().toArray()));
		 return sortedMap;
	}
}
