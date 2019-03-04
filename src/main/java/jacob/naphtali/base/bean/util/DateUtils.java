package jacob.naphtali.base.bean.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Some tools for java.util.Date
 * @author ChangJian
 * @date 2017年9月30日
 */
public class DateUtils {
	
	
	/**
	 * 日期格式及正则表达式
	 */
	public static enum Format {
		yyyy("yyyy", Field.YEAR, "%Y", "[12]\\d{3}"),
		yyyyMM("yyyyMM", Field.MONTH, "%Y%m", "[12]\\d{3}((0[1-9])|(1[0-2]))"),
		yyyy_MM("yyyy-MM", Field.MONTH, "%Y-%m", "[12]\\d{3}-((0[1-9])|(1[0-2]))"),
		yyyyMMdd("yyyyMMdd", Field.DAY, "%Y%m%d", "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})(((0[13578]|1[02])(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)(0[1-9]|[12][0-9]|30))|(02(0[1-9]|[1][0-9]|2[0-9])))"),
		yyyy_MM_dd("yyyy-MM-dd", Field.DAY, "%Y-%m-%d", "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-9])))"),
		yyyyMMdd_FS("yyyy/MM/dd", Field.DAY, "%Y/%m/%d", "([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})/(((0[13578]|1[02])/(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)/(0[1-9]|[12][0-9]|30))|(02/(0[1-9]|[1][0-9]|2[0-8])))"),
		yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm", Field.MINUTE, "%Y-%m-%d %H:%i", "[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d"),
		yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss", Field.SECOND, "%Y-%m-%d %H:%i:%s", "[1-9]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[1-2][0-9]|3[0-1])\\s+(20|21|22|23|[0-1]\\d):[0-5]\\d:[0-5]\\d"),
		;
		private String format;
		private Field min;
		private String strftimeFormat;
		private String regex;
		
		private Format(String format, Field min, String strftimeFormat, String regex) {
			this.format = format;
			this.min = min;
			this.strftimeFormat = strftimeFormat;
			this.regex = regex;
		}

		public boolean match(String date) {
			if (StringUtils.isEmpty(date)) {
				return false;
			}
			if (date.matches(regex)) {
				return true;
			}
			return false;
		}

		public String getFormat() {
			return format;
		}

		public String getStrftimeFormat() {
			return strftimeFormat;
		}

		public String getRegex() {
			return regex;
		}

		public Field getMin() {
			return min;
		}
		
	}
	
	public static enum Field {
		YEAR(1), MONTH(2), WEEK(3), DAY(5), MINUTE(1000L*60), SECOND(1000L);
		private Integer value;
		private Long ms;

		private Field(Integer value) {
			this.value = value;
		}

		private Field(Long ms) {
			this.ms = ms;
		}

		public Integer getValue() {
			return value;
		}

		public Long getMs() {
			return ms;
		}

	}
	
	
	public static String getString(String format) {
		return getString(new Date(), format);
	}
	
	public static String getString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dataStr = sdf.format(date);
		return dataStr;
	}
	
	public static String getString(Format format) {
		return getString(new Date(), format);
	}
	
	public static String getString(Date date, Format format) {
		if (null == date) {
			return null;
		}
		if (null == format) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format.format);
		return sdf.format(date);
	}
	
	public static Format getFormat(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		for (Format format : Format.values()) {
			if (date.matches(format.regex)) {
				return format;
			}
		}
		return null;
	}
	
	public static Date getDate(String date) {
		if (StringUtils.isEmpty(date)) {
			return null;
		}
		Format format = getFormat(date);
		if (null == format) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format.format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date getDate(Timestamp timestamp) {
		if (null == timestamp) {
			return null;
		}
		Date date = new Date();
		date.setTime(timestamp.getTime());
		return date;
	}
	
	
	/**
	 * 计算日期加减
	 * @author ChangJian
	 * @date 2017年10月25日
	 * @param date 为null时返回null
	 * @param field 此类中的枚举常量
	 * @param amount 计算加减的数值，可为负
	 * @return 计算结果
	 */
	public static Date add(Date date, Field field, Integer amount) {
		if (null == date) {
			return null;
		}
		if (null != field.getMs()) {
			Long dateValue = date.getTime();
			dateValue += amount * field.getMs();
			return new Date(dateValue);
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(field.getValue(), amount);
		return gc.getTime();
	}
	
	/**
	 * 计算日期加减
	 * @author ChangJian
	 * @date 2017年10月25日
	 * @param date 格式不正确时返回null
	 * @param field 此类中的枚举常量
	 * @param amount 计算加减的数值，可为负
	 * @return 计算结果
	 */
	public static String add(String date, Field field, Integer amount) {
		Date date2 = getDate(date);
		if (null == date2) {
			return null;
		} 
		if (null != field.getMs()) {
			Long dateValue = date2.getTime();
			dateValue += amount * field.getMs();
			return getString(new Date(dateValue), getFormat(date));
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date2);
		gc.add(field.getValue(), amount);
		return getString(gc.getTime(), getFormat(date));
	}
}
