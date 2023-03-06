package com.mauricio.springsecurity.component.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class ObjectUtils {
	
	public static Boolean isNotNull(Object... objects) {
		for (Object object : objects) {
			if (object == null) {
				return false;
			}
		}
		return true;
	}
	
	public static Boolean isNull(Object... objects) {
		for (Object object : objects) {
			if (object != null) {
				return false;
			}
			
			if (object instanceof Iterable<?>) {
				
			}
		}
		
		return true;
	}
	
	@SafeVarargs
	public static Boolean isEmpty(Iterable<?>... iterables) {
		for (Iterable<?> iterable : iterables) {
			if (isNotNull(iterable) && iterable.iterator().hasNext()) { 
				return false;
			}
		}
		return true;
	}
	
	@SafeVarargs
	public static Boolean isNotEmpty(Iterable<?>... iterables) {
		for (Iterable<?> iterable : iterables) {
			if (isNotNull(iterable) && iterable.iterator().hasNext()) { 
				return true;
			}
		}
		return false;
	}
	
	public static Boolean not(Boolean expression) {
		return !expression;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> Class<List<T>> convertClassToListOfClass(Class<T> objectClass) {
		Class<List<T>> clazz = (Class) List.class;
		return clazz;
	}
	
	public static <T> T convertObjectToClass(Object object, Class<T> clazz) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(object, clazz);
	}
	
	public static <T> List<T> convertArraysOfObjectToListOfClass(Object[] objects, Class<T> clazz) {
		return Arrays.stream(objects)
				  .map(object -> convertObjectToClass(object, clazz))
				  .collect(Collectors.toList());
	}

}
