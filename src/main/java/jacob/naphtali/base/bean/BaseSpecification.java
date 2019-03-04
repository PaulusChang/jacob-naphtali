package jacob.naphtali.base.bean;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.CriteriaBuilder.In;

import org.springframework.data.jpa.domain.Specification;

import jacob.naphtali.base.bean.Constraint.Null;
import jacob.naphtali.base.bean.util.DateUtils;
import jacob.naphtali.base.bean.util.DateUtils.Format;
import jacob.naphtali.base.bean.util.ReflectUtils;
import jacob.naphtali.base.bean.util.StringUtils;
import jacob.naphtali.base.constant.YesNo;

public class BaseSpecification <T extends BaseEntity> implements Specification<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 68758055930892190L;
	
	protected T t;
	protected Root<T> root;
	protected CriteriaQuery<?> query;
	protected CriteriaBuilder criteriaBuilder;

	public BaseSpecification(T t) {
		super();
		this.t = t;
	}

	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (null == t.getIsDeleted()) {
			t.setIsDeleted(YesNo.NO);
		}
		this.root = root;
		this.query = query;
		this.criteriaBuilder = criteriaBuilder;
		List<Field> fields = ReflectUtils.listFieldsExceptType(t.getClass(), OrderBean.class);
		List<Predicate> predicates = new LinkedList<>();
		for (Field field : fields) {
			if (!ReflectUtils.existValue(t, field)) {
				continue;
			}
			Predicate predicate = constraint(field, field.getAnnotation(Constraint.class));
			if (null == predicate) {
				continue;
			}
			predicates.add(predicate);
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
	
	@SuppressWarnings("unchecked")
	protected Predicate constraint(Field field, Constraint constraint) {
		if (null == constraint) {
			return constraint(field);
		}
		Object objVal = ReflectUtils.getFieldVal(t, field);
		Field aimField = field;
		if (!StringUtils.isEmpty(constraint.name())) {
			aimField = ReflectUtils.getFieldByName(t.getClass(), constraint.name());
		}
		String aimFieldName = aimField.getName();
		Expression<?> expression = root.get(aimFieldName);
		
		if (Null.IS.equals(objVal)) {
			return criteriaBuilder.isNull(expression);
		}
		if (Null.NOT.equals(objVal)) {
			return criteriaBuilder.isNotNull(expression);
		}
		if (objVal instanceof List || objVal instanceof Object[]) {
			return inPredicate(objVal, aimFieldName);
		}
		switch (constraint.type()) {
		case EQUAL:
			return criteriaBuilder.equal(expression, objVal);
			
		case UNEQUAL:
			return criteriaBuilder.notEqual(expression, objVal);
			
		case LIKE:
			if (objVal instanceof String) {
				return criteriaBuilder.like((Expression<String>) expression, String.valueOf("%" + objVal + "%"));
			}
			
		case START_WITH:
			if (objVal instanceof String) {
				return criteriaBuilder.like((Expression<String>) expression, String.valueOf(objVal + "%"));
			}
			
		case END_WITH:
			if (objVal instanceof String) {
				return criteriaBuilder.like((Expression<String>) expression, String.valueOf("%" + objVal));
			}
		default: 
			
		}

		if (objVal instanceof Number) {
			return numPredicate((Number) objVal, constraint, aimFieldName);
		}
		
		if (Date.class.isAssignableFrom(aimField.getType())) {
			return datePredicate(objVal, constraint, aimFieldName);
		}
		return null;
	}
	
	protected Predicate constraint(Field field) {
		return criteriaBuilder.equal(root.get(field.getName()), ReflectUtils.getFieldVal(t, field));
	}
	
	protected Predicate numPredicate(Number num, Constraint constraint, String fieldName) {
		switch (constraint.type()) {
		
		case MAX_CLOSE:
			return criteriaBuilder.le(root.get(fieldName).as(Number.class), num);

		case MAX_OPEN:
			return criteriaBuilder.lt(root.get(fieldName).as(Number.class), num);
			
		case MIN_CLOSE:
			return criteriaBuilder.ge(root.get(fieldName).as(Number.class), num);
			
		case MIN_OPEN:
			return criteriaBuilder.gt(root.get(fieldName).as(Number.class), num);
			
		default:
			return criteriaBuilder.equal(root.get(fieldName).as(Number.class), num);
		}
		
	}
	
	protected Predicate datePredicate(Object objVal, Constraint constraint, String fieldName) {
		if (objVal instanceof Date) {
			return predicate((Date) objVal, constraint, fieldName);
		}
		if (objVal instanceof String) {
			return stringDatePredicate((String) objVal, constraint, fieldName);
		}
		return null;
	}
	
	protected Predicate stringDatePredicate(String dateStr, Constraint constraint, String fieldName) {
		Date minDate = null;
		Date maxDate = null;
		Format format = DateUtils.getFormat(dateStr);
		if (null == format) {
			return null;
		}
		switch (constraint.type()) {
		
		case MAX_CLOSE:
			maxDate = DateUtils.getDate(DateUtils.add(dateStr, format.getMin(), 1));
			return criteriaBuilder.lessThan(root.get(fieldName).as(Date.class), maxDate);

		case MAX_OPEN:
			maxDate = DateUtils.getDate(dateStr);
			return criteriaBuilder.lessThan(root.get(fieldName).as(Date.class), maxDate);
			
		case MIN_CLOSE:
			minDate = DateUtils.getDate(dateStr);
			return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName).as(Date.class), minDate);
			
		case MIN_OPEN:
			minDate = DateUtils.getDate(DateUtils.add(dateStr, format.getMin(), 1));
			return criteriaBuilder.greaterThan(root.get(fieldName).as(Date.class), minDate);
			
		default:
			minDate = DateUtils.getDate(dateStr);
			maxDate = DateUtils.getDate(DateUtils.add(dateStr, format.getMin(), 1));
			return criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName).as(Date.class), minDate), 
					criteriaBuilder.lessThan(root.get(fieldName).as(Date.class), maxDate));
		}
	}
	
	protected Predicate predicate(Date date, Constraint constraint, String fieldName) {
		switch (constraint.type()) {
		
		case MAX_CLOSE:
			return criteriaBuilder.lessThanOrEqualTo(root.get(fieldName).as(Date.class), date);

		case MAX_OPEN:
			return criteriaBuilder.lessThan(root.get(fieldName).as(Date.class), date);
			
		case MIN_CLOSE:
			return criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName).as(Date.class), date);
			
		case MIN_OPEN:
			return criteriaBuilder.greaterThan(root.get(fieldName).as(Date.class), date);
			
		default:
			return criteriaBuilder.equal(root.get(fieldName).as(Date.class), date);
		}
	}
	/**
	 * 私有方法，就不过多做异常判断了
	 * @author ChangJian
	 * @date 2019年1月17日
	 * @param obj 要求是 objVal instanceof List || objVal instanceof Object[] 为 true
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected Predicate inPredicate(Object obj, String fieldName) {
		Object[] subObjArray;
		if (obj instanceof List) {
			subObjArray = ((List<Object>) obj).toArray();
		} else {
			subObjArray = (Object[]) obj;
		}
		if (subObjArray.length == 0) {
			return null;
		}
		In<Object> in = criteriaBuilder.in(root.get(fieldName));
		for (Object object : subObjArray) {
			in.value(object);
		}
		return in;
	}
	

}
