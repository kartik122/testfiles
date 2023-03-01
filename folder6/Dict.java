package com.kartik.dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Dict{
	public static <T> Predicate<T> distinct(final Function<? super T, Object> keyExtractor) 
	{
	  final Map<Object, Boolean> map = new ConcurrentHashMap<Object, Boolean>();
	  return new Predicate<T>() {
		public boolean test(T t) {
			return map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
		}
	  };
	}
	
	public static void main(String[] args) throws IOException {
		
		Map<String, List<String>> dict = new HashMap<String, List<String>>();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		dict.put("bat", Arrays.asList("nocturnal animal", "cricket equipment", "cricket equipment"));
		dict.put("park", Arrays.asList("playground", "car parking", "playground"));
		
		String a = br.readLine();
		List<String> l = dict.get(a);
		List<String> ans =l.stream().filter( distinct(p -> p) )
	              .collect( Collectors.toList() );
		for (String t : ans) 
			System.out.println(t);
		
	}
		
		
		
}