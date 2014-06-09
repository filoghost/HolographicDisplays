package com.gmail.filoghost.holograms.utils;

import java.util.ArrayList;
import java.util.List;

public class GenericUtils {

	public static <E> List<E> createList(E object) {
		if (object == null) {
			return null;
		}
		
		List<E> list = new ArrayList<E>();
		list.add(object);
		return list;
	}
}
