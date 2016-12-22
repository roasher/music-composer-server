package ru.pavelyurkin.musiccomposer.core.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Wish on 05.01.2016.
 */
public class CollectionUtils {

	public static <T> List<T> getListOfFirsts( Set<List<T>> setOfLists ) {
		List<T> result = new ArrayList<>(  );
		for ( List<T> list : setOfLists ) {
			if ( list != null && list.size() != 0 ) {
				result.add( list.get(0) );
			}
		}
		return result;
	}
}
