package jacob.naphtali.base.bean.util;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Some tools for reflect
 * @author ChangJian
 * @date 2017年9月30日
 */
public class ReflectUtils {
	
	public static enum GetOrSet {
		GET,SET;
	}

	/**
	 * @author ChangJian
	 * @date 2017年9月19日
	 * @param cls
	 * @return All fields from cls.class to Object.class
	 */
	public static List<Field> listFields(Class<?> cls) {
		List<Field> fields = new ArrayList<Field>();
		if (null != cls.getSuperclass()) {
			fields.addAll(listFields(cls.getSuperclass()));
		}
		Field[] fieldArray = cls.getDeclaredFields();
		for (Field field : fieldArray) {
			fields.add(field);
		}
		return fields;
	}
	
	/**
	 * Get one field of objCls with Annotation annotationClass
	 * @author ChangJian
	 * @date 2017年9月23日
	 * @param cls
	 * @return
	 */
	public static Field getField(Class<?> objCls, Class<? extends Annotation> annotationClass) {
		List<Field> fields = listFields(objCls);
		for (Field field : fields) {
			if (null != field.getAnnotation(annotationClass)) {
				return field;
			}
		}
		return null;
	}
	
	/**
	 * List fields of objCls with Annotation annotationClass
	 * @author ChangJian
	 * @date 2017年9月30日
	 * @param objCls
	 * @param annotationClass
	 * @return
	 */
	@SafeVarargs
	public static List<Field> listFields(Class<?> objCls, Class<? extends Annotation>... annotationClses) {
		List<Field> resultFields = new ArrayList<>();
		if (null == annotationClses) {
			return resultFields; 
		}
		if (annotationClses.length == 0) {
			return resultFields;
		}
		List<Field> fields = listFields(objCls);
		for (Field field : fields) {
			for (int i = 0; i < annotationClses.length; i++) {
				if (field.getAnnotation(annotationClses[i]) != null) {
					resultFields.add(field);
					break;
				}
			}
		}
		return resultFields;
	}
	
	public static List<Field> listFieldsByType(Class<?> objCls, Class<?>... types) {
		List<Field> resultFields = new ArrayList<>();
		if (null == types) {
			return resultFields; 
		}
		if (types.length == 0) {
			return resultFields;
		}
		List<Field> fields = listFields(objCls);
		List<Class<?>> typeList = Arrays.asList(types);
		for (Field field : fields) {
			if (typeList.contains(field.getType())) {
				resultFields.add(field);
			}
		}
		return resultFields;
	}
	
	@SafeVarargs
	public static List<Field> listFieldsExcept(Class<?> objCls, Class<? extends Annotation>... annotationClses) {
		List<Field> fields = listFields(objCls);
		List<Field> resultFields = new ArrayList<>();
		if (null == annotationClses) { //return all
			return fields; 
		}
		if (annotationClses.length == 0) { //return all
			return fields;
		}
		for (Field field : fields) {
			boolean add = true;
			for (Class<? extends Annotation> exceptCls : annotationClses) {
				if (null != field.getAnnotation(exceptCls)) {
					add = false;
					break;
				}
			}
			if (add) {
				resultFields.add(field);
			}
		}
		return resultFields;
	}
	
	/**
	 * 排除指定类型
	 * @author ChangJian
	 * @date 2019年1月16日
	 * @param objCls
	 * @param exceptCls
	 * @return
	 */
	public static List<Field> listFieldsExceptType(Class<?> objCls, Class<?>... exceptCls) {
		List<Field> fields = listFields(objCls);
		List<Class<?>> exceptClsList = Arrays.asList(exceptCls);
		List<Field> resultFields = new ArrayList<>();
		for (Field field : fields) {
			if (exceptClsList.contains(field.getType())) {
				continue;
			}
			resultFields.add(field);
		}
		return resultFields;
	}
	
