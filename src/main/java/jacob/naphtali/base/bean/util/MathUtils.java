package jacob.naphtali.base.bean.util;

import java.text.DecimalFormat;

/**
 * Some tools for math
 * 
 * @author ChangJian
 * @date 2017年9月30日
 */
public class MathUtils {

	/**
	 * 整数的正则表达式
	 */
	public static final String REG_INT = "-?\\d+";

	/**
	 * 小数的正则表达式
	 */
	public static final String REG_FLOAT = "-?\\d+(\\.\\d+)?";

	/**
	 * 判断字符串匹配是否int格式
	 * 
	 * @author ChangJian
	 * @date 2017年9月7日
	 * @param intStr
	 * @return
	 */
	public static boolean isMatchInt(Object obj) {
		if (null == obj) {
			return false;
		}
		if (String.valueOf(obj).matches(REG_INT)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断字符串匹配是否小数格式
	 * 
	 * @author ChangJian
	 * @date 2017年9月7日
	 * @param floatStr
	 * @return
	 */
	public static boolean isMatchFloat(String floatStr) {
		if (null == floatStr) {
			return false;
		}
		if (floatStr.matches(REG_FLOAT)) {
			return true;
		}
		return false;
	}

	/**
	 * 字符串转成Integer，如果格式不正确返回null
	 * 
	 * @author ChangJian
	 * @date 2017年9月7日
	 * @param obj
	 * @return
	 */
	public static Integer toInteger(Object obj) {
		if (isMatchInt(obj)) {
			return Integer.valueOf(String.valueOf(obj));
		}
		return null;
	}
	
	/**
	 * 字符串转成Integer，如果格式不正确返回null
	 * 
	 * @author ChangJian
	 * @date 2017年9月7日
	 * @param obj
	 * @return
	 */
	public static Short toShort(Object obj) {
		if (isMatchInt(obj)) {
			return Short.valueOf(String.valueOf(obj));
		}
		return null;
	}

	/**
	 * 字符串转成Double，如果格式不正确返回null
	 * 
	 * @author ChangJian
	 * @date 2017年9月7日
	 * @param floatStr
	 * @return
	 */
	public static Double toDouble(String floatStr) {
		if (isMatchFloat(floatStr)) {
			return Double.valueOf(floatStr);
		}
		return null;
	}
	
	public static Double toDouble(Object floatStr) {
		return toDouble(StringUtils.toString(floatStr));
	}

	/**
	 * 加法
	 * @author ChangJian
	 * @date 2018年2月7日
	 * @param objs
	 * @return
	 */
	public static Double addition(Object... objs) {
		if (null == objs) {
			return null;
		}
		Double result = 0.0;
		Double objDouble;
		for (Object obj : objs) {
			objDouble = toDouble(obj);
			if (null != objDouble) {
				result += objDouble;
			}
		}
		return result;
	}

	/**
	 * @author ChangJian
	 * @date 2017年7月20日
	 * @param numerator
	 *            分子
	 * @param denominator
	 *            分母
	 * @return
	 */
	public static Double division(Double numerator, Double denominator) {
		if (null == numerator || null == denominator || 0 == denominator) {
			return null;
		}
		return numerator / denominator;
	}

	/**
	 * @author ChangJian
	 * @date 2017年7月20日
	 * @param numerator
	 *            分子
	 * @param denominator
	 *            分母
	 * @return
	 */
	public static Double division(Integer numerator, Integer denominator) {
		if (null == numerator || null == denominator || 0 == denominator) {
			return null;
		}
		return division(1.0 * numerator, 1.0 * denominator);
	}

	/**
	 * @author ChangJian
	 * @date 2017年7月20日
	 * @param numerator
	 *            分子
	 * @param denominator
	 *            分母
	 * @return
	 */
	public static Double division(Object numerator, Object denominator) {
		if (null == numerator || null == denominator || 0 == Double.parseDouble(String.valueOf(denominator))) {
			return null;
		}
		return division(Double.parseDouble(String.valueOf(numerator)), Double.parseDouble(String.valueOf(denominator)));
	}

	/**
	 * 获取相反数
	 * @author ChangJian
	 * @date 2017年9月30日
	 */
	public static Double getOppositeNumber(Double number) {
		if (null == number) {
			return null;
		}
		return -1 * number;
	}

	/**
	 * 乘法计算
	 * @author ChangJian
	 * @date 2017年8月3日
	 */
	public static Double multiply(Double num1, Double num2) {
		if (null == num1 || null == num2) {
			return null;
		}
		return num1 * num2;
	}

	/**
	 * 乘法计算
	 * @author ChangJian
	 * @date 2017年8月3日
	 */
	public static Double multiply(Object num1, Object num2) {
		if (null == num1 || null == num2) {
			return null;
		}
		return multiply(Double.parseDouble(String.valueOf(num1)), Double.parseDouble(String.valueOf(num2)));
	}
	
	/**
	 * @author ChangJian
	 * @date 2017年10月16日
	 * @param objs
	 * @return
	 */
	public static Integer[] toIntegerArray(Object[] objs) {
		if (null == objs) {
			return null;
		}
		Integer[] ints = new Integer[objs.length];
		for (int i = 0; i < objs.length; i++) {
			ints[i] = toInteger(objs[i]);
		}
		return ints;
	}
	
	/**
	 * @author ChangJian
	 * @date 2017年10月23日
	 * @param start
	 * @param end
	 * @return
	 */
	public static int rand(Integer start, Integer end) {
		if (null != start && null != end) {
			return (int)(start + Math.random() * (end - start));
		}
		if (null != start) {
			int result = (int) (Integer.MAX_VALUE * Math.random());
			if (result < start) {
				return rand(start, null);
			}
			return result;
		}
		if (null != end) {
			return (int)(Math.random() * end);
		}
		return (int) (Integer.MAX_VALUE * Math.random());
	}
	
	public static String decimalPlace(Double num, Integer length) {
		if (null == num) {
			return null;
		}
		if (null == length) {
			return String.valueOf(num);
		}
		if (length < 0) {
			return String.valueOf(num);
		}
		String format = "0";
		if (length > 0) {
			format += ".";
		}
		for (int i = 0; i < length; i++) {
			format += "0";
		}
		DecimalFormat df = new DecimalFormat(format);
		return df.format(num);
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			System.out.println(rand(0, 5));
		}
	}
}
