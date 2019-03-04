package jacob.naphtali.base.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * where条件约束
 * @author ChangJian
 * @date 2017年9月15日
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Constraint {
	
	/**
	 * 约束类型
	 */
	Type type() default Type.EQUAL;
	/**
	 * 字段名称
	 */
	String name() default "";
	
	public enum Null {
		IS, NOT
	}
	
	public enum Type {
		EQUAL,
		UNEQUAL,
		LIKE,
		START_WITH,
		END_WITH,
		MIN_OPEN,
		MAX_OPEN,
		MIN_CLOSE,
		MAX_CLOSE,
	}

}