	/**
	 * get the getField Method or the setField Method
	 * @author ChangJian
	 * @date 2017年8月23日
	 * @return
	 */
	public static Method getOrSetMethod(Class<?> cls, String fieldName, GetOrSet getOrSet) {
		String methodName = (getOrSet + fieldName).toLowerCase();
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().toLowerCase().equals(methodName)) {
				return method;
			}
		}
		if (null != cls.getSuperclass()) {
			return getOrSetMethod(cls.getSuperclass(), fieldName, getOrSet);
		}
		return null;
	}
	
	/**
	 * @author ChangJian
	 * @date 2017年9月19日
	 */
	public static Object getFieldVal(Object obj, Field field) {
		if (null == obj) {
			return null;
		}
		if (null == field) {
			return null;
		}
		Method getMethod = getOrSetMethod(obj.getClass(), field.getName(), GetOrSet.GET);
		if (null == getMethod) {
			return null;
		}
		try {
			return getMethod.invoke(obj);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @author ChangJian
	 * @date 2017年11月8日
	 * @param obj
	 * @param fieldName fieldName, or fieldName.subFieldName.subFieldName...
	 * @return
	 */
	public static Object getFieldVal(Object obj, String fieldName) {
		if (null == obj) {
			return null;
		}
		if (StringUtils.isEmpty(fieldName)) {
			return null;
		}
		String[] fieldNameArray = fieldName.split("\\.");
		Object value = getFieldVal(obj, getFieldByName(obj.getClass(), fieldNameArray[0]));
		if (fieldNameArray.length > 1) {
			return getFieldVal(value, fieldName.substring(fieldNameArray[0].length() + 1));
		}
		return value;
	}
	
	/**
	 * @author ChangJian
	 * @date 2017年9月19日
	 */
	public static boolean setFieldVal(Object obj, Field field, Object value) {
		
		Method setMethod = getOrSetMethod(obj.getClass(), field.getName(), GetOrSet.SET);
		if (null == setMethod) {
			return false;
		}
		try {
			setMethod.invoke(obj, value);
			return true;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean setFieldVal(Object obj, String fieldName, Object value) {
		if (null == obj) {
			return false;
		}
		Field field = getFieldByName(obj.getClass(), fieldName);
		if (null == field) {
			return false;
		}
		return setFieldVal(obj, field, value);
	}

	/**
	 * @author ChangJian
	 * @date 2017年9月29日
	 */
	public static Field getFieldByName(Class<?> cls, String fieldName) {
		if (null == cls) {
			return null;
		}
		if (StringUtils.isEmpty(fieldName)) {
			return null;
		}
		List<Field> fields = listFields(cls);
		for (Field field : fields) {
			if (fieldName.equals(field.getName())) {
				return field;
			}
		}
		return null;
	}
	
	public static Method getMethodByName(Class<?> cls, String methodName) {
		if (null == cls) {
			return null;
		}
		if (StringUtils.isEmpty(methodName)) {
			return null;
		}
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}
		return null;
	}
	
	public Object methodInvoke(Method method, Object obj, Object... args) {
		if (null == method) {
			return null;
		}
		try {
			return method.invoke(obj, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void simpleCopy(Object get, Object set) {
		if (null == get) {
			return;
		}
		if (null == set) {
			try {
				set = get.getClass().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
				return;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return;
			}
		}
		if (null == set) {
			return;
		}
		List<Field> fields = listFields(get.getClass());
		for (Field field : fields) {
			setFieldVal(set, field, getFieldVal(get, field));
		}
	}
	
	/**
	 * 判断是否字段有值 
	 * @author ChangJian
	 * @date 2017年9月16日
	 * @param obj
	 * @param field
	 * @return
	 */
	public static boolean existValue(Object obj, Field field) {
		Object value = ReflectUtils.getFieldVal(obj, field);
		if (null == value) {
			return false;
		}
		if (value instanceof String) {
			if (StringUtils.isEmpty(String.valueOf(value))) {
				return false;
			}
		}
		return true;
	}
	
	public static void resetStringNullToEmpty(Object obj) {
		if (null == obj) {
			return;
		}
		List<Field> fields = listFields(obj.getClass());
		for (Field field : fields) {
			if (String.class.equals(field.getType())) {
				if (null == obj) {
					obj = "";
				}
			}
		}
	}
	
	public static Class<?> getClass(File file) {
		String path = file.getAbsolutePath();
		String className = path.substring(path.indexOf("classes") + "classes".length() + 1, path.indexOf(".class")).replaceAll("[\\\\/]", ".");
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void main(String[] args) {
		String str = "E:/Users/Administrator/Tomcat/apache-tomcat-7.0.72/webapps/Simeon/WEB-INF/classes/com/base/dao/BaseDao.class";
		System.out.println(str.substring(str.indexOf("classes") + "classes".length() + 1, str.indexOf(".class")).replaceAll("[\\\\/]", "."));
	}
	public static <T> T newInstance(Class<T> cls, Object... objs) {
		if (null == cls) {
			return null;
		}
		Class<?>[] clses = new Class[objs.length];
		for (int i = 0; i < clses.length; i++) {
			clses[i] = objs[i].getClass();
		}
 		try {
			return cls.getDeclaredConstructor(clses).newInstance(objs);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
 		return null;
	}
	
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		List<Field> fields = ReflectUtils.listFields(obj.getClass());
		String[] fieldValueArray = new String[fields.size()];
		for (int i = 0; i < fieldValueArray.length; i++) {
			fieldValueArray[i] = fields.get(i).getName() + "=" + ReflectUtils.getFieldVal(obj, fields.get(i));
		}
		return obj.getClass().getName() + Arrays.toString(fieldValueArray);
	}
}
