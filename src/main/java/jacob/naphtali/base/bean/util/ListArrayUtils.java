package jacob.naphtali.base.bean.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Some tools for List and Array
 * @author ChangJian
 * @date 2017年10月16日
 */
public class ListArrayUtils {
	/**
	 * Map集合按给定的key值排序
	 * 
	 * @author ChangJian
	 * @date 2017年8月22日
	 * @param list
	 * @param key
	 * @param isDesc
	 *            是否降序排列
	 */
	public static void sort(List<Map<String, Object>> maps, String key, boolean isDesc) {
		if (isDesc) {
			sortDesc(maps, key);
		} else {
			sortAsc(maps, key);
		}
	}
	public static void sortAsc(List<Map<String, Object>> maps, String key) {
		for (int i = 0; i < maps.size(); i++) {
			for (int j = i; j < maps.size(); j++) {
				if (null == maps.get(i).get(key) || (null != maps.get(j).get(key) && Double.parseDouble(String.valueOf(maps.get(i).get(key))) > Double.parseDouble(String.valueOf(maps.get(j).get(key))))) {
					Collections.swap(maps, i, j);
				}
			}
		}
	}
	public static void sortDesc(List<Map<String, Object>> maps, String key) {
		for (int i = 0; i < maps.size(); i++) {
			for (int j = i; j < maps.size(); j++) {
				if (null == maps.get(i).get(key) || (null != maps.get(j).get(key) && Double.parseDouble(String.valueOf(maps.get(i).get(key))) < Double.parseDouble(String.valueOf(maps.get(j).get(key))))) {
					Collections.swap(maps, i, j);
				}
			}
		}
	}
	/**
	 * 把List<Map<String, Object>> 转成 Map
	 * 
	 * @author ChangJian
	 * @date 2017年8月22日
	 * @param maps
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<K, V, NK> Map<NK, Map<K, V>> listToMap(List<Map<K, V>> maps, K key) {
		Map<NK, Map<K, V>> resultMap = new HashMap<NK, Map<K, V>>();
		for (Map<K, V> map : maps) {
			resultMap.put((NK)map.get(key), map);
		}
		return resultMap;
	}
	/**
	 * 把Map<String, T> 转成 List&lt;T&gt;
	 * 
	 * @author ChangJian
	 * @date 2017年8月22日
	 * @param map
	 * @return
	 */
	public static <T> List<T> mapToList(Map<String, T> map) {
		List<T> resultList = new ArrayList<T>();
		for (String key : map.keySet()) {
			resultList.add(map.get(key));
		}
		return resultList;
	}
	
	/**
	 * [obj1, obj2, obj3] -- obj1, obj2, obj3
	 * @author ChangJian
	 * @date 2017年6月28日
	 * @param objs
	 * @return
	 */
	public static String arrayToStringOutBorder(Object[] objs) {
		if (null == objs) {
			return null;
		}
		String objStr = Arrays.toString(objs);
		objStr = objStr.replaceAll("[\\[\\]]", "");
		return objStr;
	}
	public static String listToStringOutBorder(List<?> objs) {
		if (null == objs) {
			return null;
		}
		String objStr = objs.toString();
		objStr = objStr.replaceAll("[\\[\\]]", "");
		return objStr;
	}
	

}
