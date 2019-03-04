package jacob.naphtali.base.bean.util;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.springframework.util.StringUtils {
	
	/**
	 * @author ChangJian
	 * @date 2017年10月14日
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (null == obj) {
			return null;
		}
		return obj.toString();
	}
	
	public static boolean isUpper(char char1) {
		return 'A' <= char1 && 'Z' >= char1;
	}
	
    public static List<String> getMatcher(String regex, String source) {  
        Pattern pattern = Pattern.compile(regex);  
        Matcher matcher = pattern.matcher(source);  
        List<String> strList = new LinkedList<>();
        while (matcher.find()) {
        	String result = matcher.group(0);
        	if (strList.contains(result)) {
				continue;
			}
        	strList.add(matcher.group(0));
        }  
        return strList;  
    }  	
    
    public static String beanString(Object obj) {
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

    public static void main(String[] args) {
    	   // TODO Auto-generated method stub  
    	   String str = "Hello,World! in Java.";  
    	   Pattern pattern = Pattern.compile("l(lo)*(d!)*");  
    	   Matcher matcher = pattern.matcher(str);  
    	   while(matcher.find()){  
    	    System.out.println("Group 0:"+matcher.group(0));//得到第0组——整个匹配  
    	    System.out.println("Group 1:"+matcher.group(1));//得到第一组匹配——与(or)匹配的  
    	    System.out.println("Group 2:"+matcher.group(2));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式  
    	    System.out.println("Start 0:"+matcher.start(0)+" End 0:"+matcher.end(0));//总匹配的索引  
    	    System.out.println("Start 1:"+matcher.start(1)+" End 1:"+matcher.end(1));//第一组匹配的索引  
    	    System.out.println("Start 2:"+matcher.start(2)+" End 2:"+matcher.end(2));//第二组匹配的索引  
    	    System.out.println(str.substring(matcher.start(0),matcher.end(1)));//从总匹配开始索引到第1组匹配的结束索引之间子串——Wor  
    	   }  
	}
}
